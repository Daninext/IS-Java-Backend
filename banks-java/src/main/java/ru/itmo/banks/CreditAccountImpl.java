package ru.itmo.banks;

import java.time.LocalDate;

public class CreditAccountImpl implements Account{
    private final Client handler;
    private final RealMoney money = new RealMoney(0);
    private RealMoney moneyForPercentages = new RealMoney(0);

    private float fee;

    public CreditAccountImpl(Client handler, float fee, float startMoney) {
        this.handler = handler;
        this.fee = fee;
        money.addMoney(startMoney);
    }

    public void depositMoney(float money) throws InvalidMoneyCountBanksException {
        if (money < 0)
            throw new InvalidMoneyCountBanksException("Invalid money count");

        this.money.addMoney(money);
    }

    public void withdrawMoney(float money) throws InvalidMoneyCountBanksException {
        if (money < 0)
            throw new InvalidMoneyCountBanksException("Invalid money count");

        this.money.addMoney(-money);
    }

    public void transfer(float money, Account account) throws InvalidMoneyCountBanksException {
        withdrawMoney(money);
        account.depositMoney(money);
    }

    public void capitalize() {
        money.addMoney(moneyForPercentages);
        moneyForPercentages = new RealMoney(0);
    }

    public void calculateExtraMoney() {
        if (money.show() > 0)
            return;

        moneyForPercentages.addMoney(money.getWholePart() * fee / LocalDate.now().lengthOfYear() / 100);
    }

    public float showMoney() {
        return  money.show();
    }

    public RealMoney getMoney() {
        return money;
    }

    public float getFee() {
        return fee;
    }

    public void setFee(float newFee) {
        fee = newFee;
    }
}
