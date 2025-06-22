package domain;

//clasa reprezinta un personal medical din sistem
//stocheaza info despre nume si specializare
public class MedicalStaff {
    private static int counter = 1;
    private final int id;
    private String name;
    private String specialty;

    public MedicalStaff(String name, String specialty) {
        this.id = counter++; //autoincrement
        this.name = name;
        this.specialty = specialty;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    @Override
    public String toString() {
        return String.format("[ID: %d] %s | Specialty: %s", id, name, specialty);
    }
}