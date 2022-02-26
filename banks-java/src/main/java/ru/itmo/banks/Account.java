package ru.itmo.banks;

public interface Account {
    void depositMoney(float money) throws InvalidMoneyCountBanksException;

    void withdrawMoney(float money) throws InvalidMoneyCountBanksException, OutOfLimitBanksException, WithdrawDenyBanksException;

    void transfer(float money, Account account) throws OutOfLimitBanksException, InvalidMoneyCountBanksException, WithdrawDenyBanksException;

    void capitalize();

    void calculateExtraMoney() throws PercentNotExistsBanksException;

    float showMoney();

    RealMoney getMoney();
}
