package ru.itmo.banks;

import java.util.HashMap;
import java.util.Map;

public class DynamicPercentages {
    private final Map<Float, Float> dynamicPercentages = new HashMap<>();

    public void addPercent(float moneyValue, float percent) {
        for (Map.Entry<Float, Float> pair : dynamicPercentages.entrySet()) {
            if (pair.getKey() == moneyValue) {
                pair.setValue(percent);
                return;
            }
        }

        dynamicPercentages.put(moneyValue, percent);
    }

    public void DeletePercent(float moneyValue) {
        dynamicPercentages.remove(moneyValue);
    }

    public float getPercent(float moneyValue) throws PercentNotExistsBanksException {
        float tempPercent = -1;
        for (Map.Entry<Float, Float> pair : dynamicPercentages.entrySet()) {
            if (pair.getKey() <= moneyValue)
                tempPercent = pair.getValue();
        }

        if (tempPercent == -1)
            throw new PercentNotExistsBanksException("There isn`t same percentages");

        return tempPercent;
    }
}
