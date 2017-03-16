package Distribution_DMAT;

/**
 * Created by jiao.xue on 2017/03/16.
 */

import java.util.ArrayList;
import java.util.List;

public class Test {
    private static int a=3;
    public static char[] is=new char[a*3];


    //private static char[] is = new char[] { '1', '2', '4', '5', '6', '7', '8', '9','2'};

    private static int total;
    private static int m = 4;
    public static void main(String[] args) {


        for(int i=0;i<a*3;a++){
            is[i]=(char)('0'+i%(a*3)+1);

        }


        List<Integer> iL = new ArrayList<Integer>();
        new Test().plzh("", iL,  m);
        System.out.println("total : " + total);
    }
    private void plzh(String s, List<Integer> iL, int m) {
        if(m == 0) {
            System.out.println(s);
            total++;
            return;
        }
        List<Integer> iL2;
        for(int i = 0; i < is.length; i++) {
            iL2 = new ArrayList<Integer>();
            iL2.addAll(iL);
            if(!iL.contains(i)) {
                String str = s + is[i];
                iL2.add(i);
                plzh(str, iL2, m-1);
            }
        }
    }
}
