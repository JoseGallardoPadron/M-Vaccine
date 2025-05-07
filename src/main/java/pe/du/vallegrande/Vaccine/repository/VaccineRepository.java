package pe.du.vallegrande.Vaccine.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import pe.du.vallegrande.Vaccine.model.VaccineModel;; // Cambia a VaccineModel



public interface VaccineRepository extends ReactiveCrudRepository<VaccineModel, Long> { // Cambia el nombre de la interfaz

}
