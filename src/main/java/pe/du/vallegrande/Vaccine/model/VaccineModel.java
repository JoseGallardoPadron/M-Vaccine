package pe.du.vallegrande.Vaccine.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column; // Importa Column

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("vaccine") // Nombre de la tabla en la base de datos
public class VaccineModel {

    @Id
    private Long vaccine_id;

    @Column("name_Vaccine") // Especifica el nombre exacto de la columna
    private String nameVaccine;

    @Column("type_Vaccine") // Especifica el nombre exacto de la columna
    private String typeVaccine;

    @Column("description")
    private String description;

    @Column("active")
    private String active;
}