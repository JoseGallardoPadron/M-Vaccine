package pe.du.vallegrande.Vaccine.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("vaccine") // Nombre de la tabla en la base de datos
public class VaccineModel {

    
     @Id
    private Long vaccine_id;

    @Column("name_vaccine") // Especifica el nombre exacto de la columna
    private String nameVaccine;

    @Column("type_vaccine") // Especifica el nombre exacto de la columna
    private String typeVaccine;

    @Column("description")
    private String description;

    @Column("active") // Cambiado para reflejar el nombre correcto
    private String  Active; // Cambiado a Boolean para seguir la convención de Java
}
