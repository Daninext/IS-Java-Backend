package ru.itmo.banks;

import javax.mail.MessagingException;

public interface CreditListener {
    void changeCreditFee(float fee) throws MessagingException;
}
