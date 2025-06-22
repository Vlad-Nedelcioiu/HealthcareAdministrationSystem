package report;

import domain.Billing;
import main.HealthcareApplication;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// generarea de rapoarte pe baza facturilor
public class BillingReportStreams {

    // calculeaza venitul total din toate facturile
    public double getTotalRevenue(HealthcareApplication healthcareApp) {
        return healthcareApp.getAllBills().stream()
                .mapToDouble(Billing::getTotalCost)
                .sum();
    }

    // calculeaza venitul total per pacient
    public Map<String, Double> getRevenuePerPatient(HealthcareApplication healthcareApp) {
        return healthcareApp.getAllBills().stream()
                .collect(Collectors.groupingBy
                        (Billing::getPatientName,
                                Collectors.summingDouble(Billing::getTotalCost)));
    }

    // metoda determina serviciile facturate cel mai des
    public Map<String, Long> getMostFrequentServices(HealthcareApplication healthcareApp) {
        return healthcareApp.getAllBills().stream()
                .collect(Collectors.groupingBy
                        (Billing::getServices,
                                Collectors.counting()));
    }

    // metoda calculeaza venitul pe fiecare data a programarii
    public Map<LocalDate, Double> getRevenuePerDate(HealthcareApplication healthcareApp) {
        return healthcareApp.getAllBills().stream()
                .collect(Collectors.groupingBy
                        (bill -> bill.getAppointmentDate().toLocalDate(),
                                Collectors.summingDouble(Billing::getTotalCost)));
    }

    // metoda care returneaza lista cu pacientii care au platit cel mai mult
    public List<Map.Entry<String, Double>> getTopPayingPatients(HealthcareApplication healthcareApp, int topN) {
        return getRevenuePerPatient(healthcareApp)
                .entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(topN)
                .collect(Collectors.toList());
    }
}