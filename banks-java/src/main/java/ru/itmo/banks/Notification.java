package ru.itmo.banks;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

public interface Notification {
    void notify(Client client, String topic, String body) throws MessagingException;
}
