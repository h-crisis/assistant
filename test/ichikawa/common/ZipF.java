package ichikawa.common;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;


/**
 * Created by manabu on 2016/07/26.
 */
public class ZipF {

    public static void unzipFileWithPassword(File file, File dir, String pass) {
        try {
            System.out.println(file.getPath());
            ZipFile zipFile = new ZipFile(file.getPath());
            if(zipFile.isEncrypted()) {
                zipFile.setPassword(pass);
            }
            zipFile.extractAll(dir.getPath());
        } catch (ZipException e) {
            System.out.println("Zipファイル解凍中にエラーが生じました。");
            e.printStackTrace();
        }
    }
}
