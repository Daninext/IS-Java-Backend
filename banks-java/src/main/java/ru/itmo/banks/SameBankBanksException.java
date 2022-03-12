package ru.itmo.banks;

public class SameBankBanksException extends RuntimeException {
    public SameBankBanksException() { }

    public SameBankBanksException(String message) {
        super(message);
    }

    public SameBankBanksException(String message, Exception innerException) {
        super(message, innerException);
    }
}
