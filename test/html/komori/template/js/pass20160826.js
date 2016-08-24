/**
 * Created by komori on 2016/07/20.
 */

var styleImpassable = new ol.style.Style({
    text: new ol.style.Text({
        font: '12px Calibri,sans-serif',
        fill: new ol.style.Fill({color: "#000000"}),
        stroke: new ol.style.Stroke({color: "#ffff00", width: 2}),
    }),
    image: new ol.style.Icon({
        scale: 1,
        anchor: [0.5, 0.5],
        anchorXUnits: 'fraction',
        anchorYUnits: 'fraction',
        opacity: 1,
        src: 'http://h-crisis.niph.go.jp/assistant/wp-content/uploads/sites/4/test/img/X.png'
    })
});

var urlP = pass + 'geojson/Inpassable_Kouchi.geojson';

var ImpassableLayer = new ol.layer.Vector({
    source: new ol.source.Vector({
        url: urlP,
        format: new ol.format.GeoJSON()
    }),
    style: function(feature) {
        styleImpassable.getText().setText(feature.get('name') +  ":" + feature.get('status'));
        return styleImpassable;
    }
});

function visPassButton() {
    if (ImpassableLayer.getVisible()) {
        ImpassableLayer.setVisible(false);
        this.style.backgroundColor = "whitesmoke";
    } else {
        ImpassableLayer.setVisible(true);
        this.style.backgroundColor = "red";
    }
};