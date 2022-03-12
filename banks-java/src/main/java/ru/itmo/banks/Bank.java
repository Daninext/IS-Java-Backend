package ru.itmo.banks;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import javax.mail.MessagingException;
import javax.mail.internet.*;

public class Bank implements CapitalizeListener {
    private final CentralBankImpl centralBank;
    private final GregorianCalendar date = new GregorianCalendar(2021, GregorianCalendar.JANUARY, 1);
    private DynamicPercentages depositPercentages;
    private InternetAddress bankEmail;

    private final DebitEventManager debitEditor = new DebitEventManager();
    private final CreditEventManager creditEditor = new CreditEventManager();

    private List<Client> clients = new ArrayList<>();

    private float debitPercentages;
    private float creditFee;
    private final String name;
    private String smtp;
    private long withdrawLimit = 8000;
    private long transferLimit = 5000;

    public Bank(CentralBankImpl centralBank, String name, DynamicPercentages depositPercentages, float debitPercentages, float creditFee) throws AddressException {
        this.centralBank = centralBank;
        this.name = name;
        this.debitPercentages = debitPercentages;
        this.creditFee = creditFee;
        this.depositPercentages = depositPercentages;

        bankEmail = new InternetAddress("ourbank@bank.com");
    }

    public Client registerClient(RegistrationFormBuilder form) throws SameClientBanksException {
        if (findClient(form.getForm().getPassportNumber()) != null)
            throw new SameClientBanksException("There is same client");

        Client client = new Client(form, this);
        clients.add(client);
        return client;
    }

    public Client findClient(String passportNumber) {
        for (Client client : clients) {
            if (client.getFormBuilder().getForm().getPassportNumber() == passportNumber)
                return client;
        }

        return null;
    }

    public void createDebitAccount(Client client, long startMoney) {
        client.createDebitAccount(debitPercentages, startMoney);
        debitEditor.subscribe(client);
    }

    public void createCreditAccount(Client client, long startMoney) {
        client.createCreditAccount(creditFee, startMoney);
        creditEditor.subscribe(client);
    }

    public void createDepositAccount(Client client, GregorianCalendar endAccountTime, long startMoney) {
        client.createDepositAccount(endAccountTime, depositPercentages, startMoney);
    }

    public void capitalize(GregorianCalendar target) throws PercentNotExistsBanksException {
        while (date.before(target)) {
            date.add(GregorianCalendar.DATE, 1);

            for (Client client : clients) {
                for (Account account : client.getAccounts()) {
                    account.calculateExtraMoney();

                    if (date.get(GregorianCalendar.DATE) == 1)
                        account.capitalize();
                }
            }
        }
    }

    public void withdraw(Account account, long money) throws OutOfLimitBanksException, WithdrawDenyBanksException, InvalidMoneyCountBanksException {
        account.withdrawMoney(money);
        centralBank.makeHistory(account, null, money, new WithdrawTransactionImpl());
    }

    public void deposit(Account account, long money) throws InvalidMoneyCountBanksException {
        account.depositMoney(money);
        centralBank.makeHistory(account, null, money, new DepositTransactionImpl());
    }

    public float getDebitPercentages() {
        return debitPercentages;
    }

    public void setDebitPercentages(float debitPercentages) throws InvalidPercentagesBanksException, MessagingException {
        if (debitPercentages < 0)
            throw new InvalidPercentagesBanksException("Negative percentages");

        this.debitPercentages = debitPercentages;
        debitEditor.notify(debitPercentages);
    }

    public float getCreditFee() {
        return creditFee;
    }

    public void setCreditFee(float creditFee) throws InvalidPercentagesBanksException, MessagingException {
        if (creditFee < 0)
            throw new InvalidPercentagesBanksException("Negative fee");

        this.creditFee = creditFee;
        creditEditor.notify(creditFee);
    }

    public DynamicPercentages getDepositPercentages() {
        return depositPercentages;
    }

    public void setDepositPercentages(@NotNull DynamicPercentages depositPercentages) {
        this.depositPercentages = depositPercentages;
    }

    public long getWithdrawLimit() {
        return withdrawLimit;
    }

    public void setWithdrawLimit(long withdrawLimit) {
        this.withdrawLimit = withdrawLimit;
    }

    public long getTransferLimit() {
        return transferLimit;
    }

    public void setTransferLimit(long transferLimit) {
        this.transferLimit = transferLimit;
    }

    public InternetAddress getBankEmail() {
        return bankEmail;
    }

    public String getSmtp() {
        return smtp;
    }

    public void setSmtp(String smtp) {
        this.smtp = smtp;
    }

    public void setBankEmail(InternetAddress bankEmail) {
        this.bankEmail = bankEmail;
    }

    public String getName() {
        return name;
    }
}
