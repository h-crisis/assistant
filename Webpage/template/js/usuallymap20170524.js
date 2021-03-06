/**
 * Created by komori on 2017/05/24.
 */

/**
 * Created by komori on 2017/02/07.
 */

/**
 * Created by manabu on 2016/09/07.
 * 地図表示全般のJavaScript
 */

var HeaderHtml;
var InfoHtml;
var DetailHtml;
var poppedHtml;
var preCells;
var interCells;
var subCells;

//  Enterキーでsubmitされるのを防ぐ
$(function() {
    $(document).on("keypress", "input:not(.allow_submit)", function(event) {
        return event.which !== 13;
    });
});

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

// 地図の設定。リロードした場合はlocation.jsで設定したurlから座標取得
var view = new ol.View({
    projection: 'EPSG:4326',
    center: [centerLat, centerLon],
    zoom: 11,
    minZoom: 2,
    maxZoom: 17
});
if (document.URL.indexOf("?_") > 0){
    reURL = document.URL.split(',');
    if (isFinite(reURL[1].substr(4)) && isFinite(reURL[2].substr(4)) && isFinite(reURL[3].substr(4))) {

        var centerLatLon = [parseFloat(reURL[1].substr(4)),parseFloat(reURL[2].substr(4))];
        var centerZoom =  parseFloat(reURL[3].substr(4));
        view = new ol.View({
            projection: 'EPSG:4326',
            center: centerLatLon,
            zoom: centerZoom,
            minZoom: 2,
            maxZoom: 17
        })
    }
}


// オーバレイの設定（ポップアップ）
var popupCloser = document.getElementById('popup-closer');

var markerElement = document.getElementById('popup');

var overlayPopup = new ol.Overlay(/** @type {olx.OverlayOptions} */ ({
    element: markerElement,
    autoPan: true,
    autoPanAnimation: {
        duration: 250
    }
}));

popupCloser.onclick = function() {
    document.getElementById('info').style.display = 'none';
    document.getElementById('infoHeader').style.display = 'none';
    document.getElementById('tab001').style.display = 'none';
    document.getElementById('tab002').style.display = 'none';
    document.getElementById('tab003').style.display = 'none';
    overlayPopup.setPosition(undefined);
    popupCloser.blur();
    return false;
};

