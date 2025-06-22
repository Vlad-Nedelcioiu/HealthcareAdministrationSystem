package exceptions;

// clasa reprezinta o eroare personalizata pentru conflicte de programare
public class AppointmentConflictException extends HealthcareException {
    private static final long serialVersionUID = 456789012L;

    // constructor care primeste un mesaj de eroare si il transmite clasei parinte
    public AppointmentConflictException(String message) {
        super(message);
    }
}
