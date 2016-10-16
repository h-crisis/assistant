/**
 * Created by manabu on 2016/09/07.
 * 地図表示全般のJavaScript
 */

var HeaderHtml;
var InfoHtml;
var DetailHtml;
var preCells = "<tr><td width='110px' style=font-size:17px;background-color:#888888;color:white>";
var interCells = "</td><td width='250px' style=font-size:17px;background-color:white;text-align:right;>";
var subCells ="</td></tr>";

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
    document.getElementById('infoHeader').style.display = 'none';
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

// map上に各アイコンのレイヤーを追加して、非表示にする。
map.addLayer(shelterLayer);
map.addLayer(hospLayer);
map.addLayer(hallLayer);
map.addLayer(ImpassableLayer);
shelterLayer.setVisible(false);
hospLayer.setVisible(false);
hallLayer.setVisible(false);
ImpassableLayer.setVisible(false);

// 避難所のマップ表示制御ボタンを呼ぶ変数。表示制御関数はshelter~.js
var shelterOnOff   = document.getElementById('shelter-vis');
shelterOnOff.addEventListener('click', shelterButton);

// 役所のマップ表示制御ボタンを呼ぶ変数。表示制御関数はhalls~.js
var hallOnOff = document.getElementById('hall-vis');
hallOnOff.addEventListener('click', hallButton);

// 医療機関のマップ表示制御ボタンを呼ぶ変数。表示制御関数はhospital~.js
var hospOnOff   = document.getElementById('hospital-vis');
hospOnOff.addEventListener('click', hospButton);

// 交通不可地点のマップ表示制御ボタンを呼ぶ変数。表示制御関数はpass~.js
var visPass   = document.getElementById('pass-vis');
visPass.addEventListener('click', visPassButton);

var zoomLevel = map.getView().getZoom();

// Map上のFeatureを取得し表示する
var displayFeatureInfo = function(pixel, evt) {
    var popupHtml;
    var displayHtml;

    // 避難所レイヤーのurlを取得しpopup用のHTMLとdisplay用のHTMLを作成する
    var urlShelter = shelterLayer.getSource().getGetFeatureInfoUrl(
        evt.coordinate, view.getResolution(), view.getProjection(),
        {'INFO_FORMAT': 'text/javascript'});

    if(urlShelter && shelterLayer.getVisible()) {
        createShelterPopup(urlShelter, evt);
    }

    // geoJsonを用いた各レイヤーのポイントデータを取得する
    var feature = map.forEachFeatureAtPixel(pixel, function(feature) {
    });

    // geoJsonから情報を配列に押し込み配列の項目別に各施設のポップアップを表示する関数を呼び出す
    map.forEachFeatureAtPixel(pixel, function (feature) {
        arrayL = [];
        arrayV = [];
        for (i = 1; i < (feature.getKeys().length); i++) {
            label = feature.getKeys()[i];
            valr = feature.get(label);
            arrayL.push(label);
            arrayV.push(valr);
        }

        if(arrayL[9]=='p_num') {
            createHtmlHall();
        }

        else if(arrayL[0]=='code') {
            createHtmlHospital();
        } else if(arrayL[0]=='name') {
            createHtmlPass();
        }
    });
};

map.on('click', function(evt) {
    var coordinate = evt.coordinate;
    var pixel = map.getPixelFromCoordinate(coordinate);
    document.getElementById('info').innerHTML = "";
    document.getElementById('infoHeader').style.display = 'none';
    document.getElementById('info').style.display = 'none';
    document.getElementById('popup').style.display = 'none';
    displayFeatureInfo(evt.pixel, evt);
    console.log(map.getView().getZoom())
});

map.getView().on('change:resolution', function(evt) {
   zoomLevel = map.getView().getZoom();
});



