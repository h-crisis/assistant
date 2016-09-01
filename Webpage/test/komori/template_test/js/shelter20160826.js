/**
 * Created by manabu on 2016/05/19.
 */

var styleHinan = new ol.style.Style({
    text: new ol.style.Text({
        font: '12px Calibri,sans-serif',
        fill: new ol.style.Fill({color: "#000000"}),
        stroke: new ol.style.Stroke({color: "#00ff00", width: 2}),
    }),
    image: new ol.style.Icon({
        scale: 0.5,
        anchor: [0.5, 1],
        anchorXUnits: 'fraction',
        anchorYUnits: 'fraction',
        opacity: 0.85,
        src: "http://h-crisis.niph.go.jp/assistant/wp-content/uploads/sites/4/test/img/shelter_green.png"
    })
});

/*
var shltrJPN = new ol.layer.Vector({
    source: new ol.source.Vector({
        url: 'geojson/shelterJPN.geojson',
        format: new ol.format.GeoJSON(),
    }),
    style: function(feature, resolution) {
        var hinanNum = feature.get('避難所コー');
        styleHinan.getText().setText(resolution < 10 ? feature.get('名称') + "(避難者数"　+ feature.get('避難者数') + "人)": '');
        styleHinan.getImage().setScale(hinanNum > 300 ? 0.5 : hinanNum > 100 ? 1 : hinanNum > 20 ? 0.07 :  0.5);
        styleHinan.getImage().setOpacity(hinanNum > 0 ? 0.85 : 0);
        return styleHinan;
    }
});
*/

var urlS = pass + 'geojson/shelter.geojson';

var hinanLayer1 = new ol.layer.Vector({
    source: new ol.source.Vector({
        url: urlS,
        format: new ol.format.GeoJSON()
    }),
    style: function(feature, resolution) {
        var hinanNum = Math.round(feature.get('S01'));
        styleHinan.getImage().setScale(hinanNum > 1000 ? 0.7 : hinanNum > 300 ? 0.5 : hinanNum > 20 ? 0.3 :  0.1);
        if (shlterFilter == '') {
            styleHinan.getText().setText(resolution < 15 ?  feature.get('ID') : '');
            styleHinan.getImage().setOpacity(0.85);
        } else {
            styleHinan.getText().setText(resolution < 15 && feature.get('sikuchoson') == shlterFilter ?  feature.get('ID') : '');
            styleHinan.getImage().setOpacity(feature.get('sikuchoson') == shlterFilter ? 0.85 : 0);
        }
        return styleHinan;
    }
});

/*
var hinanLayer4 = new ol.layer.Vector({
    source: new ol.source.Vector({
        url: urlS,
        format: new ol.format.GeoJSON()
    }),
    style: function(feature, resolution) {
        var hinanNum = Math.round(feature.get('S01'));
        styleHinan.getText().setText(resolution < 30 && feature.get('sikuchoson') == '伊勢原市' ?  feature.get('ID') : '');
        styleHinan.getImage().setScale(hinanNum > 1000 ? 0.7 : hinanNum > 300 ? 0.5 : hinanNum > 20 ? 0.3 : 0.1);
        styleHinan.getImage().setOpacity(hinanNum > 0 ? 0.85 : 0);
        return styleHinan;
    }
});

var hinanLayer14 = new ol.layer.Vector({
    source: new ol.source.Vector({
        url: urlS,
        format: new ol.format.GeoJSON()
    }),
    style: function(feature, resolution) {
        var hinanNum = Math.round(feature.get('S01'));
        styleHinan.getText().setText(resolution < 30 ? "ID:" + feature.get('ID') + "(避難者数:"　+ hinanNum + "人)": '');
        styleHinan.getImage().setScale(hinanNum > 300 ? 0.7 : hinanNum > 100 ? 0.5 : hinanNum > 20 ? 0.3 : 0.1);
        styleHinan.getImage().setOpacity(hinanNum > 0 ? 0.85 : 0);
        return styleHinan;
    }
});
*/