package org.scratchGame.models;

import java.util.Map;

public class Configuration {
    private Integer columns;
    private Integer rows;
    private Map<String, Symbol> symbols;
    private Probabilities probabilities;
    private Map<String, WinCombination> win_combinations;

    public Integer getColumns() {
        return columns;
    }

    public Integer getRows() {
        return rows;
    }


    public Map<String, Symbol> getSymbols() {
        return symbols;
    }
    public Probabilities getProbabilities() {
        return probabilities;
    }


    public Map<String, WinCombination> getWin_combinations() {
        return win_combinations;
    }

}
