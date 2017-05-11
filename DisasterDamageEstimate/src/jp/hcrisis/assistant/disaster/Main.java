package jp.hcrisis.assistant.disaster;

import java.io.File;

/**
 * Created by manabu on 2016/09/13.
 */
public class Main {
    public static void main(String args[]) throws Exception {
        String code = args[3];
        File inFile = new File(args[1]);
        File dir = new File(args[2]);
        File outDir = new File(dir.getPath() + "/out/" + code);
        if(!outDir.exists()) {
            outDir.mkdir();
        }

        File masterFilesDir = new File(dir.getPath() + "/master");
        File shapeDir = new File(dir.getPath() + "/shape");
        File meshFile = new File(dir.getPath() + "/shape/mesh5th/Mesh5.shp");
        File dataFile = new File(dir.getPath() + "/master/mesh4th_data.csv");

        //FileManagement.removeFiles(outDir); // 出力フォルダを空にする

        File siFile = new File(outDir.getPath() + "/" + code + "_si.csv");
        File shpFile = new File(outDir.getPath() + "/" + code + "_si.shp");
        if(!siFile.exists()) {
            siFile.createNewFile();
        }

        if(args[0].equals("JSHIS")) {
            CreateMesh5Si.createMesh5SiFromJSHIS(inFile, siFile); // JSHISの震度ファイルから5次メッシュの震度分布CSVを作成するメソッドを呼び出す
        }
        else if(args[0].equals("SIP4")) {
            CreateMesh5Si.createMesh5SiFromSIP4(inFile, siFile); // SIP4日立から受け取る震度分布データから5次メッシュの震度分布CSVを作成するメソッド
        }

        CreateMesh5Si.createMesh5SiShape(meshFile, siFile, dataFile, shpFile);

        //new EarthquakeDamageEstimate(masterFilesDir, shapeDir, siFile, outDir, code);
        //new EarthquakeDamageDbSet(outDir, code);
        //new csv_merge(masterFilesDir,outDir, code);
        //new EstimatePatient(masterFilesDir,outDir, code);
}
}