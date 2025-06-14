package pe.du.vallegrande.Vaccine.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import pe.du.vallegrande.Vaccine.model.VaccineModel;
import pe.du.vallegrande.Vaccine.repository.VaccineRepository;
import pe.du.vallegrande.Vaccine.service.VaccineServices;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class VaccineControllerTest {

    @InjectMocks
    private VaccineController vaccineController;

    @Mock
    private VaccineRepository vaccineRepository;

    @Mock
    private VaccineServices vaccineServices;

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

        Mono<ResponseEntity<VaccineModel>> result = vaccineController.createVaccine(vaccine);

        result.subscribe(response -> {
            assertEquals(201, response.getStatusCodeValue());
            assertNotNull(response.getBody());
            assertEquals(vaccine.getVaccine_id(), response.getBody().getVaccine_id());
        });

        verify(vaccineRepository, times(1)).save(vaccine);
    }

    @Test
    public void testGetAllVaccines() {
        when(vaccineRepository.findAll()).thenReturn(Flux.just(vaccine));

        Flux<VaccineModel> result = vaccineController.getAllVaccines();

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

        Flux<VaccineModel> result = vaccineController.getInactiveVaccines();

        result.collectList().subscribe(vaccines -> {
            assertEquals(1, vaccines.size());
            assertEquals(inactiveVaccine.getVaccine_id(), vaccines.get(0).getVaccine_id());
        });

        verify(vaccineRepository, times(1)).findAll();
    }

    @Test
    public void testGetVaccineById() {
        when(vaccineRepository.findById(1L)).thenReturn(Mono.just(vaccine));

        Mono<ResponseEntity<VaccineModel>> result = vaccineController.getVaccineById(1L);

        result.subscribe(response -> {
            assertEquals(200, response.getStatusCodeValue());
            assertNotNull(response.getBody());
            assertEquals(vaccine.getVaccine_id(), response.getBody().getVaccine_id());
        });

        verify(vaccineRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetVaccineByIdNotFound() {
        when(vaccineRepository.findById(1L)).thenReturn(Mono.empty());

        Mono<ResponseEntity<VaccineModel>> result = vaccineController.getVaccineById(1L);

        result.subscribe(response -> {
            assertEquals(404, response.getStatusCodeValue());
            assertNull(response.getBody());
        });

        verify(vaccineRepository, times(1)).findById(1L);
    }

    @Test
    public void testUpdateVaccine() {
        when(vaccineRepository.save(any(VaccineModel.class))).thenReturn(Mono.just(vaccine));

        Mono<ResponseEntity<VaccineModel>> result = vaccineController.updateVaccine(1L, vaccine);

        result.subscribe(response -> {
            assertEquals(200, response.getStatusCodeValue());
            assertNotNull(response.getBody());
            assertEquals(vaccine.getVaccine_id(), response.getBody().getVaccine_id());
        });

        verify(vaccineRepository, times(1)).save(vaccine);
    }

    @Test
    public void testUpdateVaccineNotFound() {
        when(vaccineRepository.save(any(VaccineModel.class))).thenReturn(Mono.empty());

        Mono<ResponseEntity<VaccineModel>> result = vaccineController.updateVaccine(1L, vaccine);

        result.subscribe(response -> {
            assertEquals(404, response.getStatusCodeValue());
            assertNull(response.getBody());
        });

        verify(vaccineRepository, times(1)).save(vaccine);
    }

    @Test
    public void testDeactivateVaccine() {
        when(vaccineServices.deactivateVaccine(1L)).thenReturn(Mono.just(vaccine));

        Mono<ResponseEntity<VaccineModel>> result = vaccineController.deactivateVaccine(1L);

        result.subscribe(response -> {
            assertEquals(200, response.getStatusCodeValue());
            assertNotNull(response.getBody());
            assertEquals(vaccine.getVaccine_id(), response.getBody().getVaccine_id());
            assertEquals("I", response.getBody().getActive());
        });

        verify(vaccineServices, times(1)).deactivateVaccine(1L);
    }

    @Test
    public void testDeactivateVaccineNotFound() {
        when(vaccineServices.deactivateVaccine(1L)).thenReturn(Mono.empty());

        Mono<ResponseEntity<VaccineModel>> result = vaccineController.deactivateVaccine(1L);

        result.subscribe(response -> {
            assertEquals(404, response.getStatusCodeValue());
            assertNull(response.getBody());
        });

        verify(vaccineServices, times(1)).deactivateVaccine(1L);
    }

    @Test
    public void testActivateVaccine() {
        when(vaccineServices.activateVaccine(1L)).thenReturn(Mono.just(vaccine));

        Mono<ResponseEntity<VaccineModel>> result = vaccineController.activateVaccine(1L);

        result.subscribe(response -> {
            assertEquals(200, response.getStatusCodeValue());
            assertNotNull(response.getBody());
            assertEquals(vaccine.getVaccine_id(), response.getBody().getVaccine_id());
            assertEquals("A", response.getBody().getActive());
        });

        verify(vaccineServices, times(1)).activateVaccine(1L);
    }

    @Test
    public void testActivateVaccineNotFound() {
        when(vaccineServices.activateVaccine(1L)).thenReturn(Mono.empty());

        Mono<ResponseEntity<VaccineModel>> result = vaccineController.activateVaccine(1L);

        result.subscribe(response -> {
            assertEquals(404, response.getStatusCodeValue());
            assertNull(response.getBody());
        });

        verify(vaccineServices, times(1)).activateVaccine(1L);
    }
}
