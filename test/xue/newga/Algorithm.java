package newga
/**
 * Created by jiao on 2016/11/22.
 */
public class Algorithm {
//GAのアルゴリズム
private static final double uniformRate = 0.5; //交叉の概率
    private static final double mutationRate = 0.015; //異変概率
    private static final int tournamentSize = 5; //淘汰数组的大小
    private static final boolean elitism = true; //精英主义

    /* Public methods */

    // populationの进化
    public static Population evolvePopulation(Population pop) {
        // 新しいpopulationの保存
        Population newPopulation = new Population(pop.size(), false);

        // 最適な遺伝子を行列の先頭に置いとく
        if (elitism) {
            newPopulation.saveIndividual(0, pop.getFittest());
        }
// 交叉 population
        int elitismOffset;
        if (elitism) {
            elitismOffset = 1;
        } else {
            elitismOffset = 0;
        }
        // Loop over the population size and create new individuals with
        // crossover
        for (int i = elitismOffset; i < pop.size(); i++) {
            //任意の個体を二つ選ぶ
            Individual indiv1 = tournamentSelection(pop);
            Individual indiv2 = tournamentSelection(pop);
            //交叉
            Individual newIndiv = crossover(indiv1, indiv2);
            newPopulation.saveIndividual(i, newIndiv);
        }

        // Mutate population  異変
        for (int i = elitismOffset; i < newPopulation.size(); i++) {
            mutate(newPopulation.getIndividual(i));
        }

        return newPopulation;
    }
    // 二つ個体の交叉　概率はuniformRate
    private static Individual crossover(Individual indiv1, Individual indiv2) {
        Individual newSol = new Individual();
        // 随机的从 两个个体中选择
        for (int i = 0; i < indiv1.size(); i++) {
            if (Math.random() <= uniformRate) {
                newSol.setGene(i, indiv1.getGene(i));
            } else {
                newSol.setGene(i, indiv2.getGene(i));
            }
        }
        return newSol;
    }

    // 異変個体。 概率は mutationRate
    private static void mutate(Individual indiv) {
        for (int i = 0; i < indiv.size(); i++) {
            if (Math.random() <= mutationRate) {
                // randam 0 or 1
                byte gene = (byte) Math.round(Math.random());
                indiv.setGene(i, gene);
            }
        }
    }
    // 随机选择一个较优秀的个体，用了进行交叉
    private static Individual tournamentSelection(Population pop) {
        // Create a tournament population
        Population tournamentPop = new Population(tournamentSize, false);
        //随机选择 tournamentSize 个放入 tournamentPop 中
        for (int i = 0; i < tournamentSize; i++) {
            int randomId = (int) (Math.random() * pop.size());
            tournamentPop.saveIndividual(i, pop.getIndividual(randomId));
        }
        // 找到淘汰数组中最优秀的
        Individual fittest = tournamentPop.getFittest();
        return fittest;
    }

}

