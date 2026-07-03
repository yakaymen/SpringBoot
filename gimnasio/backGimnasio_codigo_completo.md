# backGimnasio — Código Backend Completo
> Spring Boot 3.5.15 · Java 25 · Spring Data JPA · MySQL 8.4 · Swagger/OpenAPI

---

## Estructura del Proyecto

```
backGimnasio/
├── pom.xml
└── src/main/
    ├── java/com/arelance/backgimnasio/
    │   ├── BackGimnasioApplication.java
    │   ├── DataLoader.java
    │   ├── config/
    │   │   ├── CorsConfig.java
    │   │   └── SwaggerConfig.java
    │   ├── dto/
    │   │   ├── EstadisticaDTO.java
    │   │   └── SuscripcionVistaDTO.java
    │   ├── model/
    │   │   ├── Socio.java
    │   │   ├── Monitor.java
    │   │   ├── Actividad.java
    │   │   └── Suscripcion.java
    │   ├── repository/
    │   │   ├── SocioRepository.java
    │   │   ├── MonitorRepository.java
    │   │   ├── ActividadRepository.java
    │   │   └── SuscripcionRepository.java
    │   └── controller/
    │       ├── SocioController.java
    │       ├── MonitorController.java
    │       ├── ActividadController.java
    │       └── SuscripcionController.java
    └── resources/
        └── application.properties
```

---

## pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
             https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.5.15</version>
        <relativePath/>
    </parent>

    <groupId>com.arelance</groupId>
    <artifactId>backGimnasio</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>backGimnasio</name>
    <description>API REST Gimnasio - Academia Arelance</description>

    <properties>
        <java.version>25</java.version>
    </properties>

    <dependencies>

        <!-- Spring Web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- Spring Data JPA -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>

        <!-- MySQL Connector -->
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <scope>runtime</scope>
        </dependency>

        <!-- Lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

        <!-- DevTools -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>

        <!-- Swagger / OpenAPI (SpringDoc) -->
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
            <version>2.8.8</version>
        </dependency>

        <!-- Tests -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

---

## application.properties

```properties
# ── Datasource ──────────────────────────────────────────────────────────────
spring.datasource.url=jdbc:mysql://localhost:3306/gimnasio?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# ── JPA / Hibernate ──────────────────────────────────────────────────────────
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.open-in-view=false

# ── Swagger / OpenAPI ────────────────────────────────────────────────────────
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.api-docs.path=/api-docs

# ── Servidor ─────────────────────────────────────────────────────────────────
server.port=8080
```

---

## 1. Modelos (Entidades JPA)

### 1.1 Socio.java

```java
package com.arelance.backgimnasio.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "socios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "suscripciones")
@EqualsAndHashCode(exclude = "suscripciones")
public class Socio {

    @Id
    @Column(length = 9)
    private String nif;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false)
    private int edad;

    @Column(unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private boolean activo;

    // ─── Rompe la recursión al serializar ────────────────────────────────────
    @JsonIgnore
    @OneToMany(mappedBy = "socio")
    private List<Suscripcion> suscripciones;
}
```

---

### 1.2 Monitor.java

```java
package com.arelance.backgimnasio.model;

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
```

---

### 1.3 Actividad.java

```java
package com.arelance.backgimnasio.model;

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
```

---

### 1.4 Suscripcion.java  ← tabla ternaria

> ⚠️ @JsonIgnore en socio y monitor para evitar recursión infinita.
> La @UniqueConstraint garantiza que (socio + monitor + actividad) sea único.

```java
package com.arelance.backgimnasio.model;

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
```

---

## 2. DTOs

### 2.1 SuscripcionVistaDTO.java

> Necesario para las consultas de listado: socio y monitor llevan @JsonIgnore
> en la entidad, así que construimos la "vista" desde JPQL.

