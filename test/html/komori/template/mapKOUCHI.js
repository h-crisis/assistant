/**
 * Created by komori on 2016/06/14.
 */

var additionalmapOsm = new ol.source.MapQuest({ layer: 'osm' });
var osmQuestLayer = new ol.layer.Tile({
    source: additionalmapOsm,
    opacity: 1
});
var additionalmapSat = new ol.source.MapQuest({ layer: 'sat' });
var satQuestLayer = new ol.layer.Tile({
    source: additionalmapSat,
    opacity: 1
});
var basemap = new ol.source.XYZ({
    url : 'http://cyberjapandata.gsi.go.jp/xyz/std/{z}/{x}/{y}.png',
    attribution  : "<a href='http://www.gsi.go.jp/kikakuchousei/kikakuchousei40182.html' target='_blank'>国土地理院</a>"
});
var baseLayer = new ol.layer.Tile({
    source: basemap,
    opacity: 1
});
var map = new ol.Map({
    layers: [baseLayer, osmQuestLayer, satQuestLayer
    ],
    target: 'map',
    view: new ol.View({
        center: ol.proj.transform([133.511129, 33.5274460, 13], 'EPSG:4326', 'EPSG:3857'),
        zoom: 11,
        minZoom: 7,
        maxZoom: 15
    }),
    controls: ol.control.defaults({
        attributionOptions: ({
            collapsible: false
        })
    }).extend([
        new ol.control.ZoomSlider(),
        new ol.control.ScaleLine()
    ])
});

var goHypoCenter = document.getElementById('goToHypoCenter');
goHypoCenter.addEventListener('click', goHypoButton);
function goHypoButton() {
    map.getView().setCenter(ol.proj.transform([133.511129, 33.5274460], 'EPSG:4326', 'EPSG:3857'))
    map.getView().setZoom(11);
}

var flagSelected = false ;
var lon, lat;
var goFacility = document.getElementById('goToSelect');
goFacility.addEventListener('click', function(evt) {

    if (flagSelected == true) {
        console.log(flagSelected);
        map.getView().setCenter([lon,lat]);
        map.getView().setZoom(14);
    }
});

var mapSwitch   = document.getElementById('map-vis');
osmQuestLayer.setVisible(false);
satQuestLayer.setVisible(false);
mapSwitch.addEventListener('click', mapButton);
var mapcount = 0;
function mapButton() {
    if (mapcount%3==0) {
        osmQuestLayer.setVisible(true);
        satQuestLayer.setVisible(false);
        baseLayer.setVisible(false);
        this.style.backgroundColor = "#86B81B";
        mapcount++;
    } else if (mapcount%3==1) {
        osmQuestLayer.setVisible(false);
        satQuestLayer.setVisible(true);
        baseLayer.setVisible(false);
        this.style.backgroundColor = "blue";
        mapcount++;
    } else {
        osmQuestLayer.setVisible(false);
        satQuestLayer.setVisible(false);
        baseLayer.setVisible(true);
        this.style.backgroundColor = "whitesmoke";
        mapcount++;
    }
};

map.addLayer(hospLayer);

var overlayInfo = new ol.Overlay({
    element: document.getElementById('overlayInfo'),
    positioning: 'top-left'
});

var displayFeatureInfo = function(pixel) {
    var feature = map.forEachFeatureAtPixel(pixel, function(feature) {
    })};
