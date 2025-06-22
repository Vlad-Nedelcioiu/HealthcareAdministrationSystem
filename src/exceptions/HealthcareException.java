package exceptions;

// clasa este o exceptie de baza pentru toate erorile din sistemul medical
public class HealthcareException extends Exception {
    private static final long serialVersionUID = 123456789L;

    // constructor care primeste un mesaj de eroare si il transmite clasei parinte (Exception)
    public HealthcareException(String message) {
        super(message);
    }
}
