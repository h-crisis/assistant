/**
 * Created by komori on 2016/06/14.
 */


var styleSI1 = new ol.style.Style({
    fill: new ol.style.Fill({
        color: 'rgba(242, 242, 255, 0.5)'
    }),
    image: new ol.style.Icon({
        scale: 1.5,
        anchor: [0.5, 1],
        anchorXUnits: 'fraction',
        anchorYUnits: 'fraction',
        opacity: 0.85,
        src: "http://h-crisis.niph.go.jp/assistant/wp-content/uploads/sites/4/test/img/1.png"
    })
});
var styleSI2 = new ol.style.Style({
    fill: new ol.style.Fill({
        color: 'rgba(0, 170, 255, 0.5)'
    }),
    image: new ol.style.Icon({
        scale: 1.5,
        anchor: [0.5, 1],
        anchorXUnits: 'fraction',
        anchorYUnits: 'fraction',
        opacity: 0.85,
        src: "http://h-crisis.niph.go.jp/assistant/wp-content/uploads/sites/4/test/img/2.png"
    })
});
var styleSI3 = new ol.style.Style({
    fill: new ol.style.Fill({
        color: 'rgba(0, 65, 255, 0.5)'
    }),
    image: new ol.style.Icon({
        scale: 1.5,
        anchor: [0.5, 1],
        anchorXUnits: 'fraction',
        anchorYUnits: 'fraction',
        opacity: 0.85,
        src: "http://h-crisis.niph.go.jp/assistant/wp-content/uploads/sites/4/test/img/3.png"
    })
});
var styleSI4 = new ol.style.Style({
    fill: new ol.style.Fill({
        color: 'rgba(255, 230, 150, 0.5)'
    }),
    image: new ol.style.Icon({
        scale: 1.5,
        anchor: [0.5, 1],
        anchorXUnits: 'fraction',
        anchorYUnits: 'fraction',
        opacity: 0.85,
        src: "http://h-crisis.niph.go.jp/assistant/wp-content/uploads/sites/4/test/img/4.png"
    })
});
var styleSI5W = new ol.style.Style({
    fill: new ol.style.Fill({
        color: 'rgba(255, 230, 0, 0.5)'
    }),
    text: new ol.style.Text({
        font: '12px Calibri,sans-serif',
        fill: new ol.style.Fill({color: "#0019ff"}),
        stroke: new ol.style.Stroke({color: "#ffe600", width: 2})
    }),
    image: new ol.style.Icon({
        scale: 1.5,
        anchor: [0.5, 1],
        anchorXUnits: 'fraction',
        anchorYUnits: 'fraction',
        opacity: 0.85,
        src: "http://h-crisis.niph.go.jp/assistant/wp-content/uploads/sites/4/test/img/5w.png"
    })
});
var styleSI5S = new ol.style.Style({
    fill: new ol.style.Fill({
        color: 'rgba(255, 153, 0, 0.5)'
    }),
    text: new ol.style.Text({
        font: '12px Calibri,sans-serif',
        fill: new ol.style.Fill({color: "#0066ff"}),
        stroke: new ol.style.Stroke({color: "#ff9900", width: 2})
    }),
    image: new ol.style.Icon({
        scale: 1.5,
        anchor: [0.5, 1],
        anchorXUnits: 'fraction',
        anchorYUnits: 'fraction',
        opacity: 0.85,
        src: "http://h-crisis.niph.go.jp/assistant/wp-content/uploads/sites/4/test/img/5s.png"
    })
});
var styleSI6W = new ol.style.Style({
    fill: new ol.style.Fill({
        color: 'rgba(255, 40, 0, 0.5)'
    }),
    text: new ol.style.Text({
        font: '12px Calibri,sans-serif',
        fill: new ol.style.Fill({color: "#00d7ff"}),
        stroke: new ol.style.Stroke({color: "#ff2800", width: 2})
    }),
    image: new ol.style.Icon({
        scale: 1.5,
        anchor: [0.5, 1],
        anchorXUnits: 'fraction',
        anchorYUnits: 'fraction',
        opacity: 0.85,
        src: "http://h-crisis.niph.go.jp/assistant/wp-content/uploads/sites/4/test/img/6w.png"
    })
});
var styleSI6S = new ol.style.Style({
    fill: new ol.style.Fill({
        color: 'rgba(165, 0, 33, 0.5)'
    }),
    text: new ol.style.Text({
        font: '12px Calibri,sans-serif',
        fill: new ol.style.Fill({color: "#00a584"}),
        stroke: new ol.style.Stroke({color: "#a50021", width: 2})
    }),
    image: new ol.style.Icon({
        scale: 1.5,
        anchor: [0.5, 1],
        anchorXUnits: 'fraction',
        anchorYUnits: 'fraction',
        opacity: 0.85,
        src: "http://h-crisis.niph.go.jp/assistant/wp-content/uploads/sites/4/test/img/6s.png"
    })
});
var styleSI7 = new ol.style.Style({
    fill: new ol.style.Fill({
        color: 'rgba(180, 0, 104, 0.5)'
    }),
    text: new ol.style.Text({
        font: '12px Calibri,sans-serif',
        fill: new ol.style.Fill({color: "#00b44c"}),
        stroke: new ol.style.Stroke({color: "#b40068", width: 2})
    }),
    image: new ol.style.Icon({
        scale: 1.5,
        anchor: [0.5, 1],
        anchorXUnits: 'fraction',
        anchorYUnits: 'fraction',
        opacity: 0.85,
        src: "http://h-crisis.niph.go.jp/assistant/wp-content/uploads/sites/4/test/img/7.png"
    })
});

var hallLayer = new ol.layer.Vector({
    source: new ol.source.Vector({
        url: 'http://h-crisis.niph.go.jp/assistant/wp-content/uploads/sites/4/test/geojson/hall.geojson',
        format: new ol.format.GeoJSON(),
    }),
    style: function(feature, resolution) {
        if (feature.get('SEIREI') != null) {
            var name = feature.get('SEIREI') + feature.get('SIKUCHOSON');
        } else {
            var name = feature.get('SIKUCHOSON')
        }
        if(feature.get('MAX_SI')<1.5) {
            return styleSI1;
        } else if(feature.get('MAX_SI')<2.5) {
            return styleSI2;
        } else if(feature.get('MAX_SI')<3.5) {
            return styleSI3;
        } else if(feature.get('MAX_SI')<4.5) {
            return styleSI4;
        } else if(feature.get('MAX_SI')<5) {
            styleSI5W.getText().setText(resolution < 200 ? name : '');
            return styleSI5W;
        } else if(feature.get('MAX_SI')<5.5) {
            styleSI5S.getText().setText(resolution < 200 ? name : '');
            return styleSI5S;
        } else if(feature.get('MAX_SI')<6) {
            styleSI6W.getText().setText(resolution < 200 ? name : '');
            return styleSI6W;
        } else if(feature.get('MAX_SI')<6.5) {
            styleSI6S.getText().setText(resolution < 200 ? name : '');
            return styleSI6S;
        } else if(feature.get('MAX_SI')>=6.5) {
            styleSI7.getText().setText(resolution < 200 ? name : '');
            return styleSI7;
    }
}});



// 役所ボタンの挙動を制御する関数
function hallButton() {
    if (hallLayer.getVisible()) {
        hallLayer.setVisible(false);
        this.style.backgroundColor = "whitesmoke";
        this.style.color = "black";
    } else {
        hallLayer.setVisible(true);
        this.style.backgroundColor = "orange";
        this.style.color = "black";
    }
};