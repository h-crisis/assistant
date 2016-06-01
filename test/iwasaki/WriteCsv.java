package iwasaki;

import java.io.IOException;
import java.io.FileWriter;
import java.io.File;
import java.io.*;
import java.util.Random;

/**
 * Created by daiki on 2016/05/28.
 */
//csvファイルを作成し、
public class WriteCsv {
    public static void main(String args[]) throws Exception
    {
        //ファイルの生成
        File WriteCSV = new File("test/iwasaki/WriteCSV.csv");
        WriteCSV.createNewFile();

        //ファイル書き込みオブジェクトの作成
        FileWriter fw = new FileWriter("test/iwasaki/WriteCSV.csv");
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(bw);


        //配列に１〜１００の数字をいれる。
        int[] data;
        data = new int[100];
        for(int i=0; i<100; i++)
        {
            data[i] = i+1;
        }


        //配列の数字を入れ替える。
        int a,r,temp;
        Random rnd = new Random();
        for ( int t=0; t<200; t++)
        {
            a = rnd.nextInt(100);
            r = rnd.nextInt(100);

            temp = data[a];
            data[a] = data[r];
            data[r] = temp;

        }

        //csvファイルへの書き込み
        for(int i=0; i<100; i++)
        {
            pw.println((i+1)+ "　 " + data[i]);
        }

        pw.close();

    }
}
