/**
 * Created by manabu on 2016/06/23.
 */

// 医療機関表示のための準備

var format = 'image/png';

var medicalInstituteSource = new ol.source.TileWMS({
    url: 'http://gis.titech.ichilab.org/geoserver/hcrisis/wms',
    params: {
        'FORMAT': format,
        'VERSION': '1.1.1',
        tiled: true,
        STYLES: '',
        LAYERS: 'hcrisis:medical_institute'
    }
})

var medicalInstituteLayer = new ol.layer.Tile({
    visible: true,
    source: medicalInstituteSource
});