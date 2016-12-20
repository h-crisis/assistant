package newga
/**
 * Created by jiao on 2016/11/22.
 */
public class FitnessCalc {
    static byte[] solution = new byte[64];

    /* Public methods */
    // 結果はbyte array
    public static void setSolution(byte[] newSolution) {
        solution = newSolution;
    }

    // char"0""1"からbyte「0」「１」に変更　 solutionに入れる
    static void setSolution(String newSolution) {
        solution = new byte[newSolution.length()];
        // Loop through each character of our string and save it in our byte
        for (int i = 0; i < newSolution.length(); i++) {
            String character = newSolution.substring(i, i + 1);
            if (character.contains("0") || character.contains("1")) {
                solution[i] = Byte.parseByte(character);
            } else {
                solution[i] = 0;
            }
        }
    }

    // solutionと比べて ，個体の適応値を計算する
    //　同じ数字なら　fitness++
    static int getFitness(Individual individual) {
        int fitness = 0;
        for (int i = 0; i < individual.size() && i < solution.length; i++) {
            if (individual.getGene(i) == solution[i]) {
                fitness++;
            }
        }
        return fitness;
    }

    //適応値が一番高い方はsolution
    static int getMaxFitness() {
        int maxFitness = solution.length;
        return maxFitness;
    }
}
