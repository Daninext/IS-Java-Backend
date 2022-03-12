package ru.itmo.banks;

import javax.mail.internet.AddressException;

public class RegistrationFormBuilder {
    private final RegistrationForm form;

    public RegistrationFormBuilder(String firstName, String secondName, String passportNumber) {
        form = new RegistrationForm(firstName, secondName, passportNumber);
    }

    public void addTelephone(String telephoneNumber) throws InvalidRegDataBanksException {
        form.setTelephone(telephoneNumber);
    }

    public void addAddress(String address) {
        form.setAddress(address);
    }

    public void addEmail(String email) throws AddressException {
        form.setEmail(email);
    }

    public RegistrationForm getForm() {
        return form;
    }
}
