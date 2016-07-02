package ichikawa;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by manabu on 2016/06/10.
 */
public class AreaMap2 {
    public static void main(String args[]) throws IOException{
        //test();
    }

    public void test() throws IOException {
        File file = new File("/Users/manabu/Dropbox/GIS/GISデータ/ゼンリン/Area Map2/ZENKOKU/MAPDAT/JPNMAP/MAP3115.DAT");
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"Shift_JIS"));

        //TextFile tx1 = new TextFile("/Users/manabu/Dropbox/GIS/GISデータ/ゼンリン/Area Map2/ZENKOKU/MAPDAT/JPNMAP/191.txt");
        //TextFile tx1 = new TextFile();
        //System.out.println(tx1.getFile().getPath());

        String str;
        int i = 0;
        LinkedList list = new LinkedList();
        while((str=br.readLine())!=null) {
            if(str.equals("[HEADER]")) { // [HEADER]セクションの処理
                while(!(str=br.readLine()).equals("")) {
                    if(str.startsWith("FigNum")) // ただし変換するFeature数は表示する
                        System.out.println(str);
                }
            }
            else if(str.startsWith("[")) {
                ArrayList<String> tmpList = new ArrayList<String>();
                tmpList.add(str);
                str = br.readLine();
                while(str!=null) { // 次の空行までのループ
                    tmpList.add(str);
                    str = br.readLine();
                    if(str==null)
                        list.addLast(tmpList);
                    else if(str.equals("")) {
                        list.addLast(tmpList);
                        continue;
                    }
                }
                if(str==null)
                    break;
            }
        }
        System.out.println(((ArrayList)list.get(0)).get(5));
    }

    public class TextFile {
        private File file;
        private PrintWriter pw;

        public TextFile() {
        }

        public TextFile(String path) throws IOException {
            file = new File(path);
            PrintWriter pw    = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"Shift_JIS")));
            if(!file.exists())
                file.createNewFile();
        }

        public void writeLine(String str) {
            pw.print(str);
        }

        public void writeLineln(String str) {
            pw.println(str);
        }

        public void closePW() {
            pw.close();
        }

        public File getFile() {
            return file;
        }
    }
}
