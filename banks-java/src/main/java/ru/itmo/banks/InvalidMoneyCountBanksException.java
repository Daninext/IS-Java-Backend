package ru.itmo.banks;

public class InvalidMoneyCountBanksException extends RuntimeException {
    public InvalidMoneyCountBanksException() { }

    public InvalidMoneyCountBanksException(String message) {
        super(message);
    }

    public InvalidMoneyCountBanksException(String message, Exception innerException) {
        super(message, innerException);
    }
}
