package domain;

import java.time.LocalDate;

//clasa reprezinta un pacient care are
//un id auto-incrementat, nume, prenume, cnp, email, data nasterii
public class Patient {
    private static int counter = 1;
    private final int id;
    private String firstName;
    private String lastName;
    private String cnp;
    private String email;
    private LocalDate dateOfBirth;


    public Patient(String firstName, String lastName, String cnp, String email, LocalDate dateOfBirth) {
        this.id = counter++;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cnp = cnp;
        this.email = email;
        this.dateOfBirth = dateOfBirth;

    }

    //getters si setters
    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCnp() {
        return cnp;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        return String.format("[ID: %d] %s %s | CNP: %s | Email: %s | DOB: %s ",
                id, firstName, lastName, cnp, email, dateOfBirth);
    }
}