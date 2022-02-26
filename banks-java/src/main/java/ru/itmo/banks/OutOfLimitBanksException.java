package ru.itmo.banks;

public class OutOfLimitBanksException extends Exception {
    public OutOfLimitBanksException() { }

    public OutOfLimitBanksException(String message) {
        super(message);
    }

    public OutOfLimitBanksException(String message, Exception innerException) {
        super(message, innerException);
    }
}
