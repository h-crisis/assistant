package ichikawa.ZmapArea2;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by manabu on 2016/06/10.
 */
public class ZmapArea2 {
    public static void main(String args[]) {

        ArrayList<String> filePathList = new ArrayList<String>();
        filePathList.add("/Volumes/My Passport/AreaMap2/ZENKOKU/MAPDAT/JPNMAP");
        filePathList.add("/Volumes/My Passport/AreaMap2/ZENKOKU/MAPDAT/WIDEMAP");
        filePathList.add("/Volumes/My Passport/AreaMap2/ZENKOKU/MAPDAT/GEOGRA");
        filePathList.add("/Volumes/My Passport/AreaMap2/ZENKOKU/MAPDAT/TOPOGR");
        filePathList.add("/Volumes/My Passport/AreaMap2/ZENKOKU/ADMDAT/HU");
        filePathList.add("/Volumes/My Passport/AreaMap2/ZENKOKU/ADMDAT/IU");
        filePathList.add("/Volumes/My Passport/AreaMap2/HOKKAIDO/MAPDAT/TOWNMP");
        filePathList.add("/Volumes/My Passport/AreaMap2/TOHOKU/MAPDAT/TOWNMP");
        filePathList.add("/Volumes/My Passport/AreaMap2/KANTO/MAPDAT/TOWNMP");
        filePathList.add("/Volumes/My Passport/AreaMap2/CHUBU/MAPDAT/TOWNMP");
        filePathList.add("/Volumes/My Passport/AreaMap2/KINKI/MAPDAT/TOWNMP");
        filePathList.add("/Volumes/My Passport/AreaMap2/CHUGOKU_SHIKOKU/MAPDAT/TOWNMP");
        filePathList.add("/Volumes/My Passport/AreaMap2/KYUSHU_OKINAWA/MAPDAT/TOWNMP");
        filePathList.add("/Volumes/My Passport/AreaMap2/OUT");

        ReadPathFile rpf = new ReadPathFile(filePathList);
        ArrayList<File> fileList = rpf.getFileList();

        OutDirs of = new OutDirs(fileList.get(13));
        ArrayList<File> outFileList = of.getOutFileList();

        System.out.println(outFileList.size());
    }
}
