package org.enventureenterprises.enventure.ui.addItem;

import java.io.File;

/**
 * Created by Moses on 8/8/16.
 */

abstract class AlbumStorageDirFactory {
    public abstract File getAlbumStorageDir(String albumName);
}
