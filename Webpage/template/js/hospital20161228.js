/**
 * Created by komori on 2016/10/22.
 */

var src;
var biNum;

//var styleHospR = new Array(16);
var styleHospR;
//for (var i = 0; i < 16 ; i++) {
    // src = "http://h-crisis.niph.go.jp/assistant/wp-content/uploads/sites/4/test/img/r/" + i + ".png";
    src = "../../img/hospital/hspt_red.png";
    //styleHospR[i] = new ol.style.Style({
    styleHospR = new ol.style.Style({
        text: new ol.style.Text({
            offsetY: 8,
            font: '12px Calibri,sans-serif',
            fill: new ol.style.Fill({color: "#0000bb"}),
            stroke: new ol.style.Stroke({color: "#ffffff", width: 2})
        }),
        image: new ol.style.Icon({
            scale: 0.075,
            anchor: [0.5, 1],
            anchorXUnits: 'fraction',
            anchorYUnits: 'fraction',
            opacity: 1,
            src: src
        })
    });
//}

//var styleHospB = new Array(16);
var styleHospB;
//for (var i = 0; i < 16 ; i++) {
    //src = "http://h-crisis.niph.go.jp/assistant/wp-content/uploads/sites/4/test/img/b/" + i + ".png";
    src = "../../img/hospital/hspt_blue.png";
    //styleHospB[i] = new ol.style.Style({
    styleHospB = new ol.style.Style({
        text: new ol.style.Text({
            offsetY: 8,
            font: '12px Calibri,sans-serif',
            fill: new ol.style.Fill({color: "#0000bb"}),
            stroke: new ol.style.Stroke({color: "#ffffff", width: 2}),
        }),
        image: new ol.style.Icon({
            scale: 0.075,
            anchor: [0.5, 1],
            anchorXUnits: 'fraction',
            anchorYUnits: 'fraction',
            opacity: 1,
            src: src
        })
    });
//}

//var styleHospG = new Array(16);
var styleHospG;
//for (var i = 0; i < 16 ; i++) {
    //src = "http://h-crisis.niph.go.jp/assistant/wp-content/uploads/sites/4/test/img/g/" + i + ".png";
    src = "../../img/hospital/hspt_grey.png";
    //styleHospG[i] = new ol.style.Style({
    styleHospG = new ol.style.Style({
        text: new ol.style.Text({
            offsetY: 8,
            font: '12px Calibri,sans-serif',
            fill: new ol.style.Fill({color: "#0000bb"}),
            stroke: new ol.style.Stroke({color: "#ffffff", width: 2})
        }),
        image: new ol.style.Icon({
            scale: 0.075,
            anchor: [0.5, 1],
            anchorXUnits: 'fraction',
            anchorYUnits: 'fraction',
            opacity: 1,
            src: src
        })
    });
//}

var styleBack = new ol.style.Style({
    image: new ol.style.Icon({
        scale: 0.075,
        anchor: [0.5, 1],
        anchorXUnits: 'fraction',
        anchorYUnits: 'fraction',
        opacity: 0.1,
        src: src
    })
});


/*
var styleHospG = new Array(16);
for (var i = 0; i < 16 ; i++) {
    src = "/Users/komori/IdeaProjects/assistant/Webpage/template/img/hospital/" + i + ".png";
    styleHospG[i] = new ol.style.Style({
        text: new ol.style.Text({
            offsetY: 8,
            font: '12px Calibri,sans-serif',
            fill: new ol.style.Fill({color: "#0000bb"}),
            stroke: new ol.style.Stroke({color: "#ffffff", width: 2})
        }),
        image: new ol.style.Icon({
            scale: 0.075,
            anchor: [0.5, 1],
            anchorXUnits: 'fraction',
            anchorYUnits: 'fraction',
            opacity: 0.85,
            src: src
        })
    })
}
*/

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

var url = 'http://h-crisis.niph.go.jp/wp-content/uploads/data/medical_status/latest/hcrisis_medical_status.geojson';

