package ru.itmo.banks;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;

public class CreditEventManager {
    private List<CreditListener> listeners = new ArrayList<>();

    public void subscribe(CreditListener listener) {
        listeners.add(listener);
    }

    public void unsubscribe(CreditListener listener) {
        listeners.remove(listener);
    }

    public void notify(float fee) throws MessagingException {
        for (CreditListener listener : listeners) {
            listener.changeCreditFee(fee);
        }
    }
}
