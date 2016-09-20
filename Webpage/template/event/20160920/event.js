/**
 * Created by komori on 2016/08/12.
 */

var eventCode = 'test';
var pass;
var mode = 'estimate';

if (mode == 'estimate') {
    hypoLat = 135.770802;
    hypoLon = 34.603707;
    var hypoPoint = ol.proj.transform([hypoLat, Math.abs(hypoLon)], 'EPSG:4326', 'EPSG:3857');
    pass = 'http://h-crisis.niph.go.jp/wp-content/uploads/sites/4/event/20160815/'
    } else if (mode == 'real') {
    var centerLat = 139.620707;
    var centerLon = 35.790259;
    pass = './'
//var pass = 'http://h-crisis.niph.go.jp/wp-content/uploads/sites/4/event/' + eventCode + '/';
    var wms = 'http://map.ichilab.org:80/geoserver/wms'
}

function estimate(){

}




