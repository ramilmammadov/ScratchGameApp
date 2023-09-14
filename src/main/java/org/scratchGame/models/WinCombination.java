package org.scratchGame.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WinCombination {
    private double reward_multiplier;
    private int count;
    private String group;
    private List<List<String>> covered_areas;
    private String when;
    private Map<String, List<String>> applied_winning_combinations = new HashMap<>();

    public double getReward_multiplier() {
        return reward_multiplier;
    }


    public int getCount() {
        return count;
    }

    public String getWhen() {
        return when;
    }


    public void addAppliedCombination(String symbol, String combination) {
        applied_winning_combinations.computeIfAbsent(symbol, k -> new ArrayList<>()).add(combination);
    }

    public Map<String, List<String>> getAppliedWinningCombinations() {
        return applied_winning_combinations;
    }

    public List<String> getAppliedCombinations(String symbol) {
        return applied_winning_combinations.get(symbol);
    }

}