```java
package com.arelance.backgimnasio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuscripcionVistaDTO {
    private Long      id;
    private String    socioNif;
    private String    socioNombre;
    private Integer   monitorId;
    private String    monitorNombre;
    private Integer   actividadId;
    private String    actividadDenominacion;
    private LocalDate fechaAlta;
    private boolean   activa;
}
```

---

### 2.2 EstadisticaDTO.java

```java
package com.arelance.backgimnasio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstadisticaDTO {
    private String nombre;
    private Long   cantidad;
}
```

---

## 3. Repositorios

### 3.1 SocioRepository.java

```java
package com.arelance.backgimnasio.repository;

import com.arelance.backgimnasio.model.Socio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SocioRepository extends JpaRepository<Socio, String> {

    // Búsqueda por nombre (contains, case-insensitive)
    List<Socio> findByNombreContainingIgnoreCase(String nombre);

    // KPIs para el dashboard
    long countByActivoTrue();
    long countByActivoFalse();
}
```

---

### 3.2 MonitorRepository.java

```java
package com.arelance.backgimnasio.repository;

import com.arelance.backgimnasio.model.Monitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MonitorRepository extends JpaRepository<Monitor, Integer> {

    // Filtro por especialidad (contains, case-insensitive)
    List<Monitor> findByEspecialidadContainingIgnoreCase(String valor);

    // KPI: número de especialidades distintas
    @Query("SELECT COUNT(DISTINCT m.especialidad) FROM Monitor m")
    long countEspecialidades();
}
```

---

### 3.3 ActividadRepository.java

```java
package com.arelance.backgimnasio.repository;

import com.arelance.backgimnasio.model.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;

@Repository
public interface ActividadRepository extends JpaRepository<Actividad, Integer> {

    // KPIs para el dashboard
    @Query("SELECT COALESCE(SUM(a.plazas), 0) FROM Actividad a")
    Integer sumPlazas();

    @Query("SELECT COALESCE(AVG(a.precioMes), 0) FROM Actividad a")
    BigDecimal avgPrecioMes();
}
```

---

### 3.4 SuscripcionRepository.java

