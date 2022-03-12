package ru.itmo.banks;

import java.time.LocalDate;
import java.util.GregorianCalendar;

public class DepositAccountImpl implements Account{
    private final Client handler;
    private final RealMoney money = new RealMoney(0);
    private RealMoney moneyForPercentages = new RealMoney(0);
    private final GregorianCalendar endTime;

    private DynamicPercentages percentages;

    public DepositAccountImpl(Client handler, GregorianCalendar endAccountTime, DynamicPercentages percentages, long startMoney) {
        this.handler = handler;
        this.endTime = endAccountTime;
        this.percentages = percentages;
        money.addMoney(money);
    }

    public void depositMoney(float money) throws InvalidMoneyCountBanksException {
        if (money < 0)
            throw new InvalidMoneyCountBanksException("Invalid money count");

        this.money.addMoney(money);
    }

    public void withdrawMoney(float money) throws InvalidMoneyCountBanksException, WithdrawDenyBanksException {
        if (money < 0 || money > this.money.show())
            throw new InvalidMoneyCountBanksException("Invalid money count");
        if ((new GregorianCalendar()).before(endTime))
            throw new WithdrawDenyBanksException("The end date has not yet arrived");

        this.money.addMoney(-money);
    }

    public void transfer(float money, Account account) throws InvalidMoneyCountBanksException, WithdrawDenyBanksException {
        if ((new GregorianCalendar()).before(endTime))
            throw new WithdrawDenyBanksException("The end date has not yet arrived");

        withdrawMoney(money);
        account.depositMoney(money);
    }

    public void capitalize() {
        money.addMoney(moneyForPercentages);
        moneyForPercentages = new RealMoney(0);
    }

    public void calculateExtraMoney() throws PercentNotExistsBanksException {
        moneyForPercentages.addMoney(money.getWholePart() * percentages.getPercent(money.getWholePart()) / LocalDate.now().lengthOfYear() / 100);
    }

    public float showMoney() {
        return  money.show();
    }

    public RealMoney getMoney() {
        return money;
    }

    public DynamicPercentages getPercentages() {
        return percentages;
    }

    public void setPercentages(DynamicPercentages newPercentages) {
        percentages = newPercentages;
    }
}
