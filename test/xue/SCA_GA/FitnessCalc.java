package SCA_GA;


import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Created by jiao.xue on 2017/02/03.
 */
public class FitnessCalc {
    //個体総数
    public static final int POPULATION_SIZE = 50;
    //遺伝子の長さ 今回は10000人と想定する
    static int NO_OF_PARAMETERS = 100000;

    //個体から生成された個体の長さ
    public static final int defaultGeneLength = 17;

    //回す回数
    public static int times = 1000;
    //回す回数2

    public static int times2 = 20;//20回同じ評価値出ましたら　stop


    public static double[] standard = new double[defaultGeneLength];
    ;

    public static HashMap<Integer, HashMap<Integer, Double>> DB = new HashMap<>(); //症状がキー、（Types of SCA がキー、確率が値）が値


    //public static void main(String args[]) throws IOException {
    public static HashMap<Integer, HashMap<Integer, Double>> make_DB() throws IOException {

            File file = new File("/Users/jiao/Dropbox/SCA/SCA.xls");

        for(int i=0;i<6;i++) {
            int type =i+1;
            File outfile1 = new File("/Users/jiao/Dropbox/SCA/"+"type"+type+"best_individuals.csv");
           //File outfile2 = new File("/Users/jiao/Dropbox/SCA/"+"type"+type+"percentage.csv");
            Calculation(file,outfile1,type);//DBを作る　ファイルを書き出す
        }

        return DB;

    }


    public static void Calculation(File file,File outfile1,int type) throws IOException {


        standard = files.standard(file, type);

        // 初期化
        Population myPop = new Population(POPULATION_SIZE, true);
        Individual best=getBestIndividual(myPop);//////////

        // リサイクル

        int generationCount = 0;
        int t=0;
        while(generationCount<times && t!=times2){//
            // while (FitnessCalc.getBestIndividual(myPop).getFitness() > 1) { //fitnessは1以下ならbest individual
            generationCount++;
              System.out.println("Generation: " + generationCount + " Fittest: "
                    + FitnessCalc.getBestIndividual(myPop).getFitness());
            myPop = Algorithm.evolvePopulation(myPop);
            ///////////////////
            Individual new_best=getBestIndividual(myPop);
            if(new_best==best){
                t++;
            }
            else {
                t=0;
                best=new_best;
            }
        }


        print_best_unit(getBestIndividual(myPop),outfile1);
        DB(getBestIndividual(myPop),type);
     // print_percentage(getBestIndividual(myPop),outfile2);


    }




    //評価値を計算する

    static double getFitness(Individual individual) throws IOException {
        int[] unit = new int[NO_OF_PARAMETERS];
        for (int i = 0; i < individual.size() ; i++) {
            unit[i]=individual.getGene(i);
        }
        double[] seed=initialization.Seed(unit);

        double evaluationValue = 0.0;
        for(int i=0;i<defaultGeneLength;i++){
            evaluationValue+=Math.abs(seed[i]-standard[i]);
            //System.out.println(evaluationValue+"="+standard_sleep_time[i]+"X"+population[i]);

        }
        return evaluationValue;

    }


    //populationを評価する
    //適応値が一番小さい方のfitnessを記録する
    static double getBestFitness(Population population) throws IOException {
        Individual individual;
        double BestFitness=999999;
        for(int i=0;i<population.size();i++){
            individual= population.getIndividual(i);
            if(getFitness(individual)<BestFitness){
                BestFitness=getFitness(individual);
            }
        }

        return BestFitness;
    }

    //populationを評価する
    //適応値が一番小さい方のfitnessを記録する
    static Individual getBestIndividual(Population population) throws IOException {
        Individual individual;
        Individual Bestindividual= population.getIndividual(0);
        double BestFitness=999999;
        for(int i=0;i<population.size();i++){
            individual= population.getIndividual(i);
            if(getFitness(individual)<BestFitness){
                BestFitness=getFitness(individual);
                Bestindividual=individual;
            }
        }

        return Bestindividual;
    }