```java
package com.arelance.backgimnasio.repository;

import com.arelance.backgimnasio.dto.EstadisticaDTO;
import com.arelance.backgimnasio.dto.SuscripcionVistaDTO;
import com.arelance.backgimnasio.model.Suscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public interface SuscripcionRepository extends JpaRepository<Suscripcion, Long> {

    // ── Listados con JOIN FETCH para evitar el problema N+1 ──────────────────

    @Query("""
        SELECT new com.arelance.backgimnasio.dto.SuscripcionVistaDTO(
            s.id,
            s.socio.nif,    s.socio.nombre,
            s.monitor.id,   s.monitor.nombre,
            s.actividad.id, s.actividad.denominacion,
            s.fechaAlta,    s.activa
        )
        FROM Suscripcion s
        JOIN s.socio JOIN s.monitor JOIN s.actividad
        """)
    List<SuscripcionVistaDTO> findAllAsDTO();

    @Query("""
        SELECT new com.arelance.backgimnasio.dto.SuscripcionVistaDTO(
            s.id,
            s.socio.nif,    s.socio.nombre,
            s.monitor.id,   s.monitor.nombre,
            s.actividad.id, s.actividad.denominacion,
            s.fechaAlta,    s.activa
        )
        FROM Suscripcion s
        JOIN s.socio JOIN s.monitor JOIN s.actividad
        WHERE s.socio.nif = :nif
        """)
    List<SuscripcionVistaDTO> findBySocioNifAsDTO(String nif);

    @Query("""
        SELECT new com.arelance.backgimnasio.dto.SuscripcionVistaDTO(
            s.id,
            s.socio.nif,    s.socio.nombre,
            s.monitor.id,   s.monitor.nombre,
            s.actividad.id, s.actividad.denominacion,
            s.fechaAlta,    s.activa
        )
        FROM Suscripcion s
        JOIN s.socio JOIN s.monitor JOIN s.actividad
        WHERE s.actividad.id = :idActividad
        """)
    List<SuscripcionVistaDTO> findByActividadIdAsDTO(Integer idActividad);

    // ── KPIs ─────────────────────────────────────────────────────────────────

    long countByActivaTrue();

    // ── Estadísticas (GROUP BY) para el dashboard ────────────────────────────

    @Query("""
        SELECT new com.arelance.backgimnasio.dto.EstadisticaDTO(
            s.actividad.denominacion, COUNT(s)
        )
        FROM Suscripcion s
        GROUP BY s.actividad.denominacion
        ORDER BY COUNT(s) DESC
        """)
    List<EstadisticaDTO> estadisticasPorActividad();

    @Query("""
        SELECT new com.arelance.backgimnasio.dto.EstadisticaDTO(
            s.monitor.nombre, COUNT(s)
        )
        FROM Suscripcion s
        GROUP BY s.monitor.nombre
        ORDER BY COUNT(s) DESC
        """)
    List<EstadisticaDTO> estadisticasPorMonitor();

    // ── Borrados en cascada (llamar ANTES de borrar la entidad padre) ─────────

    @Modifying
    @Transactional
    @Query("DELETE FROM Suscripcion s WHERE s.socio.nif = :nif")
    void deleteBySocioNif(String nif);

    @Modifying
    @Transactional
    @Query("DELETE FROM Suscripcion s WHERE s.monitor.id = :id")
    void deleteByMonitorId(Integer id);

    @Modifying
    @Transactional
    @Query("DELETE FROM Suscripcion s WHERE s.actividad.id = :id")
    void deleteByActividadId(Integer id);

    // ── Vaciado de mantenimiento ──────────────────────────────────────────────

    @Modifying
    @Transactional
    @Query("DELETE FROM Suscripcion s")
    void vaciarTabla();
}
```

---

## 4. Controladores

### 4.1 SocioController.java

```java
package com.arelance.backgimnasio.controller;

import com.arelance.backgimnasio.model.Socio;
import com.arelance.backgimnasio.repository.SocioRepository;
import com.arelance.backgimnasio.repository.SuscripcionRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/socios")
@Tag(name = "Socios", description = "Gestión de socios del gimnasio")
public class SocioController {

    @Autowired private SocioRepository       socioRepo;
    @Autowired private SuscripcionRepository suscripcionRepo;

    // GET /api/socios/consultar
    @Operation(summary = "Listar todos los socios")
    @GetMapping("/consultar")
    public ResponseEntity<List<Socio>> listar() {
        return ResponseEntity.ok(socioRepo.findAll());
    }

    // GET /api/socios/consultar/nombre?nombre=   ← literal, se resuelve ANTES que /{nif}
    @Operation(summary = "Buscar socios por nombre (contiene)")
    @GetMapping("/consultar/nombre")
    public ResponseEntity<List<Socio>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(socioRepo.findByNombreContainingIgnoreCase(nombre));
    }

    // GET /api/socios/consultar/{nif}
    @Operation(summary = "Buscar socio por NIF")
    @GetMapping("/consultar/{nif}")
    public ResponseEntity<Socio> buscarPorNif(@PathVariable String nif) {
        return socioRepo.findById(nif)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/socios/crear
    @Operation(summary = "Dar de alta un socio")
    @PostMapping("/crear")
    public ResponseEntity<?> crear(@RequestBody Socio socio) {
        if (socioRepo.existsById(socio.getNif())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Ya existe un socio con NIF " + socio.getNif()));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(socioRepo.save(socio));
    }

    // PUT /api/socios/actualizar/{nif}
    @Operation(summary = "Actualizar un socio por NIF")
    @PutMapping("/actualizar/{nif}")
    public ResponseEntity<Socio> actualizar(@PathVariable String nif, @RequestBody Socio socio) {
        if (!socioRepo.existsById(nif)) {
            return ResponseEntity.notFound().build();
        }
        socio.setNif(nif);
        return ResponseEntity.ok(socioRepo.save(socio));
    }

    // DELETE /api/socios/borrar/{nif}  — borra suscripciones primero
    @Operation(summary = "Borrar un socio (y sus suscripciones) por NIF")
    @DeleteMapping("/borrar/{nif}")
    public ResponseEntity<Void> borrar(@PathVariable String nif) {
        if (!socioRepo.existsById(nif)) {
            return ResponseEntity.notFound().build();
        }
        suscripcionRepo.deleteBySocioNif(nif);   // cascada manual
        socioRepo.deleteById(nif);
        return ResponseEntity.noContent().build();
    }

    // GET /api/socios/contar  — KPI dashboard
    @Operation(summary = "KPI de socios: total, activos y de baja")
    @GetMapping("/contar")
    public ResponseEntity<Map<String, Long>> contar() {
        Map<String, Long> kpis = new HashMap<>();
        kpis.put("total",   socioRepo.count());
        kpis.put("activos", socioRepo.countByActivoTrue());
        kpis.put("baja",    socioRepo.countByActivoFalse());
        return ResponseEntity.ok(kpis);
    }
}
```

