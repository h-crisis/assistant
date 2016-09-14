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
                        var btnCode = '?event=' + eventCode + ',id=' + result[i].get('code') + ',name=' + result[i].get('name');
                        infoHtml = createShelterInfoHtml(result[i]);


                        infoHtml = infoHtml + "<div style='border:2px solid burlywood; background-color:#888888; text-align:center' type=button ><a href=../../html/shelter-emergency.html" + btnCode + ",style='display:block; width:100%; color:white; text-decoration:none' id=niphLonLatE target=_blank>緊急時情報入力</a></div>";

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
            } else {
                document.getElementById('info').style.display = 'block';
                document.getElementById('info').innerHTML = infoHtml;
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
    if(typeof name === 'undefined') {
        return InfoHtml;
    } else {
        // 状況表示
        if(result.get('status') === null) {
            InfoHtml = InfoHtml + "（状況: 不明）";
        } else {
            InfoHtml = InfoHtml + "（状況: " + result.get('status') + "）";
        }

        // 住所の表示
        if(result.get('pref') === null) {
            InfoHtml = InfoHtml + '<br>住所: '
        } else {
            InfoHtml = InfoHtml + '<br>住所: ' + result.get('pref') + ' ';
        }

        if(result.get('gun') === null) {

        } else {
            InfoHtml = InfoHtml + result.get('gun') + ' ';
        }

        if(result.get('sikuchoson') === null) {

        } else {
            InfoHtml = InfoHtml + result.get('sikuchoson') + ' ';
        }

        if(result.get('address') === null) {

        } else {
            InfoHtml = InfoHtml + result.get('address');
        }

        // 避難者数の表示
        if(result.get('a01') === null) {
            InfoHtml = InfoHtml + "<br>避難者数: ";
        } else {
            InfoHtml = InfoHtml + "<br>避難者数: " + result.get('a01');
        }

        // 以下緊急時入力で表示される情報の表示
        if(result.get('c01_1') === null) {
            InfoHtml = InfoHtml + "<br>電気: ";
        } else {
            InfoHtml = InfoHtml + "<br>電気: " + result.get('c01_1');
        }

        if(result.get('c01_2') === null) {
            InfoHtml = InfoHtml + "<br>ガス: ";
        } else {
            InfoHtml = InfoHtml + "<br>ガス: " + result.get('c01_2');
        }

        if(result.get('c01_3') === null) {
            InfoHtml = InfoHtml + "<br>水道: ";
        } else {
            InfoHtml = InfoHtml + "<br>水道: " + result.get('c01_3');
        }

        if(result.get('c01_4') === null) {
            InfoHtml = InfoHtml + "<br>飲料水: ";
        } else {
            InfoHtml = InfoHtml + "<br>飲料水: " + result.get('c01_4');
        }

        if(result.get('c01_5') === null) {
            InfoHtml = InfoHtml + "<br>固定電話: ";
        } else {
            InfoHtml = InfoHtml + "<br>固定電話: " + result.get('c01_5');
        }

        if(result.get('c01_6') === null) {
            InfoHtml = InfoHtml + "<br>携帯電話: ";
        } else {
            InfoHtml = InfoHtml + "<br>携帯電話: " + result.get('c01_6');
        }

        if(result.get('c01_7') === null) {
            InfoHtml = InfoHtml + "<br>通信: ";
        } else {
            InfoHtml = InfoHtml + "<br>通信: " + result.get('c01_7');
        }

        if(result.get('c01_8') === null) {
            InfoHtml = InfoHtml + "<br>トイレ: ";
        } else {
            InfoHtml = InfoHtml + "<br>トイレ: " + result.get('c01_8');
        }
        if(result.get('c01_9') === null) {
            InfoHtml = InfoHtml + "<br>食料: ";
        } else {
            InfoHtml = InfoHtml + "<br>食料: " + result.get('c01_9');
        }

        return InfoHtml;
    }
}