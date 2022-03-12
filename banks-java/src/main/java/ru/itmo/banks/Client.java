package ru.itmo.banks;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

public class Client implements CreditListener, DebitListener {
    private List<Account> accounts = new ArrayList<>();
    private Notification notification = new NotificationByEMailImpl();
    private final RegistrationFormBuilder formBuilder;
    private final Bank mainBank;

    public Client(RegistrationFormBuilder form, Bank bank) {
        formBuilder = form;
        mainBank = bank;
    }

    public void createDebitAccount(float percentages, long startMoney) {
        accounts.add(new DebitAccountImpl(this, percentages, startMoney));
    }

    public void createCreditAccount(float fee, long startMoney) {
        accounts.add(new CreditAccountImpl(this, fee, startMoney));
    }

    public void createDepositAccount(GregorianCalendar endAccountTime, DynamicPercentages percentages, long startMoney) {
        accounts.add(new DepositAccountImpl(this, endAccountTime, percentages, startMoney));
    }

    public void changeDebitPercentages(float percentages) throws MessagingException {
        for (Account account : accounts) {
            if (account.getClass() == DebitAccountImpl.class) {
                ((DebitAccountImpl) account).setPercentages(percentages);
            }
        }

        notification.notify(this, "This is a percentages change notification", "We have changed percentages on your debit accounts. Now it is " + percentages);
    }

    public void changeCreditFee(float fee) throws MessagingException {
        for (Account account : accounts) {
            if (account.getClass() == CreditAccountImpl.class) {
                ((CreditAccountImpl) account).setFee(fee);
            }
        }

        notification.notify(this, "This is a fee change notification", "We have changed fee on your credit accounts. Now it is " + fee);
    }

    public void changeNotifyType(Notification notification) {
        this.notification = notification;
    }

    public List<Account> getAccounts() {
        return Collections.unmodifiableList(accounts);
    }

    public RegistrationFormBuilder getFormBuilder() {
        return formBuilder;
    }

    public Bank getBank() {
        return  mainBank;
    }
}
