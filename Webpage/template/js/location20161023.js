/**
 * Created by komori on 2016/10/19.
 * 位置情報全般のJavaScript
 */


// デフォルトの中心に移動する
var goCenter = document.getElementById('goToHypoCenter');
goCenter.addEventListener('click', goHypoButton);
function goHypoButton() {
    var pan = ol.animation.pan({
        duration: 2000,
        source: (view.getCenter())
    });
    map.beforeRender(pan);
    map.getView().setCenter(hypoPoint);
    map.getView().setZoom(11);
}

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
