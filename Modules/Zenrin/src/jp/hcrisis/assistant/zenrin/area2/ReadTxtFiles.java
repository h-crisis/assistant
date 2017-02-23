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
        // args[0] : txtファイルフォルダへのパス　args[1] : shapeファイルフォルダへのパス　args[2] : / or \
        File dir = new File(args[0]);
        File outDir = new File(args[1]);
        readFiles(dir, outDir, 1, args[2]);
    }

    public static void readFiles(File dir, File outDir, int thread, String separator) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(thread);
        if(dir.isDirectory()) {
            File fileList[] = dir.listFiles();
            for(int i=0; i<fileList.length;i++) {
                if(!fileList[i].getName().startsWith(".")) {
                    String textFileName = fileList[i].getName();
                    String fileNamePair[] = textFileName.split("\\.");
                    File shapeFile = new File(outDir.getPath() + separator + fileNamePair[0] + ".shp");
                    if(!shapeFile.exists()) {
                        executor.submit(new ReadTxtFileThread(fileList[i], outDir, separator)); // スレッドで変換
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