map.on('click', function(evt) {
    var coordinate = evt.coordinate;
    var pixel = map.getPixelFromCoordinate(coordinate);
    var info = document.getElementById('info');
    document.getElementById('info').innerHTML = '';
    var labelB = '';
    var labelC = '';
    var labelD = '';
    var labelHosp = new Array(5)
    var labelHinan = new Array(80);
    var label = new Array(10);
    var labelShltrJPN = new Array(20);
    var labelSelf = '';
    var arrayH14 =
        ['男性', '女性', '高齢者', '身体障害者', '乳幼児', '妊婦', '自立歩行不', 'その他要', '人工透析', '人工呼吸器', '電気',
            '水', 'ガス', '通信', '換気', '暖房', 'トイレ', '汚物', 'ゴミ', '食品', '衛生', '飲料水', '食事', '有無と方法',
            '医師', '看護師', '薬剤師', '事務', '総数', 'インフルエンザ', '呼吸器疾患', '呼吸困難', '発熱', '下痢', '嘔気・嘔吐',
            '発疹', '不眠・不安', '精神科疾患', '病院搬送', '高血圧', '糖尿病', '潰瘍性大腸炎', 'パーキンソン病', '備考']
    var arrayShltr = ['避難所コード','避難所名','都道府県','市区町村','住所']
    var labelHosp = new Array(10);
    var arrayHosp =
        ['都道府県', '二次医療圏', '支援要請', '医療派遣ステータス', '医療機関名', 'チーム数', '最終更新日時', '医師出勤状況'];
    var i = 0;
    var j = 0;
    var k = 0;
    var coordinate4326;

    map.forEachFeatureAtPixel(pixel, function(feature){
        labelB = feature.get('名称');
        labelC = feature.get('住所');
        label[0] = feature.get('P34_003');
        label[1] = feature.get('P34_004');
        labelD = feature.get('避難者数');
        labelHosp[0] = feature.get('prefecture');
        labelSelf = feature.getId();
        labelHinan[0] = feature.get('男');
        labelHinan[1] = feature.get('女');
        labelHinan[2] = feature.get('高齢者');
        labelHinan[3] = feature.get('身体障害者');
        labelHinan[4] = feature.get('乳幼児');
        labelHinan[5] = feature.get('妊婦');
        labelHinan[6] = feature.get('自立歩行不');
        labelHinan[7] = feature.get('その他の要');
        labelHinan[8] = feature.get('人工透析');
        labelHinan[9] = feature.get('人工呼吸器');
        labelHinan[10] = feature.get('電気');
        labelHinan[11] = feature.get('水');
        labelHinan[12] = feature.get('ガス');
        labelHinan[13] = feature.get('通信');
        labelHinan[14] = feature.get('換気');
        labelHinan[15] = feature.get('暖房');
        labelHinan[16] = feature.get('トイレ');
        labelHinan[17] = feature.get('汚物');
        labelHinan[18] = feature.get('ゴミ');
        labelHinan[19] = feature.get('食品');
        labelHinan[20] = feature.get('衛生');
        labelHinan[21] = feature.get('飲料水');
        labelHinan[22] = feature.get('食事');
        labelHinan[23] = feature.get('有無と方法');
        labelHinan[24] = feature.get('医師');
        labelHinan[25] = feature.get('看護師');
        labelHinan[26] = feature.get('薬剤師');
        labelHinan[27] = feature.get('事務');
        labelHinan[28] = feature.get('総数');
        labelHinan[29] = feature.get('インフルエ');
        labelHinan[30] = feature.get('呼吸器疾患');
        labelHinan[31] = feature.get('呼吸困難');
        labelHinan[32] = feature.get('発熱');
        labelHinan[33] = feature.get('下痢');
        labelHinan[34] = feature.get('嘔気・嘔吐');
        labelHinan[35] = feature.get('発疹');
        labelHinan[36] = feature.get('不眠・不安');
        labelHinan[37] = feature.get('精神科疾患');
        labelHinan[38] = feature.get('病院搬送・');
        labelHinan[39] = feature.get('高血圧');
        labelHinan[40] = feature.get('糖尿病');
        labelHinan[41] = feature.get('潰瘍性大腸');
        labelHinan[42] = feature.get('パーキンソ');
        labelHinan[43] = feature.get('備考');

        labelShltrJPN[0] = feature.get('避難所コー');
        labelShltrJPN[1] = feature.get('避難所名');
        labelShltrJPN[2] = feature.get('都道府県');
        labelShltrJPN[3] = feature.get('市区町村');
        labelShltrJPN[4] = feature.get('都道府県コ');
        labelShltrJPN[5] = feature.get('市区町村コ');

        arrayHosp =
            ['都道府県', '医療機関コード', '支援要否', '医療派遣ステータス', '医療機関名', 'チーム数', '最終更新日時', '医師出勤状況',
                '入院病棟の倒壊・倒壊の恐れ', '電気使用不可', '水使用不可', '医療ガス使用不可', '医療品衛生資機材使用不可', '多数患者受信', '職員状況',
                'その他', '情報取得日時', '電話番号', 'メールアドレス', '更新日時'];
        labelHosp[1] = feature.get('code');
        labelHosp[2] = feature.get('assist');
        labelHosp[3] = feature.get('mds');
        labelHosp[4] = feature.get('name');
        labelHosp[5] = feature.get('team');
        labelHosp[6] = feature.get('rd');
        labelHosp[7] = feature.get('atd');
        labelHosp[8] = feature.get('e001');
        labelHosp[9] = feature.get('e002');
        labelHosp[10] = feature.get('e003');
        labelHosp[11] = feature.get('e004');
        labelHosp[12] = feature.get('e005');
        labelHosp[13] = feature.get('e006');
        labelHosp[14] = feature.get('e007');
        labelHosp[15] = feature.get('e008');
        labelHosp[16] = feature.get('e009');
        labelHosp[17] = feature.get('e010');
        labelHosp[18] = feature.get('e011');
        labelHosp[19] = feature.get('e012');

        coordinate4326 = feature.getGeometry().getExtent();
        lon = coordinate4326[0];
        lat = coordinate4326[1];
        ordLon = feature.get('経度');
        ordLat = feature.get('緯度');
    });
    if (labelSelf === "") {
        labelSelf = "noPlace";
    }
    info.innerHTML="";
    if(labelSelf == "noPlace") {
        flagSelected = false;
        document.getElementById( 'info' ).style.display = 'none';
        overlayInfo.setPosition();
        map.addOverlay(overlayInfo);
    } else if (typeof labelHosp[1] !== "undefined") {

        flagSelected = true;
        document.getElementById( 'info' ).style.display = 'block';
        info.innerHTML = "<div style='background-color:#888888; color:white; text-align:center;' type=button id=showBtn value=隠す onclick=showHide()>閉じる</div>";
        info.innerHTML = info.innerHTML +  labelHosp[4];
                while (i < 20) {
            info.innerHTML = info.innerHTML + "<tr><td style=font-size:24px;background-color:#888888;color:white></td><td style=font-size:24px;background-color:white;text-align:right;></td></tr>";
            i++
        }
        for (i = 0; i < 20; i++) {
            info.rows[i].cells[0].innerHTML = arrayHosp[i];
            info.rows[i].cells[1].innerHTML = labelHosp[i];
        }
        overlayInfo.setPosition(coordinate);
        var element = overlayInfo.getElement();
        element.innerHTML =
            "名称    :" + labelHosp[4] + '<br>' +
            //   "住所    :" + labelHosp[1] + '<br>' +
            //   "電話番号        :" + labelHosp[2] + '<br>' +
            "支援要否    :" + labelHosp[2] + '<br>' +
            "医療派遣ステータス    :" + labelHosp[3];
        map.addOverlay(overlayInfo);
    }

});
function showHide(){
    document.getElementById('info').style.display = 'none';
}