package org.enventureenterprises.enventure.ui.addItem;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDelegate;
import android.view.WindowManager;
import android.widget.Toast;

import org.enventureenterprises.enventure.R;
import org.enventureenterprises.enventure.ui.base.BaseActivity;
import org.enventureenterprises.enventure.util.Config;
import org.enventureenterprises.enventure.util.GeneralUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import io.realm.Realm;
import timber.log.Timber;

/**
 * Created by mossplix on 7/6/17.
 */

public class AddItemActivity extends BaseActivity {
    private Realm realm;


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
        ButterKnife.bind(this);




        setContentView(R.layout.additem_activity);
        ButterKnife.bind(this);



        realm = Realm.getDefaultInstance ();



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mImageBitmap = null;

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAlbumStorageDirFactory = new FroyoAlbumDirFactory ();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumDirFactory ();
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);



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
            galleryAddPic();
            mCurrentPhotoPath = null;
        }

    }

    private File getAlbumDir() {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir("Urb");

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
                    //noPhoto.setVisibility(View.INVISIBLE);

                }
                else{
                    Toast.makeText(AddItemActivity.this,"Unable to get camera image. Please select from gallery",Toast.LENGTH_SHORT).show();
                }
                break;
            case Config.ACTIVITY_SELECT_IMAGE:
                if (resultCode == Activity.RESULT_OK) {

                    photo = data.getData();
                    Bitmap img= GeneralUtils.getThumbnail(AddItemActivity.this,data.getData(),GeneralUtils.MIME_TYPE_IMAGE );
                    //imageView.setImageBitmap(img);
                    Timber.d("Image selected: "+data.getData());
                    //noPhoto.setVisibility(View.INVISIBLE);

                }
                break;
            default:
                break;


        }


    }





    //@OnClick(R.id.submit)
    public void submitItem() {

    }
}
