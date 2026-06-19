package com.inserta.laqueseavecina.modelos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="trabajadores")
public class Trabajadores {
    @Id
    @Column(length = 9, columnDefinition = "CHAR(9)")
    private String nif;

    @Column(length = 50, nullable = false)
    private String nombre;    
    
    // Ejemplo: 1450.65€
    @Column(precision = 6, scale = 2, nullable = true)
    private BigDecimal sueldo;

    @ElementCollection
    @CollectionTable(name="tipo_trabajador",
        joinColumns = @JoinColumn(name="trabajadores_nif")
    )
    private List<Float> aulas = new ArrayList<>();


    @OneToMany(mappedBy = "trabajador")
    @JsonIgnore
    private List<Servicios> servicio = new ArrayList<>();
}
