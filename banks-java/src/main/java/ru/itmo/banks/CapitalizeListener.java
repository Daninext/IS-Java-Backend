package ru.itmo.banks;

import java.util.GregorianCalendar;

public interface CapitalizeListener {
    void capitalize(GregorianCalendar target) throws PercentNotExistsBanksException;
}
