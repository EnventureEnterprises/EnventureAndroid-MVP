package org.enventureenterprises.enventure.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;

import org.enventureenterprises.enventure.data.remote.AccessToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


/**
 * Created by Moses on 7/16/16.
 */

public class GeneralUtils {

    public static final String MIME_TYPE_AUDIO = "audio/*";
    public static final String MIME_TYPE_TEXT = "text/*";
    public static final String MIME_TYPE_IMAGE = "image/*";
    public static final String MIME_TYPE_VIDEO = "video/*";
    public static final String MIME_TYPE_APP = "application/*";
    // Initial offset for calculating the map bounds
    private static final double OFFSET_CALCULATION_INIT_DIFF = 1.0;

    // Accuracy for calculating the map bounds
    private static final float OFFSET_CALCULATION_ACCURACY = 0.01f;

    // Conversion from feet to meters
    private static final float METERS_PER_FEET = 0.3048f;

    public static AccessToken accessToken;






    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {


                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static Uri getUri(File file) {
        if (file != null) {
            return Uri.fromFile(file);
        }
        return null;
    }

    public static String getExtension(String uri) {
        if (uri == null) {
            return null;
        }

        int dot = uri.lastIndexOf(".");
        if (dot >= 0) {
            return uri.substring(dot);
        } else {
            // No extension.
            return "";
        }
    }


    public static String getMimeType(File file) {

        String extension = getExtension(file.getName());

        if (extension.length() > 0)
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.substring(1));

        return "application/octet-stream";
    }


    public static Bitmap getThumbnail(Context context, File file) {
        return getThumbnail(context, getUri(file), getMimeType(file));
    }


    public static File getFile(Context context, Uri uri) {
        if (uri != null) {
            String path = getPath(context, uri);
            if (path != null && isLocal(path)) {
                return new File(path);
            }
        }
        return null;
    }

    /**
     * @return Whether the URI is a local one.
     */
    public static boolean isLocal(String url) {
        if (url != null && !url.startsWith("http://") && !url.startsWith("https://")) {
            return true;
        }
        return false;
    }

    /**
     * @param uri The Uri to check.
     */
    public static boolean isLocalStorageDocument(Uri uri) {
        return "org.enventureenterprises.enventure.fileprovider".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }


    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static boolean isMediaUri(Uri uri) {
        return "media".equalsIgnoreCase(uri.getAuthority());
    }


    public static Bitmap getThumbnail(Context context, Uri uri, String mimeType) {

        if (!isMediaUri(uri)) {

            return null;
        }

        Bitmap bm = null;
        if (uri != null) {
            final ContentResolver resolver = context.getContentResolver();
            Cursor cursor = null;
            try {
                cursor = resolver.query(uri, null, null, null, null);
                if (cursor.moveToFirst()) {
                    final int id = cursor.getInt(0);


                    if (mimeType.contains("video")) {
                        bm = MediaStore.Video.Thumbnails.getThumbnail(
                                resolver,
                                id,
                                MediaStore.Video.Thumbnails.MINI_KIND,
                                null);
                    } else if (mimeType.contains(GeneralUtils.MIME_TYPE_IMAGE)) {
                        bm = MediaStore.Images.Thumbnails.getThumbnail(
                                resolver,
                                id,
                                MediaStore.Images.Thumbnails.MINI_KIND,
                                null);
                    }
                }
            } catch (Exception e) {

            } finally {
                if (cursor != null)
                    cursor.close();
            }
        }
        return bm;
    }


