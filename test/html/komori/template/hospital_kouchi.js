/**
 * Created by manabu on 2016/05/21.
 */


var styleHosp = new Array(17);
var src
for (var i = 0; i < 17 ; i++) {
    src = "http://h-crisis.niph.go.jp/assistant/wp-content/uploads/sites/4/test/img/" + i + ".png";
    styleHosp[i] = new ol.style.Style({
        text: new ol.style.Text({
            font: '12px Calibri,sans-serif'
        }),
        image: new ol.style.Icon({
            scale: 0.2,
            anchor: [0.5, 1],
            anchorXUnits: 'fraction',
            anchorYUnits: 'fraction',
            opacity: 0.85,
            src: src
        })
    })
}

    var styleWhite = new ol.style.Style({
        text: new ol.style.Text({
            font: '12px Calibri,sans-serif'
        }),
        image: new ol.style.Icon({
            scale: 0.2,
            anchor: [0.5, 1],
            anchorXUnits: 'fraction',
            anchorYUnits: 'fraction',
            opacity: 0.85,
            src: 'http://h-crisis.niph.go.jp/assistant/wp-content/uploads/sites/4/test/img/White.png'
        })
    });

var hospIcon = new ol.layer.Vector({
    source: new ol.source.Vector({
        url: 'http://h-crisis.niph.go.jp/assistant/wp-content/uploads/sites/4/test/geojson/EMIS_Kouchi.geojson',
        format: new ol.format.GeoJSON()
    }),
    style: function(feature) {
        styleHosp[16];
        styleHosp[16].getText().setText("");
        return styleHosp[16];
        /*
        for (var i = 0; i < 16 ; i++) {
            if (feature.get('iconNum') == i) {
                styleHosp[i];
                styleHosp[i].getText().setText("");
                return styleHosp[i];
            }
        }
        */
    }
});

var hospLayer = new ol.layer.Vector({
    source: new ol.source.Vector({
        url: 'http://h-crisis.niph.go.jp/assistant/wp-content/uploads/sites/4/test/geojson/medical_status.geojson',
        format: new ol.format.GeoJSON()
    }),
    style: function(feature, resolution) {
        styleWhite.getText().setText(resolution < 10 ? feature.get('name') : '');
        styleWhite.getImage().setOpacity(1);
        return styleWhite;
        /*
        for (var i = 0; i < 16 ; i++) {
            styleHosp[i].getText().setText(resolution < 10 ? feature.get('name') : '');
            return styleHosp[i];
        }
        */
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