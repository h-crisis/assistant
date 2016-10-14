/**
 * Created by komori on 2016/07/01.
 */

var osm_source = new ol.source.OSM({
    url: 'http://{a-c}.tile.openstreetmap.org/{z}/{x}/{y}.png'
});

var baseLayer = new ol.layer.Tile({
    source: osm_source,
    opacity: 1
});
//  test
/*
var basemap001 = new ol.source.TileWMS({
    url: 'http://192.168.1.100:8080/geoserver/zenrin/wms',
    params: {LAYERS: 'zenrin:001', VERSION: '1.3.0'}
});
var basemap101 = new ol.source.TileWMS({
    url: 'http://192.168.1.100:8080/geoserver/zenrin/wms',
    params: {LAYERS: 'zenrin:101', VERSION: '1.3.0'}
});
var basemap120 = new ol.source.TileWMS({
    url: 'http://192.168.1.100:8080/geoserver/zenrin/wms',
    params: {LAYERS: 'zenrin:120', VERSION: '1.3.0'}
});
var baseLayer001 = new ol.layer.Tile({
    source: basemap001,
    opacity: 1
});
var baseLayer101 = new ol.layer.Tile({
    source: basemap101,
    opacity: 1
});
var baseLayer120 = new ol.layer.Tile({
    source: basemap120,
    opacity: 1
});
*/
var view = new ol.View({
    center: hypoPoint,
    zoom: 9,
    minZoom: 2,
    maxZoom: 17
});

var map = new ol.Map({
    layers: [baseLayer],
    target: 'map',
    view: view,
    controls: ol.control.defaults({
        attributionOptions: ({
            collapsible: false
        })
    }).extend([
        new ol.control.ScaleLine()
    ])
});
/*
var scrnHosp   = document.getElementById('scrn-hosp');
scrnHosp.addEventListener('click', visHoButton);

var visPass   = document.getElementById('visible-pass');
visPass.addEventListener('click', visPassButton);

var visHosp   = document.getElementById('scrn-hosp');
visHosp.addEventListener('click', hospButton);
*/
var goHypoCenter = document.getElementById('goToHypoCenter');
goHypoCenter.addEventListener('click', goHypoButton);
function goHypoButton() {
    var pan = ol.animation.pan({
        duration: 2000,
        source: (view.getCenter())
    });
    map.beforeRender(pan);
    map.getView().setCenter(hypoPoint);
    map.getView().setZoom(11);
}

var goHere = document.getElementById('goToHere');
goHere.addEventListener('click', goToHere);
function goToHere() {
    var nowLatLon = document.getElementById('latlonCurr').value;
    if (nowLatLon === ""){
        alert("現在地が取得されていません。")
    } else {
        var nowLatLon = nowLatLon.split(',');;
        var latNow = nowLatLon[1].substr(0, 8);
        var lonNow = nowLatLon[0].substr(0, 8);
        nowLatLon = ol.proj.transform([lonNow, Math.abs(latNow)], 'EPSG:4326', 'EPSG:3857')
        var start = +new Date();
        var pan = ol.animation.pan({
            duration: 2000,
            source: (view.getCenter()),
            start: start
        });
        var bounce = ol.animation.bounce({
            duration: 2000,
            resolution: 4 * view.getResolution(),
            start: start
        });
        map.beforeRender(pan, bounce);
        map.getView().setCenter(nowLatLon);
        map.getView().setZoom(15);
    }
}

var flagSelected = false ;
var lon, lat;

