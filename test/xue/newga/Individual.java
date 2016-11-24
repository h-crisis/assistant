package newga;

/**
 * Created by jiao on 2016/11/22.
 */
public class Individual {
    //遺伝子の長さ
    static int defaultGeneLength = 64;
    //遺伝子行列 bitstring
    private byte[] genes = new byte[defaultGeneLength];
    // 個体の適応値
    private int fitness = 0;

    // 最初遺伝子個体をランダムで作る
    public void generateIndividual() {
        for (int i = 0; i < size(); i++) {
            byte gene = (byte) Math.round(Math.random());
            genes[i] = gene;
        }
    }

    /* Getters and setters */
    // 遺伝子の長さが違ったら、lengthの値にします
    public static void setDefaultGeneLength(int length) {
        defaultGeneLength = length;
    }

    public byte getGene(int index) {
        return genes[index];
    }
    public void setGene(int index, byte value) {
        genes[index] = value;
        fitness = 0;
    }

    /* Public methods */
    //サイズ測る
    public int size() {
        return genes.length;
    }

    //適応値

    public int getFitness() {
        if (fitness == 0) {
            fitness = FitnessCalc.getFitness(this);
        }
        return fitness;
    }

    @Override
    public String toString() {
        String geneString = "";
        for (int i = 0; i < size(); i++) {
            geneString += getGene(i);
        }
        return geneString;
    }
}
