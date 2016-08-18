package files;

import java.io.File;

/**
 * Created by manab on 2016/08/12.
 */
public class FileManagement {
    /**
     * 指定したフォルダ内を空にする
     * @param dir フォルダ
     */
    public static void removeFiles(File dir) {
        File files[] = dir.listFiles();
        for(int i=0; i<files.length; i++) {
            if(files[i].isDirectory()) {
                removeFiles(files[i]);
            }
            else {
                files[i].delete();
            }
        }
    }
}
