/**
 * Created by komori on 2016/08/12.
 */

var eventCode = '20160826'
var pass = 'http://h-crisis.niph.go.jp/wp-content/uploads/sites/4/event/' + eventCode;

shlterFilter = '南国市';

hypoLat = 133.531846;

hypoLon = 33.572346;

hypoPoint = ol.proj.transform([hypoLat, Math.abs(hypoLon)], 'EPSG:4326', 'EPSG:3857');