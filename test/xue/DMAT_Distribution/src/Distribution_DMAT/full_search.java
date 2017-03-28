package Distribution_DMAT;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static Distribution_DMAT.select_dmat.dmat;
import static Distribution_DMAT.select_dmat.kyoten;

/**
 * Created by jiao on 2017/03/16.
 */
public class full_search {

    public static ArrayList<int[]> combinations=new ArrayList<int[]>();

    //全てのDMATから必要な数のDMATを抽出します。全ての可能な組み合わせを探す
    //numは必要な数：kyoten.size()*3 (各病院3台)
    public static void setGroup(ArrayList Dmat, int num){
        int n = 1;
        int m=1;
        int n_m=1;
        for(int i=0;i<Dmat.size();i++){
            n*=(i+1);
        }
        for(int i=0;i<num;i++){
            m*=(i+1);
        }
        for(int i=0;i<Dmat.size();i++){
            n_m*=(i+1);
        }
        Set<String>sets=new HashSet<String>();
        while(true){
            String s="";
            for(int i=0;i<num;i++){

            }
        }

    }

    //////////////////////////
    //病院のDMATの配置の可能な組み合わせkyoten.size()*3

    static void permutation(int[] chars) {
        permutation(chars, 0, chars.length - 1);
    }

    static void permutation(int[] chars, int begin, int end) {
        int[] combine= new int[chars.length];
        int i;
        if(begin == end) {
            //System.out.println();

            for(i = 0; i < chars.length; ++i) {
                combine[i]=chars[i];
                //System.out.print(chars[i]);
            }
            combinations.add(combine);


        } else {
            for(i = begin; i <= end; ++i) {
                if(canSwap(chars, begin, i)) {
                    swap(chars, begin, i);
                    permutation(chars, begin + 1, end);
                    swap(chars, begin, i);
                }
            }
        }

    }

    static void swap(int[] chars, int from, int to) {
        int temp = chars[from];
        chars[from] = chars[to];
        chars[to] = temp;
    }

    static boolean canSwap(int[] chars, int begin, int end) {
        for(int i = begin; i < end; ++i) {
            if(chars[i] == chars[end]) {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        int kyoten_size=3;
        int[] chars = new int[kyoten_size*3];
        int m=1;
        for(int i=0;i<kyoten_size*3;i++){
            chars[i]=i%kyoten_size+1;
        }
        permutation(chars, 0, chars.length - 1);
        System.out.println("===================");
        for(int i=0;i<combinations.size();i++){
            int[] t=combinations.get(i);
            System.out.println();
            for(int j=0;j<t.length;j++){
                System.out.print(t[j]);
            }
        }
        System.out.println("===================");
        System.out.println(combinations.size());
    }

    private double distance_evaluation(){
        System.out.println(kyoten.size());
        System.out.println(dmat.size());
        return kyoten.size();

    }


/*    public static void main(String arg[]) throws IOException {
        File masterDir = new File("/Users/jiao/IdeaProjects/private/files_full/master");
        File outDir = new File("/Users/jiao/IdeaProjects/private/files_full/out");

        new Distribution_DMAT.select_dmat(masterDir,outDir);
        System.out.println(kyoten.size());
        System.out.println(dmat.size());
    }*/


}
