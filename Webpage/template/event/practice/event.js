/**
 * Created by komori on 2016/08/12.
 */

// eventCodeと経度（lat)と緯度（lon）を指定する
var eventCode = 'practice';
var centerLat = 139.620707;
var centerLon = 35.790259;
var arrayL = new Array(0);
var arrayV = new Array(0);
var zoomLevel = 2;

var shelterLayer = 'event_shelter_' + eventCode;

var pass = './'
//var pass = 'http://h-crisis.niph.go.jp/wp-content/uploads/sites/4/event/' + eventCode + '/';
var wms = 'http://gis.h-crisis.jp/geoserver/wms';
//var wms = 'http://map.ichilab.org:80/geoserver/wms';
