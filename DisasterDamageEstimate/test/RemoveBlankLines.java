import java.io.*;

/**
 * Created by manab on 2016/08/08.
 */
public class RemoveBlankLines {
    public static void main(String args[]) {
        File inFile = new File("files/master/temp.csv");
        File outFile = new File("files/master/mesh_base.csv");

        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), "UTF-8"));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile, false), "Shift_JIS"))) {
            String line;
            while((line = br.readLine())!=null) {
                if(!line.equals("")) {
                    String pair[] = line.split(",");
                    if(pair.length==14) {
                        line = pair[0] + "," + pair[1] + "," + pair[2] + "," + pair[3] + "," + pair[4] + "," + pair[5] +
                                "," + pair[6] + "," + pair[7] + "," + pair[8] + "," + pair[9] + "," + pair[10] + "," + pair[12] + "," + pair[13];
                    }
                    else {
                        line = pair[0] + "," + pair[1] + "," + pair[2] + "," + pair[3] + "," + pair[4] + "," + pair[5] +
                                "," + pair[6] + "," + pair[7] + "," + pair[8] + "," + pair[9] + "," + pair[10] + "," + pair[13] + "," + pair[14];
                    }
                    pw.write(line + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
