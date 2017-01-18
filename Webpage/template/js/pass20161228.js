/**
 * Created by komori on 2016/07/20.
 */

var styleImpassable = new ol.style.Style({
    text: new ol.style.Text({
        offsetY: 8,
        font: '12px Calibri,sans-serif',
        fill: new ol.style.Fill({color: "#000000"}),
        stroke: new ol.style.Stroke({color: "#ffff00", width: 2})
    }),
    image: new ol.style.Icon({
        scale: 0.075,
        anchor: [0.5, 1],
        anchorXUnits: 'fraction',
        anchorYUnits: 'fraction',
        opacity: 1,
        src: '../../img/impassable/1.png'
    })
});

var urlP = 'http://h-crisis.niph.go.jp/assistant/wp-content/uploads/data/road_info/latest/road_info.geojson';
// urlCustomRoadは、event.jsで定義する
//var urlCustomRoad = '../practice/geojson/custom_road_info.geojson'

var ImpassableLayer = new ol.layer.Vector({
    source: new ol.source.Vector({
        url: urlP,
        format: new ol.format.GeoJSON()
    }),
    style: function(feature, resolution) {
        styleImpassable.getText().setText(resolution < 0.0003 ?  feature.get('name') +  ":" + feature.get('規制内容') + "(理由:" +　feature.get('規制原因') + ")" : '');
        return styleImpassable;
    }
});

var ImpassableLayerAdd = new ol.layer.Vector({
    source: new ol.source.Vector({
        url: urlCustomRoad,
        format: new ol.format.GeoJSON()
    }),
    style: function(feature, resolution) {
        styleImpassable.getText().setText(resolution < 0.0003 ? feature.get('name') +  ":" + feature.get('規制内容') + "(理由:" +　feature.get('規制原因') + ")" : '');
        return styleImpassable;
    }
});

function visPassButton() {
    if (ImpassableLayer.getVisible()) {
        ImpassableLayer.setVisible(false);
        ImpassableLayerAdd.setVisible(false);
        this.style.backgroundColor = "";
        this.style.color = "";
    } else {
        ImpassableLayer.setVisible(true);
        ImpassableLayerAdd.setVisible(true);
        this.style.backgroundColor = "#eeeeee";
        this.style.color = "#000000";
    }
};


// 交通規制のポップアップを作成する関数
function createHtmlPass(evt) {
    document.getElementById('info').innerHTML = "";
    DetailHtml = "";
    latNow = parseFloat(arrayV[12]);
    lonNow = parseFloat(arrayV[11]);
    nowLatLon = [latNow, lonNow];
    var coordinate = nowLatLon;
    DetailHtml = "<div id='landscape'><img id='landIcon' src='../../img/impassable/1.png'></div>";
    // 建築物名表示
    if (arrayV[0].length < 18) {
        DetailHtml = DetailHtml + "<div id='blueback'><a id='title'>" + arrayV[0] + "</a>";
    } else {
        DetailHtml = DetailHtml + "<div id='blueback'><a id='titleTooLong'>" + arrayV[0] + "</a>";
    }

    DetailHtml = DetailHtml + preCells + "style='color:white;'" + interCells + "交通情報:" + arrayV[5] + subCells + "</div>";

    for (i = 1; i < arrayL.length-2; i++) {
        DetailHtml = DetailHtml + preCells + "id=NOCLR>" + arrayL[i] + "</a><a" + interCells + arrayV[i] + subCells;
    }
    document.getElementById('info').style.display = 'block';
    document.getElementById('info').innerHTML = DetailHtml;
    document.getElementById('popup').style.display = 'block';
    overlayPopup.setPosition(coordinate);
}
