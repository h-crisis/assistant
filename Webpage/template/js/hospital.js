/**
 * Created by manabu on 2016/05/21.
 */

var src;
var biNum;

var styleHospR = new Array(16);
for (var i = 0; i < 17 ; i++) {
    src = "http://h-crisis.niph.go.jp/assistant/wp-content/uploads/sites/4/test/img/r/" + i + ".png";
    styleHospR[i] = new ol.style.Style({
        text: new ol.style.Text({
            font: '12px Calibri,sans-serif',
            fill: new ol.style.Fill({color: "#0000bb"}),
            stroke: new ol.style.Stroke({color: "#ffffff", width: 2}),
        }),
        image: new ol.style.Icon({
            scale: 1,
            anchor: [0.5, 1],
            anchorXUnits: 'fraction',
            anchorYUnits: 'fraction',
            opacity: 0.85,
            src: src
        })
    })
}

var styleHospB = new Array(16);
for (var i = 0; i < 17 ; i++) {
    src = "http://h-crisis.niph.go.jp/assistant/wp-content/uploads/sites/4/test/img/b/" + i + ".png";
    styleHospB[i] = new ol.style.Style({
        text: new ol.style.Text({
            font: '12px Calibri,sans-serif',
            fill: new ol.style.Fill({color: "#0000bb"}),
            stroke: new ol.style.Stroke({color: "#ffffff", width: 2}),
        }),
        image: new ol.style.Icon({
            scale: 1,
            anchor: [0.5, 1],
            anchorXUnits: 'fraction',
            anchorYUnits: 'fraction',
            opacity: 0.85,
            src: src
        })
    })
}

var styleHospG = new Array(16);
for (var i = 0; i < 17 ; i++) {
    src = "http://h-crisis.niph.go.jp/assistant/wp-content/uploads/sites/4/test/img/g/" + i + ".png";
    styleHospG[i] = new ol.style.Style({
        text: new ol.style.Text({
            font: '12px Calibri,sans-serif',
            fill: new ol.style.Fill({color: "#0000bb"}),
            stroke: new ol.style.Stroke({color: "#ffffff", width: 2})
        }),
        image: new ol.style.Icon({
            scale: 1,
            anchor: [0.5, 1],
            anchorXUnits: 'fraction',
            anchorYUnits: 'fraction',
            opacity: 0.85,
            src: src
        })
    })
}
/*
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
 src: 'img/img/White.png'
 })
 });
 */
var url = 'http://h-crisis.niph.go.jp/wp-content/uploads/data/medical_status/latest/hcrisis_medical_status.geojson'