    //結果を書き出す
    public static void print_best_unit(Individual individual, File outfile)  {

        try{
            PrintWriter output = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outfile, false), "Shift_JIS"));//結合した結果を新しいファイル'out'に保存する

            System.out.println("Evaluation value: " + individual.getFitness());

            int x = 0;
            output.write("result,neurological presentation,dementia,cerebellar dysarthria,gait ataxia,limb ataxia,romberg sign,babinski sign,vertical supra-nuclear,gaze palsy,Gaze-evoked nystagmus,disturbance of slow,eye mobement,parkinsonism,limb reflex,hyperactive,sluggish,Normal");
            for(int i = 0; i<individual.size(); ++i) {
                x = individual.getGene(i);
                byte[] property= change(x);
                output.write("\n"+x+",");
                for(int j=0;j<defaultGeneLength;j++){//変換
                    output.write(property[j]+",");
                }
               // output.write(property[defaultGeneLength-1]);

                //  System.out.println("result"+i+" is "+change((int)Math.round(x)));

            }
            output.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static byte[] change(int x){
        byte[] property= new byte[defaultGeneLength];
        for(int i=0;i<defaultGeneLength;i++){
            property[i]=(byte)0;

        }
        for(int i=0;x>=1;i++){
            property[i]=(byte)(x%2);
            x=x/2;
        }
        return property;
    }

    //結果データベースを作る
    public static void DB(Individual individual, int type)  {


        int x = 0;
        //maps.put(type,1.0/NO_OF_PARAMETERS);
        int cal=0;
        for(int i = 0; i<individual.size(); ++i) {
            x = individual.getGene(i);
            if(x!=0) {
                cal++;
            }
        }
        System.out.println("type"+type+" is "+cal);

        for(int i = 0; i<individual.size(); ++i) {

            x = individual.getGene(i);
            if(x!=0) {
                if (!DB.containsKey(x)) {
                    HashMap<Integer, Double> maps = new HashMap<>();//症状がキー、確率が値
                    maps.put(type, 1.0 / cal);
                    //maps.put(type, 1.0);
                    DB.put(x, maps);
                }
               else if (DB.containsKey(x)) {
                    HashMap<Integer, Double> maps = new HashMap<>();//症状がキー、確率が値
                    maps = DB.get(x);
                    if (!maps.containsKey(type)) {
                        maps.put(type, 1.0 / cal);
                        //maps.put(type, 1.0);
                        DB.put(x, maps);
                    } else {

                        double t = maps.get(type) + (1.0 / cal);
                        // double t = maps.get(type) + 1.0;
                        maps.put(type, t);
                        DB.put(x, maps);
                    }
                }
            }

        }



    }

    //結果を書き出す
    public static void print_percentage(HashMap<Integer, HashMap<Integer, Double>> DB, File outfile)  {

        try{
            PrintWriter output = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outfile, false), "Shift_JIS"));//結合した結果を新しいファイル'out'に保存する

           // System.out.println("Evaluation value: " + individual.getFitness());
            output.write("type,neurological presentation,dementia,cerebellar dysarthria,gait ataxia,limb ataxia,romberg sign,babinski sign,vertical supra-nuclear,gaze palsy,Gaze-evoked nystagmus,disturbance of slow,eye mobement,parkinsonism,limb reflex,hyperactive,sluggish,Normal,");
            output.write(",sporadic,autosomal dominant,autosomal recessive,other genetic types,spastic palsy,unknown");

            for(Entry<Integer, HashMap<Integer, Double>> entry:DB.entrySet()){
                output.write("\n"+entry.getKey()+",");
                byte[] property= change(entry.getKey());

                for(int j=0;j<defaultGeneLength;j++){//変換
                    output.write(property[j]+",");
                }
                HashMap<Integer, Double> map=entry.getValue();
                double[] record= new double[6];
                for(Entry<Integer, Double> entrys:map.entrySet()) {
                    record[entrys.getKey()-1]= entrys.getValue(); //先に記録
                    //System.out.println(entrys.getKey());

                }
                for(int i=0;i<6;i++) {
                    output.write("," + record[i]);
                }
            }
            output.close();
        }catch (IOException e) {
            e.printStackTrace();
        }

    }


}