/*
var goFacility = document.getElementById('goToSelect');
goFacility.addEventListener('click', function(evt) {

    if (flagSelected == true) {
        console.log(flagSelected);
        var pan = ol.animation.pan({
            duration: 1000,
            source: (view.getCenter())
        });
        map.beforeRender(pan);
        map.getView().setCenter([lon,Math.abs(lat)]);
    }
});

var mapSwitch   = document.getElementById('map-vis');
osmQuestLayer.setVisible(false);
satQuestLayer.setVisible(false);
hybQuestLayer.setVisible(false);

var mapcount = 0;
function mapButton() {
    if (mapcount%3==0) {
        osmQuestLayer.setVisible(true);
        satQuestLayer.setVisible(false);
        baseLayer.setVisible(false);
        this.style.backgroundColor = "#86B81B";
        // var aaa = map.getProperties();
        // console.log(aaa);
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
*/

map.addLayer(hinanLayer1);
map.addLayer(hospIcon);
map.addLayer(hallLayer);
map.addLayer(ImpassableLayer);
hinanLayer1.setVisible(false);
hospIcon.setVisible(false);
hallLayer.setVisible(false);
ImpassableLayer.setVisible(false);

// var hospIndex   = document.getElementById('hospital-index');
// hospIndex.addEventListener('click', hoIndexButton);

var visHosp   = document.getElementById('scrn-hosp');
visHosp.addEventListener('click', visHoButton);

var visPass   = document.getElementById('pass-vis');
visPass.addEventListener('click', visPassButton);

var hinanOnOff1   = document.getElementById('hinan1-vis');
hinanOnOff1.addEventListener('click', hinanButton1);
var chs = 0;
var day = 0;
function hinanButton1() {
    if (chs%2 == 0) {
        day = 1;
        hinanLayer1.setVisible(true);
        this.style.backgroundColor = "green";
        chs++;
    } else {
        day = 0;
        hinanLayer1.setVisible(false);
        this.style.backgroundColor = "whitesmoke";
        chs++;
    }
};

var hallOnOff   = document.getElementById('hall-vis');
hallOnOff.addEventListener('click', hallButton);

var hospOnOff   = document.getElementById('hospital-vis');
hospOnOff.addEventListener('click', hospButton);

var overlayInfo = new ol.Overlay({
    element: document.getElementById('overlayInfo'),
    positioning: 'top-left'
});

var overlayTargetTop = new ol.Overlay({
    offset : [0,-35],
    element: document.getElementById('overlayTargetTop'),
    positioning: 'top-center'
});

var overlayTargetRight = new ol.Overlay({
    offset : [20,-35],
    element: document.getElementById('overlayTargetRight'),
    positioning: 'top-center'
});

var overlayTargetBottom = new ol.Overlay({
    offset : [0,5],
    element: document.getElementById('overlayTargetBottom'),
    positioning: 'top-center'
});

var overlayTargetLeft = new ol.Overlay({
    offset : [-20,-35],
    element: document.getElementById('overlayTargetLeft'),
    positioning: 'top-center'
});

map.getView().on('change:resolution', function() {
    var extent = map.getView().calculateExtent(map.getSize());
    var bottomLeft = ol.proj.transform(ol.extent.getBottomLeft(extent),
        'EPSG:3857', 'EPSG:4326');
    var topRight = ol.proj.transform(ol.extent.getTopRight(extent),
        'EPSG:3857', 'EPSG:4326');
    var lotLength = Math.abs(topRight[0] - bottomLeft[0]);
    if (lotLength < 0.25 && document.getElementById('hospital-vis').style.backgroundColor == "blue"){
        document.getElementById('scrn-hosp').style.display = 'block'
        //document.getElementById('overlayTargetTop').style.display = 'block';
        //document.getElementById('overlayTargetRight').style.display = 'block';
        //document.getElementById('overlayTargetBottom').style.display = 'block';
        //document.getElementById('overlayTargetLeft').style.display = 'block';
    } else {
        document.getElementById('scrn-hosp').style.display = 'none'
        //document.getElementById('overlayTargetTop').style.display = 'none';
        //document.getElementById('overlayTargetRight').style.display = 'none';
        //document.getElementById('overlayTargetBottom').style.display = 'none';
        //document.getElementById('overlayTargetLeft').style.display = 'none';
    }
    if(document.getElementById( 'vishospinfo' ).style.display == 'none'){}
    else {
        document.getElementById('vishospinfo').innerHTML = "";
        document.getElementById('vishospinfo').style.display = 'none';
        // visHoButton()
    }
});

