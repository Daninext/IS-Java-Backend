package ru.itmo.banks;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;

public class DebitEventManager {
    private List<DebitListener> listeners = new ArrayList<>();

    public void subscribe(DebitListener listener) {
        listeners.add(listener);
    }

    public void unsubscribe(DebitListener listener) {
        listeners.remove(listener);
    }

    public void notify(float percentage) throws MessagingException {
        for (DebitListener listener : listeners) {
            listener.changeDebitPercentages(percentage);
        }
    }
}
