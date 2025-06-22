package exceptions;

// clasa reprezinta o eroare personalizata pentru cazul in care un pacient exista deja in sistem
public class PatientAlreadyExistsException extends HealthcareException {
    private static final long serialVersionUID = 234567890L; 

    // constructor care primeste un mesaj de eroare si il transmite clasei parinte
    public PatientAlreadyExistsException(String message) {
        super(message);
    }
}