---

### 4.2 MonitorController.java

```java
package com.arelance.backgimnasio.controller;

import com.arelance.backgimnasio.model.Monitor;
import com.arelance.backgimnasio.repository.MonitorRepository;
import com.arelance.backgimnasio.repository.SuscripcionRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/monitores")
@Tag(name = "Monitores", description = "Gestión de monitores del gimnasio")
public class MonitorController {

    @Autowired private MonitorRepository      monitorRepo;
    @Autowired private SuscripcionRepository  suscripcionRepo;

    // GET /api/monitores/consultar
    @Operation(summary = "Listar todos los monitores")
    @GetMapping("/consultar")
    public ResponseEntity<List<Monitor>> listar() {
        return ResponseEntity.ok(monitorRepo.findAll());
    }

    // GET /api/monitores/consultar/especialidad?valor=   ← literal primero
    @Operation(summary = "Filtrar monitores por especialidad")
    @GetMapping("/consultar/especialidad")
    public ResponseEntity<List<Monitor>> filtrarPorEspecialidad(@RequestParam String valor) {
        return ResponseEntity.ok(monitorRepo.findByEspecialidadContainingIgnoreCase(valor));
    }

    // GET /api/monitores/consultar/{id}
    @Operation(summary = "Buscar monitor por ID")
    @GetMapping("/consultar/{id}")
    public ResponseEntity<Monitor> buscarPorId(@PathVariable Integer id) {
        return monitorRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/monitores/crear
    @Operation(summary = "Dar de alta un monitor")
    @PostMapping("/crear")
    public ResponseEntity<Monitor> crear(@RequestBody Monitor monitor) {
        monitor.setId(null);  // el ID lo genera la BD
        return ResponseEntity.status(HttpStatus.CREATED).body(monitorRepo.save(monitor));
    }

    // PUT /api/monitores/actualizar/{id}
    @Operation(summary = "Actualizar un monitor por ID")
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Monitor> actualizar(@PathVariable Integer id, @RequestBody Monitor monitor) {
        if (!monitorRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        monitor.setId(id);
        return ResponseEntity.ok(monitorRepo.save(monitor));
    }

    // DELETE /api/monitores/borrar/{id}  — borra suscripciones primero
    @Operation(summary = "Eliminar un monitor (y sus suscripciones) por ID")
    @DeleteMapping("/borrar/{id}")
    public ResponseEntity<Void> borrar(@PathVariable Integer id) {
        if (!monitorRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        suscripcionRepo.deleteByMonitorId(id);   // cascada manual
        monitorRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // GET /api/monitores/contar  — KPI dashboard
    @Operation(summary = "KPI de monitores: total y especialidades distintas")
    @GetMapping("/contar")
    public ResponseEntity<Map<String, Long>> contar() {
        Map<String, Long> kpis = new HashMap<>();
        kpis.put("total",          monitorRepo.count());
        kpis.put("especialidades", monitorRepo.countEspecialidades());
        return ResponseEntity.ok(kpis);
    }
}
```