map.on('moveend', function() {
    if(document.getElementById( 'vishospinfo' ).style.display == 'none'){}
    else {
        document.getElementById('vishospinfo').innerHTML = "";
        document.getElementById('vishospinfo').style.display = 'none';
        visHoButton()
    }
});

var displayFeatureInfo = function(pixel) {
    var feature = map.forEachFeatureAtPixel(pixel, function(feature) {
    })};
map.on('click', function(evt) {
    displayFeatureInfo(evt.pixel);
    var coordinate = evt.coordinate;
    var pixel = map.getPixelFromCoordinate(coordinate);
    var info = document.getElementById('info');
    var infoH = document.getElementById('infoHeader');
    info.innerHTML = '';
    infoH.innerHTML = '';
    var labelB = '';
    var labelC = '';
    var labelD = '';
    var labelHinan = new Array(80);
    var labelHall = new Array(10);
    var labelShltrJPN = new Array(20);
    var labelSelf = '';
    var arrayH7 =
        ['総数','身体障害者','妊婦', '電気', '水', 'ガス', 'トイレ', '汚物', '飲料水', '食事',
            '医療救護の有無と方法', '受診者総数', '呼吸器疾患', '発熱', '下痢', '不眠・不安'];
    var arrayShltr = ['避難所コード', '避難所名', '都道府県', '市区町村', '住所'];
    var arrayH14 =
        ['男性', '女性', '高齢者', '身体障害者', '乳幼児', '妊婦', '自立歩行不可', 'その他要', '人工透析', '人工呼吸器', '電気',
            '水', 'ガス', '通信', '換気', '暖房', 'トイレ', '汚物', 'ゴミ', '食品', '衛生', '飲料水', '食事', '有無と方法',
            '医師', '看護師', '薬剤師', '事務', '総数', 'インフルエンザ', '呼吸器疾患', '呼吸困難', '発熱', '下痢', '嘔気・嘔吐',
            '発疹', '不眠・不安', '精神科疾患', '病院搬送', '高血圧', '糖尿病', '潰瘍性大腸炎', 'パーキンソン病', '備考'];
    var arrayShltr = ['避難所コード', '避難所名', '都道府県', '市区町村', '住所'];
    var labelHosp = new Array(110);
    var arrayHosp =
        ['都道府県', '二次医療圏', '支援要請', '医療派遣ステータス', '医療機関名', 'チーム数', '最終更新日時', '医師出勤状況'];
    var arrayRoad = ['名前', '方向', '規制開始地点', '規制終了地点', '規制延長', '規制内容', '規制原因', '規制開始日時', '規制終了予定日時', '迂回路', '備考'];
    var labelRoad = new Array(11);

    var hospIcon;
    var hospIconNum;
    var i = 0;
    var coordinate4326;

    map.forEachFeatureAtPixel(pixel, function (feature) {
        labelB = feature.get('名称');
        labelC = feature.get('住所');
        labelHall[0] = feature.get('seirei');
        labelHall[1] = feature.get('sikuchoson');
        labelHall[2] = feature.get('ken');
        labelHall[3] = feature.get('p_num');
        labelHall[4] = feature.get('h_num');
        labelHall[5] = Math.round(feature.get('zenkai'));
        labelHall[6] = Math.round(feature.get('zenhankai'));
        labelHall[7] = Math.round(feature.get('dead'));
        labelHall[8] = Math.round(feature.get('injured'));
        labelHall[9] = Math.round(feature.get('severe'));
        labelHall[10] = Math.round(feature.get('evacuee'));
        labelD = feature.get('避難者数');
        labelHosp[0] = feature.get('prefecture');
        labelSelf = feature.getId();
        labelHinan[0] = Math.round(feature.get('S01'));
        labelHinan[1] = Math.round(feature.get('S02'));
        labelHinan[2] = Math.round(feature.get('S03'));
        labelHinan[3] = Math.round(feature.get('S04'));
        labelHinan[4] = Math.round(feature.get('S05'));
        labelHinan[5] = Math.round(feature.get('S06'));
        labelHinan[6] = Math.round(feature.get('S07'));
        labelHinan[7] = Math.round(feature.get('S08'));
        labelHinan[8] = Math.round(feature.get('S09'));
        labelHinan[9] = Math.round(feature.get('S10'));
        labelHinan[10] = Math.round(feature.get('S11'));
        labelHinan[11] = feature.get('S12');
        labelHinan[12] = feature.get('S13');
        labelHinan[13] = feature.get('S14');
        labelHinan[14] = feature.get('S15');
        labelHinan[15] = feature.get('S16');
        labelHinan[16] = feature.get('S17');
        labelHinan[17] = feature.get('S18');
        labelHinan[18] = feature.get('S19');
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

        labelShltrJPN[0] = feature.get('ID');
        labelShltrJPN[1] = feature.get('name');
        labelShltrJPN[2] = feature.get('prefecture');
        labelShltrJPN[3] = feature.get('shikuchoson');
        labelShltrJPN[4] = feature.get('address');
        labelShltrJPN[5] = Math.round(feature.get('S05'));
        labelShltrJPN[6] = Math.round(feature.get('S07'));
        labelShltrJPN[7] = feature.get('S12');
        labelShltrJPN[8] = feature.get('S13');
        labelShltrJPN[9] = feature.get('S14');
        labelShltrJPN[10] = feature.get('S15');
        labelShltrJPN[11] = feature.get('S19');
        labelShltrJPN[12] = feature.get('S24');
        labelShltrJPN[13] = feature.get('S25');
        labelShltrJPN[14] = feature.get('S26');
        labelShltrJPN[15] = Math.round(feature.get('S31'));
        labelShltrJPN[16] = Math.round(feature.get('S33'));
        labelShltrJPN[17] = Math.round(feature.get('S35'));
        labelShltrJPN[18] = Math.round(feature.get('S36'));
        labelShltrJPN[19] = Math.round(feature.get('S39'));


        arrayHosp =
            ['都道府県', '支援要否', '医療派遣<br>ステータス', 'チーム数', '最終更新日時', '医師出勤状況',
                '入院病棟倒壊/<br>倒壊の恐れ', '電気使用不可', '水使用不可', '医療ガス<br>使用不可', '医療品衛生<br>資機材使用不可', '多数患者受信', '職員状況',
                'その他', '情報取得日時', '電話番号', 'メールアドレス', '更新日時'];
        labelHosp[1] = feature.get('assist');
        labelHosp[2] = feature.get('mds');
        labelHosp[3] = feature.get('team');
        labelHosp[4] = feature.get('rd');
        labelHosp[5] = feature.get('atd');
        labelHosp[6] = feature.get('e001');
        labelHosp[7] = feature.get('e002');
        labelHosp[8] = feature.get('e003');
        labelHosp[9] = feature.get('e004');
        labelHosp[10] = feature.get('e005');
        labelHosp[11] = feature.get('e006');
        labelHosp[12] = feature.get('e007');
        labelHosp[13] = feature.get('e008');
        labelHosp[14] = feature.get('e009');
        labelHosp[15] = feature.get('e010');
        labelHosp[16] = feature.get('e011');
        labelHosp[17] = feature.get('e012');
        labelHosp[18] = feature.get('name');

        labelHosp[21] = feature.get('code');

        labelHosp[101] = feature.get('saigai');
        labelHosp[102] = feature.get('kyukyu');
        labelHosp[103] = feature.get('hibaku');
        labelHosp[104] = feature.get('dmat');
        hospIconNum = (labelHosp[101] * 1) + (labelHosp[102] * 2) + (labelHosp[103] * 4) + (labelHosp[104] * 8);

        labelRoad[0] = feature.get('name');
	labelRoad[1] = feature.get('方向');
        labelRoad[2] = feature.get('規制開始地点');
	labelRoad[3] = feature.get('規制終了地点');
	labelRoad[4] = feature.get('規制延長');
	labelRoad[5] = feature.get('status');
	labelRoad[6] = feature.get('cause');
	labelRoad[7] = feature.get('規制開始日時');
	labelRoad[8] = feature.get('規制終了予定日時');
	labelRoad[9] = feature.get('迂回路');
	labelRoad[10] = feature.get('備考');

        coordinate4326 = feature.getGeometry().getExtent();
        coordinate4326 = ol.proj.transform([coordinate4326[0],Math.abs(coordinate4326[1])], 'EPSG:3857', 'EPSG:4326');
        lon = coordinate4326[0];
        lat = coordinate4326[1];
    });
    if (labelSelf === "") {
        labelSelf = "noPlace";
    }
    info.innerHTML = "";
    infoH.innerHTML = "";
    if (labelSelf == "noPlace") {
        flagSelected = false;
        info.style.display = 'none';
        infoH.style.display = 'none';
        overlayInfo.setPosition();
        map.addOverlay(overlayInfo);
    } else if(typeof labelB === "undefined" && typeof labelHosp[0] === "undefined") {
        flagSelected = true;
        info.style.display = 'none';
        infoH.style.display = 'none';
        info.innerHTML = "<div style='border:2px solid burlywood; background-color:#888888; color:white; text-align:center' type=button id=showBtn value=隠す onclick=showHide()>閉じる</div>";
        info.innerHTML = info.innerHTML +  labelHall[0];
        while (i < 2) {
            info.innerHTML = info.innerHTML + "<tr><td style=font-size:24px;background-color:#888888;color:white>a</><td style=font-size:24px;background-color:white;>b</td></tr>";
            i++
        }
        info.innerHTML = info.innerHTML + "<a href=mqtt/shelter-emergency.html id=niphLonLat target=_blank>NIPH</a>" + "<br>";
        var niphAddress=document.getElementById('niphLonLat');
        niphAddress.href='http://niph.go.jp';
        for (i = 0; i < 2; i++){
            info.rows[0].cells[0].innerHTML = "名称";
            info.rows[1].cells[0].innerHTML = "住所";
            info.rows[i].cells[1].innerHTML = labelHosp[18];
        }
        overlayInfo.setPosition(coordinate);
        var element = overlayInfo.getElement();
        if (labelHall[0] != null) {
            element.innerHTML =
                labelHall[2] + labelHall[0] + labelHall[1] + '<br>' +
                "人口    :" + labelHall[3] + "人" + '<br>' +
                "世帯数    :" + labelHall[4] + "世帯" + '<br>' +
                "全壊棟数    :" + labelHall[5] + "棟" + '<br>' +
                "半壊棟数    :" + labelHall[6] + "棟" + '<br>' +
                "死者数    :" + labelHall[7] + "人" + '<br>' +
                "負傷者数    :" + labelHall[8] + "人" + '<br>' +
                "重症者数    :" + labelHall[9] + "人" + '<br>' +
                "避難者数    :" + labelHall[10] + "人";
        } else if(labelRoad[0] != null) {
	    element.innerHTML =
		'<font color="red">' + 
		labelRoad[0] + '</font><br>' +
		"規制内容：" + labelRoad[5] + '<br>' +
		"規制原因：" + labelRoad[6];
	} else {
            element.innerHTML =
                labelHall[2] + labelHall[1] + '<br>' +
                "人口    :" + labelHall[3] + "人" + '<br>' +
                "世帯数    :" + labelHall[4] + "世帯" + '<br>' +
                "全壊棟数    :" + labelHall[5] + "棟" + '<br>' +
                "半壊棟数    :" + labelHall[6] + "棟" + '<br>' +
                "死者数    :" + labelHall[7] + "人" + '<br>' +
                "負傷者数    :" + labelHall[8] + "人" + '<br>' +
                "重症者数    :" + labelHall[9] + "人" + '<br>' +
                "避難者数    :" + labelHall[10] + "人";
        }
        map.addOverlay(overlayInfo);
    } else if (typeof labelHosp[1] !== "undefined") {


        flagSelected = true;
        info.style.display = 'block';
        infoH.style.display = 'block';

        while (i < 18) {
            info.innerHTML = info.innerHTML + "<tr><td style='font-size:20px;background-color:#888888;color:white;width:150px;'></td><td style='font-size:20px;background-color:white;text-align:right;width:220px;'></td></tr>";
            i++
        }
        for (i = 0; i < 18; i++) {
            info.rows[i].cells[0].innerHTML = arrayHosp[i];
            info.rows[i].cells[1].innerHTML = labelHosp[i];
        }

        if (labelHosp[1] == "要") {
            hospIcon = "http://h-crisis.niph.go.jp/assistant/wp-content/uploads/sites/4/test/img/r/" + hospIconNum + ".png";
        } else if (labelHosp[1] == "未" && labelHosp[2] == "未入力") {
            hospIcon = "http://h-crisis.niph.go.jp/assistant/wp-content/uploads/sites/4/test/img/g/" + hospIconNum + ".png";
        } else {
            hospIcon = "http://h-crisis.niph.go.jp/assistant/wp-content/uploads/sites/4/test/img/b/" + hospIconNum + ".png";
        }

        infoH.innerHTML = "<tr><td style='font-size:20px;background-color:#888888;color:white; text-align:center; width: 380px' type=button id=showBtn value=隠す onclick='showHide()'>閉じる</td></tr>";
        infoH.innerHTML = infoH.innerHTML + "<tr><td style='font-size:20px;background-color:whitesmoke; color:black; text-align:left; table-layout:fixed; width: 380px'><img src=" + hospIcon + "/ width=20 height=20 >" + labelHosp[18] + "</td></tr>";
        if (labelHosp[1] != "未") {
            infoH.innerHTML = infoH.innerHTML + "<tr><td style='font-size:20px; border:2px solid burlywood; background-color:#888888; text-align:center; table-layout:fixed; width: 380px' type=button ><a href=mqtt/shelter-emergency.html style='display:block; width:100%; color:white; text-decoration:none' id=hospLonLatD target=_blank>詳細情報を閲覧</a></td></tr>";
            var hospAddress = document.getElementById('hospLonLatD');
            hospAddress.href = '../../html/hospital-detail.html' + '?' + 'ID=' + labelHosp[21] + ',Name=' + labelHosp[18] + ',x=' + lon + ',y=' + lat;
        }
        // infoH.innerHTML = infoH.innerHTML + '<img src=' + hospIcon + '/>';

        info.innerHTML = info.innerHTML + '<a href="https://www.wds.emis.go.jp" target="_blank">広域災害救急医療情報システム(EMIS)へ移動</a>';

        overlayInfo.setPosition(coordinate);
        var element = overlayInfo.getElement();
        element.innerHTML =
            "名称    :" + labelHosp[18] + '<br>' +
            //   "住所    :" + labelHosp[1] + '<br>' +
            //   "電話番号        :" + labelHosp[2] + '<br>' +
            "支援要否    :" + labelHosp[1] + '<br>' +
            "医療派遣ステータス    :" + labelHosp[2] + '<br>' +
            '<img src=' + hospIcon + '/>';
        map.addOverlay(overlayInfo);
    }else if (day == 1 || day == 4) {

        flagSelected = true;
        info.style.display = 'block';
        infoH.style.display = 'block';

        document.getElementById( 'info' ).style.display = 'block';
        infoH.innerHTML = "<tr><td style='font-size:20px;background-color:#888888;color:white; text-align:center; width: 380px' type=button id=showBtn value=隠す onclick='showHide()'>閉じる</td></tr>";
        infoH.innerHTML = infoH.innerHTML + "<tr><td style='font-size:20px;background-color:whitesmoke; color:black; text-align:left; table-layout:fixed; width: 380px'>" + labelShltrJPN[1] + "</td></tr>";
        // infoH.innerHTML = infoH.innerHTML + "<tr><td style='font-size:20px;background-color:whitesmoke; color:black; text-align:left; table-layout:fixed; width: 380px'><img src=" + hospIcon + "/ width=20 height=20 >" + labelHosp[18] + "</td></tr>";
        info.innerHTML = info.innerHTML +  "<div style='border:2px solid burlywood; background-color:#888888; text-align:center' type=button ><a href=../../html/shelter-emergency.html style='display:block; width:100%; color:white; text-decoration:none' id=niphLonLatE target=_blank>緊急時情報入力</a></div>";
        var niphAddressE=document.getElementById('niphLonLatE');
        niphAddressE.href='../../html/shelter-emergency.html' + '?' + 'event=20161014_01,' + 'ID=' + labelShltrJPN[0] + ',Name=' + labelShltrJPN[1] + ',x=' + lon + ',y=' + lat;
        info.innerHTML = info.innerHTML +  "<div style='border:2px solid burlywood; background-color:#888888; text-align:center' type=button ><a href=../../html/shelter-emergency.html style='display:block; width:100%; color:white; text-decoration:none' id=niphLonLatH target=_blank>避難所情報入力</a></div>";
        var niphAddressH=document.getElementById('niphLonLatH');
        niphAddressH.href='../../html/shelter-hmethod.html' + '?' + 'event=20161014_01,' + 'ID=' + labelShltrJPN[0] + ',Name=' + labelShltrJPN[1] + ',Address=' + labelShltrJPN[4] + ',x=' + lon + ',y=' + lat;
        info.innerHTML = info.innerHTML +  "<div style='border:2px solid burlywood; background-color:#888888; text-align:center' type=button ><a href=m../../html/shelter-emergency.html style='display:block; width:100%; color:white; text-decoration:none' id=niphLonLatI target=_blank>避難者情報入力</a></div>";
        var niphAddressI=document.getElementById('niphLonLatI');
        niphAddressI.href='../../html/shelter-evacuee.html' + '?' + 'event=20161014_01,' + 'ID=' + labelShltrJPN[0] + ',Name=' + labelShltrJPN[1] + ',Address=' + labelShltrJPN[4] + ',x=' + lon + ',y=' + lat;
        while (i < 16) {
            info.innerHTML = info.innerHTML + "<tr><td style=font-size:20px;background-color:#888888;color:white></td><td style=font-size:20px;background-color:white;text-align:right;></td></tr>";
            i++
        }
        info.rows[0].cells[0].innerHTML = "総数";
        info.rows[0].cells[1].innerHTML = labelHinan[0] + "人";
        for (i = 1; i < 16; i++){
            info.rows[i].cells[0].innerHTML = arrayH7[i];
            if (labelShltrJPN[i+4] === null){
                info.rows[i].cells[1].innerHTML = "情報無し";
            } else if(labelShltrJPN[i+4] >= 0) {
                info.rows[i].cells[1].innerHTML = labelShltrJPN[i+4] + "人";
            } else {
                info.rows[i].cells[1].innerHTML = labelShltrJPN[i+4];
                if (info.rows[i].cells[1].innerHTML === "undefined"){
                    info.rows[i].cells[1].innerHTML = "情報無し";
                }
            }
        }
        overlayInfo.setPosition(coordinate);
        var element = overlayInfo.getElement();
        element.innerHTML =
            "名称    :" + labelShltrJPN[1] + '<br>' +
            "住所    :" + labelShltrJPN[4] + '<br>' +
            "避難者数 :" + labelHinan[0] + "人";
        map.addOverlay(overlayInfo);
    } else if (day == 14) {
        flagSelected = true;
        var urlInputScreen = 'mqtt/shelter-emergency_2.html' + '?' + 'ID=' + labelC + ',Name=' + labelB + ',x=' + lon + ',y=' + lat;
        document.getElementById( 'info' ).style.display = 'block';
        info.innerHTML = "<div style='background-color:#888888; color:white; text-align:center;' type=button id=showBtn value=隠す onclick=showHide()>閉じる</div>";
        info.innerHTML = info.innerHTML +  labelB;
        info.innerHTML = info.innerHTML +  "<div style='background-color:#888888; text-align:center' type=button ><a href=mqtt/shelter-emergency.html style='display:block; width:100%; color:white; text-decoration:none' id=niphLonLat target=_blank>緊急時入力画面を開く</a></div>";
        var niphAddress=document.getElementById('niphLonLat');
        niphAddress.href='mqtt/shelter-emergency_2.html' + '?' + 'ID=' + labelC + ',Name=' + labelB + ',x=' + lon + ',y=' + lat;
        while (i < 44) {
            info.innerHTML = info.innerHTML + "<tr><td style=font-size:24px;background-color:#888888;color:white></td><td style=font-size:24px;background-color:white;text-align:right;></td></tr>";
            i++
        }
        if (labelHinan[43] === ""){
            info.innerHTML = info.innerHTML + "備考:情報無し" + "<br>";
        }
        else {
            info.innerHTML = info.innerHTML + "備考:" + labelHinan[43] + "<br>";
        }
        info.rows[0].cells[0].innerHTML = "総数";
        info.rows[0].cells[1].innerHTML = labelD + "人";
        for (i = 1; i < 44; i++){
            info.rows[i].cells[0].innerHTML = arrayH14[i-1];
            if (labelHinan[i] === null){
                info.rows[i].cells[1].innerHTML = "情報無し";
            } else if(labelHinan[i] >= 0) {
                info.rows[i].cells[1].innerHTML = labelHinan[i] + "人";
            } else  {
                info.rows[i].cells[1].innerHTML = labelHinan[i];
                if (info.rows[i].cells[1].innerHTML === "undefined"){
                    info.rows[i].cells[1].innerHTML = "情報無し";
                }
            }
        }
        overlayInfo.setPosition(coordinate);
        var element = overlayInfo.getElement();
        element.innerHTML =
            "名称    :" + labelB + '<br>' +
            "住所    :" + labelC + '<br>' +
            "避難者数 :" + labelD + "人";
        map.addOverlay(overlayInfo);

    }

});

function showHide(){
    document.getElementById('info').style.display = 'none';
    document.getElementById('infoHeader').style.display = 'none';
}

function choiceHosp(obj) {
    hosplonlat = obj.getAttribute('value');

    latlon = hosplonlat.split(",");

    document.getElementById('latlonHosp').value = hospLatLon;

    var hospPlace = ol.proj.transform([latlon[1], Math.abs(latlon[0])], 'EPSG:4326', 'EPSG:3857');

    var pan = ol.animation.pan({
        duration: 2000,
        source: (view.getCenter()),
    });
    map.beforeRender(pan);
    map.getView().setCenter(hospPlace);
    map.getView().setZoom(15);

    overlayTargetTop.setPosition(hospPlace);
    overlayTargetRight.setPosition(hospPlace);
    overlayTargetBottom.setPosition(hospPlace);
    overlayTargetLeft.setPosition(hospPlace);
    map.addOverlay(overlayTargetTop);
    map.addOverlay(overlayTargetRight);
    map.addOverlay(overlayTargetBottom);
    map.addOverlay(overlayTargetLeft);

    document.getElementById('tdfkinfo').style.display = 'none';
    document.getElementById('hospinfo').style.display = 'none';
    document.getElementById('vishospinfo').style.display = 'none';
};
