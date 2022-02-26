package ru.itmo.banks;

import org.jetbrains.annotations.NotNull;
import javax.mail.internet.*;

public class RegistrationForm {
    private String firstName;
    private String secondName;
    private String passportNumber;
    private String telephone = null;
    private String address = null;
    private InternetAddress email = null;

    public RegistrationForm(String firstName, String secondName, String passportNumber) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.passportNumber = passportNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String value) throws InvalidRegDataBanksException {
        for (char ch : value.toCharArray()) {
            if (!Character.isLetter(ch))
                throw new InvalidRegDataBanksException("Invalid first name");
        }

        firstName = value;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String value) throws InvalidRegDataBanksException {
        for (char ch : value.toCharArray()) {
            if (!Character.isLetter(ch))
                throw new InvalidRegDataBanksException("Invalid second name");
        }

        secondName = value;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String value) throws InvalidRegDataBanksException {
        for (char ch : value.toCharArray()) {
            if (!Character.isDigit(ch))
                throw new InvalidRegDataBanksException("Invalid passport number");
        }

        passportNumber = value;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(@NotNull String value) throws InvalidRegDataBanksException {
        if (value.toCharArray()[0] == '+')
            value = value.replaceFirst("\\+", "");

        for (char ch : value.toCharArray()) {
            if (!Character.isDigit(ch))
                throw new InvalidRegDataBanksException("Invalid telephone number");
        }

        telephone = value;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public InternetAddress getEmail() {
        return email;
    }

    public void setEmail(String email) throws AddressException {
        this.email = new InternetAddress(email);
    }

    public boolean isConfirmed() {
        return telephone != null && address != null;
    }
}
