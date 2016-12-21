package jp.hcrisis.assistant.zenrin.area2;

import com.vividsolutions.jts.geom.*;
import gis.CreateShape;
import org.geotools.data.DataUtilities;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by manabu on 2016/07/04.
 */
public class ReadTxtFiles {
    public static void main(String args[]) throws Exception {
        File dir = new File("/Users/manabu/Desktop/ZenrinOUT/TXT");
        File outDir = new File("/Users/manabu/Desktop/ZenrinOUT/SHP");
        readFiles(dir, outDir, 4);
    }

    public static void readFiles(File dir, File outDir, int thread) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(thread);
        if(dir.isDirectory()) {
            File fileList[] = dir.listFiles();
            for(int i=0; i<fileList.length;i++) {
                if(!fileList[i].getName().startsWith(".")) {
                    String textFileName = fileList[i].getName();
                    String fileNamePair[] = textFileName.split("\\.");
                    File shapeFile = new File(outDir.getPath() + "/" + fileNamePair[0] + ".shp");
                    if(!shapeFile.exists()) {
                        executor.submit(new ReadTxtFileThread(fileList[i], outDir)); // スレッドで変換
                    }
                }
            }
        }
        else {
            System.out.println("\tError: 出力パスがディレクトリではありません。");
            throw new RuntimeException("Error: 出力パスがディレクトリではありません。");
        }
        executor.shutdown();
    }
 }
