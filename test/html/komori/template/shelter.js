/**
 * Created by manabu on 2016/05/19.
 */

var styleHinan = new ol.style.Style({
    text: new ol.style.Text({
        font: '12px Calibri,sans-serif'
    }),
    image: new ol.style.Icon({
        scale: 0.05,
        anchor: [0.5, 1],
        anchorXUnits: 'fraction',
        anchorYUnits: 'fraction',
        opacity: 0.85,
        src: "img/shelter_green.png"
    })
});

var hinanLayer1 = new ol.layer.Vector({
    source: new ol.source.Vector({
        url: 'geojson/HinanDay1.geojson',
        format: new ol.format.GeoJSON()
    }),
    style: function(feature, resolution) {
        var hinanNum = feature.get('避難者数');
        styleHinan.getText().setText(resolution < 10 ? feature.get('名称') + "(避難者数"　+ feature.get('避難者数') + "人)": '');
        styleHinan.getImage().setScale(hinanNum > 300 ? 0.15 : hinanNum > 100 ? 0.1 : hinanNum > 20 ? 0.07 :  0.05);
        styleHinan.getImage().setOpacity(hinanNum > 0 ? 0.85 : 0);
        return styleHinan;
    }
});

var hinanLayer4 = new ol.layer.Vector({
    source: new ol.source.Vector({
        url: 'geojson/HinanDay4.geojson',
        format: new ol.format.GeoJSON()
    }),
    style: function(feature, resolution) {
        var hinanNum = feature.get('避難者数');
        styleHinan.getText().setText(resolution < 10 ? feature.get('名称') + "(避難者数"　+ feature.get('避難者数') + "人)": '');
        styleHinan.getImage().setScale(hinanNum > 300 ? 0.15 : hinanNum > 100 ? 0.1 : hinanNum > 20 ? 0.07 : 0.05);
        styleHinan.getImage().setOpacity(hinanNum > 0 ? 0.85 : 0);
        return styleHinan;
    }
});

var hinanLayer14 = new ol.layer.Vector({
    source: new ol.source.Vector({
        url: 'geojson/HinanDay14.geojson',
        format: new ol.format.GeoJSON()
    }),
    style: function(feature, resolution) {
        var hinanNum = feature.get('避難者数');
        styleHinan.getText().setText(resolution < 10 ? feature.get('名称') + "(避難者数"　+ feature.get('避難者数') + "人)": '');
        styleHinan.getImage().setScale(hinanNum > 300 ? 0.15 : hinanNum > 100 ? 0.1 : hinanNum > 20 ? 0.07 : 0.05);
        styleHinan.getImage().setOpacity(hinanNum > 0 ? 0.85 : 0);
        return styleHinan;
    }
});
