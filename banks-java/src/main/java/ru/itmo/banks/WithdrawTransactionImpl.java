package ru.itmo.banks;

public class WithdrawTransactionImpl implements Transaction {
    public void cancelTransaction(AccountTransaction transaction) {
        transaction.getOutAccount().getMoney().addMoney(transaction.getMoney());
    }
}
