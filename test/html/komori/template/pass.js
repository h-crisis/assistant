/**
 * Created by komori on 2016/07/20.
 */

var styleImpassable = new ol.style.Style({
    text: new ol.style.Text({
        font: '12px Calibri,sans-serif'
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

var ImpassableLayer = new ol.layer.Vector({
    source: new ol.source.Vector({
        url: 'http://h-crisis.niph.go.jp/assistant/wp-content/uploads/sites/4/test/geojson/Inpassable_Kouchi.geojson',
        format: new ol.format.GeoJSON()
    }),
    style: function(feature, zoomlevel) {
        styleImpassable.getText().setText(zoomlevel > 10 ? feature.get('name') +  ":" + feature.get('status') : '');
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