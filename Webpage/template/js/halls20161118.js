/**
 * Created by komori on 2016/06/14.
 */
var styleSI1 = new ol.style.Style({
    fill: new ol.style.Fill({
        color: 'rgba(242, 242, 255, 0.5)'
    }),
    image: new ol.style.Icon({
        scale: 0.075,
        anchor: [0.5, 1],
        anchorXUnits: 'fraction',
        anchorYUnits: 'fraction',
        opacity: 0.85,
        src: "/Users/komori/IdeaProjects/assistant/Webpage/template/img/dist/1.png"
    })
});
var styleSI2 = new ol.style.Style({
    fill: new ol.style.Fill({
        color: 'rgba(0, 170, 255, 0.5)'
    }),
    image: new ol.style.Icon({
        scale: 0.075,
        anchor: [0.5, 1],
        anchorXUnits: 'fraction',
        anchorYUnits: 'fraction',
        opacity: 0.85,
        src: "/Users/komori/IdeaProjects/assistant/Webpage/template/img/dist/2.png"
    })
});
var styleSI3 = new ol.style.Style({
    fill: new ol.style.Fill({
        color: 'rgba(0, 65, 255, 0.5)'
    }),
    image: new ol.style.Icon({
        scale: 0.075,
        anchor: [0.5, 1],
        anchorXUnits: 'fraction',
        anchorYUnits: 'fraction',
        opacity: 0.85,
        src: "/Users/komori/IdeaProjects/assistant/Webpage/template/img/dist/3.png"
    })
});
var styleSI4 = new ol.style.Style({
    fill: new ol.style.Fill({
        color: 'rgba(255, 230, 150, 0.5)'
    }),
    image: new ol.style.Icon({
        scale: 0.075,
        anchor: [0.5, 1],
        anchorXUnits: 'fraction',
        anchorYUnits: 'fraction',
        opacity: 0.85,
        src: "/Users/komori/IdeaProjects/assistant/Webpage/template/img/dist/4.png"
    })
});
var styleSI5W = new ol.style.Style({
    fill: new ol.style.Fill({
        color: 'rgba(255, 230, 0, 0.5)'
    }),
    text: new ol.style.Text({
        offsetY: 8,
        font: '12px Calibri,sans-serif',
        fill: new ol.style.Fill({color: "#0019ff"}),
        stroke: new ol.style.Stroke({color: "#ffe600", width: 2})
    }),
    image: new ol.style.Icon({
        scale: 0.075,
        anchor: [0.5, 1],
        anchorXUnits: 'fraction',
        anchorYUnits: 'fraction',
        opacity: 0.85,
        src: "/Users/komori/IdeaProjects/assistant/Webpage/template/img/dist/5-1.png"
    })
});
var styleSI5S = new ol.style.Style({
    fill: new ol.style.Fill({
        color: 'rgba(255, 153, 0, 0.5)'
    }),
    text: new ol.style.Text({
        offsetY: 8,
        font: '12px Calibri,sans-serif',
        fill: new ol.style.Fill({color: "#0066ff"}),
        stroke: new ol.style.Stroke({color: "#ff9900", width: 2})
    }),
    image: new ol.style.Icon({
        scale: 0.075,
        anchor: [0.5, 1],
        anchorXUnits: 'fraction',
        anchorYUnits: 'fraction',
        opacity: 0.85,
        src: "/Users/komori/IdeaProjects/assistant/Webpage/template/img/dist/5-2.png"
    })
});
var styleSI6W = new ol.style.Style({
    fill: new ol.style.Fill({
        color: 'rgba(255, 40, 0, 0.5)'
    }),
    text: new ol.style.Text({
        offsetY: 8,
        font: '12px Calibri,sans-serif',
        fill: new ol.style.Fill({color: "#00d7ff"}),
        stroke: new ol.style.Stroke({color: "#ff2800", width: 2})
    }),
    image: new ol.style.Icon({
        scale: 0.075,
        anchor: [0.5, 1],
        anchorXUnits: 'fraction',
        anchorYUnits: 'fraction',
        opacity: 0.85,
        src: "/Users/komori/IdeaProjects/assistant/Webpage/template/img/dist/6-1.png"
    })
});
var styleSI6S = new ol.style.Style({
    fill: new ol.style.Fill({
        color: 'rgba(165, 0, 33, 0.5)'
    }),
    text: new ol.style.Text({
        offsetY: 8,
        font: '12px Calibri,sans-serif',
        fill: new ol.style.Fill({color: "#00a584"}),
        stroke: new ol.style.Stroke({color: "#a50021", width: 2})
    }),
    image: new ol.style.Icon({
        scale: 0.075,
        anchor: [0.5, 1],
        anchorXUnits: 'fraction',
        anchorYUnits: 'fraction',
        opacity: 0.85,
        src: "/Users/komori/IdeaProjects/assistant/Webpage/template/img/dist/6-2.png"
    })
});
var styleSI7 = new ol.style.Style({
    fill: new ol.style.Fill({
        color: 'rgba(180, 0, 104, 0.5)'
    }),
    text: new ol.style.Text({
        offsetY: 8,
        font: '12px Calibri,sans-serif',
        fill: new ol.style.Fill({color: "#00b44c"}),
        stroke: new ol.style.Stroke({color: "#b40068", width: 2})
    }),
    image: new ol.style.Icon({
        scale: 0.075,
        anchor: [0.5, 1],
        anchorXUnits: 'fraction',
        anchorYUnits: 'fraction',
        opacity: 0.85,
        src: "/Users/komori/IdeaProjects/assistant/Webpage/template/img/dist/7.png"
    })
});