var hospLayer = new ol.layer.Vector({
    source: new ol.source.Vector({
        //url: 'geojson/hcrisis_medical_status.geojson',
        url: url,
        format: new ol.format.GeoJSON()
    }),
    style: function(feature, resolution) {
        biNum = (feature.get('saigai') * 8) + (feature.get('kyukyu') * 4) + (feature.get('hibaku') * 2) + (feature.get('dmat') * 1);

        if(deviceIs == "MOB") {
            styleHospR.getImage().setScale(0.25);
            styleHospG.getImage().setScale(0.25);
            styleHospB.getImage().setScale(0.25);
            styleHospR.getText().setScale(2.5);
            styleHospG.getText().setScale(2.5);
            styleHospB.getText().setScale(2.5);
            styleHospR.getText().setOffsetY(18);
            styleHospG.getText().setOffsetY(18);
            styleHospB.getText().setOffsetY(18);
        }

        if (feature.get('assist') == "要"){
            styleHospR.getText().setText(resolution < 0.0003 ? feature.get('name') : '')
            styleHospR.getImage().setOpacity(resolution < 0.0009 ? 1 : 0);
            return styleHospR;
        } else if(feature.get('assist') == "未" && feature.get('mds') == "未入力") {
            styleHospG.getImage().setOpacity(resolution < 0.0009 ? 1 : 0);
            styleHospG.getText().setText(resolution < 0.0003 ? feature.get('name') : '');
            return styleHospG;
        } else {
            styleHospB.getImage().setOpacity(resolution < 0.0009 ? 1 : 0);
            styleHospB.getText().setText(resolution < 0.0003 ? feature.get('name') : '');
            return styleHospB;
                /*
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
            */
        }

    }
});