---

### 4.3 ActividadController.java

```java
package com.arelance.backgimnasio.controller;

import com.arelance.backgimnasio.model.Actividad;
import com.arelance.backgimnasio.repository.ActividadRepository;
import com.arelance.backgimnasio.repository.SuscripcionRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/actividades")
@Tag(name = "Actividades", description = "Gestión de actividades del gimnasio")
public class ActividadController {

    @Autowired private ActividadRepository   actividadRepo;
    @Autowired private SuscripcionRepository suscripcionRepo;

    // GET /api/actividades/consultar
    @Operation(summary = "Listar todas las actividades")
    @GetMapping("/consultar")
    public ResponseEntity<List<Actividad>> listar() {
        return ResponseEntity.ok(actividadRepo.findAll());
    }

    // GET /api/actividades/consultar/{id}
    @Operation(summary = "Buscar actividad por ID")
    @GetMapping("/consultar/{id}")
    public ResponseEntity<Actividad> buscarPorId(@PathVariable Integer id) {
        return actividadRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/actividades/crear
    @Operation(summary = "Dar de alta una actividad")
    @PostMapping("/crear")
    public ResponseEntity<Actividad> crear(@RequestBody Actividad actividad) {
        actividad.setId(null);  // el ID lo genera la BD
        return ResponseEntity.status(HttpStatus.CREATED).body(actividadRepo.save(actividad));
    }

    // PUT /api/actividades/actualizar/{id}
    @Operation(summary = "Actualizar una actividad por ID")
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Actividad> actualizar(@PathVariable Integer id, @RequestBody Actividad actividad) {
        if (!actividadRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        actividad.setId(id);
        return ResponseEntity.ok(actividadRepo.save(actividad));
    }

    // DELETE /api/actividades/borrar/{id}  — borra suscripciones primero
    @Operation(summary = "Eliminar una actividad (y sus suscripciones) por ID")
    @DeleteMapping("/borrar/{id}")
    public ResponseEntity<Void> borrar(@PathVariable Integer id) {
        if (!actividadRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        suscripcionRepo.deleteByActividadId(id);  // cascada manual
        actividadRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // GET /api/actividades/contar  — KPI dashboard
    @Operation(summary = "KPI de actividades: total, plazas y precio medio")
    @GetMapping("/contar")
    public ResponseEntity<Map<String, Object>> contar() {
        Map<String, Object> kpis = new HashMap<>();
        kpis.put("total",        actividadRepo.count());
        kpis.put("plazasTotales", actividadRepo.sumPlazas());
        kpis.put("precioMedio",  actividadRepo.avgPrecioMes());
        return ResponseEntity.ok(kpis);
    }
}
```

---

### 4.4 SuscripcionController.java

