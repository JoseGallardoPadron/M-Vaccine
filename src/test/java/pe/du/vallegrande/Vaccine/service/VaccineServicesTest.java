package pe.du.vallegrande.Vaccine.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import pe.du.vallegrande.Vaccine.model.VaccineModel;
import pe.du.vallegrande.Vaccine.repository.VaccineRepository;

@ExtendWith(MockitoExtension.class)
public class VaccineServicesTest {

    @InjectMocks
    private VaccineServices vaccineServices;

    @Mock
    private VaccineRepository vaccineRepository;

    private VaccineModel vaccine;

    @BeforeEach
    public void setUp() {
        // Inicializa un objeto de vacuna para usar en las pruebas
        vaccine = new VaccineModel();
        vaccine.setVaccine_id(1L);
        vaccine.setActive("A");
    }

    @Test
    public void testCreateVaccine() {
        when(vaccineRepository.save(any(VaccineModel.class))).thenReturn(Mono.just(vaccine));

        Mono<VaccineModel> result = vaccineServices.createVaccine(vaccine);

        result.subscribe(v -> {
            assertNotNull(v);
            assertEquals(vaccine.getVaccine_id(), v.getVaccine_id());
        });

        verify(vaccineRepository, times(1)).save(vaccine);
    }

    @Test
    public void testGetAllVaccines() {
        when(vaccineRepository.findAll()).thenReturn(Flux.just(vaccine));

        Flux<VaccineModel> result = vaccineServices.getAllVaccines();

        result.collectList().subscribe(vaccines -> {
            assertEquals(1, vaccines.size());
            assertEquals(vaccine.getVaccine_id(), vaccines.get(0).getVaccine_id());
        });

        verify(vaccineRepository, times(1)).findAll();
    }

    @Test
    public void testGetInactiveVaccines() {
        VaccineModel inactiveVaccine = new VaccineModel();
        inactiveVaccine.setVaccine_id(2L);
        inactiveVaccine.setActive("I");

        when(vaccineRepository.findAll()).thenReturn(Flux.just(vaccine, inactiveVaccine));

        Flux<VaccineModel> result = vaccineServices.getInactiveVaccines();

        result.collectList().subscribe(vaccines -> {
            assertEquals(1, vaccines.size());
            assertEquals(inactiveVaccine.getVaccine_id(), vaccines.get(0).getVaccine_id());
        });

        verify(vaccineRepository, times(1)).findAll();
    }

    @Test
    public void testGetActiveVaccines() {
        VaccineModel activeVaccine = new VaccineModel();
        activeVaccine.setVaccine_id(3L);
        activeVaccine.setActive("A");

        when(vaccineRepository.findAll()).thenReturn(Flux.just(vaccine, activeVaccine));

        Flux<VaccineModel> result = vaccineServices.getActiveVaccines();

        result.collectList().subscribe(vaccines -> {
            assertEquals(2, vaccines.size());
        });

        verify(vaccineRepository, times(1)).findAll();
    }

    @Test
    public void testGetVaccineById() {
        when(vaccineRepository.findById(1L)).thenReturn(Mono.just(vaccine));

        Mono<VaccineModel> result = vaccineServices.getVaccineById(1L);

        result.subscribe(v -> {
            assertNotNull(v);
            assertEquals(vaccine.getVaccine_id(), v.getVaccine_id());
        });

        verify(vaccineRepository, times(1)).findById(1L);
    }

    @Test
    public void testUpdateVaccine() {
        when(vaccineRepository.save(any(VaccineModel.class))).thenReturn(Mono.just(vaccine));

        Mono<VaccineModel> result = vaccineServices.updateVaccine(1L, vaccine);

        result.subscribe(v -> {
            assertNotNull(v);
            assertEquals(vaccine.getVaccine_id(), v.getVaccine_id());
        });

        verify(vaccineRepository, times(1)).save(vaccine);
    }

    @Test
    public void testDeactivateVaccine() {
        when(vaccineRepository.findById(1L)).thenReturn(Mono.just(vaccine));
        when(vaccineRepository.save(any(VaccineModel.class))).thenReturn(Mono.just(vaccine));

        Mono<VaccineModel> result = vaccineServices.deactivateVaccine(1L);

        result.subscribe(v -> {
            assertEquals("I", v.getActive());
        });

        verify(vaccineRepository, times(1)).findById(1L);
        verify(vaccineRepository, times(1)).save(vaccine);
    }

    @Test
    public void testActivateVaccine() {
        when(vaccineRepository.findById(1L)).thenReturn(Mono.just(vaccine));
        when(vaccineRepository.save(any(VaccineModel.class))).thenReturn(Mono.just(vaccine));

        Mono<VaccineModel> result = vaccineServices.activateVaccine(1L);

        result.subscribe(v -> {
            assertEquals("A", v.getActive());
        });

        verify(vaccineRepository, times(1)).findById(1L);
        verify(vaccineRepository, times(1)).save(vaccine);
    }
}
