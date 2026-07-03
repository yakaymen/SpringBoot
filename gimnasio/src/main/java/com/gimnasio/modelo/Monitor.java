package com.gimnasio.modelo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "monitores")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "suscripciones")
@EqualsAndHashCode(exclude = "suscripciones")
public class Monitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String especialidad;

    @Column(unique = true, length = 150)
    private String email;

    // ─── Rompe la recursión al serializar ────────────────────────────────────
    @JsonIgnore
    @OneToMany(mappedBy = "monitor")
    private List<Suscripcion> suscripciones;
}
