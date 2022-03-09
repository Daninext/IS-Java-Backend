package ru.itmo.banks;

import java.time.LocalDate;

public class DebitAccountImpl implements Account {
    private final Client handler;
    private final RealMoney money = new RealMoney(0);
    private RealMoney moneyForPercentages = new RealMoney(0);

    private float percentages;

    public DebitAccountImpl(Client handler, float percentages, float startMoney) {
        this.handler = handler;
        this.percentages = percentages;
        money.addMoney(startMoney);
    }

    public void depositMoney(float money) throws InvalidMoneyCountBanksException {
        if (money < 0)
            throw new InvalidMoneyCountBanksException("Invalid money count");

        this.money.addMoney(money);
    }

    public void withdrawMoney(float money) throws InvalidMoneyCountBanksException, OutOfLimitBanksException {
        if (money < 0 || money > this.money.show())
            throw new InvalidMoneyCountBanksException("Invalid money count");
        if (!handler.getFormBuilder().getForm().isConfirmed() && money > handler.getBank().getWithdrawLimit())
            throw  new OutOfLimitBanksException("Out of withdraw limit");

        this.money.addMoney(-money);
    }

    public void transfer(float money, Account account) throws OutOfLimitBanksException, InvalidMoneyCountBanksException {
        if (!handler.getFormBuilder().getForm().isConfirmed() && money > handler.getBank().getTransferLimit())
            throw  new OutOfLimitBanksException("Out of transfer limit");

        withdrawMoney(money);
        account.depositMoney(money);
    }

    public void capitalize() {
        money.addMoney(moneyForPercentages);
        moneyForPercentages = new RealMoney(0);
    }

    public void calculateExtraMoney() {
        float temp = money.getWholePart() * percentages / LocalDate.now().lengthOfYear() / 100;
        moneyForPercentages.addMoney(temp);
    }

    public float showMoney() {
        return  money.show();
    }

    public RealMoney getMoney() {
        return money;
    }

    public float getPercentages() {
        return percentages;
    }

    public void setPercentages(float newPercentages) {
        percentages = newPercentages;
    }
}
