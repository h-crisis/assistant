/**
 * Created by komori on 2016/06/14.
 */

var styleOffice = new ol.style.Style({
    text: new ol.style.Text({
        font: '12px Calibri,sans-serif'
    }),
    image: new ol.style.Icon({
        scale: 0.06,
        anchor: [0.5, 1],
        anchorXUnits: 'fraction',
        anchorYUnits: 'fraction',
        opacity: 0.85,
        src: "img/b.png"
    })
});

var officeLayer = new ol.layer.Vector({
    source: new ol.source.Vector({
        url: 'geojson/Office.geojson',
        format: new ol.format.GeoJSON(),
    }),
    style: function(feature, resolution) {
        styleOffice.getText().setText(resolution < 10 ? feature.get('P34_003') : '');
        return styleOffice;
    }
});



// 役所ボタンの挙動を制御する関数
function officeButton() {
    if (officeLayer.getVisible()) {
        officeLayer.setVisible(false);
        this.style.backgroundColor = "whitesmoke";
        this.style.color = "black";
        console.log(officeList);
    } else {
        officeLayer.setVisible(true);
        this.style.backgroundColor = "black";
        this.style.color = "whitesmoke";
    }
};