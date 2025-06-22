package domain;

import java.io.Serializable;
import java.time.LocalDateTime;

//clasa reprezinta o factura medicala pentru pacient
public class Billing implements Serializable {
    private static final long serialVersionUID = 1L;
    private int patientId;
    private String patientName;
    private String services;
    private double totalCost;
    private LocalDateTime appointmentDate;

    public Billing(int patientId, String patientName, String services, double totalCost, LocalDateTime appointmentDate) {
        this.patientId = patientId;
        this.patientName = patientName;
        this.services = services;
        this.totalCost = totalCost;
        this.appointmentDate = appointmentDate;
    }

    // getters si setters
    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    @Override
    public String toString() {
        return "Billing [Patient ID=" + patientId + ", Patient Name=" + patientName + ", Services=" + services +
                ", Total Cost=" + totalCost + ", Appointment Date=" + appointmentDate + "]";
    }
}
