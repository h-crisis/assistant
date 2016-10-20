package iwasaki;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
/**
 * Created by daiki on 2016/05/27.
 */
//test.csvファイルの競技人口の合計を求める。
public class test {
    public static void main(String args[]) throws Exception
    {

        File file = new File("test/iwasaki/test.csv");
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        String str = br.readLine();
        int sum = 0;

        while((str = br.readLine()) != null) {
            String[] pair = str.split(",");
            System.out.println(pair[2]);
            sum = sum + Integer.parseInt(pair[2]);

        }
        br.close();
        System.out.println("合計は" + sum + "です。");
    }


}

