package org.scratchGame.models;

public class Symbol {
    private double reward_multiplier;
    private String type;
    private Double extra; // Only for bonuses
    private String impact; // Only for bonuses

    public double getReward_multiplier() {
        return reward_multiplier;
    }

    public String getType() {
        return type;
    }

    public Double getExtra() {
        return extra;
    }

    public String getImpact() {
        return impact;
    }
}