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

            File file = new File("assistant/test/iwasaki/test.csv");
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String str = br.readLine();
            while(str != null)
            {
                System.out.println(str);
                str = br.readLine();
            }

            br.close();





        int sum =0;
        for(int i=0; i<3; i++)
        {
            String num = br.readLine();
            int number = Integer.parseInt(num);
            sum = sum + number;
        }



        System.out.println("競技者人口の人数は" + sum +"です");

    }


}

