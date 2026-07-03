package com.gimnasio.modelo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="socios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"suscripciones"})
@EqualsAndHashCode(exclude = {"suscripciones"})
public class Socio {
    @Id
    @Column(length = 9, nullable = false)
    private String dni;        // SQL -> dni VARCHAR(9) PRIMARY KEY

    @Column(length = 50, nullable = false)
    private String nombre;     // SQL -> nombre VARCHAR(50) NOT NULL

    @Column(nullable = false)
    private int edad;          // SQL -> edad INT NOT NULL

    @Column(length = 50, nullable = false)
    private String email;      // SQL -> email VARCHAR(50) NOT NULL

    @Column(nullable = false)
    private boolean activo;    // SQL -> activo BOOLEAN NOT NULL

    // Rompe la recursion al serializar a JSON, ya que la relación es bidireccional
    @JsonIgnore
    @OneToMany(mappedBy = "socio")
    private java.util.List<Suscripcion> suscripciones;

}
