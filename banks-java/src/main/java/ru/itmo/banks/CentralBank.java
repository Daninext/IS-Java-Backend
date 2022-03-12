package ru.itmo.banks;

import javax.mail.internet.AddressException;
import java.util.GregorianCalendar;

public interface CentralBank {
    Bank registerBank(String name, DynamicPercentages depositPercentages, float debitPercentages, float creditFee) throws AddressException, SameBankBanksException;

    Bank findBank(String name);

    void capitalize(GregorianCalendar target)  throws PercentNotExistsBanksException;

    void transactionMoney(Account outAccount, Account toAccount, float money) throws OutOfLimitBanksException, WithdrawDenyBanksException, InvalidMoneyCountBanksException;

    void makeHistory(Account outAccount, Account toAccount, float money, Transaction transaction);

    void cancelLastTransaction(Account account);
}
