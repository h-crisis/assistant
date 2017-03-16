package GA_new;

import java.util.HashMap;

/**
 * Created by jiao on 2017/03/10.
 */
public class Population {
    Individual[] individuals;
    /*
     * 個体
     */
    // 群を作る
    public Population(int populationSize, boolean initialise, HashMap map) {
        individuals = new Individual[populationSize];
        // 初期化
        if (initialise) {
            Individual newIndividual = new Individual();
            newIndividual.generate_dsitance_Individual(map);
            saveIndividual(0, newIndividual);//距離で評価した最適解を初期個体に入れる
            for (int i = 1; i < size(); i++) {
                newIndividual = new Individual();
                newIndividual.generateIndividual(map);
                saveIndividual(i, newIndividual);
            }
        }
    }

    /* Getters */
    public Individual getIndividual(int index) {
        return individuals[index];
    }

    public Individual getFittest() {
        Individual fittest = individuals[0];
        // Loop through individuals to find fittest
        for (int i = 0; i < size(); i++) {
            if (fittest.getFitness() <= getIndividual(i).getFitness()) {
                fittest = getIndividual(i);
            }
        }
        return fittest;
    }

    /* Public methods */
    // Get population size
    public int size() {
        return individuals.length;
    }
    // Save individual
    public void saveIndividual(int index, Individual indiv) {
        individuals[index] = indiv;
    }
}
