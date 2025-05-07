package pe.du.vallegrande.Vaccine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.du.vallegrande.Vaccine.model.VaccineModel;
import pe.du.vallegrande.Vaccine.repository.VaccineRepository;
import pe.du.vallegrande.Vaccine.service.VaccineServices;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/vaccines") // Ruta base para el controlador
public class VaccineController {

    @Autowired
    private VaccineRepository vaccineRepository;


    @Autowired
    private VaccineServices vaccineServices; // Inyecta el servicio

    // Crear las Vacunas
    @PostMapping("/create")
    public Mono<ResponseEntity<VaccineModel>> createVaccine(@RequestBody VaccineModel vaccine) {
        // Asegúrate de que el id no esté presente al crear una nueva vacuna
        vaccine.setVaccine_id(null); // O asegurarte de que el id sea null antes de guardar
        return vaccineRepository.save(vaccine)
                .map(savedVaccine -> ResponseEntity.status(HttpStatus.CREATED).body(savedVaccine))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    // Listar todas las vacunas
    @GetMapping
    public Flux<VaccineModel> getAllVaccines() {
        return vaccineRepository.findAll();
    }

  // Listar solo las vacunas inactivas (cambia la URL para evitar ambigüedad)
@GetMapping("/inactive")
public Flux<VaccineModel> getInactiveVaccines() {
    return vaccineRepository.findAll()
              .filter(vaccine -> "I".equals(vaccine.getActive())); // Filtra las vacunas inactivas
}

    // Obtener una vacuna por ID
    @GetMapping("/{id}")
    public Mono<ResponseEntity<VaccineModel>> getVaccineById(@PathVariable Long id) {
        return vaccineRepository.findById(id)
                .map(vaccine -> ResponseEntity.ok(vaccine))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // Actualizar una vacuna
    @PutMapping("/{id}")
    public Mono<ResponseEntity<VaccineModel>> updateVaccine(@PathVariable Long id, @RequestBody VaccineModel vaccine) {
        vaccine.setVaccine_id(id); // Asegúrate de que el ID sea el correcto
        return vaccineRepository.save(vaccine)
                .map(updatedVaccine -> ResponseEntity.ok(updatedVaccine))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

   // Eliminar (inactivar) una vacuna
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<VaccineModel>> deactivateVaccine(@PathVariable Long id) {
    return vaccineServices.deactivateVaccine(id) // Llamada al servicio que cambia el estado
            .map(deactivatedVaccine -> ResponseEntity.ok(deactivatedVaccine)) // Retorna la vacuna con estado "I"
            .defaultIfEmpty(ResponseEntity.notFound().build()); // Si no se encuentra la vacuna, retorna Not Found
}

 // Activar (revertir a activo) una vacuna
 @PatchMapping("/activate/{id}") // Método PATCH para actualizar parcialmente
public Mono<ResponseEntity<VaccineModel>> activateVaccine(@PathVariable Long id) {
     return vaccineServices.activateVaccine(id) // Llamada al servicio que cambia el estado
             .map(activatedVaccine -> ResponseEntity.ok(activatedVaccine)) // Retorna la vacuna con estado "A"
             .defaultIfEmpty(ResponseEntity.notFound().build()); // Si no se encuentra la vacuna, retorna Not Found
}

}