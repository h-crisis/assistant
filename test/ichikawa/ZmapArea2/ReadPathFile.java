package ichikawa.ZmapArea2;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by manabu on 2016/06/10.
 */
public class ReadPathFile {

    //設定ファイル
    private File configFile;

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

    private ArrayList<File> fileList;

    ReadPathFile() {
        System.out.println("STEP01)ReadPathFileインスタンスが作成されました。");
    }

    ReadPathFile(String configFilePath) {
        this();
        configFile = new File(configFilePath);
        if(!configFile.exists()) {
            System.out.println("\tError: 設定ファイルがありません。");
            throw new RuntimeException("STEP01)設定ファイルがありません。");
        }
        setConfigFile();
    }

    ReadPathFile(ArrayList<String> filePathList) {
        this();
        pathJPNMAP = filePathList.get(0);
        pathWIDEMAP = filePathList.get(1);
        pathGEOGRA = filePathList.get(2);
        pathTOPOGR = filePathList.get(3);
        pathHU = filePathList.get(4);
        pathIU = filePathList.get(5);
        pathHOKKAIDO = filePathList.get(6);
        pathTOHOKU = filePathList.get(7);
        pathKANTO = filePathList.get(8);
        pathCHUBU = filePathList.get(9);
        pathKINKI = filePathList.get(10);
        pathCHUGOKU_SHIKOKU = filePathList.get(11);
        pathKYUSHU_OKINAWA = filePathList.get(12);

        fileJPNMAP = new File(pathJPNMAP);
        fileWIDEMAP = new File(pathWIDEMAP);
        fileGEOGRA = new File(pathGEOGRA);
        fileTOPOGR = new File(pathTOPOGR);
        fileHU = new File(pathHU);
        fileIU = new File(pathIU);
        fileHOKKAIDO = new File(pathHOKKAIDO);
        fileTOHOKU = new File(pathTOHOKU);
        fileKANTO = new File(pathKANTO);
        fileCHUBU = new File(pathCHUBU);
        fileKINKI = new File(pathKINKI);
        fileCHUGOKU_SHIKOKU = new File(pathCHUGOKU_SHIKOKU);
        fileKYUSHU_OKINAWA = new File(pathKYUSHU_OKINAWA);

        confirmConfigFile();
        createFileList();
    }

    /**
     * 設定ファイルを読み込むメソッド。DATファイルのあるディレクトリパスを設定する。
     */
    private void setConfigFile() {
        
    }
    
    private void confirmConfigFile() {
        System.out.println("\tデータファイルの確認をします。");
        if(fileJPNMAP.exists())
            System.out.println("\t\t01.全国図ディレクトリは存在します。");
        else {
            System.out.println("\t\tError: 全国図ディレクトリが存在しません。");
            throw new RuntimeException("全国図ディレクトリが存在しません。");
        }

        if(fileWIDEMAP.exists())
            System.out.println("\t\t02.地方図ディレクトリは存在します。");
        else {
            System.out.println("\t\tError: 地方図ディレクトリが存在しません。");
            throw new RuntimeException("地方図ディレクトリが存在しません。");
        }

        if(fileGEOGRA.exists())
            System.out.println("\t\t03.地勢図ディレクトリは存在します。");
        else {
            System.out.println("\t\tError: 地勢図ディレクトリが存在しません。");
            throw new RuntimeException("地勢図ディレクトリが存在しません。");
        }

        if(fileTOPOGR.exists())
            System.out.println("\t\t04.道路地図ディレクトリは存在します。");
        else {
            System.out.println("\t\tError: 道路地図ディレクトリが存在しません。");
            throw new RuntimeException("道路地図ディレクトリが存在しません。");
        }

        if(fileHU.exists())
            System.out.println("\t\t05.市区町村面ディレクトリは存在します。");
        else {
            System.out.println("\t\tError: 市区町村面ディレクトリが存在しません。");
            throw new RuntimeException("市区町村面ディレクトリが存在しません。");
        }

        if(fileIU.exists())
            System.out.println("\t\t06.大字面ディレクトリは存在します。");
        else {
            System.out.println("\t\tError: 大字面ディレクトリが存在しません。");
            throw new RuntimeException("大字面ディレクトリが存在しません。");
        }

        if(fileHOKKAIDO.exists())
            System.out.println("\t\t07.北海道ディレクトリは存在します。");
        else {
            System.out.println("\t\tError: 北海道ディレクトリが存在しません。");
            throw new RuntimeException("北海道ディレクトリが存在しません。");
        }

        if(fileTOHOKU.exists())
            System.out.println("\t\t08.東北ディレクトリは存在します。");
        else {
            System.out.println("\t\tError: 東北ディレクトリが存在しません。");
            throw new RuntimeException("東北ディレクトリが存在しません。");
        }

        if(fileKANTO.exists())
            System.out.println("\t\t09.関東ディレクトリは存在します。");
        else {
            System.out.println("\t\tError: 関東ディレクトリが存在しません。");
            throw new RuntimeException("関東ディレクトリが存在しません。");
        }

        if(fileCHUBU.exists())
            System.out.println("\t\t10.中部ディレクトリは存在します。");
        else {
            System.out.println("\t\tError: 中部ディレクトリが存在しません。");
            throw new RuntimeException("中部ディレクトリが存在しません。");
        }

        if(fileKINKI.exists())
            System.out.println("\t\t11.近畿ディレクトリは存在します。");
        else {
            System.out.println("\t\tError: 近畿ディレクトリが存在しません。");
            throw new RuntimeException("近畿ディレクトリが存在しません。");
        }

        if(fileCHUGOKU_SHIKOKU.exists())
            System.out.println("\t\t12.中国四国ディレクトリは存在します。");
        else {
            System.out.println("\t\tError: 中国四国ディレクトリが存在しません。");
            throw new RuntimeException("中国四国ディレクトリが存在しません。");
        }

        if(fileKYUSHU_OKINAWA.exists())
            System.out.println("\t\t13.九州沖縄ディレクトリは存在します。");
        else {
            System.out.println("\t\tError: 九州沖縄ディレクトリが存在しません。");
            throw new RuntimeException("九州沖縄ディレクトリが存在しません。");
        }
    }

    public void createFileList() {
        fileList = new ArrayList();
        fileList.add(fileJPNMAP);
        fileList.add(fileWIDEMAP);
        fileList.add(fileGEOGRA);
        fileList.add(fileTOPOGR);
        fileList.add(fileHU);
        fileList.add(fileIU);
        fileList.add(fileHOKKAIDO);
        fileList.add(fileTOHOKU);
        fileList.add(fileKANTO);
        fileList.add(fileCHUBU);
        fileList.add(fileKINKI);
        fileList.add(fileCHUGOKU_SHIKOKU);
        fileList.add(fileKYUSHU_OKINAWA);
    }

    /**
     * データファイルを保持するリストを返すメソッド。
     * @return データファイルのリスト
     */
    public ArrayList getFileList() {
        return fileList;
    }

}
