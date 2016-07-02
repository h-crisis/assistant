package ichikawa.ZmapArea2;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by manabu on 2016/06/12.
 */
public class OutDirs {

    // ディレクトリへのパス
    private String pathJPNMAP; // 全国図ディレクトリへのパス
    private String pathWIDEMAP; // 地方図ディレクトリへのパス
    private String pathGEOGRA; // 地勢図ディレクトリへのパス

    private File fileJPNMAP; // 全国図ディレクトリへのパス
    private File fileWIDEMAP; // 地方図ディレクトリへのパス
    private File fileGEOGRA; // 地勢図ディレクトリへのパス


    // 以下はサブディレクトリ構造
    private String pathTOPOGR; // 道路地図ディレクトリへのパス
    private File fileTOPOGR; // 道路地図ディレクトリへのパス

    private String pathHU; // 市区町村面へのパス
    private String pathIU; // 大地面へのパス（2階層ディイレクトリ）

    private File fileHU; // 市区町村面へのパス
    private File fileIU; // 大字面へのパス（2階層ディイレクトリ）

    private String pathHOKKAIDO; // 北海道ディレクトリへのパス
    private String pathTOHOKU; // 道北ディレクトリへのパス
    private String pathKANTO; // 関東ディレクトリへのパス
    private String pathCHUBU; // 中部ディレクトリへのパス
    private String pathKINKI; // 近畿ディレクトリへのパス
    private String pathCHUGOKU_SHIKOKU; // 中国四国ディレクトリへのパス
    private String pathKYUSHU_OKINAWA; // 九州沖縄ディレクトリへのパス

    private File fileHOKKAIDO; // 北海道ディレクトリへのパス
    private File fileTOHOKU; // 道北ディレクトリへのパス
    private File fileKANTO; // 関東ディレクトリへのパス
    private File fileCHUBU; // 中部ディレクトリへのパス
    private File fileKINKI; // 近畿ディレクトリへのパス
    private File fileCHUGOKU_SHIKOKU; // 中国四国ディレクトリへのパス
    private File fileKYUSHU_OKINAWA; // 九州沖縄ディレクトリへのパス

    private ArrayList<File> outFileList; // 出力ディレクトリ一覧

    OutDirs() {
        System.out.println("STEP02)OutFilesインスタンスが作成されました。");
    }

    OutDirs(File outDir) {
        this();
        removeAllFiles(outDir);
        setOutFiles(outDir);
        createOutDirs();
    }

    private void removeAllFiles(File dir) {
        System.out.println("\t出力フォルダを空にします（全てのファイル・フォルダを削除します）。");
        if(dir.isDirectory()) {
            File fileList[] = dir.listFiles();
            for(int i=0; i<fileList.length; i++) {
                if(fileList[i].isFile())
                    fileList[i].delete();
                else {
                    removeFiles(fileList[i]);
                    fileList[i].delete();
                }
            }
        }
        else {
            System.out.println("\tError: 出力パスがディレクトリではありません。");
            throw new RuntimeException("Error: 出力パスがディレクトリではありません。");
        }
    }

    private void removeFiles(File dir) {
        File fileList[] = dir.listFiles();
        for(int i=0; i<fileList.length; i++) {
            if(fileList[i].isFile())
                fileList[i].delete();
            else {
                removeFiles(fileList[i]);
                fileList[i].delete();
            }
        }
    }
    
    private void setOutFiles(File dir) {
        // 全国図・地方図・地勢図の出力準備をする。
        pathJPNMAP = dir.getPath() + "/JPNMAP";
        pathWIDEMAP = dir.getPath() + "/WIDEMAP";
        pathGEOGRA = dir.getPath() + "/GEOGRA";

        fileJPNMAP = new File(pathJPNMAP);
        fileWIDEMAP = new File(pathWIDEMAP);
        fileGEOGRA = new File(pathGEOGRA);

        // 道路地図の出力準備をする
        pathTOPOGR = dir.getPath() + "/TOPOGR";
        fileTOPOGR = new File(pathTOPOGR);

        // 市区町村面・大字面の出力準備をする
        pathHU = dir.getPath() + "/HU";
        pathIU = dir.getPath() + "/IU";

        fileHU = new File(pathHU);
        fileIU = new File(pathIU);

        // 詳細地図の出力準備をする
        pathHOKKAIDO = dir.getPath() + "/HOKKAIDO";
        pathTOHOKU = dir.getPath() + "/TOHOKU";
        pathKANTO = dir.getPath() + "/KANTO";
        pathCHUBU = dir.getPath() + "/CHUBU";
        pathKINKI = dir.getPath() + "/KINKI";
        pathCHUGOKU_SHIKOKU = dir.getPath() + "/CHUGOKU_SHIKOKU";
        pathKYUSHU_OKINAWA = dir.getPath() + "/KYUSHU_OKINAWA";

        fileHOKKAIDO = new File(pathHOKKAIDO);
        fileTOHOKU = new File(pathTOHOKU);
        fileKANTO = new File(pathKANTO);
        fileCHUBU = new File(pathCHUBU);
        fileKINKI = new File(pathKINKI);
        fileCHUGOKU_SHIKOKU = new File(pathCHUGOKU_SHIKOKU);
        fileKYUSHU_OKINAWA = new File(pathKYUSHU_OKINAWA);

    }

    /**
     * 出力フォルダを生成するメソッド
     */
    private void createOutDirs() {
        fileJPNMAP.mkdir();
        fileWIDEMAP.mkdir();
        fileGEOGRA.mkdir();
        fileTOPOGR.mkdir();
        fileHU.mkdir();
        fileIU.mkdir();
        fileHOKKAIDO.mkdir();
        fileTOHOKU.mkdir();
        fileKANTO.mkdir();
        fileCHUBU.mkdir();
        fileKINKI.mkdir();
        fileCHUGOKU_SHIKOKU.mkdir();
        fileKYUSHU_OKINAWA.mkdir();
        System.out.println("\t出力フォルダを作成しました。");

        outFileList = new ArrayList<File>();
        outFileList.add(fileJPNMAP);
        outFileList.add(fileWIDEMAP);
        outFileList.add(fileGEOGRA);
        outFileList.add(fileTOPOGR);
        outFileList.add(fileHU);
        outFileList.add(fileIU);
        outFileList.add(fileHOKKAIDO);
        outFileList.add(fileTOHOKU);
        outFileList.add(fileKANTO);
        outFileList.add(fileCHUBU);
        outFileList.add(fileKINKI);
        outFileList.add(fileCHUGOKU_SHIKOKU);
        outFileList.add(fileKYUSHU_OKINAWA);
    }

    /**
     * 出力フォルダの一覧を返すメソッド。
     * @return 出力ファイル一覧のリスト
     */
    public ArrayList<File> getOutFileList() {
        return outFileList;
    }
}
