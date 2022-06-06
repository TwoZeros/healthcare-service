import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoFileRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.medical.MedicalService;
import ru.netology.patient.service.medical.MedicalServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class TestHealthCareService {
    PatientInfoFileRepository patientInfoFileRepository;
    SendAlertService sendAlertService;
    MedicalService medicalService;
    PatientInfo patientInfo;

    @BeforeEach
    void setUp() {
        patientInfoFileRepository = Mockito.mock(PatientInfoFileRepository.class);
        sendAlertService = Mockito.mock(SendAlertService.class);
        patientInfo = new PatientInfo("Семен", "Михайлов", LocalDate.of(1982, 1, 16),
                new HealthInfo(new BigDecimal("36.6"), new BloodPressure(125, 78)));
        Mockito.when(patientInfoFileRepository.getById(Mockito.any()))
                .thenReturn(patientInfo);
        medicalService = new MedicalServiceImpl(patientInfoFileRepository, sendAlertService);
    }

    @Test
    public void bloodPressureIsNotNormal() {
        BloodPressure currentPressure = new BloodPressure(60, 120);
        medicalService.checkBloodPressure(UUID.randomUUID().toString(), currentPressure);
        Mockito.verify(sendAlertService, Mockito.times(1)).send(Mockito.any());
    }

    @Test
    public void temperatureIsNotNormal(){
        BigDecimal currentTemperature = new BigDecimal("33.9");
        medicalService.checkTemperature(UUID.randomUUID().toString(), currentTemperature);
        Mockito.verify(sendAlertService, Mockito.times(1)).send(Mockito.any());
    }

    @Test
    public void bloodPressureIsNormal() {
        BloodPressure currentPressure = new BloodPressure(125, 78);
        medicalService.checkBloodPressure(UUID.randomUUID().toString(), currentPressure);
        Mockito.verify(sendAlertService, Mockito.never()).send(Mockito.any());
    }

    @Test
    public void temperatureIsNormal(){
        BigDecimal currentTemperature = new BigDecimal("36.9");
        medicalService.checkTemperature(UUID.randomUUID().toString(), currentTemperature);
        Mockito.verify(sendAlertService, Mockito.never()).send(Mockito.any());
    }
}