var hospLayer = new ol.layer.Vector({
    source: new ol.source.Vector({
        // url: 'geojson/hcrisis_medical_status.geojson',
        url: url,
        format: new ol.format.GeoJSON()
    }),
    style: function(feature, resolution) {
        biNum = (feature.get('saigai') * 8) + (feature.get('kyukyu') * 4) + (feature.get('hibaku') * 2) + (feature.get('dmat') * 1);
        if (feature.get('assist') == "要"){
            for (var i = 0; i < 16 ; i++) {
                if (biNum == i) {
                    styleHospR[i];
                    // styleHospR[i].getText().setText(resolution < 40 && (feature.get('prefecture') == "静岡県" || feature.get('prefecture') == "山梨県" || feature.get('prefecture') == "愛知県" || feature.get('prefecture') == "三重県" || feature.get('prefecture') == "和歌山県" ) ? feature.get('name') : '');
                    styleHospR[i].getText().setText(resolution < 0.0003 ? feature.get('name') : '');
                    styleHospR[i].getImage().setScale(resolution < 0.00001 ? 0.5 : 0.4);
                    // styleHospR[i].getImage().setOpacity(feature.get('prefecture') == "静岡県" || feature.get('prefecture') == "山梨県" || feature.get('prefecture') == "愛知県" || feature.get('prefecture') == "三重県" || feature.get('prefecture') == "和歌山県" ? 1 : 0);
                    styleHospR[i].getImage().setOpacity(1);
                    return styleHospR[i];
                }
            }
        } else if(feature.get('assist') == "未" && feature.get('mds') == "未入力") {
            for (var i = 0; i < 16 ; i++) {
                if (biNum == i) {
                    styleHospG[i];
                    // styleHospG[i].getText().setText(resolution < 40 && (feature.get('prefecture') == "静岡県" || feature.get('prefecture') == "山梨県" || feature.get('prefecture') == "愛知県" || feature.get('prefecture') == "三重県" || feature.get('prefecture') == "和歌山県" ) ? feature.get('name') : '');
                    styleHospG[i].getText().setText(resolution < 0.0003 ? feature.get('name') : '');
                    styleHospG[i].getImage().setScale(resolution < 0.00001 ? 0.5 : 0.4);
                    // styleHospG[i].getImage().setOpacity(feature.get('prefecture') == "静岡県" || feature.get('prefecture') == "山梨県" || feature.get('prefecture') == "愛知県" || feature.get('prefecture') == "三重県" || feature.get('prefecture') == "和歌山県" ? 1 : 0);
                    styleHospG[i].getImage().setOpacity(1);
                    return styleHospG[i];
                }
            }
        } else {
            for (var i = 0; i < 16 ; i++) {
                if (biNum == i) {
                    styleHospB[i];
                    // styleHospB[i].getText().setText(resolution < 40 && (feature.get('prefecture') == "静岡県" || feature.get('prefecture') == "山梨県" || feature.get('prefecture') == "愛知県" || feature.get('prefecture') == "三重県" || feature.get('prefecture') == "和歌山県" ) ? feature.get('name') : '');
                    styleHospB[i].getText().setText(resolution < 0.0003 ? feature.get('name') : '');
                    styleHospB[i].getImage().setScale(resolution < 0.00001 ? 0.5 : 0.4);
                    // styleHospB[i].getImage().setOpacity(feature.get('prefecture') == "静岡県" || feature.get('prefecture') == "山梨県" || feature.get('prefecture') == "愛知県" || feature.get('prefecture') == "三重県" || feature.get('prefecture') == "和歌山県" ? 1 : 0);
                    styleHospB[i].getImage().setOpacity(1);
                    return styleHospB[i];
                }
            }
        }

    }
});

/*
 var hospLayer = new ol.layer.Vector({
 source: new ol.source.Vector({
 url: 'geojson/hcrisis_medical_status.geojson',
 format: new ol.format.GeoJSON()
 }),
 style: function(feature) {

 styleWhite.getImage().setOpacity(feature.get('prefecture') == "高知県" ? 1 : 0);
 return styleWhite;
 }
 });
 */

// 病院ボタンの挙動を制御する関数
function hospButton() {
    if (!hospLayer.getVisible()) {
        hospLayer.setVisible(true);
        this.style.backgroundColor = "blue";
    } else {
        hospLayer.setVisible(false);
        this.style.backgroundColor = "whitesmoke";
    }
};

// 医療機関のポップアップを作成する関数
function createHtmlHospital() {
    document.getElementById('info').innerHTML = "";
    DetailHtml = "";
    HeaderHtml = "<div style='border:2px solid burlywood; background-color:#888888; color:white; text-align:center' type=button id=showBtn value=隠す onclick=showHide()>閉じる</div>";

    // 建築物名表示
    if (arrayV[0] === null) {
    } else {
        HeaderHtml = HeaderHtml + arrayV[1];
    }
    HeaderHtml = HeaderHtml + "<div style='border:2px solid burlywood; background-color:#888888; color:white; text-align:center' type=button id=showBtn value=隠す onclick=showDetail()>詳細情報の表示切替</div>";
    for (i = 0; i < arrayL.length; i++) {
        DetailHtml = DetailHtml + preCells + arrayL[i] + interCells + arrayV[i] + subCells;
    }
    document.getElementById('infoHeader').style.display = 'block';
    document.getElementById('infoHeader').innerHTML = HeaderHtml;
    document.getElementById('info').style.display = 'block';
    document.getElementById('info').innerHTML = DetailHtml;
}

