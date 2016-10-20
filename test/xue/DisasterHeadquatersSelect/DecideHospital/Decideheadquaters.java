package DisasterHeadquatersSelect.DecideHospital;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by daiki on 2016/07/19.
 * 入力ファイル：hospital_info.csv
 * 災害拠点病院の選定にあたり、結果の出力ファイル
 * OutHeadQuatercandidate.csv (震度5.5以上の病院)
 * OutHeadQueater.csv(震度5.5以上、かつ総キャパシティは総重症者数を超える)
 */
public class Decideheadquaters {
    ;
    public static ArrayList rengecode = new ArrayList();
    public static ArrayList Hospitalcode = new ArrayList();
    public static ArrayList rengeHopitalcode = new ArrayList();
    public static ArrayList Hopitallan = new ArrayList();
    public static ArrayList Hospitallon = new ArrayList();
    public static ArrayList Numberofpatient = new ArrayList();
    public static ArrayList Totalrengecode = new ArrayList();
    public static ArrayList Hospitalcapacity = new ArrayList();
    public static ArrayList hospitalseisimic = new ArrayList();
    public static ArrayList TotalHeadquaterCandidate = new ArrayList();
    public static ArrayList HeadquaterCandidateSeismic = new ArrayList();
    public static ArrayList HeadquaterCandidateHospital = new ArrayList();
    public static ArrayList HeadquaterCandidateHospitallan = new ArrayList();
    public static ArrayList HeadquaterCandidateHospitallon = new ArrayList();
    public static ArrayList HeadquaterCandidateTotalNumberofpatientinrenge = new ArrayList();
    public static ArrayList HeadquaterCandidaterenge = new ArrayList();
    public static ArrayList HeadquaterCandidateCapacity = new ArrayList();
    public static ArrayList HeadquaterSeismic = new ArrayList();
    public static ArrayList HeadquaterHospital = new ArrayList();
    public static ArrayList HeadquaterHospitallan = new ArrayList();
    public static ArrayList HeadquaterHospitallon = new ArrayList();
    public static ArrayList TotalHeadquater = new ArrayList();
    public static ArrayList HeadquaterTotalNumberofpatientinrenge = new ArrayList();
    public static ArrayList Headquaterrenge = new ArrayList();
    public static ArrayList HeadquaterCapacity = new ArrayList();
    public static ArrayList rengeHopitalcapacity = new ArrayList();
    public static ArrayList rengeHospitalseismic = new ArrayList();
    public static HashMap<String, Double> Numberofpatieninrenge = new HashMap<String, Double>();
    public static HashMap<ArrayList<String>, Double> HospitalCapacity = new HashMap<ArrayList<String>, Double>();
    public static HashMap<ArrayList<String>, Double> HospitalSeismic = new HashMap<ArrayList<String>, Double>();


    ////////各2次医療圏における病院のキャパの設定//////
    public static HashMap<ArrayList<String>, Double> getHospitalCapacity() {
        String temp1 = (String) rengecode.get(0);

        int counter = 0;

        for (int i = 0; i < rengeHopitalcode.size(); ++i) {
            ArrayList test1 = new ArrayList();

            test1.add(rengeHopitalcode.get(i));
            test1.add(Hospitalcode.get(i));

            rengeHopitalcapacity.add(test1);


        }

        for (int i = 0; i < rengeHopitalcode.size(); ++i) {
            String str = (String) Hospitalcapacity.get(i);
            double num = Double.parseDouble(str);
            HospitalCapacity.put((ArrayList<String>) rengeHopitalcapacity.get(i), num);

        }

        return HospitalCapacity;
    }

    /////////各2次医療圏における病院の震度の取得////////
    public static HashMap<ArrayList<String>, Double> getHospitalseismic() {
        String temp1 = (String) rengecode.get(0);

        int counter = 0;

        for (int i = 0; i < rengeHopitalcode.size(); ++i) {
            ArrayList test1 = new ArrayList();

            test1.add(rengeHopitalcode.get(i));
            test1.add(Hospitalcode.get(i));

            rengeHospitalseismic.add(test1);

        }

        for (int i = 0; i < rengeHopitalcode.size(); ++i) {
            String str = (String) hospitalseisimic.get(i);
            double num = Double.parseDouble(str);
            HospitalSeismic.put((ArrayList<String>) rengeHospitalseismic.get(i), num);

        }

        return HospitalSeismic;
    }


