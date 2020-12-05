package com.example.mobiledatacolection.widget.geo;

import org.osmdroid.tileprovider.IRegisterReceiver;
import org.osmdroid.tileprovider.MapTileProviderArray;
import org.osmdroid.tileprovider.modules.MapTileModuleProviderBase;

import java.io.File;
import java.util.Collections;

/**
 * This class is a simplification of the the MapTileProviderArray: it only
 * allows a single provider.
 */
public class OsmMBTileProvider extends MapTileProviderArray {

    public OsmMBTileProvider(IRegisterReceiver receiverRegistrar, File file) {

        /**
         * Call the super-constructor.
         *
         * MapTileProviderBase requires a TileSource. As far as I can tell it is
         * only used in its method rescaleCache(...) to get the pixel size of a
         * tile. It seems to me that this is inappropriate, as a MapTileProvider
         * can have multiple sources (like the module array defined below) and
         * therefore multiple tileSources which might return different values!!
         *
         * If the requirement is that the tile size is equal across tile
         * sources, then the parameter should be obtained from a different
         * location, From TileSystem for example.
         */
        super(OsmMBTileSource.createFromFile(file), receiverRegistrar);

        // Create the module provider; this class provides a TileLoader that
        // actually loads the tile from the DB.
        OsmMBTileModuleProvider moduleProvider;
        moduleProvider = new OsmMBTileModuleProvider(receiverRegistrar, (OsmMBTileSource) getTileSource());

        MapTileModuleProviderBase[] tileProviderArray;
        tileProviderArray = new MapTileModuleProviderBase[]{moduleProvider};

        // Add the module provider to the array of providers; mTileProviderList
        // is defined by the superclass.
        Collections.addAll(mTileProviderList, tileProviderArray);
    }

    // TODO: implement public Drawable getMapTile(final MapTile pTile) {}
    //       The current implementation is needlessly complex because it uses
    //       MapTileProviderArray as a basis.

}
