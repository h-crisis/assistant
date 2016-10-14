package jp.hcrisis.assistant.disaster;

import java.io.File;

/**
 * Created by manabu on 2016/09/13.
 */
public class Main {
    public static void main(String args[]) throws Exception {
        //File dirSIP4 = new File("/Users/manabu/OneDrive - CityRiver.NET/GIS/GISデータ/SIP4_東京湾北部地震/SIP4_東京湾北部地震_日立提供");
        //File outFileSIP4 = new File("/Users/manabu/OneDrive - CityRiver.NET/GIS/GISデータ/SIP4_東京湾北部地震/SIP4_東京湾北部地震.csv");

        File inFileJSHIS = new File("/Users/manabu/OneDrive - 国立保健医療科学院/GIS/GISデータ/J-SHIS_F008101_中央構造線断層帯金剛山地東縁/S-V3-F008101-MAP-CASE1.csv");
        File outFileJSHIS = new File("/Users/manabu/OneDrive - 国立保健医療科学院/GIS/GISデータ/J-SHIS_F008101_中央構造線断層帯金剛山地東縁/J-SHIS_F008101_中央構造線断層帯金剛山地東縁.csv");

        //CreateMesh5Si.createMesh5SiFromSIP4(dirSIP4, outFileSIP4); // SIP4日立から受け取る震度分布データから5次メッシュの震度分布CSVを作成するメソッド
        CreateMesh5Si.createMesh5SiFromJSHIS(inFileJSHIS, outFileJSHIS); // JSHISの震度ファイルから5次メッシュの震度分布CSVを作成するメソッドを呼び出す

        File masterFilesDir = new File("files/master");
        File shapeDir = new File("files/shape");
        //File siFile = new File("files/JSHIS/ShutoHokubu/shuto_hokubu.csv");
        File siFile = new File("/Users/manabu/OneDrive - 国立保健医療科学院/GIS/GISデータ/J-SHIS_F008101_中央構造線断層帯金剛山地東縁/J-SHIS_F008101_中央構造線断層帯金剛山地東縁.csv");
        File outDir = new File("files/out");
        String code = "20161017_01";

        new EarthquakeDamageEstimate(masterFilesDir, shapeDir, siFile, outDir, code);
        new EarthquakeDamageDbSet(outDir, code);
    }
}