    ////////各2次医療県内のCapacityの合計値を求める//////
    public static void getTotalHeadquaterCandidate() {
        String temp = null;
        int counter = 0;
        double sum = 0.0;
        double d = 0.0;
        String test = null;
        for (int i = 0; i < HeadquaterCandidaterenge.size(); ++i) {
            d = (Double) HeadquaterCandidateCapacity.get(i);

            if (i != 0) {
                temp = (String) HeadquaterCandidaterenge.get(i - 1);
            }

            test = (String) HeadquaterCandidaterenge.get(i);

            if (!(test.equals(temp))) {
                //System.out.println(counter);
                for (int j = counter; j > 0; j--) {
                    //System.out.println("sum" + sum);
                    TotalHeadquaterCandidate.add(sum);
                }
                sum = 0;
                counter = 0;
            }

            if (i == HeadquaterCandidateHospital.size() - 1) {
                sum += d;
                //System.out.println(sum);
                for (int j = counter; j >= 0; j--) {
                    //System.out.println(sum);
                    TotalHeadquaterCandidate.add(sum);
                }
                sum = 0;
                counter = 0;
            }

            sum += d;
            counter++;
        }
    }


    /////////各２次医療圏の重症患者の合計値の算出////////
    public static HashMap<String, Double> getNubmerofpatinetinrenge(ArrayList numberofpatient, ArrayList rengecode) {

        double sum = 0.0;
        String temp = (String) rengecode.get(0);
        int counter = 0;
        double x;
        String temp1;
        //ArrayList Totalrengecode = new ArrayList();


        for (int i = 0; i < rengecode.size(); ++i) {

            temp1 = (String) numberofpatient.get(i);
            x = Double.parseDouble(temp1);

            temp = (String) rengecode.get(i);
            if (i != 0) {
                temp = (String) rengecode.get(i - 1);
            }

            if (!(temp.equals(rengecode.get(i)))) {
                Numberofpatieninrenge.put((String) rengecode.get(i - 1), sum);
                Totalrengecode.add(rengecode.get(i - 1));
                //System.out.println(sum);
                sum = 0;
            }

            if (i == rengecode.size() - 1) {
                sum += x;
                Numberofpatieninrenge.put((String) rengecode.get(i - 1), sum);
                Totalrengecode.add(rengecode.get(i - 1));
                //System.out.println(sum);
                sum = 0;
            }

            sum += x;
        }

        return Numberofpatieninrenge;
    }


    /////////拠点本部となる病院の選定///////
    /////////震度5.5以上によって災害本部の候補を決める///////
    public static void getHeadquaterCandidate() {
        String temp;
        double a;
        double b;
        double c;
        double d;
        double sum = 0.0;
        temp = (String) rengecode.get(0);
        for (int i = 0; i < Hospitalcode.size(); ++i) {
            a = HospitalSeismic.get(rengeHospitalseismic.get(i));
            b = HospitalCapacity.get(rengeHopitalcapacity.get(i));
            c = Numberofpatieninrenge.get(rengecode.get(i));
            d = HospitalCapacity.get(rengeHopitalcapacity.get(i));
            int counter = 0;

            if (a > 5.5) {

                HeadquaterCandidateTotalNumberofpatientinrenge.add(Numberofpatieninrenge.get(rengecode.get(i)));
                HeadquaterCandidaterenge.add(rengecode.get(i));
                HeadquaterCandidateHospital.add(Hospitalcode.get(i));
                HeadquaterCandidateSeismic.add(a);
                HeadquaterCandidateCapacity.add(d);
                HeadquaterCandidateHospitallan.add(Hopitallan.get(i));
                HeadquaterCandidateHospitallon.add(Hospitallon.get(i));

                sum += d;
                counter++;

            }

        }

    }

