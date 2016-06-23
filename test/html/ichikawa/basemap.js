/**
 * Created by manabu on 2016/06/23.
 */

// 国土地理院の背景地図を利用する準備
var gsiMap = new ol.source.XYZ({
    url : 'http://cyberjapandata.gsi.go.jp/xyz/std/{z}/{x}/{y}.png',
    attribution  : "<a href='http://www.gsi.go.jp/kikakuchousei/kikakuchousei40182.html' target='_blank'>国土地理院</a>"
});

var gsiMapLayer = new ol.layer.Tile({
    source: gsiMap,
    opacity: 1
});
