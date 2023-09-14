package org.scratchGame.models;

import java.util.List;
import java.util.Map;

public class OutputJson {
    private String[][] matrix;
    private double reward;
    private Map<String, List<String>> applied_winning_combinations;
    private String applied_bonus_symbol;

    public OutputJson(String[][] matrix, double reward, Map<String, List<String>> applied_winning_combinations, String applied_bonus_symbol) {
        this.matrix = matrix;
        this.reward = reward;
        this.applied_winning_combinations = applied_winning_combinations;
        this.applied_bonus_symbol = applied_bonus_symbol;
    }

    public String[][] getMatrix() {
        return matrix;
    }

    public double getReward() {
        return reward;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }

    public Map<String, List<String>> getApplied_winning_combinations() {
        return applied_winning_combinations;
    }


    public void setApplied_bonus_symbol(String applied_bonus_symbol) {
        this.applied_bonus_symbol = applied_bonus_symbol;
    }
}