var urlH = pass + 'geojson/halls.geojson';
// var url =  'http://h-crisis.niph.go.jp/wp-content/uploads/sites/4/event/20160815/geojson/halls.geojson';

var hallLayer = new ol.layer.Vector({
    source: new ol.source.Vector({
        url: urlH,
        format: new ol.format.GeoJSON(),
    }),
    style: function(feature, resolution) {
        if (feature.get('seirei') != null) {
            var name = feature.get('seirei') + feature.get('sikuchoson');
        } else {
            var name = feature.get('sikuchoson')
        }
        if(feature.get('max_si')<1.5) {
            return styleSI1;
        } else if(feature.get('max_si')<2.5) {

            return styleSI2;
        } else if(feature.get('max_si')<3.5) {
            return styleSI3;
        } else if(feature.get('max_si')<4.5) {
            return styleSI4;
        } else if(feature.get('max_si')<5) {
            styleSI5W.getText().setText(zoomLevel > 10 ? name : '');
            return styleSI5W;
        } else if(feature.get('max_si')<5.5) {
            styleSI5S.getText().setText(zoomLevel > 10 ? name : '');
            return styleSI5S;
        } else if(feature.get('max_si')<6) {
            styleSI6W.getText().setText(zoomLevel > 10 ? name : '');
            return styleSI6W;
        } else if(feature.get('max_si')<6.5) {
            styleSI6S.getText().setText(zoomLevel > 10 ? name : '');
            return styleSI6S;
        } else if(feature.get('max_si')>=6.5) {
            styleSI7.getText().setText(zoomLevel > 10 ? name : '');
            return styleSI7;
        }
    }});

// 役所ボタンの挙動を制御する関数
function hallButton() {
    if (!hallLayer.getVisible()) {
        hallLayer.setVisible(true);
        this.style.backgroundColor = "#eeeeee";
        this.style.color = "#000000";
    } else {
        hallLayer.setVisible(false);
        this.style.backgroundColor = "";
        this.style.color = "";
    }
};

