package org.enventureenterprises.enventure.ui.addItem;

/**
 * Created by Moses on 8/8/16.
 */

import android.os.Environment;

import java.io.File;

public final class BaseAlbumDirFactory extends AlbumStorageDirFactory {

    // Standard storage location for digital camera files
    private static final String CAMERA_DIR = "/dcim/";

    @Override
    public File getAlbumStorageDir(String albumName) {
        return new File (
                Environment.getExternalStorageDirectory()
                        + CAMERA_DIR
                        + albumName
        );
    }
}

