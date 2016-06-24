/**
 * Created by manabu on 2016/06/23.
 */

// 避難所表示のための準備

var shelterSource = new ol.source.Vector({
    format: new ol.format.GeoJSON(),
    url: function(extent) {
        return 'http://gis.titech.ichilab.org/geoserver/wfs?service=WFS&' +
            'version=1.1.0&request=GetFeature&typename=hcrisis:shelter&' +
            'outputFormat=application/json&srsname=EPSG:4612&' +
            'bbox=' + extent.join(',') + ',EPSG:3857';
    },
    strategy: ol.loadingstrategy.tile(ol.tilegrid.createXYZ({
        maxZoom: 19
    }))
});


var shelterLayer = new ol.layer.Vector({
    source: shelterSource,
    style: new ol.style.Style({
        stroke: new ol.style.Stroke({
            color: 'rgba(0, 0, 255, 1.0)',
            width: 2
        })
    })
});