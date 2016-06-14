/**
 * Created by manabu on 2016/05/21.
 */

var styleHosp = new ol.style.Style({
    text: new ol.style.Text({
        font: '12px Calibri,sans-serif'
    }),
    image: new ol.style.Icon({
        scale: 0.2,
        anchor: [0.5, 1],
        anchorXUnits: 'fraction',
        anchorYUnits: 'fraction',
        opacity: 0.85,
        src: "img/hospital.png"
    })
});

var hospLayer = new ol.layer.Vector({
    source: new ol.source.Vector({
        url: 'geojson/Hospital.geojson',
        format: new ol.format.GeoJSON()
    }),
    style: function(feature, resolution) {
        styleHosp.getText().setText(resolution < 10 ? feature.get('医療機関名') : '');
        return styleHosp;
    }
});

// 病院ボタンの挙動を制御する関数
function hospButton() {
    if (hospLayer.getVisible()) {
        hospLayer.setVisible(false);
        this.style.backgroundColor = "whitesmoke";
    } else {
        hospLayer.setVisible(true);
        this.style.backgroundColor = "red";
    }
};