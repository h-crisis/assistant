/**
 * Created by komori on 2016/08/12.
 */

var pass = 'http://h-crisis.niph.go.jp/wp-content/uploads/sites/4/event/20160831/'

shlterFilter = '';

hypoLat = 141.346376;

hypoLon = 39.576466;

hypoPoint = ol.proj.transform([hypoLat, Math.abs(hypoLon)], 'EPSG:4326', 'EPSG:3857');