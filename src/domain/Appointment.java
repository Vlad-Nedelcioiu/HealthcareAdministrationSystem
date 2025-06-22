package domain;

import java.time.LocalDateTime;

//clasa appointment reprezinta o programare medicala intre pacient si medic
public class Appointment {
    private static int counter = 1;
    private final int id;
    private String patientFullName;
    private String doctorName;
    private LocalDateTime appointmentDate;

    public Appointment(String patientFullName, String doctorName, LocalDateTime appointmentDate) {
        this.id = counter++; //autoincrement
        this.patientFullName = patientFullName;
        this.doctorName = doctorName;
        this.appointmentDate = appointmentDate;
    }

    //getters si setters
    public int getId() {
        return id;
    }

    public String getPatientFullName() {
        return patientFullName;
    }

    public void setPatientFullName(String patientFullName) {
        this.patientFullName = patientFullName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    @Override
    public String toString() {
        return String.format("[ID: %d] Appointment with Dr. %s and %s on %s",
                id, doctorName, patientFullName, appointmentDate);
    }
}