// 震度のポップアップを作成する関数
function createHtmlHall(evt,feature) {
    document.getElementById('info').innerHTML = "";
    var coordinate = feature.T.geometry.A;
    DetailHtml = "";

    // 適切なアイコンurlを取得する
    var iconUrl;
    if(feature.get('max_si')<1.5) {
        iconUrl = '/Users/komori/IdeaProjects/assistant/Webpage/template/img/dist/1.png';
    } else if(feature.get('max_si')<2.5) {
        iconUrl = '/Users/komori/IdeaProjects/assistant/Webpage/template/img/dist/2.png';
    } else if(feature.get('max_si')<3.5) {
        iconUrl = '/Users/komori/IdeaProjects/assistant/Webpage/template/img/dist/3.png';
    } else if(feature.get('max_si')<4.5) {
        iconUrl = '/Users/komori/IdeaProjects/assistant/Webpage/template/img/dist/4.png';
    } else if(feature.get('max_si')<5) {
        iconUrl = '/Users/komori/IdeaProjects/assistant/Webpage/template/img/dist/5-1.png';
    } else if(feature.get('max_si')<5.5) {
        iconUrl = '/Users/komori/IdeaProjects/assistant/Webpage/template/img/dist/5-2.png';
    } else if(feature.get('max_si')<6) {
        iconUrl = '/Users/komori/IdeaProjects/assistant/Webpage/template/img/dist/6-1.png';
    } else if(feature.get('max_si')<6.5) {
        iconUrl = '/Users/komori/IdeaProjects/assistant/Webpage/template/img/dist/6-2.png';
    } else if(feature.get('max_si')>=6.5) {
        iconUrl = '/Users/komori/IdeaProjects/assistant/Webpage/template/img/dist/7.png';
    }

    DetailHtml = "<div id='landscape'><img id='landIcon' src=" + iconUrl + "></div>";

    // 建築物名表示
    if (arrayV[0] === null) {
    } else {
        if(arrayV[6] == 0 && arrayV[7] == 0) {
            DetailHtml = DetailHtml + "<a id='title'>" + arrayV[4] + arrayV[8] + "</a>";
        }
        else if(arrayV[7] == 0) {
            DetailHtml = DetailHtml + "<a id='title'>" + arrayV[4] + arrayV[6] + arrayV[8] + "</a>";
        }
        else {
            DetailHtml = DetailHtml + "<a id='title'>" + arrayV[4] + arrayV[7] + arrayV[8] + "</a>";
        }
    }

    DetailHtml = DetailHtml + '<hr>';
    DetailHtml = DetailHtml + preCells + '../../img/img/1.png' + interCells + arrayV[3] + subCells; // 住所表示
    DetailHtml = DetailHtml + preCells + '../../img/img/2.png' + interCells + arrayV[9] + subCells; // 人口表示
    DetailHtml = DetailHtml + preCells + '../../img/img/3.png' + interCells + arrayV[10] + subCells; // 世帯数表示
    DetailHtml = DetailHtml + preCells + '../../img/img/4.png' + interCells + arrayV[14] + subCells; // 全壊建物数表示
    DetailHtml = DetailHtml + preCells + '../../img/img/5.png' + interCells + arrayV[15] + subCells; // 半壊建物数表示
    DetailHtml = DetailHtml + preCells + '../../img/img/6.png' + interCells + arrayV[16] + subCells; // 死者数表示
    DetailHtml = DetailHtml + preCells + '../../img/img/7.png' + interCells + arrayV[17] + subCells; // 負傷者表示
    DetailHtml = DetailHtml + preCells + '../../img/img/8.png' + interCells + arrayV[18] + subCells; // 重傷者数表示
    DetailHtml = DetailHtml + preCells + '../../img/img/9.png' + interCells + arrayV[19] + subCells; // 避難者数表示
    /*
    DetailHtml = DetailHtml + preCells + '最大震度' + interCells + arrayV[11] + subCells; // 最大震度表示
    DetailHtml = DetailHtml + preCells + '最小震度' + interCells + arrayV[12] + subCells; // 最小震度表示
    DetailHtml = DetailHtml + preCells + '平均震度' + interCells + arrayV[13] + subCells; // 平均震度表示
    /*
     for (i = 0; i < arrayL.length; i++) { // 震度分布で表示するもの
     DetailHtml = DetailHtml + preCells + arrayL[i] + interCells + arrayV[i] + subCells;
     }
     */
    
    document.getElementById('info').style.display = 'block';
    document.getElementById('info').innerHTML = DetailHtml;
    document.getElementById('infoWrapV').style.display = 'block';
    document.getElementById('infoWrapH').style.display = 'block';
    document.getElementById('popup').style.display = 'block';
    overlayPopup.setPosition(coordinate);
}