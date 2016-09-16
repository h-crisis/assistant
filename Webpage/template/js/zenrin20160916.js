/**
 * Created by komori on 2016/09/16.
 */

var zenrin001Layer = new ol.layer.Tile({
    source: new ol.source.TileWMS({
        url: 'http://gis.h-crisis.jp/geoserver/wms',
        params: {
            'VERSION': '1.1.1',
            tiled: true,
            STYLES: '',
            LAYERS: 'zenrin:001'
        }
    })

});

var zenrin002Layer = new ol.layer.Tile({
    source: new ol.source.TileWMS({
        url: 'http://gis.h-crisis.jp/geoserver/wms',
        params: {
            'VERSION': '1.1.1',
            tiled: true,
            STYLES: '',
            LAYERS: 'zenrin:002'
        }
    }),

});

var zenrin005Layer = new ol.layer.Tile({
    source: new ol.source.TileWMS({
        url: 'http://gis.h-crisis.jp/geoserver/wms',
        params: {
            'VERSION': '1.1.1',
            tiled: true,
            STYLES: '',
            LAYERS: 'zenrin:005'
        }
    }),

});

var zenrin0Layer = new ol.layer.Tile({
    source: new ol.source.TileWMS({
        url: 'http://gis.h-crisis.jp/geoserver/wms',
        params: {
            'VERSION': '1.1.1',
            tiled: true,
            STYLES: '',
            LAYERS: 'zenrin:0'
        }
    }),

});

var zenrin006Layer = new ol.layer.Tile({
    source: new ol.source.TileWMS({
        url: 'http://gis.h-crisis.jp/geoserver/wms',
        params: {
            'VERSION': '1.1.1',
            tiled: true,
            STYLES: '',
            LAYERS: 'zenrin:006'
        }
    }),

});

var zenrinMap = true;
if(zenrinMap) {
    map.addLayer(zenrin001Layer);
    map.addLayer(zenrin002Layer);
    map.addLayer(zenrin005Layer);
    map.addLayer(zenrin0Layer);
    map.addLayer(zenrin006Layer);
}
