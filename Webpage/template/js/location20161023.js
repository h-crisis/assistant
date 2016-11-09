/**
 * Created by komori on 2016/10/19.
 * 位置情報全般のJavaScript
 */

//
    
// Webブラウザから取得した現在地に移動する
var goHere = document.getElementById('goToHere');
goHere.addEventListener('click', goToHere);
function goToHere() {
    var nowLatLon = document.getElementById('latlonCurr').value;
    if (nowLatLon === ""){
        alert("現在地が取得されていません。")
    } else {
        nowLatLon = nowLatLon.split(',');
        var latNow = parseFloat(nowLatLon[0].substr(0, 8));
        var lonNow = parseFloat(nowLatLon[1].substr(0, 8));
        nowLatLon = [latNow, lonNow];
        var start = +new Date();
        var pan = ol.animation.pan({
            duration: 2000,
            source: (view.getCenter()),
            start: start
        });
        var bounce = ol.animation.bounce({
            duration: 2000,
            resolution: 4 * view.getResolution(),
            start: start
        });
        map.beforeRender(pan, bounce);
        map.getView().setCenter(nowLatLon);
        map.getView().setZoom(15);
    }
}

//　eventで設定した中心地に移動する
var goHypocenter = document.getElementById('goToHypoCenter');
goHypocenter.addEventListener('click', goToHypo);
function goToHypo() {
    var centerLatLon = [centerLat,centerLon];
    var start = +new Date();
    var pan = ol.animation.pan({
        duration: 2000,
        source: (view.getCenter()),
        start: start
    });
    var bounce = ol.animation.bounce({
        duration: 2000,
        resolution: 4 * view.getResolution(),
        start: start
    });
    map.beforeRender(pan, bounce);
    map.getView().setCenter(centerLatLon);
    map.getView().setZoom(15);
}

// リロード時に中心座標をリロード前のものと同一にするためurlに座標を渡す
function relocateCenter(){
    if (reURL.indexOf("?")) {
        reURL = document.URL.substr( 0, document.URL.indexOf("?")) + "?,lat=" + userLat +",lon=" + userLon + ",zom=" +userZoom;
    } else {
        reURL = document.URL + "?,lat=" + userLat + ",lon=" + userLon + ",zom=" + userZoom;
    }
}