```java
package com.arelance.backgimnasio.controller;

import com.arelance.backgimnasio.dto.EstadisticaDTO;
import com.arelance.backgimnasio.dto.SuscripcionVistaDTO;
import com.arelance.backgimnasio.model.*;
import com.arelance.backgimnasio.repository.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/suscripciones")
@Tag(name = "Suscripciones", description = "Gestión de suscripciones ternarias del gimnasio")
public class SuscripcionController {

    @Autowired private SuscripcionRepository suscripcionRepo;
    @Autowired private SocioRepository       socioRepo;
    @Autowired private MonitorRepository     monitorRepo;
    @Autowired private ActividadRepository   actividadRepo;

    // GET /api/suscripciones/consultar
    @Operation(summary = "Listar todas las suscripciones con socio, monitor y actividad")
    @GetMapping("/consultar")
    public ResponseEntity<List<SuscripcionVistaDTO>> listar() {
        return ResponseEntity.ok(suscripcionRepo.findAllAsDTO());
    }

    // GET /api/suscripciones/socio/{nif}
    @Operation(summary = "Suscripciones de un socio por NIF")
    @GetMapping("/socio/{nif}")
    public ResponseEntity<List<SuscripcionVistaDTO>> porSocio(@PathVariable String nif) {
        return ResponseEntity.ok(suscripcionRepo.findBySocioNifAsDTO(nif));
    }

    // GET /api/suscripciones/actividad/{id}
    @Operation(summary = "Socios suscritos a una actividad por ID")
    @GetMapping("/actividad/{id}")
    public ResponseEntity<List<SuscripcionVistaDTO>> porActividad(@PathVariable Integer id) {
        return ResponseEntity.ok(suscripcionRepo.findByActividadIdAsDTO(id));
    }

    // POST /api/suscripciones/crear/{nif}/{idMonitor}/{idActividad}
    // Body (opcional): { "fechaAlta": "2025-09-15", "activa": true }
    @Operation(summary = "Crear suscripción ternaria: socio + monitor + actividad")
    @PostMapping("/crear/{nif}/{idMonitor}/{idActividad}")
    public ResponseEntity<?> crear(
            @PathVariable String  nif,
            @PathVariable Integer idMonitor,
            @PathVariable Integer idActividad,
            @RequestBody(required = false) Map<String, Object> body) {

        // Verificar que existan las tres entidades
        Optional<Socio>    socioOpt    = socioRepo.findById(nif);
        Optional<Monitor>  monitorOpt  = monitorRepo.findById(idMonitor);
        Optional<Actividad> actividadOpt = actividadRepo.findById(idActividad);

        if (socioOpt.isEmpty() || monitorOpt.isEmpty() || actividadOpt.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Socio, Monitor o Actividad no encontrados"));
        }

        // Construir la entidad
        Suscripcion s = new Suscripcion();
        s.setSocio(socioOpt.get());
        s.setMonitor(monitorOpt.get());
        s.setActividad(actividadOpt.get());
        s.setFechaAlta(LocalDate.now());   // valor por defecto
        s.setActiva(true);

        // Sobreescribir con los valores del body si se envían
        if (body != null) {
            if (body.get("fechaAlta") != null) {
                s.setFechaAlta(LocalDate.parse(body.get("fechaAlta").toString()));
            }
            if (body.get("activa") != null) {
                s.setActiva(Boolean.parseBoolean(body.get("activa").toString()));
            }
        }

        try {
            Suscripcion guardada = suscripcionRepo.save(s);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("id", guardada.getId(),
                                 "mensaje", "Suscripción creada correctamente"));
        } catch (DataIntegrityViolationException e) {
            // Violación de la UniqueConstraint (socio, monitor, actividad)
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "El socio ya está suscrito a esta actividad con este monitor"));
        }
    }

    // DELETE /api/suscripciones/borrar/todo   ← literal, tiene prioridad sobre /{id}
    @Operation(summary = "Vaciar todas las suscripciones (mantenimiento)")
    @DeleteMapping("/borrar/todo")
    public ResponseEntity<Map<String, String>> vaciar() {
        suscripcionRepo.vaciarTabla();
        return ResponseEntity.ok(Map.of("mensaje", "Tabla de suscripciones vaciada correctamente"));
    }

    // DELETE /api/suscripciones/borrar/{id}
    @Operation(summary = "Eliminar una suscripción por ID")
    @DeleteMapping("/borrar/{id}")
    public ResponseEntity<Void> borrar(@PathVariable Long id) {
        if (!suscripcionRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        suscripcionRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // GET /api/suscripciones/estadisticas/actividad
    @Operation(summary = "Estadísticas de suscripciones agrupadas por actividad")
    @GetMapping("/estadisticas/actividad")
    public ResponseEntity<List<EstadisticaDTO>> estadisticasPorActividad() {
        return ResponseEntity.ok(suscripcionRepo.estadisticasPorActividad());
    }

    // GET /api/suscripciones/estadisticas/monitor
    @Operation(summary = "Estadísticas de suscripciones agrupadas por monitor")
    @GetMapping("/estadisticas/monitor")
    public ResponseEntity<List<EstadisticaDTO>> estadisticasPorMonitor() {
        return ResponseEntity.ok(suscripcionRepo.estadisticasPorMonitor());
    }

    // GET /api/suscripciones/contar  — KPI dashboard
    @Operation(summary = "KPI de suscripciones: total, activas y media por socio")
    @GetMapping("/contar")
    public ResponseEntity<Map<String, Object>> contar() {
        long total  = suscripcionRepo.count();
        long activas = suscripcionRepo.countByActivaTrue();
        long socios = socioRepo.count();
        double media = socios > 0 ? Math.round((double) total / socios * 10.0) / 10.0 : 0.0;

        Map<String, Object> kpis = new HashMap<>();
        kpis.put("total",        total);
        kpis.put("activas",      activas);
        kpis.put("mediaPorSocio", media);
        return ResponseEntity.ok(kpis);
    }
}
```

