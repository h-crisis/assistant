/**
 * Created by komori on 2016/07/12.
 */

var getCSV = d3.dsv(',', 'text/csv; charset=shift_jis');
var tdfkNum;
var lat;
var lon;
var hospLatLon;

function hoIndexButton() {
    var i = 0;
    var tdfk =
        ['男性', '女性', '高齢者', '身体障害者', '乳幼児', '妊婦', '自立歩行不', 'その他要', '人工透析', '人工呼吸器', '電気',
            '水', 'ガス', '通信', '換気', '暖房', 'トイレ', '汚物', 'ゴミ', '食品', '衛生', '飲料水', '食事', '有無と方法',
            '医師', '看護師', '薬剤師', '事務', '総数', 'インフルエンザ', '呼吸器疾患', '呼吸困難', '発熱', '下痢', '嘔気・嘔吐',
            '発疹', '不眠・不安', '精神科疾患', '病院搬送', '高血圧', '糖尿病', '潰瘍性大腸炎', 'パーキンソン病', '備考']
    if (document.getElementById( 'tdfkinfo' ).style.display = 'block') {
        while (i < 47) {
            tdfkinfo.innerHTML = tdfkinfo.innerHTML + "<tr><td style=font-size:24px;background-color:white;text-align:right;></td></tr>";
            i++
        }
        for (i = 0; i < 47; i++) {
            tdfkinfo.rows[i].cells[0].innerHTML = "<div style='background-color:#888888; color:white; text-align:center' type=button id=tdfkBtn value=" + (i + 1) + " onclick=choiceTdfk(this)>" + tdfk[i] + "</div>";
        }
        document.getElementById( 'tdfkinfo' ).style.display = 'block';
        return document.getElementById('tdfkBtn').value
    } else {
        document.getElementById( 'tdfkinfo' ).style.display = 'none';
    }
};

function choiceTdfk(obj) {
    tdfkNum = obj.getAttribute('value');

    getCSV('geojson/EMIS.csv', function (data) {
        // dataを処理する
        hospinfo.innerHTML = "";
        var code = "";
        var aa = 0;
        var bb = 0;
        var text = "";
        for(var i=0; i<data.length; i++) {
            if (tdfkNum < 10) {
                code = data[i].コード.substr(0, 3);
                if (code > 99) {
                    code = code.substr(0, 1);
                    if (tdfkNum == code) {
                        hospinfo.innerHTML = hospinfo.innerHTML + "<tr><td style='font-size:24px;color:white;background-color:#888888;text-align:center' type=button id=tdkBtn value=" + i + " onclick=choiceHosp(this)>" + data[i].医療機関名 + "</td></tr>";
                    }
                }
            } else {
                code = data[i].コード.substr(0, 3);
                if (code < 99) {
                    code = data[i].コード.substr(0, 1) + data[i].コード.substr(2, 1);
                    if (tdfkNum == code) {
                        hospinfo.innerHTML = hospinfo.innerHTML + "<tr><td style='font-size:24px;color:white;background-color:#888888;text-align:center' type=button id=tdkBtn value=" + i + " onclick=choiceHosp(this)>" + data[i].医療機関名 + "</td></tr>";
                    }
                }
            }
        }
        d3.select("#result").html(text);
        document.getElementById( 'hospinfo' ).style.display = 'block';
    });
}

