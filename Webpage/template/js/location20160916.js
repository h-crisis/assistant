/**
 * Created by manabu on 2016/09/07.
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
        var nowLatLon = nowLatLon.split(',');;
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

function nowLocation(){
    var nowTime = new Date();
    var nowYer = nowTime.getFullYear();
    var nowMon = nowTime.getMonth() + 1;
    var nowDay = nowTime.getDate();
    var nowHrs = nowTime.getHours();
    var nowMin = nowTime.getMinutes();
    var nowSec = nowTime.getSeconds();
    nowTime = + nowYer + "/" + nowMon + "/" + nowDay + "/" + nowHrs + ":" + nowMin + ":" + nowSec;
    var pop = new ol.Overlay({
        element: document.getElementById('pop')
    });
    var rand = ((Math.random() - 0.5) * 0.01);
    if (gettableNC = true) {
        navigator.geolocation.getCurrentPosition(
            // 位置情報取得成功時
            function location(pos) {
                nowLatLon = pos.coords.longitude + "," + pos.coords.latitude;
                document.getElementById('latlonCurr').value = nowLatLon;
                console.log(nowLatLon,nowTime);
                var lon = pos.coords.longitude + rand;
                var lat = pos.coords.latitude + rand;
                pop.setPosition([lon,lat]);
                map.addOverlay(pop);
            }),
        function (error)
        {
            var message = "";

            switch (error.code) {
                // 位置情報取得できない場合
                case error.POSITION_UNAVAILABLE:
                    message = "位置情報の取得ができませんでした。";
                    break;
                // Geolocation使用許可されない場合
                case error.PERMISSION_DENIED:
                    message = "位置情報取得の使用許可がされませんでした。";
                    break;
                // タイムアウトした場合
                case error.PERMISSION_DENIED_TIMEOUT:
                    message = "位置情報取得中にタイムアウトしました。";
                    break;
            }
            window.alert(message);
            return nowLatLon;
        }
    }};

setInterval(nowLocation,6000);