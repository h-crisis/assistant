/**
 * Created by manabu on 2016/05/19.
 */

var shelterLayer = new ol.layer.Tile({
    source: new ol.source.TileWMS({
        url: wms,
        params: {
            'VERSION': '1.1.1',
            tiled: true,
            STYLES: '',
            LAYERS: 'shelter'
        }
    })
});

function createShelterPopup(url, evt) {
    var popupHtml;
    var infoHtml;
    var headerHtml;
    var coordinate = evt.coordinate;

    if (url) {
        var parser = new ol.format.GeoJSON();
        var content = document.getElementById('popup-content');
        $.ajax({
            url: url,
            type: "GET",
            dataType: 'jsonp',
            jsonpCallback: 'parseResponse'
        }).then(function(response) {
            var result = parser.readFeatures(response);
            if (result.length) {
                for (var i = 0, ii = result.length; i < ii; ++i) {
                    if (i == 0) {
                        popupHtml = createShelterPopupHtml(result[i]);
                        infoHtml = createShelterInfoHtml(result[i]);
                        headerHtml = createShelterHeaderHtml(result[i]);
                    }
                    else {
                        popupHtml = popupHtml + '<br><hr><br>' + 'name: ' + result[i].get('name');
                    }
                }
            }

            // ここから表示するしないの制御
            if(typeof popupHtml === "undefined") {
                content.innerHTML = '';
                overlayPopup.setPosition(undefined);
                document.getElementById('info').style.display = 'none';
                document.getElementById('infoHeader').style.display = 'none';
            } else {
                document.getElementById('info').style.display = 'block';
                document.getElementById('info').innerHTML = infoHtml;
                document.getElementById('infoHeader').style.display = 'block';
                document.getElementById('infoHeader').innerHTML = headerHtml;
                content.innerHTML = popupHtml;
                overlayPopup.setPosition(coordinate);
            }
        });

    }
}

/**
 * クリックした場所の避難所の情報を表示するためのHTMLを生成する関数
 * @param result 地図上の選択したFeature
 * @returns {*}
 */
function createShelterPopupHtml(result) {
    var popupHtml = result.get('name');
    if(typeof name === 'undefined') {
        return popupHtml;
    } else {
        // 状況表示
        if(result.get('status') === null) {
            popupHtml = popupHtml + "（状況: 不明）";
        } else {
            popupHtml = popupHtml + "（状況: " + result.get('status') + "）";
        }

        // 住所の表示
        if(result.get('pref') === null) {
            popupHtml = popupHtml + '<br>住所: '
        } else {
            popupHtml = popupHtml + '<br>住所: ' + result.get('pref') + ' ';
        }

        if(result.get('gun') === null) {

        } else {
            popupHtml = popupHtml + result.get('gun') + ' ';
        }

        if(result.get('sikuchoson') === null) {

        } else {
            popupHtml = popupHtml + result.get('sikuchoson') + ' ';
        }

        if(result.get('address') === null) {

        } else {
            popupHtml = popupHtml + result.get('address');
        }

        // 避難者数の表示
        if(result.get('a01') === null) {
            popupHtml = popupHtml + "<br>避難者数: ";
        } else {
            popupHtml = popupHtml + "<br>避難者数: " + result.get('a01');
        }

        return popupHtml;
    }
}

