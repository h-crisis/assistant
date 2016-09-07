/**
 * Created by manabu on 2016/09/07.
 * 地図表示全般のJavaScript
 */

// BaseLayerの設定 OpenStreetMapを設定する
var osm_source = new ol.source.OSM({
    url: 'http://{a-c}.tile.openstreetmap.org/{z}/{x}/{y}.png'
});

var baseLayer = new ol.layer.Tile({
    source: osm_source,
    opacity: 1
});

// 地図の設定
var view = new ol.View({
    projection: 'EPSG:4326',
    center: [centerLat,centerLon],
    zoom: 9,
    minZoom: 2,
    maxZoom: 17
});

var map = new ol.Map({
    layers: [baseLayer, shelterLayer],
    target: 'map',
    view: view,
    controls: ol.control.defaults({
        attributionOptions: ({
            collapsible: false
        })
    }).extend([
        new ol.control.ScaleLine()
    ])
});

// Map上のFeatureを取得する
var displayFeatureInfo = function(pixel) {
    var feature = map.forEachFeatureAtPixel(pixel, function(feature) {
    })
};

map.on('click', function(evt) {
    displayFeatureInfo(evt.pixel);

    map.forEachFeatureAtPixel(pixel, function (feature) {
        createShelterPopupHTML(feature);
    })
})

