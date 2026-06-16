package com.inserta.crudalumnos.modelo;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="asignaturas")
public class Asignatura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;         // SQL -> id INT AUTO_INCREMENT PRIMARY KEY

    @Column(length = 50, nullable = false)
    private String denominacion;

    @Column(columnDefinition = "TINYINT", nullable = true)
    private int curso;          // SQL -> curso TINYINT NULL

    // Vamos a añadir un campo ENUM
    // IMPORTANTE: No hace falta crear la tabla del ENUM (aulas)
    @ElementCollection
    @CollectionTable(name="asignaturas_aulas",
        joinColumns = @JoinColumn(name="asignatura_id")
    )
    private List<Float> aulas = new ArrayList<>();
    


    // Falta poner las relaciones con la tabla secundaria
    @OneToMany(mappedBy = "asignatura")
    private List<AlumnoAsignatura> alumnoAsignaturas = new ArrayList<>();
}
