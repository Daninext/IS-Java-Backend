package ru.itmo.banks;

import javax.mail.MessagingException;

public interface DebitListener {
    void changeDebitPercentages(float percentages) throws MessagingException;
}
