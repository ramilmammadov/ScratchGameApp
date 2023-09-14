package org.scratchGame;

import com.google.gson.Gson;
import org.scratchGame.models.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        try {
            Configuration config = readConfigFile();
            String[][] matrix = generateMatrix(config);
            WinCombination appliedCombinations = applyWinningCombinations(matrix, config);
            double reward = calculateReward(appliedCombinations, config);

            List<String> appliedCombinationsForA = appliedCombinations.getAppliedCombinations("A");
            Map<String, List<String>> appliedWinningCombinations = new HashMap<>();
            appliedWinningCombinations.put("A", appliedCombinationsForA);

            OutputJson output = new OutputJson(matrix, reward, appliedWinningCombinations, null);
            applyBonusSymbolLogic(output, config);

            if (reward == 0) {
                output.setApplied_bonus_symbol(null);
            }

            System.out.println(new Gson().toJson(output));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Configuration readConfigFile() throws IOException {
        Gson gson = new Gson();
        ClassLoader classLoader = Main.class.getClassLoader();
        try (Reader reader = new InputStreamReader(Objects.requireNonNull(classLoader.getResourceAsStream("config.json")))) {
            return gson.fromJson(reader, Configuration.class);
        }
    }

    private static String[][] generateMatrix(Configuration config) {
        int columns = config.getColumns();
        int rows = config.getRows();
        String[][] matrix = new String[rows][columns];

        // Implement logic to fill the matrix with symbols based on probabilities
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                List<StandardSymbol> probabilities = config.getProbabilities().getStandardSymbols();
                int randomIndex = (int) (Math.random() * probabilities.size());
                StandardSymbol symbolProbabilities = probabilities.get(randomIndex);
                Map<String, Integer> symbols = symbolProbabilities.getSymbols();
                String symbol = getRandomSymbol(symbols);
                matrix[i][j] = symbol;
            }
        }

        return matrix;
    }

    private static String getRandomSymbol(Map<String, Integer> symbolProbabilities) {
        int totalProbability = symbolProbabilities.values().stream().mapToInt(Integer::intValue).sum();
        int randomValue = (int) (Math.random() * totalProbability);

        for (Map.Entry<String, Integer> entry : symbolProbabilities.entrySet()) {
            randomValue -= entry.getValue();
            if (randomValue <= 0) {
                return entry.getKey();
            }
        }

        return null; // This should not happen if probabilities are correctly configured
    }

    private static WinCombination applyWinningCombinations(String[][] matrix, Configuration config) {
        WinCombination appliedCombinations = new WinCombination();

        for (Map.Entry<String, WinCombination> entry : config.getWin_combinations().entrySet()) {
            String combinationName = entry.getKey();
            WinCombination combination = entry.getValue();

            if (combination.getWhen().equals("same_symbols")) {
                int count = combination.getCount();

                // Check horizontally
                for (String[] strings : matrix) {
                    for (int j = 0; j <= strings.length - count; j++) {
                        String symbol = strings[j];
                        boolean isMatching = true;
                        for (int k = 1; k < count; k++) {
                            if (!strings[j + k].equals(symbol)) {
                                isMatching = false;
                                break;
                            }
                        }
                        if (isMatching) {
                            appliedCombinations.addAppliedCombination(symbol, combinationName);
                        }
                    }
                }

                // Check vertically
                for (int j = 0; j < matrix[0].length; j++) {
                    for (int i = 0; i <= matrix.length - count; i++) {
                        String symbol = matrix[i][j];
                        boolean isMatching = true;
                        for (int k = 1; k < count; k++) {
                            if (!matrix[i + k][j].equals(symbol)) {
                                isMatching = false;
                                break;
                            }
                        }
                        if (isMatching) {
                            appliedCombinations.addAppliedCombination(symbol, combinationName);
                        }
                    }
                }
            }
        }

        return appliedCombinations;
    }

    private static double calculateReward(WinCombination appliedCombinations, Configuration config) {
        int reward = 0;

        for (Map.Entry<String, List<String>> entry : appliedCombinations.getAppliedWinningCombinations().entrySet()) {
            String symbol = entry.getKey();
            List<String> combinations = entry.getValue();
            Symbol symbolConfig = config.getSymbols().get(symbol);
            int rewardMultiplier = (int) symbolConfig.getReward_multiplier();

            for (String combination : combinations) {
                WinCombination combinationConfig = config.getWin_combinations().get(combination);
                double combinationMultiplier = combinationConfig.getReward_multiplier();
                reward += rewardMultiplier * combinationMultiplier;
            }
        }

        return reward;
    }

    private static void applyBonusSymbolLogic(OutputJson output, Configuration config) {
        String[][] matrix = output.getMatrix();

        for (String[] strings : matrix) {
            for (String symbol : strings) {
                Symbol symbolConfig = config.getSymbols().get(symbol);

                if (symbolConfig.getType().equals("bonus")) {
                    String impact = symbolConfig.getImpact();

                    switch (impact) {
                        case "multiply_reward":
                            int rewardMultiplier = (int) symbolConfig.getReward_multiplier();
                            output.setReward(output.getReward() * rewardMultiplier);
                            break;
                        case "extra_bonus":
                            double extraBonus = symbolConfig.getExtra();
                            output.setReward(output.getReward() + extraBonus);
                            break;
                        // case "miss": // Handle if needed
                    }
                    output.setApplied_bonus_symbol(symbol);
                }
            }
        }
    }
}
