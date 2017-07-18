package org.enventureenterprises.enventure.ui.addItem;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatImageView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.enventureenterprises.enventure.R;
import org.enventureenterprises.enventure.data.model.DailyReport;
import org.enventureenterprises.enventure.data.model.Item;
import org.enventureenterprises.enventure.data.model.MonthlyReport;
import org.enventureenterprises.enventure.data.model.WeeklyReport;
import org.enventureenterprises.enventure.data.remote.EnventureApi;
import org.enventureenterprises.enventure.ui.base.BaseActivity;
import org.enventureenterprises.enventure.ui.general.ProgressDialogFragment;
import org.enventureenterprises.enventure.util.Config;
import org.enventureenterprises.enventure.util.GeneralUtils;
import org.enventureenterprises.enventure.util.rx.Transformers;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import timber.log.Timber;

/**
 * Created by mossplix on 7/6/17.
 */

public class AddItemActivity extends BaseActivity {
    private Realm realm;
    private Long item_id;
    private String item_name;
    private ActionBar actionBar;
    private ProgressDialogFragment progressFragment;

    @Inject
    EnventureApi client;

    Item item;


    @BindView(R.id.camera)
    AppCompatImageView cameraImageView;

    @BindView(R.id.gallery)
    AppCompatImageView galleryImageView;

    @BindView(R.id.quantity)
    TextInputEditText quantityEditText;

    @BindView(R.id.name)
    TextInputEditText nameEditText;

    @BindView(R.id.totalcost)
    TextInputEditText totalCostEditText;

    @BindView(R.id.selected_photo)
    ImageView imageView;

    @BindView(R.id.no_photo)
    TextView noPhoto;

    @BindView(R.id.name_layout)
    TextInputLayout nameLayout;

    @BindView(R.id.quantity_layout)
    TextInputLayout quantityLayout;

    @BindView(R.id.totalcost_layout)
    TextInputLayout totalcostLayout;





    private Bitmap mImageBitmap;
    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";

    private static final Integer REQUEST_STORAGE = 2;


    private boolean isReceiverRegistered;
    String mCurrentPhotoPath;
    private static final String BITMAP_STORAGE_KEY = "viewbitmap";

