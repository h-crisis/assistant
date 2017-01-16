/**
 * Created by komori on 2016/10/22.
 */

var src;
var biNum;

var styleHospR = new Array(16);
for (var i = 0; i < 16 ; i++) {
    src = "http://h-crisis.niph.go.jp/assistant/wp-content/uploads/sites/4/test/img/r/" + i + ".png";
    styleHospR[i] = new ol.style.Style({
        text: new ol.style.Text({
            offsetY: 8,
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
for (var i = 0; i < 16 ; i++) {
    src = "http://h-crisis.niph.go.jp/assistant/wp-content/uploads/sites/4/test/img/b/" + i + ".png";
    styleHospB[i] = new ol.style.Style({
        text: new ol.style.Text({
            offsetY: 8,
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
for (var i = 0; i < 16 ; i++) {
    src = "http://h-crisis.niph.go.jp/assistant/wp-content/uploads/sites/4/test/img/g/" + i + ".png";
    styleHospG[i] = new ol.style.Style({
        text: new ol.style.Text({
            offsetY: 8,
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
        url: 'geojson/hcrisis_medical_status.geojson',
        // url: url,
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
        this.style.backgroundColor = "#eeeeee";
        this.style.color = "#000000";
    } else {
        hospLayer.setVisible(false);
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
        '電気', '水', '医療ガス', '医薬品衛生資器材', '多数患者受診', '職員状況',
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

    // 適切なアイコンurlを取得する
    var iconUrl;
    biNum = (feature.get('saigai') * 8) + (feature.get('kyukyu') * 4) + (feature.get('hibaku') * 2) + (feature.get('dmat') * 1);
    if (feature.get('assist') == "要"){
        for (var i = 0; i < 16 ; i++) {
            if (biNum == i) {
                iconUrl = '../../img/hospital/' + i + '.png';
               // iconUrl = 'http://h-crisis.niph.go.jp/assistant/wp-content/uploads/sites/4/test/img/r/' + i + '.png'
            }
        }
    } else if(feature.get('assist') == "未" && feature.get('mds') == "未入力") {
        for (var i = 0; i < 16 ; i++) {
            if (biNum == i) {
                iconUrl = '../../img/hospital/' + i + '.png';
                // iconUrl = 'http://h-crisis.niph.go.jp/assistant/wp-content/uploads/sites/4/test/img/g/' + i + '.png'
            }
        }
    } else {
        for (var i = 0; i < 16 ; i++) {
            if (biNum == i) {
                iconUrl = '../../img/hospital/' + i + '.png';
                // iconUrl = 'http://h-crisis.niph.go.jp/assistant/wp-content/uploads/sites/4/test/img/b/' + i + '.png'
            }
        }
    }

    DetailHtml = "<div id='landscape'><img id='landIcon' src=" + iconUrl + "></div>";

    // 建築物名表示
    if (arrayV[1].length < 18) {
        DetailHtml = DetailHtml + "<div id='blueback'><a id='title'>" + arrayV[1] + "</a>";
    } else {
        DetailHtml = DetailHtml + "<div id='blueback'><a id='titleTooLong'>" + arrayV[1] + "</a>";
    }
    DetailHtml = DetailHtml + preCells + "style='color:white;'" + interCells + arrayV[5] + subCells + "</div>";
    // HeaderHtml = HeaderHtml + "<div style='border:2px solid burlywood; background-color:#888888; color:white; text-align:center' type=button id=showBtn value=隠す onclick=showDetail()>詳細情報の表示切替</div>";
    for (i = 0; i < tagName.length; i++) {
        if(arrayV[i] == 0) {
            arrayV[i] = "NO"
        } else if(arrayV[i] == 1) {
            arrayV[i] = "YES"
        }

        // 名称,住所,支援要否,医療派遣ステータス,詳細入力/機能/手術不可,詳細入力/機能/人工透析不可,緊急時入力/入院病棟倒壊・倒壊の恐れ,
        // 緊急時入力/ライフライン・サプライ状況/電気使用不可,緊急時入力/ライフライン・サプライ状況/水使用不可,
        // 緊急時入力/ライフライン・サプライ状況/医療ガス使用不可,緊急時入力/ライフライン・サプライ状況/医薬品衛生資器材使用不可,更新日時
        // if(i == 1 || i == 5 || i == 10 || i == 11 || i == 39 || i == 40 || i == 15 || i == 16 || i == 17 || i == 18 || i == 19 || i == 13 )

        // 名称,住所,支援要否,医療派遣ステータス,緊急時入力/入院病棟倒壊・倒壊の恐れ,緊急時入力/ライフライン・サプライ状況/電気使用不可,
        // 緊急時入力/ライフライン・サプライ状況/水使用不可,緊急時入力/ライフライン・サプライ状況/医療ガス使用不可,
        // 緊急時入力/ライフライン・サプライ状況/医薬品衛生資器材使用不可,緊急時入力/多数患者受診,緊急時入力/職員状況,更新日時
        if(i == 10 || i == 11 || i == 15 || i == 16 || i == 17 || i == 18 || i == 19 || i == 20 || i == 21 || i == 13)
        DetailHtml = DetailHtml + preCells + "id=NOCLR>" + tagName[i] + "</a><a" + interCells + arrayV[i] + subCells;
    }
    document.getElementById('info').style.display = 'block';
    document.getElementById('info').innerHTML = DetailHtml;
    document.getElementById('popup').style.display = 'block';
    overlayPopup.setPosition(coordinate);
}