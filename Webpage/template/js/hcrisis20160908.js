/**
 * Created by manabu on 2016/09/07.
 * 地図表示全般のJavaScript
 */

// BaseLayerの設定 OpenStreetMapを設定する
var osm_source = new ol.source.OSM({
    url: 'http://{a-c}.tile.openstreetmap.org/{z}/{x}/{y}.png'
});
var xyz_source = new ol.source.XYZ({
    url : 'http://cyberjapandata.gsi.go.jp/xyz/std/{z}/{x}/{y}.png'
});

var bingmaps_basemap = new ol.source.BingMaps({
    key: 'Ak-dzM4wZjSqTlzveKz5u0d4IQ4bRzVI309GxmkgSVr1ewS6iPSrOvOKhA-CJlm3',
    imagerySet: 'Road'
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


// オーバレイの設定（ポップアップ）
var popupCloser = document.getElementById('popup-closer');

var overlayPopup = new ol.Overlay(/** @type {olx.OverlayOptions} */ ({
    element: document.getElementById('popup'),
    autoPan: true,
    autoPanAnimation: {
        duration: 250
    }
}));

popupCloser.onclick = function() {
    document.getElementById('info').style.display = 'none';
    overlayPopup.setPosition(undefined);
    popupCloser.blur();
    return false;
};

var map = new ol.Map({
    layers: [baseLayer, shelterLayer],
    overlays: [overlayPopup],
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

// Map上のFeatureを取得し表示する
var displayFeatureInfo = function(pixel, evt) {
    var popupHtml;
    var displayHtml;

    // 避難所レイヤーのurlを取得しpopup用のHTMLとdisplay用のHTMLを作成する
    var urlShelter = shelterLayer.getSource().getGetFeatureInfoUrl(
        evt.coordinate, view.getResolution(), view.getProjection(),
        {'INFO_FORMAT': 'text/javascript'});

    if(urlShelter) {
        createShelterPopup(urlShelter, evt);
    }
};

map.on('click', function(evt) {
    displayFeatureInfo(evt.pixel, evt);
})

