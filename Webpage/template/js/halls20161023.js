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
        scale: 0.25,
        anchor: [0.5, 1],
        anchorXUnits: 'fraction',
        anchorYUnits: 'fraction',
        opacity: 0.85,
        src: "/Users/komori/Desktop/hospital_002.png"
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
        console.log(url);
        console.log(resolution);
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
function createHtmlHall() {
    document.getElementById('info').innerHTML = "";
    if (window.innerWidth >= 780) {
        HeaderHtml = "<div style='border-radius:10px; margin:0 0 5px; font-family:helvetica; background-color:#333333; color:white; text-align:center; opacity: 1; width:350px' type=button id=showBtn value=隠す onclick=showHide() onmousemove='this.style.opacity=0.8' onmouseout='this.style.opacity=1'>閉じる</div>";
    } else if (window.innerWidth >= 480) {
        HeaderHtml = "<div style='border-radius:10px; margin:0 0 5px; font-family:helvetica; background-color:#333333; color:white; text-align:center; opacity: 1; width:220px' type=button id=showBtn value=隠す onclick=showHide() onmousemove='this.style.opacity=0.8' onmouseout='this.style.opacity=1'>閉じる</div>";
    } else {
        HeaderHtml = "<div style='border-radius:10px; margin:0 0 5px; font-family:helvetica; background-color:#333333; color:white; text-align:center; opacity: 1; width:220px' type=button id=showBtn value=隠す onclick=showHide() onmousemove='this.style.opacity=0.8' onmouseout='this.style.opacity=1'>閉じる</div>";
    }
    // 建築物名表示
    if (arrayV[0] === null) {
    } else {
        if(arrayV[6]===null && arrayV[7]===null) {
            HeaderHtml = HeaderHtml + "<a style='font-family: Helvetica'>" + arrayV[4] + arrayV[8] + "</a>";
        }
        else if(arrayV[7]===null) {
            HeaderHtml = HeaderHtml + "<a style='font-family: Helvetica'>" + arrayV[4] + arrayV[6] + arrayV[8] + "</a>";
        }
        else {
            HeaderHtml = HeaderHtml + "<a style='font-family: Helvetica'>" + arrayV[4] + arrayV[6] + arrayV[7] + arrayV[8] + "</a>";
        }
    }

    DetailHtml = "";

    DetailHtml = DetailHtml + preCells + '住所' + interCells + arrayV[3] + subCells; // 住所表示
    DetailHtml = DetailHtml + preCells + '人口' + interCells + arrayV[9] + subCells; // 人口表示
    DetailHtml = DetailHtml + preCells + '世帯数' + interCells + arrayV[10] + subCells; // 世帯数表示
    DetailHtml = DetailHtml + preCells + '最大震度' + interCells + arrayV[11] + subCells; // 最大震度表示
    DetailHtml = DetailHtml + preCells + '最小震度' + interCells + arrayV[12] + subCells; // 最小震度表示
    DetailHtml = DetailHtml + preCells + '平均震度' + interCells + arrayV[13] + subCells; // 平均震度表示
    DetailHtml = DetailHtml + preCells + '全壊建物数' + interCells + arrayV[14] + subCells; // 全壊建物数表示
    DetailHtml = DetailHtml + preCells + '半壊建物数' + interCells + arrayV[15] + subCells; // 半壊建物数表示
    DetailHtml = DetailHtml + preCells + '死者数' + interCells + arrayV[16] + subCells; // 死者数表示
    DetailHtml = DetailHtml + preCells + '負傷者数' + interCells + arrayV[17] + subCells; // 負傷者表示
    DetailHtml = DetailHtml + preCells + '重傷者数' + interCells + arrayV[18] + subCells; // 重傷者数表示
    DetailHtml = DetailHtml + preCells + '避難者数' + interCells + arrayV[19] + subCells; // 避難者数表示

    /*
     for (i = 0; i < arrayL.length; i++) { // 震度分布で表示するもの
     DetailHtml = DetailHtml + preCells + arrayL[i] + interCells + arrayV[i] + subCells;
     }
     */
    document.getElementById('infoHeader').style.display = 'block';
    document.getElementById('infoHeader').innerHTML = HeaderHtml;
    document.getElementById('info').style.display = 'block';
    document.getElementById('info').innerHTML = DetailHtml;
}