    //////その他の基準から災害拠点本部の絞りこみを行う////////
    //////各2次医療圏のキャパシティの合計とその２次医療圏内における重症患者の合計を比較し、キャパシティのほうが重症患者の合計値より小さい場合にはその医療圏内の本部候補を本部とする////
    public static void getHeadquater() {



        Headquaterrenge = (ArrayList) HeadquaterCandidaterenge.clone();
        HeadquaterHospital = (ArrayList) HeadquaterCandidateHospital.clone();
        HeadquaterSeismic = (ArrayList) HeadquaterCandidateSeismic.clone();
        HeadquaterTotalNumberofpatientinrenge = (ArrayList) HeadquaterCandidateTotalNumberofpatientinrenge.clone();
        HeadquaterCapacity = (ArrayList) HeadquaterCandidateCapacity.clone();
        TotalHeadquater = (ArrayList) TotalHeadquaterCandidate.clone();
        HeadquaterHospitallan = (ArrayList) HeadquaterCandidateHospitallan.clone();
        HeadquaterHospitallon = (ArrayList) HeadquaterCandidateHospitallon.clone();


        for (int i = 0; i < Headquaterrenge.size(); ++i) {
            if ((Double) HeadquaterTotalNumberofpatientinrenge.get(i) > (Double) HeadquaterCapacity.get(i)) {
                Headquaterrenge.remove(i);
                HeadquaterHospital.remove(i);
                HeadquaterSeismic.remove(i);
                HeadquaterTotalNumberofpatientinrenge.remove(i);
                HeadquaterCapacity.remove(i);
                TotalHeadquater.remove(i);
                HeadquaterHospitallan.remove(i);
                HeadquaterHospitallon.remove(i);
            }

        }

    }


