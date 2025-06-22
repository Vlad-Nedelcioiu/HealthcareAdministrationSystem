package domain;

import java.io.Serializable;
import java.time.LocalDateTime;

//clasa reprezinta fisa medicala a pacientului
//salveaza info despre diagnostic, tratament, medicamente, data programarii si numele doctorului
public class MedicalRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    private String diagnosis;
    private String treatment;
    private String medications;
    private LocalDateTime appointmentDate;
    private String doctorName;

    public MedicalRecord(String diagnosis, String treatment, String medications, LocalDateTime appointmentDate, String doctorName) {
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.medications = medications;
        this.appointmentDate = appointmentDate;
        this.doctorName = doctorName;
    }

    //getters si setters
    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getMedications() {
        return medications;
    }

    public void setMedications(String medications) {
        this.medications = medications;
    }

    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    @Override
    public String toString() {
        return "MedicalRecord{" +
                "diagnosis='" + diagnosis + '\'' +
                ", treatment='" + treatment + '\'' +
                ", medications='" + medications + '\'' +
                ", appointmentDate=" + appointmentDate +
                ", doctorName='" + doctorName + '\'' +
                '}';
    }
}
