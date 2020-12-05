package com.example.mobiledatacolection.widget.geo;

import android.graphics.drawable.Drawable;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.IRegisterReceiver;
import org.osmdroid.tileprovider.modules.MapTileFileStorageProviderBase;
import org.osmdroid.tileprovider.modules.MapTileModuleProviderBase;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.util.StreamUtils;

import java.io.InputStream;

import timber.log.Timber;

public class OsmMBTileModuleProvider extends MapTileFileStorageProviderBase {

    protected OsmMBTileSource tileSource;

    public OsmMBTileModuleProvider(IRegisterReceiver receiverRegistrar, OsmMBTileSource tileSource) {

        // Call the super constructor
        super(receiverRegistrar,
                Configuration.getInstance().getTileFileSystemThreads(),
                Configuration.getInstance().getTileFileSystemMaxQueueSize());

        // Initialize fields
        this.tileSource = tileSource;

    }

    @Override
    protected String getName() {
        return "MBTiles File Archive Provider";
    }

    @Override
    protected String getThreadGroupName() {
        return "mbtilesarchive";
    }

    @Override
    public MapTileModuleProviderBase.TileLoader getTileLoader() {
        return new TileLoader();
    }

    @Override
    public boolean getUsesDataConnection() {
        return false;
    }

    @Override
    public int getMinimumZoomLevel() {
        return tileSource.getMinimumZoomLevel();
    }

    @Override
    public int getMaximumZoomLevel() {
        return tileSource.getMaximumZoomLevel();
    }

    @Override
    public void setTileSource(ITileSource tileSource) {
        Timber.w("*** Warning: someone's trying to reassign MBTileModuleProvider's tileSource!");
        if (tileSource instanceof OsmMBTileSource) {
            this.tileSource = (OsmMBTileSource) tileSource;
        } else {
        // logger.warn("*** Warning: and it wasn't even an MBTileSource! That's just rude!");

        }
    }

    private class TileLoader extends MapTileModuleProviderBase.TileLoader {

        @Override
        public Drawable loadTile(long mapTileIndex) {
            InputStream inputStream = null;

            try {
                inputStream = tileSource.getInputStream(mapTileIndex);

                if (inputStream != null) {

                    // Note that the finally clause will be called before
                    // the value is returned!
                    return tileSource.getDrawable(inputStream);
                }

            } catch (Throwable e) {
                Timber.e(e, "Error loading tile");

            } finally {
                if (inputStream != null) {
                    StreamUtils.closeStream(inputStream);
                }
            }

            return null;
        }
    }

}
