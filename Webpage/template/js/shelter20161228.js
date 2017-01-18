/**
 * Created by komori on 2016/12/20.
 */

var shelterLayerWmsLayer = 'event_shelter_' + eventCode;

var shelterLayer = new ol.layer.Tile({
    source: new ol.source.TileWMS({
        url: wms,
        params: {
            'VERSION': '1.1.1',
            tiled: true,
            STYLES: '',
            LAYERS: shelterLayerWmsLayer
        }
    })
});
var sInfo;
var dInfo;
var eInfo;
var dClick = 0;
var coordinate;

function createShelterPopup(url, evt) {
    var popupHtml;
    var headerHtml;
    var infoHtml;
    var detailHtml;
    var evacueeHtml;

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
                        headerHtml = createShelterHeaderHtml(result[i]);
                        infoHtml = createShelterInfoHtml(result[i]);
                        sInfo = infoHtml;
                        detailHtml = detailInfoHtml(result[i]);
                        dInfo = detailHtml;
                        evacueeHtml = evacueeInfoHtml(result[i]);
                        eInfo = evacueeHtml;
                    }
                    else {
                    }
                }
            }

            // ここから表示するしないの制御
            if(typeof infoHtml !== "undefined") {
                document.getElementById('info').style.display = 'block';
                document.getElementById('info').innerHTML = infoHtml;
                document.getElementById('popup').style.display = 'block';
                /*
                var element = document.getElementsByClassName('infoTab');
                for (var i=0;i<element.length;i++) {
                    element[i].style.display = "block";
                }
                */
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

function createShelterHeaderHtml(result) {}

function createShelterInfoHtml(result) {
    var InfoHtml = result.get('name');

    if(typeof name === 'undefined') {
        return InfoHtml;
    } else {

        coordinate = result.get('geometry').A;

        var iconUrl;
        if(result.get('status') === null) {
            iconUrl = '../../img/shelter/grey.png';
        } else if(result.get('status') === 'evaluated' && result.get('a01') !== null) {
            iconUrl = '../../img/shelter/red.png';
        } else if(result.get('status') === 'evaluated' && result.get('a01') === null) {
            iconUrl = '../../img/shelter/blue.png';
        } else {
            iconUrl = '../../img/shelter/yellow.png';
        }

        InfoHtml = "<div id='landscape'><img id='landIcon' src=" + iconUrl + "></div>";

        // 建築物名表示
        if (result.get('name') === null) {
        } else {
            if(result.get('name').length < 17) {
                InfoHtml = InfoHtml + "<div id='blueback'><a id='title'>" + result.get('name') + "</a>";
                // InfoHtml = InfoHtml + "<br><a style='font-family: Helvetica'>(" + result.get('pref') + result.get('gun') + result.get('sikuchoson') + result.get('address') + ")</a>"
            } else {
                InfoHtml = InfoHtml + "<div id='blueback'><a id='titleTooLong'>" + result.get('name') + "</a>";
                // InfoHtml = InfoHtml + "<br><a style='font-family: Helvetica'>(" + result.get('pref') + result.get('gun') + result.get('sikuchoson') + result.get('address') + ")</a>"
            }
        }

        // 状況表示

        // 住所の表示
        InfoHtml = InfoHtml + preCells + "style='color:white;'" + interCells + "避難所" + subCells;
        InfoHtml = InfoHtml + preCells + "style='color:white;'" + interCells + "ID:"　+ result.get('code') + subCells + "</div>";
        InfoHtml = InfoHtml + preCells + interCells + "&nbsp;&nbsp;" +  result.get('pref') + result.get('gun') + result.get('sikuchoson') + result.get('address') + subCells;

        // 避難者数の表示
        if(result.get('a01') === null || result.get('a01') === "不明") {
            InfoHtml = InfoHtml + preCells + "id=NOCLRShelter>避難者数</a><a" + interCells + "不明" + subCells;
        } else {
            InfoHtml = InfoHtml + preCells + "id=NOCLRShelter>避難者数</a><a" + interCells + result.get('a01') + subCells;
        }

        InfoHtml = InfoHtml + "<hr>";

        // 以下緊急時入力で表示される情報の表示
        if(result.get('c01_1') === null || result.get('c01_1') === "不明") {
            InfoHtml = InfoHtml + preCells + "id=NT>電　　気</a><a" + interCells + "不明" + subCells;
        } else {
            if(result.get('c01_1') === "可") {
                InfoHtml = InfoHtml + preCells + "id=EX>電　　気</a><a" + interCells + result.get('c01_1') + subCells;
            } else if(result.get('c01_1') === "不可") {
                InfoHtml = InfoHtml + preCells + "id=TR>電　　気</a><a" + interCells + result.get('c01_1') + subCells;
            } else if(result.get('c01_1') === "無し") {
                InfoHtml = InfoHtml + preCells + "id=UK>電　　気</a><a" + interCells + result.get('c01_1') + subCells;
            }
        }

        if(result.get('c01_2') === null || result.get('c01_2') === "不明") {
            InfoHtml = InfoHtml + preCells + "id=NT>ガ　　ス</a><a" + interCells + "不明" + subCells;
        } else {
            if(result.get('c01_2') === "可") {
                InfoHtml = InfoHtml + preCells + "id=EX>ガ　　ス</a><a" + interCells + result.get('c01_2') + subCells;
            } else if(result.get('c01_2') === "不可") {
                InfoHtml = InfoHtml + preCells + "id=TR>ガ　　ス</a><a" + interCells + result.get('c01_2') + subCells;
            } else if (result.get('c01_2') === "無し") {
                InfoHtml = InfoHtml + preCells + "id=UK>ガ　　ス</a><a" + interCells + result.get('c01_2') + subCells;
            }
        }

        if(result.get('c01_3') === null || result.get('c01_3') === "不明") {
            InfoHtml = InfoHtml + preCells + "id=NT>水　　道</a><a " + interCells + "不明" + subCells;
        } else {
            if(result.get('c01_3') === "可") {
                InfoHtml = InfoHtml + preCells + "id=EX>水　　道</a><a" + interCells + result.get('c01_3') + subCells;
            } else if(result.get('c01_3') === "不可") {
                InfoHtml = InfoHtml + preCells + "id=TR>水　　道</a><a" + interCells + result.get('c01_3') + subCells;
            } else if(result.get('c01_3') === "無し") {
                InfoHtml = InfoHtml + preCells + "id=UK>水　　道</a><a" + interCells + result.get('c01_3') + subCells;
            }
        }

        if(result.get('c01_4') === null || result.get('c01_4') === "不明") {
            InfoHtml = InfoHtml + preCells + "id=NT>飲 料 水</a><a" + interCells + "不明" + subCells;
        } else {
            if(result.get('c01_4') === "飲用可") {
                InfoHtml = InfoHtml + preCells + "id=EX>飲 料 水</a><a" + interCells + result.get('c01_4') + subCells;
            } else if(result.get('c01_4') === "飲用不可") {
                InfoHtml = InfoHtml + preCells + "id=TR>飲 料 水</a><a" + interCells + result.get('c01_4') + subCells;
            } else if(result.get('c01_4') === "無し") {
                InfoHtml = InfoHtml + preCells + "id=UK>飲 料 水</a><a" + interCells + result.get('c01_4') + subCells;
            }
        }

        if(result.get('c01_5') === null || result.get('c01_5') === "不明") {
            InfoHtml = InfoHtml + preCells + "id=NT>固定電話</a><a" + interCells + "不明" + subCells;
        } else {
            if(result.get('c01_5') === "可") {
                InfoHtml = InfoHtml + preCells + "id=EX>固定電話</a><a" + interCells + result.get('c01_5') + subCells;
            } else if(result.get('c01_5') === "不可") {
                InfoHtml = InfoHtml + preCells + "id=TR>固定電話</a><a" + interCells + result.get('c01_5') + subCells;
            } else if(result.get('c01_5') === "無し") {
                InfoHtml = InfoHtml + preCells + "id=UK>固定電話</a><a" + interCells + result.get('c01_5') + subCells;
            }
        }

        if(result.get('c01_8') === null || result.get('c01_8') === "不明") {
            InfoHtml = InfoHtml + preCells + "id=NT>携帯電話</a><a" + interCells + "不明" + subCells;
        } else {
            if(result.get('c01_8') === "可") {
                InfoHtml = InfoHtml + preCells + "id=EX>携帯電話</a><a" + interCells + result.get('c01_8') + subCells;
            } else if(result.get('c01_8') === "不可") {
                InfoHtml = InfoHtml + preCells + "id=TR>携帯電話</a><a" + interCells + result.get('c01_8') + subCells;
            } else if(result.get('c01_8') === "無し") {
                InfoHtml = InfoHtml + preCells + "id=UK>携帯電話</a><a" + interCells + result.get('c01_8') + subCells;
            }
        }

        if(result.get('c01_9') === null || result.get('c01_9') === "不明") {
            InfoHtml = InfoHtml + preCells + "id=NT>通　　信</a><a" + interCells + "不明" + subCells;
        } else {
            if(result.get('c01_9') === "可") {
                InfoHtml = InfoHtml + preCells + "id=EX'>通　　信</a><a" + interCells + result.get('c01_9') + subCells;
            } else if(result.get('c01_9') === "不可") {
                InfoHtml = InfoHtml + preCells + "id=TR'>通　　信</a><a" + interCells + result.get('c01_9') + subCells;
            } else if(result.get('c01_9') === "無し") {
                InfoHtml = InfoHtml + preCells + "id=UK>通　　信</a><a" + interCells + result.get('c01_9') + subCells;
            }
        }

        if(result.get('c02_6') === null || result.get('c02_6') === "不明") {
            InfoHtml = InfoHtml + preCells + "id=NT>ト イ レ</a><a" + interCells + "不明" + subCells;
        } else {
            if(result.get('c02_6') === "使用可") {
                InfoHtml = InfoHtml + preCells + "id=EX>ト イ レ</a><a" + interCells + result.get('c02_6') + subCells;
            } else if(result.get('c02_6') === "使用不可") {
                InfoHtml = InfoHtml + preCells + "id=TR>ト イ レ</a><a" + interCells + result.get('c02_6') + subCells;
            } else if(result.get('c02_6') === "無し") {
                InfoHtml = InfoHtml + preCells + "id=UK>ト イ レ</a><a" + interCells + result.get('c02_6') + subCells;
            }
        }

        if(result.get('c04_1') === null || result.get('c04_1') === "不明") {
            InfoHtml = InfoHtml + preCells + "id=NT>食　　料</a><a" + interCells + "不明" + subCells;
        } else {
            if(result.get('c04_1') === "有") {
                InfoHtml = InfoHtml + preCells + "id=EX>食　　料</a><a" + interCells + result.get('c04_1') + subCells;
            } else if(result.get('c04_1') === "無し") {
                InfoHtml = InfoHtml + preCells + "id=UK>食　　料</a><a" + interCells + result.get('c04_1') + subCells;
            }
        }
        var inputCode = '?event=' + eventCode + ',id=' + result.get('code') + ',name=' + result.get('name')　+ ',address=' + result.get('address');
        InfoHtml = InfoHtml + "<div id='inputShelter' onclick=window.open('../../html/shelter-emergency.html" + inputCode +  "','_blank')><img id='inputIcon' src='../../img/img/document.png '></div>";
        return InfoHtml;
    }
}


function detailInfoHtml(result){
    DetailHtml = result.get('name');
    var tagId = ['a01','a01_1','a01_2','a02','a03','a04','a05','a06','a07','a08','a09','b02','b03','b03_1','b04','b04_1',
        'b04_2','b04_3','b05','b05_1','b05_2','b05_3','b06_1','b06_2','b06_3','c01_1','c01_2','c01_3','c01_4','c01_5','c01_8',
        'c01_9','c01_7','c02_1','c02_2','c02_3','c02_4','c02_5','c02_6','c02_6_1','c02_6_2','c02_6_3','c02_6_4','c02_6_5',
        'c02_6_6','c02_7','c02_7_1','c02_8','c02_8_1','c02_9','c03_1','c03_2','c03_3','c03_4','c03_5','c03_6','c03_7',
        'c03_8','c03_9','c03_10','c03_11','c03_12','c04_1','c04_1_1','c04_2','c04_3','c04_4'];
    var tagName = ['避難者数','避難者数(昼)','避難者数(夜)','電話','FAX','メールアドレス','施設の広さ','スペース密度','一人当たり専有面積',
        '交通機関','避難者への情報伝達手段','連絡体制','自主組織有無','自主組織について','外部組織有無','外部組織:チーム数',
        '外部組織:人数','外部組織:職種','ボランティア有無','ボランティア:チーム数','ボランティア:人数','ボランティア:職種','救護所','巡回診療',
        '地域の医師との連携','電気','ガス','水道','飲料水','固定電話','携帯電話','通信','ライフラインに関する対応','洗濯機','冷蔵庫','冷暖房',
        '照明','調理設備','トイレ充足度','トイレ箇所数','下水','トイレ清掃','トイレ汲み取り','手洗い場','手指消毒','風呂の充足度','風呂清掃状況',
        '喫煙所','分煙','設備に関する対応','施設の清掃状況','床の清掃','ゴミ収集場所','靴類履き替え場所','空調管理','粉塵','生活騒音','寝具',
        '寝具乾燥対策','ペット対策','ペットの収容対策','衛生面に関する対応','食事の充足度','食事回数/日','炊き出し','残飯処理','食事に関する対応'];
    var btnId = ['a01','a01_1','a01_2','a02','a03','a04','a05','a07','a08','a09','b02','b03_1','b04_1','b04_2','b04_3','b05_1','b05_2','b05_3','c01_7','c02_6_1','c02_7_1','c02_9',
        'c03_12','c04_1_1','c04_4','a06','b03','b04','b05','b06_1','b06_2','b06_3','c01_1','c01_2','c01_3','c01_4','c01_5','c01_8','c01_9','c02_1','c02_2','c02_3','c02_4','c02_5','c02_6',
        'c02_6_2','c02_6_3','c02_6_4','c02_6_5','c02_6_6','c02_7','c02_8','c02_8_1','c03_1','c03_2','c03_3','c03_4','c03_5','c03_6','c03_7','c03_8','c03_9','c03_10','c03_11',
        'c04_1','c04_2','c04_3'];

    coordinate = result.get('geometry').A;
    DetailHtml = "<div id='smap' class='map' style='width: 100px;height: 100px'></div>";

    // 建築物名表示
    if (result.get('name') === null) {
    } else {
        DetailHtml = DetailHtml + "<a style='font-family: Helvetica'>" + result.get('name') + "</a>";
        DetailHtml = DetailHtml + "<br><a style='font-family: Helvetica'>(" + result.get('pref') + result.get('gun') + result.get('sikuchoson') + result.get('address') + ")</a>"
    }

    var btnCode = '?event=' + eventCode + ',id=' + result.get('code') + ',name=' + result.get('name').replace(/ /g,"")　+ ',address=' + result.get('address').replace(/ /g,"") ;
    for (var i=0; i<btnId.length; i++) {
        // if (result.get(btnIdSh[i]) === null || result.get(btnIdSh[i])===undefined) {
        //    EvacueeHtml = EvacueeHtml + preCells + tagName[i] + interCells + subCells;
        btnCode = btnCode + "," + btnId[i] + "=" + result.get(btnId[i]);
    }
    if (window.innerWidth >= 780) {
        DetailHtml = DetailHtml +"<div style='border-radius: 10px; margin:0 0 5px; font-family:helvetica; background-color:#333333; text-align:center; opacity:1; width:350px' type=button onmousemove='this.style.opacity=0.8' onmouseout='this.style.opacity=1'><a style='display:block; width:100%; color:white; text-decoration: none' href=../../html/shelter-hmethod20161023.html" + btnCode + " id=niphLonLatE target=_blank>避難所シート入力</a></div>";
    } else if (window.innerWidth >= 480) {
        DetailHtml = DetailHtml +"<div style='border-radius: 10px; margin:0 0 5px; font-family:helvetica; background-color:#333333; text-align:center; opacity:1; width:220px' type=button onmousemove='this.style.opacity=0.8' onmouseout='this.style.opacity=1'><a style='display:block; width:100%; color:white; text-decoration: none' href=../../html/shelter-hmethod20161023.html" + btnCode + " id=niphLonLatE target=_blank>避難所シート入力</a></div>";
    } else {
        DetailHtml = DetailHtml +"<div style='border-radius: 10px; margin:0 0 5px; font-family:helvetica; background-color:#333333; text-align:center; opacity:1; width:220px' type=button onmousemove='this.style.opacity=0.8' onmouseout='this.style.opacity=1'><a style='display:block; width:100%; color:white; text-decoration: none' href=../../html/shelter-hmethod20161023.html" + btnCode + " id=niphLonLatE target=_blank>避難所シート入力</a></div>";
    }

    // 状況表示
    if(result.get('status') === null) {
        DetailHtml = DetailHtml + preCells + "状況" + interCells + "推計値" + subCells;
    } else if(result.get('status') === 'evaluated') {
        DetailHtml = DetailHtml + preCells　+ "状況" + interCells + "調査値" + subCells;
    }
    else {
        DetailHtml = DetailHtml + preCells　+ "状況" + interCells + "不明" + subCells;
    }

    // 住所の表示
    DetailHtml = DetailHtml + preCells + "避難所コード" + interCells + result.get('code') + subCells;
    DetailHtml = DetailHtml + preCells + "都道府県" + interCells + result.get('pref') + subCells;
    DetailHtml = DetailHtml + preCells + "郡" + interCells + result.get('gun') + subCells;
    DetailHtml = DetailHtml + preCells + "市区町村" + interCells + result.get('sikuchoson')　+ subCells;
    DetailHtml = DetailHtml + preCells + "住所" + interCells + result.get('address')　+ subCells;

    // 詳細情報の表示
    for (var i=0; i<tagId.length; i++) {
        if (result.get(tagId[i]) === null || result.get(tagId[i])===undefined) {
            DetailHtml = DetailHtml + preCells + tagName[i] + interCells + subCells;
        } else {
            DetailHtml = DetailHtml + preCells + tagName[i] + interCells + result.get(tagId[i]) + subCells;
        }
    }
    return DetailHtml
}

function evacueeInfoHtml(result){
    EvacueeHtml = result.get('name');
    var tagId = ['d01','d01_1','d01_2','d02','d02_1','d03','d04','d05','d05_1','d05_2','d05_3','d06','d06_1', 'd06_2',
        'd06_3','d06_4','d07','d08','d09','d10','d12','d11','d11_1','d11_2','d11_3', 'd13','e01','e01_1','e01_2','e01_3',
        'e02','f01','f01_1','f01_2','f01_3','f02','f02_1','f02_2','f02_3','f03','f03_1','f03_2','f03_3','f04','f04_1',
        'f04_2','f04_3','f05','f05_1','f05_2','f05_3','f06','f06_1','f06_2','f06_3','f07','f07_1','f07_2','f07_3','f08',
        'f08_1','f08_2','f08_3','f09','f09_1','f09_2','f09_3','f10','f10_1','f10_2','f10_3','f11','f12','f13','f14','f15',
        'f16','g01','g02','g03','g04','h01','h02','h03','h04'];
    var tagName = ['高齢者','75歳以上','要介護認定高齢者','妊婦','うち妊婦健診受診困難者数','産婦','乳児','幼児・児童','身体障害児',
        '知的障害児','発達障害児','障害者','身体障害者','知的障害者','精神障害者','発達障害者','難病患者','在宅酸素療養者','人工透析者',
        'アレルギー疾患児・者','外国人','要援助者数','要援助者の内全介助者','要援助者の内一部介助','要援助者の内認知障害',
        '要配慮者への対応等情報','服薬者','高血圧治療薬服薬者','糖尿病治療薬服薬者','向精神薬服薬者','服薬者への対応等情報','外傷者数総数',
        '乳児・幼児の外傷者','妊婦の外傷者','高齢者の外傷者','下痢症状者総数','乳児・幼児の下痢症状者','妊婦のの下痢症状者','高齢者の下痢症状者',
        '嘔吐症状者総数','乳児・幼児の嘔吐症状者','妊婦の嘔吐症状者','高齢者の嘔吐症状者','発熱症状者総数','乳児・幼児の発熱症状者',
        '妊婦の発熱症状者','高齢者の発熱症状者','咳症状者総数','乳児・幼児の咳症状者','妊婦の咳症状者','高齢者の咳症状者','便秘症状者総数',
        '乳児・幼児の便秘症状者','妊婦の便秘症状者','高齢者の便秘症状者','食欲不振者総数','乳児・幼児の食欲不振者','妊婦の食欲不振者',
        '高齢者の食欲不振者','頭痛症状者総数','乳児・幼児の頭痛症状者','妊婦の頭痛症状者','高齢者の頭痛症状者','不眠症状者総数',
        '乳児・幼児の不眠症状者','妊婦の不眠症状者','高齢者の不眠症状者','不安症状者総数','乳児・幼児の不安症状者','妊婦の不安症状者',
        '高齢者の不安症状者','専門的医療ニーズ','小児疾患','精神疾患','周産期','歯科','有症者への対応等情報','食中毒様症状(下痢、嘔吐などの動向)',
        '風邪様症状(咳・発熱などの動向)','感染症症状・その他','防疫に関するその他情報','全体の健康状態','活動内容','アセスメント','課題・申し送り'];

    coordinate = result.get('geometry').A;
    EvacueeHtml = "<div id='smap' class='map' style='width: 100px;height: 100px'></div>";

    // 建築物名表示
    if (result.get('name') === null) {
    } else {
        EvacueeHtml = EvacueeHtml + "<a style='font-family: Helvetica'>" + result.get('name') + "</a>";
        EvacueeHtml = EvacueeHtml + "<br><a style='font-family: Helvetica'>(" + result.get('pref') + result.get('gun') + result.get('sikuchoson') + result.get('address') + ")</a>"
    }

    var btnCode = '?event=' + eventCode + ',id=' + result.get('code') + ',name=' + result.get('name').replace(/ /g,"")　+ ',address=' + result.get('address').replace(/ /g,"");
    for (var i=0; i<tagId.length; i++) {
        // if (result.get(tagIdSh[i]) === null || result.get(tagIdSh[i])===undefined) {
        //    EvacueeHtml = EvacueeHtml + preCells + tagName[i] + interCells + subCells;
        btnCode = btnCode + "," + tagId[i] + "=" + result.get(tagId[i]);
    }
    if (window.innerWidth >= 780) {
        EvacueeHtml = EvacueeHtml + "<div style='border-radius: 10px; margin:0 0 5px; font-family:helvetica; background-color:#333333; text-align:center; opacity:1; width:350px' type=button onmousemove='this.style.opacity=0.8' onmouseout='this.style.opacity=1'><a style='display:block; width:100%; color:white; text-decoration: none' href=../../html/shelter-evacuee20161023.html" + btnCode + " id=niphLonLatE target=_blank>避難所避難者シート入力</a></div>";
    } else if (window.innerWidth >= 480) {
        EvacueeHtml = EvacueeHtml + "<div style='border-radius: 10px; margin:0 0 5px; font-family:helvetica; background-color:#333333; text-align:center; opacity:1; width:220px' type=button onmousemove='this.style.opacity=0.8' onmouseout='this.style.opacity=1'><a style='display:block; width:100%; color:white; text-decoration: none' href=../../html/shelter-evacuee20161023.html" + btnCode + " id=niphLonLatE target=_blank>避難所避難者シート入力</a></div>";
    } else {
        EvacueeHtml = EvacueeHtml + "<div style='border-radius: 10px; margin:0 0 5px; font-family:helvetica; background-color:#333333; text-align:center; opacity:1; width:220px' type=button onmousemove='this.style.opacity=0.8' onmouseout='this.style.opacity=1'><a style='display:block; width:100%; color:white; text-decoration: none' href=../../html/shelter-evacuee20161023.html" + btnCode + " id=niphLonLatE target=_blank>避難所避難者シート入力</a></div>";
    }

    // 状況表示
    if(result.get('status') === null) {
        EvacueeHtml = EvacueeHtml + preCells + "状況" + interCells + "推計値" + subCells;
    } else if(result.get('status') === 'evaluated') {
        EvacueeHtml = EvacueeHtml + preCells　+ "状況" + interCells + "調査値" + subCells;
    }
    else {
        EvacueeHtml = EvacueeHtml + preCells　+ "状況" + interCells + "不明" + subCells;
    }

    // 住所の表示
    EvacueeHtml = EvacueeHtml + preCells + "避難所コード" + interCells + result.get('code') + subCells;
    EvacueeHtml = EvacueeHtml + preCells + "都道府県" + interCells + result.get('pref') + subCells;
    EvacueeHtml = EvacueeHtml + preCells + "郡" + interCells + result.get('gun') + subCells;
    EvacueeHtml = EvacueeHtml + preCells + "市区町村" + interCells + result.get('sikuchoson')　+ subCells;
    EvacueeHtml = EvacueeHtml + preCells + "住所" + interCells + result.get('address')　+ subCells;

    // 詳細情報の表示
    for (var i=0; i<tagId.length; i++) {
        if (result.get(tagId[i]) === null || result.get(tagId[i])===undefined) {
            EvacueeHtml = EvacueeHtml + preCells + tagName[i] + interCells + subCells;
        } else {
            EvacueeHtml = EvacueeHtml + preCells + tagName[i] + interCells + result.get(tagId[i]) + subCells;
        }
    }
    return EvacueeHtml
}

function showHide(){
    // 情報画面を非表示にする
    document.getElementById('info').style.display = 'none';
    var element = document.getElementsByClassName('infoTab');
    for (var i=0;i<element.length;i++) {
        element[i].style.display = "none";
    }
}

function showDetail(id){
    // 避難所情報の概要/詳細切り替え
    /*
     if (dClick%3 == 0) {
     document.getElementById('info').innerHTML = dInfo;
     dClick++;
     } else if (dClick%3 == 1) {
     document.getElementById('info').innerHTML = eInfo;
     dClick++;
     } else {
     document.getElementById('info').innerHTML = sInfo;
     dClick++;
     }
     */
    var tab = document.getElementById(id);
    if(id == 'tab001') {
        document.getElementById('info').innerHTML = sInfo;
        document.getElementById(id).style.backgroundColor = '#eeeeee';
        document.getElementById(id).style.color = '#000000';
        document.getElementById('tab002').style.backgroundColor = '';
        document.getElementById('tab002').style.color = '';
        document.getElementById('tab003').style.backgroundColor = '';
        document.getElementById('tab003').style.color = '';
    } else if(id == 'tab002') {
        document.getElementById('info').innerHTML = dInfo;
        document.getElementById('tab001').style.backgroundColor = '#333333';
        document.getElementById('tab001').style.color = '#ffffff';
        document.getElementById(id).style.backgroundColor = '#eeeeee';
        document.getElementById(id).style.color = '#000000';
        document.getElementById('tab003').style.backgroundColor = '';
        document.getElementById('tab003').style.color = '';
    } else if(id == 'tab003') {
        document.getElementById('info').innerHTML = eInfo;
        document.getElementById('tab001').style.backgroundColor = '#333333';
        document.getElementById('tab001').style.color = '#ffffff';
        document.getElementById('tab002').style.backgroundColor = '';
        document.getElementById('tab002').style.color = '';
        document.getElementById(id).style.backgroundColor = '#eeeeee';
        document.getElementById(id).style.color = '#000000';
    }
}


function shelterButton() {
    // 避難所をマップに表示させるかどうか
    if (!shelterLayer.getVisible()) {
        shelterLayer.setVisible(true);
        this.style.backgroundColor = "#eeeeee";
        this.style.color = "#000000";
    } else {
        shelterLayer.setVisible(false);
        this.style.backgroundColor = "";
        this.style.color = "";
    }
}

function openShelterInput() {
    // 避難所をマップに表示させるかどうか
    var btnCode = '?event=' + eventCode + ',id=' + result.get('code') + ',name=' + result.get('name')　+ ',address=' + result.get('address');
    DetailHtml = "<div style='border:2px solid burlywood; background-color:#888888; text-align:center' type=button ><a href=../../html/shelter-emergency.html" + btnCode + " ,style='display:block; width:100%; color:white; text-decoration:none' id=niphLonLatE target=_blank>緊急時情報入力</a></div>"

}