    public static void main(String args[]) throws Exception {

        ///////ファイルの参照（都道府県名、二次医療圏、コード、病院名、Capacity,重症患者数,震度、災害拠点病院の有無,緯度、経度//////
        File test = new File("test/xue/DisasterHeadquatersSelect/DecideHospital/hospital_info.csv");

        ////////各医療圏における災害拠点病院のピックアップと総被災者数の算出/////
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(test), "SHIFT_JIS"));

        String str;
        int counter = 0;
        int count = 0;

        while ((str = br.readLine()) != null) {


            ArrayList test1 = new ArrayList();


            if (counter > 0) {
                String[] pair = str.split(",");
                if (pair[7].equals("1")) {
                    Hospitalcode.add(pair[2]);
                    rengeHopitalcode.add(pair[1]);
                    Hospitalcapacity.add(pair[4]);
                    hospitalseisimic.add(pair[6]);
                    Hopitallan.add(pair[8]);
                    Hospitallon.add(pair[9]);

                }


                String str1 = (String) pair[5];
                double num = Double.parseDouble(str1);
                Numberofpatient.add(str1);
                rengecode.add(pair[1]);

            }
            counter++;
        }


        getNubmerofpatinetinrenge(Numberofpatient, rengecode);
        //pirntNumberofPatientrenge();
        getHospitalCapacity();
        //printHopitalCapacity();
        getHospitalseismic();
        //printHopitalSeismic();
        getHeadquaterCandidate();
        getTotalHeadquaterCandidate();
        printHeadquaterCandidate();
        getHeadquater();
        printHeadquater();
        writeHeadquaterCandidate();
        writeHeadquater();





    }

    public static void pirntNumberofPatientrenge() {
        //System.out.println(Totalrengecode.get(0));
        for (int i = 0; i < Totalrengecode.size(); ++i) {
            System.out.println("rangecode  " + Totalrengecode.get(i) + "  TotalNumberofpatient" + Numberofpatieninrenge.get(Totalrengecode.get(i)));
        }
    }

    public static void printHopitalCapacity() {
        for (int i = 0; i < Hospitalcode.size(); ++i) {
            System.out.println("rangecode " + rengeHopitalcode.get(i) + "  Hopitalcode " + Hospitalcode.get(i) + "  HopitalCpacity " + HospitalCapacity.get(rengeHopitalcapacity.get(i)));
        }
    }

    public static void printHopitalSeismic() {
        for (int i = 0; i < Hospitalcode.size(); ++i) {
            System.out.println("rangecode " + rengeHopitalcode.get(i) + "  Hopitalcode " + Hospitalcode.get(i) + "  HopitalSeismic " + HospitalSeismic.get(rengeHospitalseismic.get(i)));
        }

    }

    public static void printHeadquaterCandidate() {
        System.out.println(" ");
        for (int i = 0; i < HeadquaterCandidateHospital.size(); ++i) {
            System.out.println("renge " + HeadquaterCandidaterenge.get(i) + "  HospitalNumber" + HeadquaterCandidateHospital.get(i) + "  seismic " + HeadquaterCandidateSeismic.get(i) + "  TotalNumberofpatientinrenge " + HeadquaterCandidateTotalNumberofpatientinrenge.get(i) + " Capacity " + HeadquaterCandidateCapacity.get(i) + "  TotalCpacity " + TotalHeadquaterCandidate.get(i) + "  Hospitallan " + HeadquaterCandidateHospitallan.get(i) + "  Hospitallon " + HeadquaterCandidateHospitallon.get(i));
        }

    }

    public static void printHeadquater() {
        System.out.println(" ");
        for (int i = 0; i < HeadquaterHospital.size(); ++i) {
            System.out.println("renge " + Headquaterrenge.get(i) + "  HospitalNumber" + HeadquaterHospital.get(i) + "  seismic " + HeadquaterSeismic.get(i) + "  TotalNumberofpatientinrenge " + HeadquaterTotalNumberofpatientinrenge.get(i) + " Capacity " + HeadquaterCapacity.get(i) + "  TotalCpacity " + TotalHeadquater.get(i) + "  Hospitallan " + HeadquaterHospitallan.get(i) + "  Hospitallon " + HeadquaterHospitallon.get(i));
        }
    }

    public static void writeHeadquaterCandidate() throws Exception {

        //ファイル書き込みオブジェクトの作成
        File file = new File("test/xue/DisasterHeadquatersSelect/DecideHospital/OutHeadQuatercandidate.csv");

        PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file, false), "UTF-8"));
        pw.write("range,HospitalNumber,seismic,TotalNumberofpatientinrenge,Capacity,TotalCapacity,Hospitallan,Hospitallon");


        for (int i = 0; i < HeadquaterCandidaterenge.size(); ++i) {
            pw.write("\n" + HeadquaterCandidaterenge.get(i) + "," + HeadquaterCandidateHospital.get(i) + "," + HeadquaterCandidateSeismic.get(i) + "," + HeadquaterCandidateTotalNumberofpatientinrenge.get(i) + "," + HeadquaterCandidateCapacity.get(i) + "," + TotalHeadquaterCandidate.get(i) + "," + HeadquaterCandidateHospitallan.get(i) + "," + HeadquaterCandidateHospitallon.get(i));
        }

        pw.close();
    }

    public static void writeHeadquater() throws Exception {

        //ファイル書き込みオブジェクトの作成
        File file = new File("test/xue/DisasterHeadquatersSelect/DecideHospital/OutHeadQuater.csv");

        PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file, false), "UTF-8"));
        pw.write("range,HospitalNumber,seismic,TotalNumberofpatientinrenge,Capacity,TotalCapacity,Hospitallan,Hospitallon");


        for (int i = 0; i < Headquaterrenge.size(); ++i) {
            pw.write("\n" + Headquaterrenge.get(i) + "," + HeadquaterHospital.get(i) + "," + HeadquaterSeismic.get(i) + "," + HeadquaterTotalNumberofpatientinrenge.get(i) + "," + HeadquaterCapacity.get(i) + "," + TotalHeadquater.get(i) + "," + HeadquaterHospitallan.get(i) + "," + HeadquaterHospitallon.get(i));
        }

        pw.close();
    }
}
