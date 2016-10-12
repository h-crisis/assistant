/**
 * Created by komori on 2016/08/12.
 */

var eventCode = 'test';

var pass = 'http://h-crisis.niph.go.jp/wp-content/uploads/sites/4/event/20160815/'

hypoLat = 135.770802;

hypoLon = 34.603707;

hypoPoint = ol.proj.transform([hypoLat, Math.abs(hypoLon)], 'EPSG:4326', 'EPSG:3857');
