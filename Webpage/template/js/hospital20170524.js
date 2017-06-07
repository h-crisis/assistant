/**
 * Created by komori on 2017/06/07.
 */

var styleCL = new ol.style.Style({
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
        src: '../../img/hospital/hspt_red.png'
    })
});

function createHtmlhosp(evt) {
    document.getElementById('info').innerHTML = "";
    DetailHtml = "";
    latNow = parseFloat(arrayV[3]);
    lonNow = parseFloat(arrayV[4]);
    nowLatLon = [lonNow, latNow];
    var coordinate = nowLatLon;
    DetailHtml = "<div id='landscape'><img id='landIcon' src='../../img/hospital/hspt_red.png'></div>";
    // 建築物名表示
    if (arrayV[1].length < 18) {
        DetailHtml = DetailHtml + "<div id='blueback'><a id='title'>" + arrayV[1] + "</a>";
    } else {
        DetailHtml = DetailHtml + "<div id='blueback'><a id='titleTooLong'>" + arrayV[1] + "</a>";
    }

    DetailHtml = DetailHtml + preCells + "style='color:white;'" + interCells + "住所:" + arrayV[2] + arrayV[4] + arrayV[5] + subCells + "</div>";

    for (i = 1; i < arrayL.length-2; i++) {
        DetailHtml = DetailHtml + preCells + "id=NOCLR>" + arrayL[i] + "</a><a" + interCells + arrayV[i] + subCells;
    }
    document.getElementById('info').style.display = 'block';
    document.getElementById('info').innerHTML = DetailHtml;
    document.getElementById('popup').style.display = 'block';
    //console.log(coordinate);
    //overlayPopup.setPosition(coordinate);
}


function hospButton() {
    // 医療機関をマップに表示させるかどうか
    if (!hospLayer.getVisible()) {
        hospLayer.setVisible(true);
        this.style.backgroundColor = "#eeeeee";
        this.style.color = "#000000";
    } else {
        hospLayer.setVisible(false);
        this.style.backgroundColor = "";
        this.style.color = "";
    }
}