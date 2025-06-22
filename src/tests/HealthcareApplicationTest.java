package tests;

import domain.*;
import exceptions.AppointmentConflictException;
import exceptions.InvalidCNPException;
import exceptions.MedicalStaffNotFoundException;
import exceptions.PatientAlreadyExistsException;
import main.HealthcareApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HealthcareApplicationTest {
    private HealthcareApplication healthcareApp;
    private int patient1Id;
    private int staff1Id;
    private LocalDateTime appointmentTime;

    @BeforeEach
    void setUp() throws PatientAlreadyExistsException, InvalidCNPException, AppointmentConflictException, MedicalStaffNotFoundException {
        healthcareApp = new HealthcareApplication();

        // pacient
        healthcareApp.registerPatient("John", "Doe", "1234567890123", "john.doe@example.com", LocalDate.of(1990, 1, 1));

        // doctor
        healthcareApp.registerMedicalStaff("Dr. Smith", "Cardiology");

        // id uri pentru pacienti si cadre medicale
        patient1Id = healthcareApp.getAllPatients().get(0).getId();
        staff1Id = healthcareApp.getAllMedicalStaff().get(0).getId();

        // programare
        appointmentTime = LocalDateTime.of(2025, 2, 1, 10, 0);
        healthcareApp.scheduleAppointment(patient1Id, staff1Id, appointmentTime);
    }

    @Test
    void testRegisterPatient() throws PatientAlreadyExistsException, InvalidCNPException {
        healthcareApp.registerPatient("Jane", "Doe", "1234567890124", "jane.doe@example.com", LocalDate.of(1995, 5, 15));
        List<Patient> patients = healthcareApp.getAllPatients();
        assertEquals(2, patients.size(), "There should be 2 registered patients.");
    }

    @Test
    void testRegisterMedicalStaff() {
        healthcareApp.registerMedicalStaff("Dr. Adams", "Neurology");
        List<MedicalStaff> staff = healthcareApp.getAllMedicalStaff();
        assertEquals(2, staff.size(), "There should be 2 registered doctors.");
    }

    @Test
    void testScheduleAppointment() throws AppointmentConflictException, MedicalStaffNotFoundException {
        List<Appointment> appointments = healthcareApp.getAllAppointments();
        assertEquals(1, appointments.size(), "There should be 1 scheduled appointment.");
        assertEquals("John Doe", appointments.get(0).getPatientFullName(), "The patient name should be John Doe.");
    }

    @Test
    void testGetAppointmentsByDoctor() {
        List<Appointment> doctorAppointments = healthcareApp.getAppointmentsByDoctor("Dr. Smith");
        assertEquals(1, doctorAppointments.size(), "There should be 1 appointment for Dr. Smith.");
    }

    @Test
    void testGetAppointmentsByDate() {
        List<Appointment> appointmentsOnDate = healthcareApp.getAppointmentsByDate(appointmentTime.toLocalDate());
        assertEquals(1, appointmentsOnDate.size(), "There should be 1 appointment on the specified date.");
    }

    @Test
    void testUpdatePatientEmail() {
        healthcareApp.updatePatientEmail(patient1Id, "john.new@example.com");
        assertEquals("john.new@example.com", healthcareApp.getAllPatients().get(0).getEmail(), "The patient's email should be updated.");
    }

    @Test
    void testDeletePatient() {
        healthcareApp.deletePatient(patient1Id);
        assertTrue(healthcareApp.getAllPatients().isEmpty(), "The patient should be deleted.");
    }

    @Test
    void testDeleteMedicalStaff() {
        healthcareApp.deleteMedicalStaff(staff1Id);
        assertTrue(healthcareApp.getAllMedicalStaff().isEmpty(), "The doctor should be deleted.");
    }

    @Test
    void testCancelAppointment() {
        healthcareApp.cancelAppointment(1);
        assertTrue(!healthcareApp.getAllAppointments().isEmpty(), "The appointment should be canceled.");
    }

    @Test
    void testGenerateBill() {
        healthcareApp.generateBill(patient1Id, staff1Id, "Consultation", 200.00);
        assertEquals(1, healthcareApp.getAllBills().size(), "There should be one generated bill.");
    }

    @Test
    void testGetBillsByPatientId() {
        healthcareApp.generateBill(patient1Id, staff1Id, "Consultation", 200.00);
        List<Billing> bills = healthcareApp.getBillsByPatientId(patient1Id);
        assertEquals(1, bills.size(), "The patient should have 1 bill.");
    }

    @Test
    void testAddMedicalRecord() {
        healthcareApp.addMedicalRecord(patient1Id, "Hypertension", "Lifestyle changes", "Blood pressure medication", appointmentTime, "Dr. Smith");
        List<MedicalRecord> records = healthcareApp.getMedicalRecordsByPatientId(patient1Id);
        assertEquals(1, records.size(), "The patient should have 1 medical record.");
    }

    @Test
    void testGetMedicalRecordsByPatientId() {
        healthcareApp.addMedicalRecord(patient1Id, "Hypertension", "Lifestyle changes", "Blood pressure medication", appointmentTime, "Dr. Smith");
        healthcareApp.addMedicalRecord(patient1Id, "Diabetes", "Diet management", "Insulin", appointmentTime.plusMonths(1), "Dr. Smith");
        List<MedicalRecord> records = healthcareApp.getMedicalRecordsByPatientId(patient1Id);
        assertEquals(2, records.size(), "The patient should have 2 medical records.");
    }
}

