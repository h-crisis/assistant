/**
 * Created by komori on 2016/10/06.
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
var sInfo;
var dInfo;
var dClick = 0;

function createShelterPopup(url, evt) {
    var popupHtml;
    var headerHtml;
    var infoHtml;
    var detailHtml;
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
                        headerHtml = createShelterHeaderHtml(result[i]);
                        infoHtml = createShelterInfoHtml(result[i]);
                        sInfo = infoHtml;
                        detailHtml = detailInfoHtml(result[i]);
                        dInfo = detailHtml;
                    }
                    else {
                        popupHtml = popupHtml + '<br><hr><br>' + 'name: ' + result[i].get('name');
                    }
                }
            }

            // ここから表示するしないの制御
            if(typeof popupHtml !== "undefined") {
                document.getElementById('infoHeader').style.display = 'block';
                document.getElementById('infoHeader').innerHTML = headerHtml;
                document.getElementById('info').style.display = 'block';
                document.getElementById('info').innerHTML = infoHtml;
                document.getElementById('popup').style.display = 'block';
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
    popupHtml = result.get('name');
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

function createShelterHeaderHtml(result) {
    HeaderHtml = result.get('name');
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
        HeaderHtml = HeaderHtml + "<div style='border:2px solid burlywood; background-color:#888888; color:white; text-align:center' type=button id=showBtn value=隠す onclick=showDetail()>詳細情報の表示切替</div>";
    }
    return HeaderHtml;
}

function createShelterInfoHtml(result) {
    var InfoHtml = result.get('name');

    if(typeof name === 'undefined') {
        return InfoHtml;
    } else {

        // 避難所情報入力ボタン表示
        var btnCode = '?event=' + eventCode + ',id=' + result.get('code') + ',name=' + result.get('name')　+ ',address=' + result.get('address');
        InfoHtml = "<div style='border:2px solid burlywood; background-color:#888888; text-align:center' type=button ><a href=../../html/shelter-emergency.html" + btnCode + " ,style='display:block; width:100%; color:white; text-decoration:none' id=niphLonLatE target=_blank>緊急時情報入力</a></div>"
            + "<div style='border:2px solid burlywood; background-color:#888888; text-align:center' type=button ><a href=../../html/shelter-hmethod.html" + btnCode + " ,style='display:block; width:100%; color:white; text-decoration:none' id=niphLonLatE target=_blank>避難所シート入力</a></div>"
            + "<div style='border:2px solid burlywood; background-color:#888888; text-align:center' type=button ><a href=../../html/shelter-evacuee.html" + btnCode + " ,style='display:block; width:100%; color:white; text-decoration:none' id=niphLonLatE target=_blank>避難所避難者シート入力</a></div>";

        // 状況表示
        if(result.get('status') === null) {
            InfoHtml = InfoHtml + preCells + "状況" + interCells + "不明" + subCells;
        } else {
            InfoHtml = InfoHtml + "<br>（状況: " + result.get('status') + "）";
        }

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

function detailInfoHtml(result){
    DetailHtml = result.get('name');
    var preCells = "<tr><td width='110px' style=font-size:17px;background-color:#888888;color:white>";
    var interCells = "</td><td width='250px' style=font-size:17px;background-color:white;text-align:right;>";
    var subCells ="</td></tr>";
    var tagId = ['a01','a01_1','a01_2','a02','a03','a04','a05','a06','a07','a08','a09','b01','b02','b03','b03_1','b04','b04_1',
        'b04_2','b04_3','b05','b05_1','b05_2','b05_3','b06_1','b06_2','b06_3','c01_1','c01_2','c01_3','c01_4','c01_5','c01_8',
        'c01_9','c01_7','c02_1','c02_2','c02_3','c02_4','c02_5','c02_6','c02_6_1','c02_6_2','c02_6_3','c02_6_4','c02_6_5',
        'c02_6_6','c02_7','c02_7_1','c02_8','c02_8_1','c02_9','c03_1','c03_2','c03_3','c03_4','c03_5','c03_6','c03_7',
        'c03_8','c03_9','c03_10','c03_11','c03_12','c04_1','c04_1_1','c04_2','c04_3','c04_4'];
    var tagName = ['避難者数','避難者数(昼)','避難者数(夜)','電話','FAX','メールアドレス','施設の広さ','スペース密度','一人当たり専有面積',
        '交通機関','避難者への情報伝達手段','管理統括・代表者の氏名(立場)','連絡体制','自主組織有無','自主組織について','外部組織有無','外部組織:チーム数',
        '外部組織:人数','外部組織:職種','ボランティア有無','ボランティア:チーム数','ボランティア:人数','ボランティア:職種','救護所','巡回診療',
        '地域の医師との連携','電気','ガス','水道','飲料水','固定電話','携帯電話','通信','ライフラインに関する対応','洗濯機','冷蔵庫','冷暖房',
        '照明','調理設備','トイレ充足度','トイレ箇所数','下水','トイレ清掃','トイレ汲み取り','手洗い場','手指消毒','風呂の充足度','風呂清掃状況',
        '喫煙所','分煙','設備に関する対応','施設の清掃状況','床の清掃','ゴミ収集場所','靴類履き替え場所','空調管理','粉塵','生活騒音','寝具',
        '寝具乾燥対策','ペット対策','ペットの収容対策','衛生面に関する対応','食事の充足度','食事回数/日','炊き出し','残飯処理','食事に関する対応']
        var btnCode = '?event=' + eventCode + ',id=' + result.get('code') + ',name=' + result.get('name')　+ ',address=' + result.get('address');
        DetailHtml = "<div style='border:2px solid burlywood; background-color:#888888; text-align:center' type=button ><a href=../../html/shelter-emergency.html" + btnCode + " ,style='display:block; width:100%; color:white; text-decoration:none' id=niphLonLatE target=_blank>緊急時情報入力</a></div>"
            + "<div style='border:2px solid burlywood; background-color:#888888; text-align:center' type=button ><a href=../../html/shelter-hmethod.html" + btnCode + " ,style='display:block; width:100%; color:white; text-decoration:none' id=niphLonLatE target=_blank>避難所シート入力</a></div>"
            + "<div style='border:2px solid burlywood; background-color:#888888; text-align:center' type=button ><a href=../../html/shelter-evacuee.html" + btnCode + " ,style='display:block; width:100%; color:white; text-decoration:none' id=niphLonLatE target=_blank>避難所避難者シート入力</a></div>";

        // 状況表示
        if(result.get('status') === null) {
            DetailHtml = DetailHtml + preCells + "状況" + interCells + "不明" + subCells;
        } else {
            DetailHtml = DetailHtml + "<br>（状況: " + result.get('status') + "）";
        }

        // 住所の表示
        if(result.get('pref') === null) {
            DetailHtml = DetailHtml + preCells + "都道府県" + interCells + subCells;
        } else {
            DetailHtml = DetailHtml + preCells + "都道府県" + interCells + result.get('pref') + subCells;
        }

        if(result.get('gun') === "") {

        } else {
            DetailHtml = DetailHtml + preCells + "住所" + interCells + result.get('gun') + result.get('sikuchoson')+ result.get('address') + subCells;
        }

        if(result.get('sikuchoson') === null) {

        } else {
            DetailHtml = DetailHtml + preCells + "住所" + interCells + result.get('sikuchoson')+ result.get('address') + subCells;
        }

        // 詳細情報の表示
        for (var i=0; i<tagId.length; i++) {
            if (result.get(tagId[i]) === null) {
                DetailHtml = DetailHtml + preCells + tagName[i] + interCells + subCells;
            } else {
                DetailHtml = DetailHtml + preCells + tagName[i] + interCells + result.get(tagId[i]) + subCells;
            }
        }
    return DetailHtml
}

function showHide(){
    // 情報画面を非表示にする
    document.getElementById('info').style.display = 'none';
    document.getElementById('infoHeader').style.display = 'none';
}

function showDetail(){
    // 避難所情報の概要/詳細切り替え
    if (dClick%2 == 0) {
        document.getElementById('info').innerHTML = dInfo;
        dClick++;
    } else {
        document.getElementById('info').innerHTML = sInfo;
        dClick++;
    }
}

function shelterButton() {
    // 避難所をマップに表示させるかどうか
    if (!shelterLayer.getVisible()) {
        shelterLayer.setVisible(true);
        this.style.backgroundColor = "green";
    } else {
        shelterLayer.setVisible(false);
        this.style.backgroundColor = "whitesmoke";
    }
};