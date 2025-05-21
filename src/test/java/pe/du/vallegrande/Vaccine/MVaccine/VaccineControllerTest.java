package pe.du.vallegrande.Vaccine.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pe.du.vallegrande.Vaccine.model.VaccineModel;
import pe.du.vallegrande.Vaccine.repository.VaccineRepository;
import pe.du.vallegrande.Vaccine.service.VaccineServices;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class VaccineControllerTest {

    @InjectMocks
    private VaccineController vaccineController;

    @Mock
    private VaccineRepository vaccineRepository;

    @Mock
    private VaccineServices vaccineServices;

    private VaccineModel vaccine;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        vaccine = new VaccineModel();
        vaccine.setVaccine_id(1L);
        vaccine.setActive("A");
    }

    // Prueba para crear una vacuna
    @Test
    void createVaccine_ShouldReturnCreatedVaccine() {
        when(vaccineRepository.save(vaccine)).thenReturn(Mono.just(vaccine));

        Mono<ResponseEntity<VaccineModel>> response = vaccineController.createVaccine(vaccine);

        assertEquals(HttpStatus.CREATED, response.block().getStatusCode());
        assertEquals(vaccine, response.block().getBody());
    }

    // Prueba para obtener una vacuna por ID
    @Test
    void getVaccineById_ShouldReturnVaccine() {
        when(vaccineRepository.findById(1L)).thenReturn(Mono.just(vaccine));

        Mono<ResponseEntity<VaccineModel>> response = vaccineController.getVaccineById(1L);

        assertEquals(HttpStatus.OK, response.block().getStatusCode());
        assertEquals(vaccine, response.block().getBody());
    }

    // Prueba para desactivar una vacuna
@Test
void deactivateVaccine_ShouldReturnDeactivatedVaccine() {
    when(vaccineServices.deactivateVaccine(1L)).thenReturn(Mono.just(vaccine)); // Simular el servicio
    vaccine.setActive("I"); // Cambia el estado a inactivo para la verificación

    Mono<ResponseEntity<VaccineModel>> response = vaccineController.deactivateVaccine(1L);

    assertEquals(HttpStatus.OK, response.block().getStatusCode());
    assertEquals("I", response.block().getBody().getActive()); // Verifica que el estado sea "I"
}

// Prueba para activar una vacuna
@Test
void activateVaccine_ShouldReturnActivatedVaccine() {
    // Cambia el estado a inactivo para simular la activación
    vaccine.setActive("I");
    
    // Simular el servicio y devolver una copia del objeto con el estado "A"
    VaccineModel activatedVaccine = new VaccineModel();
    activatedVaccine.setVaccine_id(vaccine.getVaccine_id());
    activatedVaccine.setActive("A");
    
    when(vaccineServices.activateVaccine(1L)).thenReturn(Mono.just(activatedVaccine)); // Simular el servicio

    Mono<ResponseEntity<VaccineModel>> response = vaccineController.activateVaccine(1L);

    assertEquals(HttpStatus.OK, response.block().getStatusCode());
    assertEquals("A", response.block().getBody().getActive()); // Verifica que el estado sea "A"
}

}
