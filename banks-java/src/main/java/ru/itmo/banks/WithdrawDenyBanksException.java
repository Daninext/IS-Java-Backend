package ru.itmo.banks;

public class WithdrawDenyBanksException extends Exception {
    public WithdrawDenyBanksException() { }

    public WithdrawDenyBanksException(String message) {
        super(message);
    }

    public WithdrawDenyBanksException(String message, Exception innerException) {
        super(message, innerException);
    }
}
