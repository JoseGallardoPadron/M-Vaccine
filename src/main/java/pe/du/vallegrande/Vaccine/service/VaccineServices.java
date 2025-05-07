package pe.du.vallegrande.Vaccine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.du.vallegrande.Vaccine.model.VaccineModel;
import pe.du.vallegrande.Vaccine.repository.VaccineRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class VaccineServices {

    @Autowired
    private VaccineRepository vaccineRepository;

    // Crear una nueva vacuna
    public Mono<VaccineModel> createVaccine(VaccineModel vaccine) {
        return vaccineRepository.save(vaccine);
    }

    // Listar todas las vacunas
    public Flux<VaccineModel> getAllVaccines() {
        return vaccineRepository.findAll();
    }

    // Método para listar solo las vacunas inactivas
    public Flux<VaccineModel> getInactiveVaccines() {
        return vaccineRepository.findAll()
                .filter(vaccine -> "I".equals(vaccine.getActive())); // Filtra las vacunas inactivas
    }

      // Método para listar solo las vacunas activass
    public Flux<VaccineModel> getActiveVaccines() {
        return vaccineRepository.findAll()
                .filter(vaccine -> "A".equals(vaccine.getActive())); // Filtra las vacunas activivas
    }


    // Obtener una vacuna por ID
    public Mono<VaccineModel> getVaccineById(Long id) {
        return vaccineRepository.findById(id);
    }

    // Actualizar una vacuna
    public Mono<VaccineModel> updateVaccine(Long id, VaccineModel vaccine) {
        vaccine.setVaccine_id(id); // Asegúrate de que el ID sea el correcto
        return vaccineRepository.save(vaccine);
    }


    
    // Eliminar una vacuna
    public Mono<VaccineModel> deactivateVaccine(Long id) {
        return vaccineRepository.findById(id)
            .flatMap(vaccine -> {
                vaccine.setActive("I"); // Cambia el estado a inactivo
                return vaccineRepository.save(vaccine); // Guarda el cambio
            });
    }
    
    
    
// Activar una vacuna (cambiar el estado a "A")
public Mono<VaccineModel> activateVaccine(Long id) {
    return vaccineRepository.findById(id)
            .flatMap(existingVaccine -> {
                existingVaccine.setActive("A"); // Cambia el estado a "A"
                return vaccineRepository.save(existingVaccine); // Guarda la vacuna con el estado actualizado
            });
}

}