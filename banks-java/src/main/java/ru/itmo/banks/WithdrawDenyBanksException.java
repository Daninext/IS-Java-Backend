package ru.itmo.banks;

public class WithdrawDenyBanksException extends RuntimeException {
    public WithdrawDenyBanksException() { }

    public WithdrawDenyBanksException(String message) {
        super(message);
    }

    public WithdrawDenyBanksException(String message, Exception innerException) {
        super(message, innerException);
    }
}
