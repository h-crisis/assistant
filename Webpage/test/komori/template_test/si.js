/**
 * Created by manabu on 2016/05/19.
 */
var styleSI7 = new ol.style.Style({
    fill: new ol.style.Fill({
        color: 'rgba(180, 0, 104, 0.5)'
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
var styleSI6S = new ol.style.Style({
    fill: new ol.style.Fill({
        color: 'rgba(165, 0, 33, 0.5)'
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
var styleSI6W = new ol.style.Style({
    fill: new ol.style.Fill({
        color: 'rgba(255, 40, 0, 0.5)'
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
var styleSI5S = new ol.style.Style({
    fill: new ol.style.Fill({
        color: 'rgba(255, 153, 0, 0.5)'
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
var styleSI5W = new ol.style.Style({
    fill: new ol.style.Fill({
        color: 'rgba(255, 230, 0, 0.5)'
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

// 震度GeoJSONを読み込む
// 本震のファイル名「si_h01.GeoJson」
// 余震のファイル名「si_y01.GeoJson」
// 余震発生毎に,si_y02.GeoJson,si_y03.GeoJson...とファイルが作成される
var layerSI = new ol.layer.Vector({
    source: new ol.source.Vector({
        url: 'http://h-crisis.niph.go.jp/assistant/wp-content/uploads/sites/4/test/geojson/si_h01.geojson',
        format: new ol.format.GeoJSON()
    }),
    style: function(feature, resolution) {
        if(feature.get('SI')<1.5) {
            return styleSI1;
        } else if(feature.get('SI')<2.5) {
            return styleSI2;
        } else if(feature.get('SI')<3.5) {
            return styleSI3;
        } else if(feature.get('SI')<4.5) {
            return styleSI4;
        } else if(feature.get('SI')<5) {
            return styleSI5W;
        } else if(feature.get('SI')<5.5) {
            return styleSI5S;
        } else if(feature.get('SI')<6) {
            return styleSI6W;
        } else if(feature.get('SI')<6.5) {
            return styleSI6S;
        } else if(feature.get('SI')>=6.5) {
            return styleSI7;
        } else {}
    }
});

// 震度ボタンの挙動を制御する関数
function siButton() {
    if (layerSI.getVisible()) {
        hybQuestLayer.setVisible(false);
        layerSI.setVisible(false);
        this.style.backgroundColor = "whitesmoke";
    } else {
        layerSI.setVisible(true);
        hybQuestLayer.setVisible(true);
        this.style.backgroundColor = "orange";
    }
};