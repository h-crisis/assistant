/**
 * Created by manabu on 2016/06/08.
 */
// 現在位置を取得するJavaScript


function Location() {
    this.x = 0.0;
    this.y = 0.0;
    this.z = 0.0;
}

Location.prototype = {
    getLocation: function() {
        if( navigator.geolocation ) {
            alert("Yes");
        }
        else {
            alert("no");
        }
    }
}