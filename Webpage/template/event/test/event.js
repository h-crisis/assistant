/**
 * Created by komori on 2016/08/12.
 */

var eventCode = 'test';
var pass = './'
//var pass = 'http://h-crisis.niph.go.jp/wp-content/uploads/sites/4/event/' + eventCode + '/';
var wms = 'http://map.ichilab.org:80/geoserver/wms'

shlterFilter = '南国市';

hypoLat = 133.531846;

hypoLon = 33.572346;

hypoPoint = ol.proj.transform([hypoLat, Math.abs(hypoLon)], 'EPSG:4326', 'EPSG:3857');