package ru.itmo.banks;

import javax.mail.internet.AddressException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class CentralBankImpl implements CentralBank{
    private List<Bank> banks = new ArrayList<>();
    private List<AccountTransaction> transactions = new ArrayList<>();

    private final CapitalizeEventManager capitalizeEditor = new CapitalizeEventManager();

    public Bank registerBank(String name, DynamicPercentages depositPercentages, float debitPercentages, float creditPercentages) throws AddressException, SameBankBanksException {
        if (findBank(name) == null) {
            Bank bank = new Bank(this, name, depositPercentages, debitPercentages, creditPercentages);
            banks.add(bank);
            capitalizeEditor.subscribe(bank);
            return bank;
        }

        throw new SameBankBanksException("There is same Bank");
    }

    public Bank findBank(String name) {
        for (Bank bank : banks) {
            if (bank.getName() == name)
                return bank;
        }

        return null;
    }

    public void capitalize(GregorianCalendar target) throws PercentNotExistsBanksException {
        capitalizeEditor.notify(target);
    }

    public void transactionMoney(Account outAccount, Account toAccount, float money) throws OutOfLimitBanksException, WithdrawDenyBanksException, InvalidMoneyCountBanksException {
        outAccount.transfer(money, toAccount);
        makeHistory(outAccount, toAccount, money, new TransferTransactionImpl());
    }

    public void makeHistory(Account outAccount, Account toAccount, float money, Transaction transaction) {
        transactions.add(new AccountTransaction(transaction, outAccount, toAccount, money));
    }

    public void cancelLastTransaction(Account account) {
        for (int i = transactions.size() - 1; i != -1; --i) {
            if (transactions.get(i).getOutAccount() == account || transactions.get(i).getToAccount() == account) {
                transactions.get(i).getTransaction().cancelTransaction(transactions.get(i));

                transactions.remove(i);
                break;
            }
        }
    }
}
