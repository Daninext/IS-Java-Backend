package ru.itmo.banks;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class NotificationByEMailImpl implements Notification {
    public void notify(Client client, String topic, String body) throws MessagingException {
        if (client.getFormBuilder().getForm().getEmail() != null) {
            Properties properties = System.getProperties();
            properties.setProperty(client.getBank().getSmtp(), "localhost");

            MimeMessage message = new MimeMessage(Session.getDefaultInstance(properties));
            message.setFrom(client.getBank().getBankEmail());
            message.addRecipients(Message.RecipientType.TO, new InternetAddress[]{client.getFormBuilder().getForm().getEmail()});
            message.setSubject(topic);
            message.setText(body);

            Transport.send(message);
        }
    }
}
