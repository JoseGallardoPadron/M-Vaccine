package pe.du.vallegrande.Vaccine.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import pe.du.vallegrande.Vaccine.model.VaccineModel; // Cambia a VaccineModel

public interface VaccineRepository extends ReactiveCrudRepository<VaccineModel, Long> { // Cambiado a UUID
}
