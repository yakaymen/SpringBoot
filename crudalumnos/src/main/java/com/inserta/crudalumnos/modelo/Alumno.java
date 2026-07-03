package com.inserta.crudalumnos.modelo;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity                     // -> Esto es una clase para definir una entidad
@Data                       // -> Lombok (Setter, Getter, hash...)
@NoArgsConstructor          // -> Genera Constructor por defecto
@AllArgsConstructor         // -> Genera constructor completo (todos los params)
@Table(name="alumnos")      // -> Nombre Tabla (por debajo usar Hibernate)
public class Alumno {
    @Id                         // -> Clave Principal (PK de la tabla)
    @Column(length = 9, columnDefinition = "CHAR(9)")
    private String nif;         // SQL -> nif CHAR(9) NOT NULL PRIMARY KEY

    // Resto de los campos (siempre PRIVADOS!)
    @Column(length = 50, nullable = false)
    private String nombre;      // SQL -> nombre VARCHAR(50) NOT NULL

    // Aquí por ejemplo NO le ponemos ninguna etiqueta (@Column)
    private Integer edad;       // SQL -> edad INT NULL

    @Column(columnDefinition = "TINYINT(1)")
    private Boolean genero;     // SQL -> genero BOOLEAN

    // Se añade las relaciones en la tabla principal
    // En el mappedBy se pone el nombre de la clase en minúsculas
    @OneToMany(mappedBy = "alumno")
    @JsonIgnore
    private List<AlumnoAsignatura> alumnoAsignaturas = new ArrayList<>();
    
}