function createShelterInfoHtml(result) {
    var InfoHtml = result.get('name');
    var preCells = "<tr><td style=font-size:24px;background-color:#888888;color:white>";
    var interCells = "</td><td style=font-size:24px;background-color:white;text-align:right;>";
    var subCells ="</td></tr>";

    if(typeof name === 'undefined') {
        return InfoHtml;
    } else {

        // 避難所情報入力ボタン表示
        var btnCode = '?event=' + eventCode + ',id=' + result.get('code') + ',name=' + result.get('name')　+ ',address=' + result.get('address');
        InfoHtml = "<div style='border:2px solid burlywood; background-color:#888888; text-align:center' type=button ><a href=../../html/shelter-emergency.html" + btnCode + " ,style='display:block; width:100%; color:white; text-decoration:none' id=niphLonLatE target=_blank>緊急時情報入力</a></div>"
            + "<div style='border:2px solid burlywood; background-color:#888888; text-align:center' type=button ><a href=../../html/shelter-hmethod.html" + btnCode + " ,style='display:block; width:100%; color:white; text-decoration:none' id=niphLonLatE target=_blank>避難所シート入力</a></div>"
            + "<div style='border:2px solid burlywood; background-color:#888888; text-align:center' type=button ><a href=../../html/shelter-evacuee.html" + btnCode + " ,style='display:block; width:100%; color:white; text-decoration:none' id=niphLonLatE target=_blank>避難所避難者シート入力</a></div>";

        // 住所の表示
        if(result.get('pref') === null) {
            InfoHtml = InfoHtml + preCells + "都道府県" + interCells + subCells;
            // InfoHtml = InfoHtml + '<br>都道府県'
        } else {
            InfoHtml = InfoHtml + preCells + "都道府県" + interCells + result.get('pref') + subCells;
            // InfoHtml = InfoHtml + '<br>住所: ' + result.get('pref') + ' ';
        }

        if(result.get('gun') === "") {

        } else {
            InfoHtml = InfoHtml + preCells + "住所" + interCells + result.get('gun') + result.get('sikuchoson')+ result.get('address') + subCells;
            //InfoHtml = InfoHtml + result.get('gun') + ' ';
        }

        if(result.get('sikuchoson') === null) {

        } else {
            InfoHtml = InfoHtml + preCells + "住所" + interCells + result.get('sikuchoson')+ result.get('address') + subCells;
            // InfoHtml = InfoHtml + result.get('sikuchoson') + ' ';
        }
/*
        if(result.get('address') === null) {

        } else {
            InfoHtml = InfoHtml + result.get('address');
        }
*/
        // 避難者数の表示
        if(result.get('a01') === null) {
            InfoHtml = InfoHtml + preCells + "避難者数" + interCells + subCells;
        } else {
            InfoHtml = InfoHtml + preCells + "避難者数" + interCells +  result.get('a01') + subCells;
        }

        // 以下緊急時入力で表示される情報の表示
        if(result.get('c01_1') === null) {
            InfoHtml = InfoHtml + preCells + "電気" + interCells + subCells;
        } else {
            InfoHtml = InfoHtml + preCells + "電気" + interCells +  result.get('c01_1') + subCells;
        }

        if(result.get('c01_2') === null) {
            InfoHtml = InfoHtml + preCells + "ガス" + interCells + subCells;
        } else {
            InfoHtml = InfoHtml + preCells + "ガス" + interCells +  result.get('c01_2') + subCells;
        }

        if(result.get('c01_3') === null) {
            InfoHtml = InfoHtml + preCells + "水道" + interCells + subCells;
        } else {
            InfoHtml = InfoHtml + preCells + "水道" + interCells +  result.get('c01_3') + subCells;
        }

        if(result.get('c01_4') === null) {
            InfoHtml = InfoHtml + preCells + "飲料水" + interCells + subCells;
        } else {
            InfoHtml = InfoHtml + preCells + "飲料水" + interCells +  result.get('c01_4') + subCells;
        }

        if(result.get('c01_5') === null) {
            InfoHtml = InfoHtml + preCells + "固定電話" + interCells + subCells;
        } else {
            InfoHtml = InfoHtml + preCells + "固定電話" + interCells +  result.get('c01_5') + subCells;
        }

        if(result.get('c01_8') === null) {
            InfoHtml = InfoHtml + preCells + "携帯電話" + interCells + subCells;
        } else {
            InfoHtml = InfoHtml + preCells + "携帯電話" + interCells +  result.get('c01_8') + subCells;
        }

        if(result.get('c01_9') === null) {
            InfoHtml = InfoHtml + preCells + "通信" + interCells + subCells;
        } else {
            InfoHtml = InfoHtml + preCells + "通信" + interCells +  result.get('c01_9') + subCells;
        }

        if(result.get('c02_6') === null) {
            InfoHtml = InfoHtml + preCells + "トイレ" + interCells + subCells;
        } else {
            InfoHtml = InfoHtml + preCells + "トイレ" + interCells +  result.get('c02_6') + subCells;
        }
        if(result.get('c04_1') === null) {
            InfoHtml = InfoHtml + preCells + "食料" + interCells + subCells;
        } else {
            InfoHtml = InfoHtml + preCells + "食料" + interCells +  result.get('c04_1') + subCells;
        }
        return InfoHtml;
    }
}

function createShelterHeaderHtml(result) {
    var HeaderHtml = result.get('name');
    if (typeof name === 'undefined') {
        return HeaderHtml;
    } else {
        // 閉じるボタン表示
        HeaderHtml = "<div style='border:2px solid burlywood; background-color:#888888; color:white; text-align:center' type=button id=showBtn value=隠す onclick=showHide()>閉じる</div>";

        // 建築物名表示
        if (result.get('name') === null) {
        } else {
            HeaderHtml = HeaderHtml + result.get('name');
        }
        // 状況表示
        if(result.get('status') === null) {
            HeaderHtml = HeaderHtml + "<br>（状況: 不明）";
        } else {
            HeaderHtml = HeaderHtml + "<br>（状況: " + result.get('status') + "）";
        }
    }
    return HeaderHtml;
}

function showHide(){
    // 情報画面を非表示にする
    document.getElementById('info').style.display = 'none';
    document.getElementById('infoHeader').style.display = 'none';
}