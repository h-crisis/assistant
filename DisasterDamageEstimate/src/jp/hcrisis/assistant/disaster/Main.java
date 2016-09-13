package jp.hcrisis.assistant.disaster;

import java.io.File;

/**
 * Created by manabu on 2016/09/13.
 */
public class Main {
    public static void main(String args[]) {
        File dirSIP4 = new File("/Users/manabu/OneDrive - CityRiver.NET/GIS/GISデータ/SIP4_南海トラフ/SIP4_南海トラフ_日立提供");
        File outFileSIP4 = new File("/Users/manabu/OneDrive - CityRiver.NET/GIS/GISデータ/SIP4_南海トラフ/out.csv");

        File inFileJSHIS = new File("/Users/manabu/OneDrive - CityRiver.NET/GIS/GISデータ/JSHIS_F007701_生駒断層/MAP/S-V2-F007701-MAP-CASE1.csv");
        File outFileJSHIS = new File("/Users/manabu/OneDrive - CityRiver.NET/GIS/GISデータ/JSHIS_F007701_生駒断層/out.csv");

        //CreateMesh5Si.createMesh5SiFromSIP4(dirSIP4, outFileSIP4); // SIP4日立から受け取る震度分布データから5次メッシュの震度分布CSVを作成するメソッド
        CreateMesh5Si.createMesh5SiFromJSHIS(inFileJSHIS, outFileJSHIS); // JSHISの震度ファイルから5次メッシュの震度分布CSVを作成するメソッドを呼び出す
    }
}
