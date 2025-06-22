package exceptions;

// reprezinta o eroare personalizata pentru cazul in care un medic nu este gasit
public class MedicalStaffNotFoundException extends HealthcareException {
    private static final long serialVersionUID = 345678901L;

    // constructor care primeste un mesaj de eroare si il transmite clasei parinte
    public MedicalStaffNotFoundException(String message) {
        super(message);
    }
}