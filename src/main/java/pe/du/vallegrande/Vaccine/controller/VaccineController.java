package pe.du.vallegrande.Vaccine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.du.vallegrande.Vaccine.model.VaccineModel;
import pe.du.vallegrande.Vaccine.repository.VaccineRepository;
import pe.du.vallegrande.Vaccine.service.VaccineServices;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/vaccines")
@CrossOrigin(origins = "*")
public class VaccineController {

    @Autowired
    private VaccineRepository vaccineRepository;

    @Autowired
    private VaccineServices vaccineServices;

    // Crear vacunas - Solo ADMIN
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<VaccineModel>> createVaccine(@RequestBody VaccineModel vaccine) {
        vaccine.setVaccine_id(null);
        return vaccineRepository.save(vaccine)
                .map(savedVaccine -> ResponseEntity.status(HttpStatus.CREATED).body(savedVaccine))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    // Listar todas las vacunas - USER y ADMIN
    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Flux<VaccineModel> getAllVaccines() {
        return vaccineRepository.findAll();
    }

    // Listar vacunas inactivas - Solo ADMIN (información sensible)
    @GetMapping("/inactive")
    @PreAuthorize("hasRole('ADMIN')")
    public Flux<VaccineModel> getInactiveVaccines() {
        return vaccineRepository.findAll()
                .filter(vaccine -> "I".equals(vaccine.getActive()));
    }

    // Listar vacunas activas - USER y ADMIN
    @GetMapping("/active")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Flux<VaccineModel> getActiveVaccines() {
        return vaccineServices.getActiveVaccines();
    }

    // Obtener vacuna por ID - USER y ADMIN
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Mono<ResponseEntity<VaccineModel>> getVaccineById(@PathVariable Long id) {
        return vaccineRepository.findById(id)
                .map(vaccine -> ResponseEntity.ok(vaccine))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // Actualizar vacuna - Solo ADMIN
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<VaccineModel>> updateVaccine(@PathVariable Long id, @RequestBody VaccineModel vaccine) {
        vaccine.setVaccine_id(id);
        return vaccineRepository.save(vaccine)
                .map(updatedVaccine -> ResponseEntity.ok(updatedVaccine))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // Eliminar (inactivar) vacuna - Solo ADMIN
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<VaccineModel>> deactivateVaccine(@PathVariable Long id) {
        return vaccineServices.deactivateVaccine(id)
                .map(deactivatedVaccine -> ResponseEntity.ok(deactivatedVaccine))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

<<<<<<< HEAD
    // Activar vacuna - Solo ADMIN
    @PatchMapping("/activate/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<VaccineModel>> activateVaccine(@PathVariable Long id) {
        return vaccineServices.activateVaccine(id)
                .map(activatedVaccine -> ResponseEntity.ok(activatedVaccine))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
=======
 // Activar (revertir a activo) una vacuna
 @PatchMapping("/activate/{id}") // Método PATCH para actualizar parcialmente
public Mono<ResponseEntity<VaccineModel>> activateVaccine(@PathVariable Long id) {
     return vaccineServices.activateVaccine(id) // Llamada al servicio que cambia el estado
             .map(activatedVaccine -> ResponseEntity.ok(activatedVaccine)) // Retorna la vacuna con estado "A"
             .defaultIfEmpty(ResponseEntity.notFound().build()); // Si no se encuentra la vacuna, retorna Not Found
}

>>>>>>> 53e93535f37b58b02be10ede41a1e80a8a95d07e
}