---

## 5. Configuración

### 5.1 CorsConfig.java

```java
package com.arelance.backgimnasio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:4200"); // Angular dev server
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);

        return new CorsFilter(source);
    }
}
```

---

### 5.2 SwaggerConfig.java

```java
package com.arelance.backgimnasio.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI gimnasioOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Gimnasio")
                        .description("CRUD completo — Academia Arelance")
                        .version("1.0.0"));
    }
}
```

---

## 6. DataLoader.java (datos demo)

```java
package com.arelance.backgimnasio;

import com.arelance.backgimnasio.model.*;
import com.arelance.backgimnasio.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DataLoader implements CommandLineRunner {

    private final SocioRepository       socioRepo;
    private final MonitorRepository     monitorRepo;
    private final ActividadRepository   actividadRepo;
    private final SuscripcionRepository suscripcionRepo;

    public DataLoader(SocioRepository socioRepo,
                      MonitorRepository monitorRepo,
                      ActividadRepository actividadRepo,
                      SuscripcionRepository suscripcionRepo) {
        this.socioRepo       = socioRepo;
        this.monitorRepo     = monitorRepo;
        this.actividadRepo   = actividadRepo;
        this.suscripcionRepo = suscripcionRepo;
    }

    @Override
    public void run(String... args) {

        // Evita duplicar datos si ya existen
        if (socioRepo.count() > 0) {
            System.out.println("⚠️  Datos demo ya cargados — omitiendo DataLoader.");
            return;
        }

        // ─── Socios ───────────────────────────────────────────────────────────
        Socio s1 = socioRepo.save(new Socio("11111111A", "Lucía Romero", 28, "lucia@mail.com",  true,  null));
        Socio s2 = socioRepo.save(new Socio("22222222B", "Marcos Gil",   34, "marcos@mail.com", true,  null));
        Socio s3 = socioRepo.save(new Socio("33333333C", "Sara Núñez",   22, "sara@mail.com",   false, null));
        Socio s4 = socioRepo.save(new Socio("44444444D", "Iván Torres",  41, "ivan@mail.com",   true,  null));

        // ─── Monitores ────────────────────────────────────────────────────────
        Monitor m1 = monitorRepo.save(new Monitor(null, "Diego Vela",  "Spinning", "diego@gym.com",  null));
        Monitor m2 = monitorRepo.save(new Monitor(null, "Nora Ferri",  "Yoga",     "nora@gym.com",   null));
        Monitor m3 = monitorRepo.save(new Monitor(null, "Hugo Sanz",   "CrossFit", "hugo@gym.com",   null));
        Monitor m4 = monitorRepo.save(new Monitor(null, "Aitana Roca", "Pilates",  "aitana@gym.com", null));

        // ─── Actividades ──────────────────────────────────────────────────────
        Actividad a1 = actividadRepo.save(new Actividad(null, "Spinning", 20, new BigDecimal("34.90"), null));
        Actividad a2 = actividadRepo.save(new Actividad(null, "Yoga",     15, new BigDecimal("27.50"), null));
        Actividad a3 = actividadRepo.save(new Actividad(null, "CrossFit", 18, new BigDecimal("39.90"), null));
        Actividad a4 = actividadRepo.save(new Actividad(null, "Pilates",  12, new BigDecimal("29.90"), null));

        // ─── Suscripciones ────────────────────────────────────────────────────
        suscripcionRepo.save(new Suscripcion(null, s1, m1, a1, LocalDate.of(2025,  9, 15), true));
        suscripcionRepo.save(new Suscripcion(null, s2, m2, a2, LocalDate.of(2025,  9, 20), true));
        suscripcionRepo.save(new Suscripcion(null, s4, m3, a3, LocalDate.of(2025, 10,  1), false));
        suscripcionRepo.save(new Suscripcion(null, s3, m4, a4, LocalDate.of(2025, 10,  5), true));

        System.out.println("✅ Datos demo cargados correctamente.");
    }
}
```

