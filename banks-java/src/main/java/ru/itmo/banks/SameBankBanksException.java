package ru.itmo.banks;

public class SameBankBanksException extends Exception {
    public SameBankBanksException() { }

    public SameBankBanksException(String message) {
        super(message);
    }

    public SameBankBanksException(String message, Exception innerException) {
        super(message, innerException);
    }
}
