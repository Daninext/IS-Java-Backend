package ru.itmo.banks;

public class TransferTransactionImpl implements Transaction{
    public void cancelTransaction(AccountTransaction transaction) {
        transaction.getOutAccount().getMoney().addMoney(transaction.getMoney());
        transaction.getToAccount().getMoney().addMoney(-transaction.getMoney());
    }
}
