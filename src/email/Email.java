package email;

import domain.MedicalStaff;
import domain.Patient;

import java.io.Serializable;
import java.util.ArrayList;

public class Email implements Serializable {
    private static final long serialVersionUID = -3686472195559526951L;
    private MedicalStaff from;
    private ArrayList<Patient> to, copy;
    private String title, body;

    public MedicalStaff getFrom() {
        return from;
    }


    public Email setFrom(MedicalStaff from) {
        this.from = from;
        return this;
    }

    // metoda pentru a obtine lista destinatarilor principali
    public ArrayList<Patient> getTo() {
        return to;
    }

    // metoda pentru a seta lista destinatarilor principali si a returna obiectul actualizat
    public Email setTo(ArrayList<Patient> to) {
        this.to = to;
        return this;
    }

    // metoda alternativa pentru a seta un singur destinatar principal
    public Email setTo(Patient to) {
        ArrayList<Patient> toList = new ArrayList<>();
        toList.add(to);
        setTo(toList);
        return this;
    }

    public ArrayList<Patient> getCopy() {
        return copy;
    }

    public Email setCopy(ArrayList<Patient> copy) {
        this.copy = copy;
        return this;
    }

    // metoda alternativa pentru a seta un singur CC
    public Email setCopy(Patient copy) {
        ArrayList<Patient> copyList = new ArrayList<>();
        copyList.add(copy);
        setCopy(copyList);
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Email setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getBody() {
        return body;
    }

    public Email setBody(String body) {
        this.body = body;
        return this;
    }

    @Override
    public String toString() {
        return "SEND EMAIL:\n" +
                "From: " + getFrom() + "\n" +
                "To: " + getTo() + "\n" +
                "Copy: " + getCopy() + "\n" +
                "Title: " + getTitle() + "\n" +
                "Body: " + getBody() + "\n";
    }
}
