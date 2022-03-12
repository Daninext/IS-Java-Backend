package ru.itmo.banks;

import org.jetbrains.annotations.NotNull;

public class RealMoney {
    private int fractionalPart;
    private long wholePart;

    public RealMoney(float money) {
        addMoney(money);
    }

    public RealMoney(RealMoney money) {
        addMoney(money);
    }

    public void addMoney(float money) {
        if (money != 0) {
            long temp = (long) Math.floor(money);
            wholePart += temp;
            setFractionalPart(fractionalPart + (int) ((money * 1000) - temp * 1000));
        }
    }

    public void addMoney(@NotNull RealMoney money) {
        wholePart += money.wholePart;
        setFractionalPart(fractionalPart + money.fractionalPart);
    }

    public float show() {
        return (wholePart + fractionalPart);
    }

    public int getFractionalPart() {
        return fractionalPart;
    }

    public long getWholePart() {
        return wholePart;
    }

    private void setFractionalPart(int value) {
        fractionalPart = value;

        if (fractionalPart >= 1000) {
            wholePart += fractionalPart / 1000;
            fractionalPart /= 1000;
        } else if (fractionalPart < 0) {
            --wholePart;
            fractionalPart += 1000;
        }
    }
}
