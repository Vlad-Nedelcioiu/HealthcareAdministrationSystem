package exceptions;

// reprezinta o eroare personalizata pentru CNP-uri invalide
public class InvalidCNPException extends HealthcareException {
    private static final long serialVersionUID = 567890123L;

    // constructor care primeste un mesaj de eroare si il transmite clasei parinte
    public InvalidCNPException(String message) {
        super(message);
    }
}