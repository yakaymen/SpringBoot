package com.gimnasio.modelo;

 /*1.4 Suscripcion.java  ← tabla ternaria
> ⚠️ @JsonIgnore en socio y monitor para evitar recursión infinita.
> La @UniqueConstraint garantiza que (socio + monitor + actividad) sea único.
*/

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(
    name = "suscripciones",
    uniqueConstraints = @UniqueConstraint(
        name  = "uk_socio_monitor_actividad",
        columnNames = {"socio_nif", "monitor_id", "actividad_id"}
    )
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"socio", "monitor", "actividad"})
@EqualsAndHashCode(exclude = {"socio", "monitor", "actividad"})
public class Suscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "socio_nif", nullable = false)
    @JsonIgnore                          // ← rompe ciclo socio → suscripcion
    private Socio socio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "monitor_id", nullable = false)
    @JsonIgnore                          // ← rompe ciclo monitor → suscripcion
    private Monitor monitor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actividad_id", nullable = false)
    private Actividad actividad;         // ← sí se serializa (para respuestas simples)

    @Column(nullable = false)
    private LocalDate fechaAlta;

    @Column(nullable = false)
    private boolean activa;
}