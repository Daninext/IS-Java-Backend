package ru.itmo.banks;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class CapitalizeEventManager {
    List<CapitalizeListener> listeners = new ArrayList<>();

    public void subscribe(CapitalizeListener listener) {
        listeners.add(listener);
    }

    public void unsubscribe(CapitalizeListener listener) {
        listeners.remove(listener);
    }

    public void notify(GregorianCalendar target) throws PercentNotExistsBanksException {
        for (CapitalizeListener listener : listeners) {
            listener.capitalize(target);
        }
    }
}