    public static String getPath(final Context context, final Uri uri) {


        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // LocalStorageProvider
            if (isLocalStorageDocument(uri)) {
                // The path is the id
                return DocumentsContract.getDocumentId(uri);
            }
            // ExternalStorageProvider
            else if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static boolean areCrashesEnabled(Context context) {
        SharedPreferences preferences;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("are_crashes_enabled", false);
    }



   /* public static void reloadToken(final Context context,Model model){

        long now = System.currentTimeMillis();

        if(PrefUtils.getTokenExpires (context) < now) {

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor ();
            interceptor.setLevel (HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder ().addInterceptor (interceptor).build ();



            TwitterAuthToken authToken;


            TwitterSession tsession = Twitter.getSessionManager ().getActiveSession ();
            DigitsSession dsession = Digits.getActiveSession ();

            if ( tsession != null || dsession != null ) {

                if ( dsession != null ) {
                    authToken = dsession.getAuthToken ();
                } else {
                    authToken = tsession.getAuthToken ();
                }

                Retrofit retrofit2 = new Retrofit.Builder ()
                        .baseUrl (Config.BaseEndpoint)
                        .client (client)
                        .addConverterFactory (GsonConverterFactory.create ())
                        .addCallAdapterFactory (RxJavaCallAdapterFactory.create ())
                        .build ();
                UrbApiService apiService = retrofit2.create (UrbApiService.class);
                String token = "oauth_token=" + authToken.token + "%26oauth_token_secret=" + authToken.secret;
                Call<AccessToken> call = apiService.getAccessToken (Config.CLIENT_ID, token, "convert_token", "twitter");


                call.enqueue (new retrofit2.Callback<AccessToken> () {
                    @Override
                    public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                        if ( response.code () == 200 ) {

                            accessToken = response.body ();


                    *//*

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("token", accessToken.getAccessToken());
                    editor.apply();
                    *//*

                            PrefUtils.setAuthToken (context, accessToken.getAccessToken ());
                            long now = System.currentTimeMillis ();
                            long expires = accessToken.getExpiresIn ()*1000;
                            PrefUtils.setTokenExpires (context, now + expires);
                            PrefUtils.setLoggedIn (context);





                                model.sendInstallation (PrefUtils.getFirebaseToken (context)).observeOn (Schedulers.newThread ())
                                        .subscribeOn (Schedulers.io ())
                                        .subscribe (new Action1<BaseResponse> () {
                                            @Override
                                            public void call(BaseResponse res) {

                                            }
                                        }, new Action1<Throwable> () {
                                            @Override
                                            public void call(Throwable throwable) {


                                            }
                                        });


                                model.getProfile (true).observeOn (Schedulers.newThread ())
                                        .subscribeOn (Schedulers.io ())
                                        .subscribe (new Action1<User> () {
                                            @Override
                                            public void call(User user) {

                                            }
                                        }, new Action1<Throwable> () {
                                            @Override
                                            public void call(Throwable throwable) {


                                            }
                                        });


                                model.getPref (true).observeOn (Schedulers.newThread ())
                                        .subscribeOn (Schedulers.io ())
                                        .subscribe (new Action1<Pref> () {
                                            @Override
                                            public void call(Pref pref) {

                                            }
                                        }, new Action1<Throwable> () {
                                            @Override
                                            public void call(Throwable throwable) {


                                            }
                                        });



                        } else {
                            Timber.e (response.errorBody ().toString ());
                        }


                    }

                    @Override
                    public void onFailure(Call<AccessToken> call, Throwable t) {
                        //Handle failure
                        Timber.e (t.getMessage ());
                    }
                });
            }
        }

    }*/

    public List<Float> getSearchDistanceAvailableOptions() {
        final List<Float> options = Arrays.asList(250.0f, 1000.0f, 2000.0f, 5000.0f);

        List<Float> typedOptions = new ArrayList<Float>();
        for (Number option : options) {
            typedOptions.add(option.floatValue());
        }

        return typedOptions;
    }




    // Returns the URI path to the Bitmap displayed in specified ImageView
    public static Uri getLocalBitmapUri(Context mContext, ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            // Use methods on Context to access package-specific directories on external storage.
            // This way, you don't need to request external read/write permission.
            // See https://youtu.be/5xVh-7ywKpE?t=25m25s
            File file = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }




    private static final SimpleDateFormat[] ACCEPTED_TIMESTAMP_FORMATS = {
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US),
            new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy", Locale.US),
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US),
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.US),
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US),
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US),
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US),
            new SimpleDateFormat ("yyyy-MM-dd'T'HH:mm:ss Z", Locale.US)
    };

    private static final SimpleDateFormat VALID_IFMODIFIEDSINCE_FORMAT =
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);


    public static Date parseTimestamp(String timestamp) {
        for (SimpleDateFormat format : ACCEPTED_TIMESTAMP_FORMATS) {
            // TODO: We shouldn't be forcing the time zone when parsing dates.
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            try {
                return format.parse(timestamp);
            } catch (ParseException ex) {
                continue;
            }
        }

        // All attempts to parse have failed
        return null;
    }





}