var map = new ol.Map({
    layers: [baseLayer],
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

map.addControl(new ol.control.Zoom({
    className: 'custom-zoom'
}));


// map上に各アイコンのレイヤーを追加して、非表示にする。

// zip.jsを利用してzipファイルからレイヤー作成。
zipBlob("", blob, function(zippedBlob) {
    // unzip the first file from zipped data stored in zippedBlob
    hospitalUnzipBlob(zippedBlob, function(unzippedBlob) {
        // logs the uncompressed Blob
        hospLayer = new ol.layer.Vector({
            source: new ol.source.Vector({
                features:  (new ol.format.GeoJSON()).readFeatures(unzippedBlob),
                format: new ol.format.GeoJSON()
            }),
            style: function(feature, resolution) {
                styleCL.getImage().setOpacity(resolution < 0.0009 ? 1 : 0);
                return styleCL;
            }
        });
        map.addLayer(hospLayer);
        map.once('postrender', function(){
            hospLayer.setVisible(false);
        });
    });

    hcUnzipBlob(zippedBlob, function(unzippedBlob) {
        // logs the uncompressed Blob
        HCLayer = new ol.layer.Vector({
            source: new ol.source.Vector({
                features:  (new ol.format.GeoJSON()).readFeatures(unzippedBlob),
                format: new ol.format.GeoJSON()
            }),
            style: function(feature, resolution) {
                styleHC.getImage().setOpacity(resolution < 0.0009 ? 1 : 0);
                return styleHC;
            }
        });
        map.addLayer(HCLayer);
        map.once('postrender', function(){
            HCLayer.setVisible(false);
        });
    });

    shelterUnzipBlob(zippedBlob, function(unzippedBlob) {
        // logs the uncompressed Blob
        shelterLayer = new ol.layer.Vector({
            source: new ol.source.Vector({
                features:  (new ol.format.GeoJSON()).readFeatures(unzippedBlob),
                format: new ol.format.GeoJSON()
            }),
            style: function(feature, resolution) {
                styleSH.getImage().setOpacity(resolution < 0.0009 ? 1 : 0);
                return styleSH;
            }
        });
        map.addLayer(shelterLayer);
        map.once('postrender', function(){
            shelterLayer.setVisible(false);
        });
    });

});

// 避難所のマップ表示制御ボタンを呼ぶ変数。表示制御関数はshelter~.js
var hcOnOff   = document.getElementById('healthcenter-vis');
hcOnOff.addEventListener('click', HCButton);

var hospitalOnOff   = document.getElementById('hospital-vis');
hospitalOnOff.addEventListener('click', hospButton);

var shelterOnOff   = document.getElementById('shelter-vis');
shelterOnOff.addEventListener('click', shelterButton);

var zoomLevel = map.getView().getZoom();


// Map上のFeatureを取得し表示する
var displayFeatureInfo = function(pixel, evt) {
    /*
     // 避難所レイヤーのurlを取得しpopup用のHTMLとdisplay用のHTMLを作成する
     var urlShelter = shelterLayer.getSource().getGetFeatureInfoUrl(
     evt.coordinate, view.getResolution(), view.getProjection(),
     {'INFO_FORMAT': 'text/javascript'});

     if(urlShelter && shelterLayer.getVisible()) {
     createShelterPopup(urlShelter, evt);
     }

     // geoJsonを用いた各レイヤーのポイントデータを取得する
     var feature = map.forEachFeatureAtPixel(pixel, function(feature) {
     return feature;
     });
     */
    // geoJsonから情報を配列に押し込み配列の項目別に各施設のポップアップを表示する関数を呼び出す
    map.forEachFeatureAtPixel(pixel, function (feature) {
        arrayL = [];
        arrayV = [];
        for (i = 1; i < (feature.getKeys().length); i++) {
            label = feature.getKeys()[i];
            valr = feature.get(label);
            arrayL.push(label);
            if (label == "lat" || label == "lon" || label == "LAT" || label == "LON") {
                arrayV.push(valr)
            } else {
                if (isFinite(valr)) {
                    arrayV.push(Math.round(valr))
                } else {
                    arrayV.push(valr)
                }
            }
        }

        if(typeof(feature.T.dep1) == "undefined"){
        }
        else {
            arrayH = [];
            arrayH.push(feature.T.dep1,feature.T.dep2,feature.T.dep3,feature.T.dep4,feature.T.dep5,feature.T.dep6);
            arrayH.push(feature.T.dep7,feature.T.dep8,feature.T.dep9,feature.T.dep10,feature.T.dep11,feature.T.dep12);
            arrayH.push(feature.T.dep13,feature.T.dep14,feature.T.dep15,feature.T.dep16,feature.T.dep17,feature.T.dep18);
            arrayH.push(feature.T.dep19,feature.T.dep20,feature.T.dep21,feature.T.dep22);
        }

        if (arrayV[0].length == 7) {
            createHtmlHC(evt, feature);
        } else if (arrayL[0] == "CODE") {
            createHtmlshelter(evt, feature);
        } else if (arrayV[0].length == 11) {
            createHtmlhosp(evt, feature);
        }
        /*
         else if(arrayL[0]=='code') {
         createHtmlHospital(evt, feature);
         } else if(arrayL[0]=='name') {
         createHtmlPass(evt);
         }
         */

    });
};


map.on('click', function(evt,feature) {
    var coordinate = evt.coordinate;
    var pixel = map.getPixelFromCoordinate(coordinate);

    if (window.innerWidth >= 780) {
        /*
         preCells = "<tr><td id='tagCell' width='110px' style='font-size:17px;font-family:helvetica;background-color:#ffffff;padding: 2px;'>";
         interCells = "</td><td id='nameCell' width='230px' style='font-size:17px;font-family:helvetica;background-color:white;'>";
         subCells = "</td></tr>";
         */
        preCells = "<p><a ";
        interCells = ">";
        subCells = "</a></p>";
    } else if (window.innerWidth >= 480) {
        preCells = "<p><a>";
        interCells = "<a>";
        subCells = "</a></p>";
    } else {
        preCells = "<tr><td id='tagCell' width='130px' style='font-size:10px;font-family:helvetica;background-color:#ffffff;padding: 2px;'>";
        interCells = "</td><td id='nameCell' width='120px' style='font-size:10px;font-family:helvetica;background-color:white;'>";
        subCells = "</td></tr>";
    }
    document.getElementById('info').innerHTML = "";
    document.getElementById('infoHeader').style.display = 'none';
    document.getElementById('info').style.display = 'none';
    document.getElementById('popup').style.display = 'none';
    var element = document.getElementsByClassName('infoTab');
    for (var i=0;i<element.length;i++) {
        element[i].style.display = "none";
        element[i].style.backgroundColor = '';
        element[i].style.color = '';
    }
    displayFeatureInfo(evt.pixel, evt);
});

map.getView().on('change:resolution', function(evt) {
    zoomLevel = map.getView().getZoom();
});

// 画面移動時点の中心座標を拾う
map.on('moveend', function() {
    userLat = map.getView().getCenter()[0];
    userLon = map.getView().getCenter()[1];
    userZoom = map.getView().getZoom();

    var size = /** @type {ol.Size} */ (map.getSize());
    var extent = map.getView().calculateExtent(size);
});

// 15分毎にリロードする
function reflash(){
    var mesag = "情報が更新されました。画面を更新しますか？";
    if (window.confirm(mesag)) {
        relocateCenter();
        document.location.href = reURL;
    } else {
    }
}
setInterval(reflash,900000);