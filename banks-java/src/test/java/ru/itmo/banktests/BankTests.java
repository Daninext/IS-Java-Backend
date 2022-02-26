package ru.itmo.banktests;

import org.junit.*;
import ru.itmo.banks.*;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

public class BankTests {
    private CentralBank centralBank;

    @Before
    public void setUp() {
        centralBank = new CentralBankImpl();
    }

    @Test
    public void addNewClientToNewBank() throws SameBankBanksException, AddressException, SameClientBanksException {
        Bank bank = centralBank.registerBank("Sberbank", new DynamicPercentages(), 1, 1);
        Client client = bank.registerClient(new RegistrationFormBuilder("Daniil", "Arsentev", "123456"));
        client.getFormBuilder().addAddress("Hell");

        assertEquals(bank, centralBank.findBank("Sberbank"));
        assertEquals(client, bank.findClient("123456"));
    }

    @Test
    public void registerNewAccount_AndWaitOneYear() throws SameClientBanksException, SameBankBanksException, MessagingException, InvalidPercentagesBanksException, PercentNotExistsBanksException {
        Bank bank = centralBank.registerBank("Sberbank", new DynamicPercentages(), 1, 1);
        Client client = bank.registerClient(new RegistrationFormBuilder("Daniil", "Arsentev", "123456"));

        bank.createDebitAccount(client, 100000);
        bank.setDebitPercentages(3.65f);

        GregorianCalendar temp = new GregorianCalendar(2021, GregorianCalendar.JANUARY, 1);
        temp.add(GregorianCalendar.YEAR, 1);
        centralBank.capitalize(temp);

        assertEquals(103704, client.getAccounts().get(0).getMoney().getWholePart());
    }

    @Test
    public void transferMoney_AndCancelIt() throws SameBankBanksException, AddressException, SameClientBanksException, OutOfLimitBanksException, WithdrawDenyBanksException, InvalidMoneyCountBanksException {
        Bank bank = centralBank.registerBank("Sberbank", new DynamicPercentages(), 1, 1);
        Client client = bank.registerClient(new RegistrationFormBuilder("Daniil", "Arsentev", "123456"));

        bank.createDebitAccount(client, 100000);

        Client client2 = bank.registerClient(new RegistrationFormBuilder("Artem", "Tutov", "1123226"));

        bank.createDebitAccount(client2, 1000);

        centralBank.transactionMoney(client.getAccounts().get(0), client2.getAccounts().get(0), 5000);
        assertEquals(6000, client2.getAccounts().get(0).getMoney().getWholePart());

        centralBank.cancelLastTransaction(client.getAccounts().get(0));
        assertEquals(1000, client2.getAccounts().get(0).getMoney().getWholePart());
    }

    @Test
    public void withdrawMoreThanHave_CatchException() throws SameBankBanksException, AddressException, SameClientBanksException, InvalidRegDataBanksException {
        Bank bank = centralBank.registerBank("Sberbank", new DynamicPercentages(), 1, 1);
        Client client = bank.registerClient(new RegistrationFormBuilder("Daniil", "Arsentev", "123456"));
        client.getFormBuilder().addAddress("Hell");
        client.getFormBuilder().addTelephone("+796464665");

        bank.createDebitAccount(client, 1000);
        assertThrows(InvalidMoneyCountBanksException.class, () -> {
            bank.withdraw(client.getAccounts().get(0), 10000);
        });

        Client client2 = bank.registerClient(new RegistrationFormBuilder("Artem", "Tutov", "1123226"));

        bank.createDebitAccount(client2, 10000);

        assertThrows(OutOfLimitBanksException.class, () -> {
            bank.withdraw(client2.getAccounts().get(0), 10000);
        });
    }
}
