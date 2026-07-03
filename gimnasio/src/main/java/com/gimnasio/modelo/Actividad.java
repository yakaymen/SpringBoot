package com.gimnasio.modelo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "actividades")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "suscripciones")
@EqualsAndHashCode(exclude = "suscripciones")
public class Actividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String denominacion;

    @Column(nullable = false)
    private int plazas;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal precioMes;

    // ─── Rompe la recursión al serializar ────────────────────────────────────
    @JsonIgnore
    @OneToMany(mappedBy = "actividad")
    private List<Suscripcion> suscripciones;
}