var clinicDep = new ol.layer.Vector({
    source: new ol.source.Vector({
        url: 'geojson/clinical_departments.geojson',
        format: new ol.format.GeoJSON()
    }),
    style: function(feature, resolution) {
        styleBack.getImage().setOpacity(resolution < 0.0009 ? 0.1 : 0);
        return styleBack;
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
        clinicDep.setVisible(true);
        this.style.backgroundColor = "#eeeeee";
        this.style.color = "#000000";
    } else {
        hospLayer.setVisible(false);
        clinicDep.setVisible(false);
        this.style.backgroundColor = "";
        this.style.color = "";
    }
};

// 医療機関のポップアップを作成する関数
function createHtmlHospital(evt,feature) {

    document.getElementById('info').innerHTML = "";
    DetailHtml = "";
    var coordinate = feature.T.geometry.A;

    var tagNameOfficial = ['医療機関コード', '名称', '都道府県名', '二次医療圏', '市区町村', '住所', '災害拠点医療機関', '救急指定医療機関',
        '被爆対応医療機関', 'DMAT配置医療機関', '支援要否', '医療派遣ステータス',
        'チーム数', '更新日時', '医師出勤状況', '緊急時入力/入院病棟倒壊・倒壊の恐れ', '緊急時入力/ライフライン・サプライ状況/電気使用不可',
        '緊急時入力/ライフライン・サプライ状況/水使用不可', '緊急時入力/ライフライン・サプライ状況/医療ガス使用不可',
        '緊急時入力/ライフライン・サプライ状況/医薬品衛生資器材使用不可', '緊急時入力/多数患者受診', '緊急時入力/職員状況',
        '緊急時入力/その他', '緊急時入力/情報取得日時', '緊急時入力/緊急連絡先/電話番号', '緊急時入力/緊急連絡先/メールアドレス', '緊急時入力/更新日時',
        '詳細入力/施設の倒壊・倒壊の恐れ/入院病棟', '詳細入力/施設の倒壊・倒壊の恐れ/救急外来', '詳細入力/施設の倒壊・倒壊の恐れ/一般外来',
        '詳細入力/施設の倒壊・倒壊の恐れ/手術室', '詳細入力/施設の倒壊・倒壊の恐れ/その他', '詳細入力/ライフライン・サプライ状況/電気使用不可',
        '詳細入力/ライフライン・サプライ状況/水使用不可', '詳細入力/ライフライン・サプライ状況/医療ガス使用不可',
        '詳細入力/ライフライン・サプライ状況/配管損傷有無', '詳細入力/ライフライン・サプライ状況/食糧供給なし',
        '詳細入力/ライフライン・サプライ状況/医薬品供給なし', '詳細入力/ライフライン・サプライ状況/詳細有無',
        '詳細入力/機能/手術不可', '詳細入力/機能/人工透析不可', '詳細入力/現在の患者数状況/実働病床数', '詳細入力/現在の患者数状況/受入患者数/重症',
        '詳細入力/現在の患者数状況/受入患者数/中等症', '詳細入力/現在の患者数状況/在院患者数/重症', '詳細入力/現在の患者数状況/在院患者数/中等症',
        '詳細入力/今後、転送が必要な患者数/重症', '詳細入力/今後、転送が必要な患者数/中等症', '詳細入力/今後、転送が必要な患者数/人工呼吸',
        '詳細入力/今後、転送が必要な患者数/酸素', '詳細入力/今後、転送が必要な患者数/担送', '詳細入力/今後、転送が必要な患者数/護送',
        '詳細入力/今後、受け入れ可能な患者数/災害時診療能力', '詳細入力/今後、受け入れ可能な患者数/重症', '詳細入力/今後、受け入れ可能な患者数/中等症',
        '詳細入力/今後、受け入れ可能な患者数/人工呼吸', '詳細入力/今後、受け入れ可能な患者数/酸素', '詳細入力/今後、受け入れ可能な患者数/担送',
        '詳細入力/今後、受け入れ可能な患者数/護送', '詳細入力/外来受付状況/外来受付状況', '詳細入力/外来受付状況/受付時間帯1/開始',
        '詳細入力/外来受付状況/受付時間帯1/終了', '詳細入力/外来受付状況/受付時間帯2/開始', '詳細入力/外来受付状況/受付時間帯2/終了',
        '詳細入力/外来受付状況/受付時間帯3/開始', '詳細入力/外来受付状況/受付時間帯3/終了', '詳細入力/職員数/出勤医師数',
        '詳細入力/職員数/出勤医師数 内DMAT', '詳細入力/職員数/出勤看護師数', '詳細入力/職員数/出勤看護師数 内DMAT',
        '詳細入力/職員数/その他出勤人数', '詳細入力/職員数/その他出勤人数 内DMAT', '詳細入力/その他/その他',
        '詳細入力/情報取得日時', '詳細入力/更新日時', '救護所有無', '所属本部'];

    var tagName = ['医療機関コード', '名称', '都道府県名', '二次医療圏', '市区町村', '住所', '災害拠点医療機関', '救急指定医療機関',
        '被爆対応医療機関', 'DMAT配置医療機関', '支援要否', '医療派遣ステータス', 'チーム数', '更新日時', '医師出勤状況', '病棟倒壊・倒壊の恐れ',
        '電気不足', '水不足', '医療ガス不足', '医薬品衛生資器材不足', '多数患者受診', '職員不足',
        '緊急時入力/その他', '緊急時入力/情報取得日時', '緊急時入力/緊急連絡先/電話番号', '緊急時入力/緊急連絡先/メールアドレス', '緊急時入力/更新日時',
        '詳細入力/施設の倒壊・倒壊の恐れ/入院病棟', '詳細入力/施設の倒壊・倒壊の恐れ/救急外来', '詳細入力/施設の倒壊・倒壊の恐れ/一般外来',
        '詳細入力/施設の倒壊・倒壊の恐れ/手術室', '詳細入力/施設の倒壊・倒壊の恐れ/その他', '詳細入力/ライフライン・サプライ状況/電気使用不可',
        '詳細入力/ライフライン・サプライ状況/水使用不可', '詳細入力/ライフライン・サプライ状況/医療ガス使用不可',
        '詳細入力/ライフライン・サプライ状況/配管損傷有無', '詳細入力/ライフライン・サプライ状況/食糧供給なし',
        '詳細入力/ライフライン・サプライ状況/医薬品供給なし', '詳細入力/ライフライン・サプライ状況/詳細有無',
        '詳細入力/機能/手術不可', '詳細入力/機能/人工透析不可', '詳細入力/現在の患者数状況/実働病床数', '詳細入力/現在の患者数状況/受入患者数/重症',
        '詳細入力/現在の患者数状況/受入患者数/中等症', '詳細入力/現在の患者数状況/在院患者数/重症', '詳細入力/現在の患者数状況/在院患者数/中等症',
        '詳細入力/今後、転送が必要な患者数/重症', '詳細入力/今後、転送が必要な患者数/中等症', '詳細入力/今後、転送が必要な患者数/人工呼吸',
        '詳細入力/今後、転送が必要な患者数/酸素', '詳細入力/今後、転送が必要な患者数/担送', '詳細入力/今後、転送が必要な患者数/護送',
        '詳細入力/今後、受け入れ可能な患者数/災害時診療能力', '詳細入力/今後、受け入れ可能な患者数/重症', '詳細入力/今後、受け入れ可能な患者数/中等症',
        '詳細入力/今後、受け入れ可能な患者数/人工呼吸', '詳細入力/今後、受け入れ可能な患者数/酸素', '詳細入力/今後、受け入れ可能な患者数/担送',
        '詳細入力/今後、受け入れ可能な患者数/護送', '詳細入力/外来受付状況/外来受付状況', '詳細入力/外来受付状況/受付時間帯1/開始',
        '詳細入力/外来受付状況/受付時間帯1/終了', '詳細入力/外来受付状況/受付時間帯2/開始', '詳細入力/外来受付状況/受付時間帯2/終了',
        '詳細入力/外来受付状況/受付時間帯3/開始', '詳細入力/外来受付状況/受付時間帯3/終了', '詳細入力/職員数/出勤医師数',
        '詳細入力/職員数/出勤医師数 内DMAT', '詳細入力/職員数/出勤看護師数', '詳細入力/職員数/出勤看護師数 内DMAT',
        '詳細入力/職員数/その他出勤人数', '詳細入力/職員数/その他出勤人数 内DMAT', '詳細入力/その他/その他',
        '詳細入力/情報取得日時', '詳細入力/更新日時', '救護所有無', '所属本部'];

    var tagClnc = ['救急科','呼吸器科','消化器科(胃腸科)','循環器科','小児科','精神科','神経科(神経内科)','外科','整形外科', '形成外科',
        '脳神経外科','心臓血管外科','産婦人科','眼科','耳鼻咽喉科','皮膚科','泌尿器科','放射線科', '麻酔科','歯科','内科'];

    // 適切なアイコンurlを取得する
    var iconUrl;
    var tagColor;
    biNum = (feature.get('saigai') * 8) + (feature.get('kyukyu') * 4) + (feature.get('hibaku') * 2) + (feature.get('dmat') * 1);
    if (feature.get('assist') == "要"){
                iconUrl = '../../img/hospital/hspt_red.png' ;
                tagColor = "red";
               // iconUrl = 'http://h-crisis.niph.go.jp/assistant/wp-content/uploads/sites/4/test/img/r/' + i + '.png'
    } else if(feature.get('assist') == "未" && feature.get('mds') == "未入力") {
                iconUrl = '../../img/hospital/hspt_grey.png';
                tagColor = "grey";
                // iconUrl = 'http://h-crisis.niph.go.jp/assistant/wp-content/uploads/sites/4/test/img/g/' + i + '.png'
    } else {
                iconUrl = '../../img/hospital/hspt_blue.png';
                tagColor = "blue";
                // iconUrl = 'http://h-crisis.niph.go.jp/assistant/wp-content/uploads/sites/4/test/img/b/' + i + '.png'
    }

    DetailHtml = "<div id='landscape'><img id='landIcon' src=" + iconUrl + "></div>";

    // 建築物名表示
    if (arrayV[1].length < 18) {
        DetailHtml = DetailHtml + "<div id='blueback'><a id='title'>" + arrayV[1] + "</a>";
    } else {
        DetailHtml = DetailHtml + "<div id='blueback'><a id='titleTooLong'>" + arrayV[1] + "</a>";
    }
    DetailHtml = DetailHtml + preCells + "style='color:white;'" + interCells + "医療機関" + subCells;
    DetailHtml = DetailHtml + preCells + "style='color:white;'" + interCells + arrayV[5] + subCells + "</div>";
    // HeaderHtml = HeaderHtml + "<div style='border:2px solid burlywood; background-color:#888888; color:white; text-align:center' type=button id=showBtn value=隠す onclick=showDetail()>詳細情報の表示切替</div>";
    for (i = 0; i < tagName.length; i++) {

        // 名称,住所,支援要否,医療派遣ステータス,詳細入力/機能/手術不可,詳細入力/機能/人工透析不可,緊急時入力/入院病棟倒壊・倒壊の恐れ,
        // 緊急時入力/ライフライン・サプライ状況/電気使用不可,緊急時入力/ライフライン・サプライ状況/水使用不可,
        // 緊急時入力/ライフライン・サプライ状況/医療ガス使用不可,緊急時入力/ライフライン・サプライ状況/医薬品衛生資器材使用不可,更新日時
        // if(i == 1 || i == 5 || i == 10 || i == 11 || i == 39 || i == 40 || i == 15 || i == 16 || i == 17 || i == 18 || i == 19 || i == 13 )

        // 名称,住所,支援要否,医療派遣ステータス,緊急時入力/入院病棟倒壊・倒壊の恐れ,緊急時入力/ライフライン・サプライ状況/電気使用不可,
        // 緊急時入力/ライフライン・サプライ状況/水使用不可,緊急時入力/ライフライン・サプライ状況/医療ガス使用不可,
        // 緊急時入力/ライフライン・サプライ状況/医薬品衛生資器材使用不可,緊急時入力/多数患者受診,緊急時入力/職員状況,更新日時

        if (i == 10 && tagColor == "red") {
            DetailHtml = DetailHtml + preCells + "id=RED style='width:50px; margin-right:85px;'>" + "支援要否" + "</a><a" + interCells + arrayV[i] + subCells;
            DetailHtml = DetailHtml + preCells + "id=RED>" + tagName[i + 1] + "</a><a" + interCells + arrayV[i + 1] + subCells;
        } else if (i == 10 && tagColor == "grey"){
            DetailHtml = DetailHtml + preCells + "id=YELO style='width:50px; margin-right:85px;'>" + tagName[i] + "</a><a" + interCells + arrayV[i] + subCells;
            DetailHtml = DetailHtml + preCells + "id=YELO>" + tagName[i + 1] + "</a><a" + interCells + arrayV[i + 1] + subCells;
        } else if (i == 10 && tagColor == "blue"){
            DetailHtml = DetailHtml + preCells + "id=NOCLR>" + tagName[i] + "</a><a" + interCells + arrayV[i] + subCells;
            DetailHtml = DetailHtml + preCells + "id=NOCLR>" + tagName[i + 1] + "</a><a" + interCells + arrayV[i + 1] + subCells;
        }

        if(i == 15 || i == 16 || i == 17 || i == 18 || i == 19 || i == 20 || i == 21 || i == 13) {
            if(arrayV[i] == 0) {
                arrayV[i] = "NO";
                DetailHtml = DetailHtml + preCells + "id=NOCLR>" + tagName[i] + "</a><a" + interCells + arrayV[i] + subCells;
            } else if(arrayV[i] == 1) {
                arrayV[i] = "YES";
                DetailHtml = DetailHtml + preCells + "id=RED>" + tagName[i] + "</a><a" + interCells + arrayV[i] + subCells;
            } else {
                DetailHtml = DetailHtml + preCells + "id=NOCLR>" + tagName[i] + "</a><a" + interCells + arrayV[i] + subCells;
            }
        }
    }

    DetailHtml = DetailHtml + "<hr>";
    DetailHtml = DetailHtml + preCells + "id=NOCLR>診療科情報 </a>";

    if (arrayH.length > 0) {
        var totalvalue = arrayH.reduce(function (x, y) {
            return x + y;
        })
    } else {
        var totalvalue = 0
    }

    if (totalvalue > 0) {
        for (i = 0;i < tagClnc.length; i++) {
            console.log(arrayH[i]);
            if(i % 3 == 0){
                etailHtml = DetailHtml + "<tr>"
            }
            if (arrayH[i] == "0"){
                DetailHtml = DetailHtml + "<td id='clinicNO'>" + tagClnc[i] + "</td>";
            } else if (arrayH[i] == "1") {
                DetailHtml = DetailHtml + "<td id='clinicON'>" + tagClnc[i] + "</td>";
            }
            if(i % 3 == 2){
                DetailHtml = DetailHtml + "</tr>"
            }
        }
    } else {
        DetailHtml = DetailHtml + "<tr><td id='clinicON' width:110px; style='border:1px #ffffff solid;'>情報がありません</td></tr>";
        for (i = 0;i < 6; i++) {
                DetailHtml = DetailHtml + "<tr><td id='clinicON' width:110px; style='border:1px #ffffff solid;'>　</td></tr>";
        }
    }

    DetailHtml = DetailHtml + "<div id='inputIcon' onclick='detailInfoOnOff()'><img id='detailIcon' src='../../img/img/detail.png'></div>"

    // ポップアップで表示する部分の入力
    poppedHtml = "<table class='fadeLayerDI'>";
    for (i = 0; i < tagName.length; i = i + 2) {
        poppedHtml = poppedHtml +  "<tr><th scope='row'>" + tagName[i] + "</th><td>" + arrayV[i] + "</td></tr>";
        if (i + 1 < tagName.length) {
            poppedHtml = poppedHtml +  "<tr><th scope='row' class='even'>" + tagName[i + 1] + "</th><td class='even'>" + arrayV[i + 1] + "</td></tr>"
        }
    }
    poppedHtml = poppedHtml + "</table>";
    document.getElementById('fadeLayerDetailInfo').innerHTML = poppedHtml;

    document.getElementById('info').style.display = 'block';
    document.getElementById('info').innerHTML = DetailHtml;
    document.getElementById('popup').style.display = 'block';
    overlayPopup.setPosition(coordinate);
}

