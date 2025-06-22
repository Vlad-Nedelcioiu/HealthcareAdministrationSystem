package tests;

import domain.Billing;
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

import static org.junit.jupiter.api.Assertions.*;

class BillingTest {
    private HealthcareApplication healthcareApp;
    private int patient1Id, patient2Id, staff1Id, staff2Id;

    @BeforeEach
    void setUp() throws PatientAlreadyExistsException, InvalidCNPException, AppointmentConflictException, MedicalStaffNotFoundException {
        healthcareApp = new HealthcareApplication();

        // pacienti
        healthcareApp.registerPatient("John", "Doe", "1234567890123", "john.doe@example.com", LocalDate.of(1990, 1, 1));
        healthcareApp.registerPatient("Jane", "Doe", "1234567890124", "jane.doe@example.com", LocalDate.of(1995, 5, 15));

        // cadru medical
        healthcareApp.registerMedicalStaff("Dr. Smith", "Cardiology");
        healthcareApp.registerMedicalStaff("Dr. Adams", "Neurology");

        // ids pentru pacienti si cadre medicale
        patient1Id = healthcareApp.getAllPatients().get(0).getId();
        patient2Id = healthcareApp.getAllPatients().get(1).getId();
        staff1Id = healthcareApp.getAllMedicalStaff().get(0).getId();
        staff2Id = healthcareApp.getAllMedicalStaff().get(1).getId();

        // programari
        healthcareApp.scheduleAppointment(patient1Id, staff1Id, LocalDateTime.of(2025, 2, 1, 10, 0));
        healthcareApp.scheduleAppointment(patient2Id, staff2Id, LocalDateTime.of(2025, 2, 2, 14, 0));
    }

    @Test
    void testGenerateBillSuccessfully() {
        healthcareApp.generateBill(patient1Id, staff1Id, "Consultation", 200.00);

        List<Billing> bills = healthcareApp.getAllBills();
        assertEquals(1, bills.size(), "There should be exactly one bill.");
        Billing bill = bills.get(0);
        assertEquals(patient1Id, bill.getPatientId(), "The patient ID should match.");
        assertEquals("John Doe", bill.getPatientName(), "The patient name should match.");
        assertEquals("Consultation", bill.getServices(), "The services should match.");
        assertEquals(200.00, bill.getTotalCost(), "The total cost should match.");
    }

    @Test
    void testGenerateBillForInvalidPatientOrStaff() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                healthcareApp.generateBill(999, staff1Id, "Consultation", 200.00)
        );
        assertEquals("Invalid patient or staff ID", exception.getMessage());
    }

    @Test
    void testGenerateBillWithoutAppointment() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                healthcareApp.generateBill(patient1Id, staff2Id, "Consultation", 200.00)
        );
        assertEquals("No appointment found for the given patient and staff", exception.getMessage());
    }

    @Test
    void testGetBillsByPatientId() {
        healthcareApp.generateBill(patient1Id, staff1Id, "Consultation", 200.00);
        healthcareApp.generateBill(patient2Id, staff2Id, "Follow-up", 150.00);

        List<Billing> johnBills = healthcareApp.getBillsByPatientId(patient1Id);
        List<Billing> janeBills = healthcareApp.getBillsByPatientId(patient2Id);

        assertEquals(1, johnBills.size(), "John should have one bill.");
        assertEquals("Consultation", johnBills.get(0).getServices(), "John's service should be 'Consultation'.");

        assertEquals(1, janeBills.size(), "Jane should have one bill.");
        assertEquals("Follow-up", janeBills.get(0).getServices(), "Jane's service should be 'Follow-up'.");
    }

    @Test
    void testGetAllBills() {
        healthcareApp.generateBill(patient1Id, staff1Id, "Consultation", 200.00);
        healthcareApp.generateBill(patient2Id, staff2Id, "Follow-up", 150.00);

        List<Billing> allBills = healthcareApp.getAllBills();
        assertEquals(2, allBills.size(), "There should be exactly two bills.");
    }
}
