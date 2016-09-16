/**
 * Created by komori on 2016/06/20.
 */

var nowLatLon;

if (navigator.geolocation) {
    // 現在の位置情報取得を実施
    navigator.geolocation.getCurrentPosition(
        // 位置情報取得成功時
        function location(pos) {
            nowLatLon = pos.coords.longitude + "," + pos.coords.latitude
            document.getElementById('latlonCurr').value=nowLatLon;
            gettableNC = true;
        },
        // 位置情報取得失敗時
        function (error) {
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
        });
} else {
    window.alert("本ブラウザではGeolocationが使えません");
}