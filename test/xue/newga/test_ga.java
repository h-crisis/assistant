package newga
/**
 * Created by jiao on 2016/11/22.
 */
public class test_ga {
    public static void main(String[] args) {

        // 任意の列を入力
        FitnessCalc
                .setSolution("1111000000000000000000000000000000000000000000000000000000001111");

        // 初期化
        Population myPop = new Population(50, true);

        // リサイクル
        int generationCount = 0;
        while (myPop.getFittest().getFitness() < FitnessCalc.getMaxFitness()) {
            generationCount++;
            System.out.println("Generation: " + generationCount + " Fittest: "
                    + myPop.getFittest().getFitness());
            myPop = Algorithm.evolvePopulation(myPop);
        }
        System.out.println("Solution found!");
        System.out.println("Generation: " + generationCount);
        System.out.println("Final Fittest Genes:");
        System.out.println(myPop.getFittest());

    }
}