---

## 7. BackGimnasioApplication.java

```java
package com.arelance.backgimnasio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackGimnasioApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackGimnasioApplication.class, args);
    }
}
```

---

## Resumen de todos los endpoints

| Verbo | Endpoint | Controller |
|-------|----------|------------|
| GET | `/api/socios/consultar` | SocioController |
| GET | `/api/socios/consultar/nombre?nombre=` | SocioController |
| GET | `/api/socios/consultar/{nif}` | SocioController |
| POST | `/api/socios/crear` | SocioController |
| PUT | `/api/socios/actualizar/{nif}` | SocioController |
| DELETE | `/api/socios/borrar/{nif}` | SocioController |
| GET | `/api/socios/contar` | SocioController |
| GET | `/api/monitores/consultar` | MonitorController |
| GET | `/api/monitores/consultar/especialidad?valor=` | MonitorController |
| GET | `/api/monitores/consultar/{id}` | MonitorController |
| POST | `/api/monitores/crear` | MonitorController |
| PUT | `/api/monitores/actualizar/{id}` | MonitorController |
| DELETE | `/api/monitores/borrar/{id}` | MonitorController |
| GET | `/api/monitores/contar` | MonitorController |
| GET | `/api/actividades/consultar` | ActividadController |
| GET | `/api/actividades/consultar/{id}` | ActividadController |
| POST | `/api/actividades/crear` | ActividadController |
| PUT | `/api/actividades/actualizar/{id}` | ActividadController |
| DELETE | `/api/actividades/borrar/{id}` | ActividadController |
| GET | `/api/actividades/contar` | ActividadController |
| GET | `/api/suscripciones/consultar` | SuscripcionController |
| GET | `/api/suscripciones/socio/{nif}` | SuscripcionController |
| GET | `/api/suscripciones/actividad/{id}` | SuscripcionController |
| POST | `/api/suscripciones/crear/{nif}/{idMonitor}/{idActividad}` | SuscripcionController |
| DELETE | `/api/suscripciones/borrar/todo` | SuscripcionController |
| DELETE | `/api/suscripciones/borrar/{id}` | SuscripcionController |
| GET | `/api/suscripciones/estadisticas/actividad` | SuscripcionController |
| GET | `/api/suscripciones/estadisticas/monitor` | SuscripcionController |
| GET | `/api/suscripciones/contar` | SuscripcionController |

> Swagger disponible en: `http://localhost:8080/swagger-ui.html`