    private Uri photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);

        setContentView(R.layout.additem_activity);
        ButterKnife.bind(this);



        realm = Realm.getDefaultInstance ();



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Product");

        mImageBitmap = null;

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAlbumStorageDirFactory = new FroyoAlbumDirFactory ();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumDirFactory ();
        }

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                item_id = extras.getLong("item");
                item_name = extras.getString("name");

            }

        } else {
            item_id = savedInstanceState.getLong("item");
            item_name = savedInstanceState.getString("name");
        }

        item = realm.where(Item.class).equalTo ("created_ts",item_id).findFirst ();
        if(item != null) {

            nameEditText.setText(item.getName());
            quantityEditText.setText(item.getQuantity().toString());
            totalCostEditText.setText(item.getTotalCost().toString());
        }


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        nameEditText.addTextChangedListener(new FormTextWatcher(nameEditText));
        quantityEditText.addTextChangedListener(new FormTextWatcher(quantityEditText));
        totalCostEditText.addTextChangedListener(new FormTextWatcher(totalCostEditText));



    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = null;
        try {
            f = setUpPhotoFile();
            mCurrentPhotoPath = f.getAbsolutePath();
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
            photo = Uri.fromFile(f);
        } catch (IOException e) {
            e.printStackTrace();
            f = null;
            mCurrentPhotoPath = null;
        }

        startActivityForResult(takePictureIntent, Config.REQUEST_TAKE_PHOTO);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_STORAGE)
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            }
    }




    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(BITMAP_STORAGE_KEY, mImageBitmap);
        if (mCurrentPhotoPath != null) {

            outState.putString ("mCurrentPhotoPath", mCurrentPhotoPath);
        }

        super.onSaveInstanceState(outState);
    }


    private void handleCameraPhoto() {

        if (mCurrentPhotoPath != null) {
            setPic();
            galleryAddPic();
            mCurrentPhotoPath = null;
        }

    }

    private File getAlbumDir() {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir("Enventure");

            if (storageDir != null) {
                if (! storageDir.mkdirs()) {
                    if (! storageDir.exists()){
                        Timber.d("failed to create directory");
                        return null;
                    }
                }
            }

        } else {
            Timber.e("External storage is not mounted READ/WRITE.");
        }

        return storageDir;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File albumF = getAlbumDir();
        File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
        return imageF;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        photo = contentUri;
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }

    // Returns true if external storage for photos is available
    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    private File setUpPhotoFile() throws IOException {

        File f = createImageFile();
        mCurrentPhotoPath = f.getAbsolutePath();

        return f;
    }

    // Returns the Uri for a photo stored on disk given the fileName
    public Uri getPhotoFileUri() {
        // Only continue if the SD Card is mounted
        String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())+".jpg";
        if (isExternalStorageAvailable()) {
            // Get safe storage directory for photos
            // Use `getExternalFilesDir` on Context to access package-specific directories.
            // This way, we don't need to request external read/write runtime permissions.
            File mediaStorageDir = new File(
                   getExternalFilesDir(Environment.DIRECTORY_PICTURES), "org.enventureenterprises.enventure");

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){

            }

            // Return the file target for the photo based on filename
            return Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator + fileName));
        }
        return null;
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch(requestCode){
            case Config.REQUEST_TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {

                    handleCameraPhoto();
                    noPhoto.setVisibility(View.INVISIBLE);

                }
                else{
                    Toast.makeText(AddItemActivity.this,"Unable to get camera image. Please select from gallery",Toast.LENGTH_SHORT).show();
                }
                break;
            case Config.ACTIVITY_SELECT_IMAGE:
                if (resultCode == Activity.RESULT_OK) {

                    photo = data.getData();
                    Bitmap img= GeneralUtils.getThumbnail(AddItemActivity.this,data.getData(),GeneralUtils.MIME_TYPE_IMAGE );
                    imageView.setImageBitmap(img);
                    Timber.d("Image selected: "+data.getData());
                    //noPhoto.setVisibility(View.INVISIBLE);

                }
                break;
            default:
                break;


        }


    }

    @OnClick(R.id.gallery)
    public void chooseGallery(){
        Intent intent=new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, Config.ACTIVITY_SELECT_IMAGE);

    }

    @OnClick(R.id.camera)
    public void takePhoto(){

        int hasPermission = ActivityCompat.checkSelfPermission(AddItemActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (android.content.pm.PackageManager.PERMISSION_GRANTED == hasPermission) {
            dispatchTakePictureIntent();
        }
        else {
            ActivityCompat.requestPermissions(AddItemActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_save, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id){
            case R.id.save:

                if (!validateName()) {
                    return false;
                }



                if (!validateQuantity()) {
                    return false;
                }

                if (!validateTotalCost()) {
                    return false;
                }


                realm = Realm.getDefaultInstance ();
                realm.beginTransaction();
                DateTime d = new DateTime();
                DateTimeFormatter fmt = DateTimeFormat.forPattern("EEEE");
                Item inventoryItem = new Item ();
                DailyReport drep = realm.where(DailyReport.class)
                        .equalTo("name", fmt.withLocale(Locale.getDefault()).print(d))
                        .findFirst();
                WeeklyReport wrep = realm.where(WeeklyReport.class)
                        .equalTo("name", Integer.toString(d.getWeekOfWeekyear()))
                        .findFirst();
                MonthlyReport mrep = realm.where(MonthlyReport.class)
                        .equalTo("name", d.toString("MMM"))
                        .findFirst();


                if (drep == null) {
                    drep = new DailyReport();
                    drep.setName(fmt.withLocale(Locale.getDefault()).print(d));
                }
                if (wrep == null) {
                    wrep = new WeeklyReport();
                    wrep.setName(Integer.toString(d.getWeekOfWeekyear()));
                }

                if (mrep == null) {
                    mrep = new MonthlyReport();
                    mrep.setName(d.toString("MMM"));
                    mrep = new MonthlyReport();
                }
                inventoryItem.setName(nameEditText.getText().toString());
                inventoryItem.setQuantity(Integer.parseInt(quantityEditText.getText().toString()));
                inventoryItem.setTotalCost(Double.parseDouble(totalCostEditText.getText().toString()));
                inventoryItem.setImage(photo.toString());
                inventoryItem.setSynced(false);
                inventoryItem.setCreated(d.toDate());

                inventoryItem.setCreatedTs(d.getMillis());

                mrep.setTotalSpent(Double.parseDouble(totalCostEditText.getText().toString()));
                mrep.setUpdated(d.toDate());



                drep.setTotalSpent(Double.parseDouble(totalCostEditText.getText().toString()));
                drep.setUpdated(d.toDate());



                wrep.setTotalSpent(Double.parseDouble(totalCostEditText.getText().toString()));
                wrep.setUpdated(d.toDate());

                realm.copyToRealmOrUpdate (inventoryItem);
                realm.copyToRealmOrUpdate (drep);
                realm.copyToRealmOrUpdate (mrep);
                realm.copyToRealmOrUpdate (wrep);


                realm.commitTransaction();

                client.createItem(inventoryItem)
                        .compose(Transformers.neverError());





                Intent intent = new Intent(AddItemActivity.this, ItemDetail.class);
                intent.putExtra("item_id",inventoryItem.getCreatedTs());
                intent.putExtra("item",inventoryItem.getCreatedTs());
                intent.putExtra("item_name",inventoryItem.getName());
                startActivityWithTransition(intent, R.anim.slide_in_right, R.anim.fade_out_slide_out_left);

                break;
            case android.R.id.home:
                // this takes the user 'back', as if they pressed the left-facing triangle icon on the main android toolbar.
                // if this doesn't work as desired, another possibility is to call `finish()` here.
                this.onBackPressed();
                break;


        }

        return super.onOptionsItemSelected(item);
    }



    private boolean validateName() {
        if (nameEditText.getText().toString().trim().isEmpty()) {
            nameLayout.setErrorEnabled(true);
            nameEditText.setError("The Product name is required");
            requestFocus(nameEditText);
            return false;

        }
        else if(Item.byName(getRealm(),nameEditText.getText().toString()) !=null)
        {
            nameLayout.setErrorEnabled(true);
            nameEditText.setError("A Product with a similar name already exists");
            requestFocus(nameEditText);
            return false;
        }


        else
        {
            nameLayout.setErrorEnabled (false);
        }

        return true;
    }

    private boolean validateQuantity() {
        String quantity = quantityEditText.getText().toString();
        if (quantity.trim().isEmpty()) {
            quantityLayout.setErrorEnabled(true);
            quantityEditText.setError("Quantity is a required Field");
            requestFocus(quantityEditText);
            return false;
        }

        else if(Double.parseDouble(quantity) == Double.NaN)
        {
            quantityLayout.setErrorEnabled(true);
            quantityEditText.setError("Invalid entry. Enter numbers only");
            requestFocus(quantityEditText);
            return false;
        }
        else {
            quantityLayout.setErrorEnabled (false);
        }

        return true;
    }

    private boolean validateTotalCost() {
        String totalcost = totalCostEditText.getText().toString();
        if (totalCostEditText.getText().toString().trim().isEmpty()) {
            totalcostLayout.setErrorEnabled(true);
            totalCostEditText.setError("Total Cost is a required Field");
            requestFocus(totalCostEditText);
            return false;
        }
        else if(Double.parseDouble(totalcost) == Double.NaN)
        {
            totalcostLayout.setErrorEnabled(true);
            totalCostEditText.setError("Invalid entry. Enter numbers only");
            requestFocus(totalCostEditText);
            return false;

        }
        else {
            totalcostLayout.setErrorEnabled (false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    private class FormTextWatcher implements TextWatcher {

        private View view;

        private FormTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.name:
                    validateName ();
                    break;
                case R.id.quantity:
                    validateQuantity ();
                    break;
                case R.id.totalcost:
                    validateTotalCost ();
                    break;

            }
        }
    }

    private void startProgress() {
        progressFragment= ProgressDialogFragment.newInstance ("creating post");
        progressFragment.show(getSupportFragmentManager(), "progress");
    }

    private void finishProgress() {
        progressFragment.dismiss();
    }



    private void setPic() {

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

		/* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        }

		/* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

		/* Associate the Bitmap to the ImageView */
        imageView.setImageBitmap(bitmap);
        imageView.setVisibility(View.VISIBLE);

    }







}
