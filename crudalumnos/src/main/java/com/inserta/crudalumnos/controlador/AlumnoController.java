package com.inserta.crudalumnos.controlador;

import com.inserta.crudalumnos.modelo.Alumno;
import com.inserta.crudalumnos.repositorio.AlumnoRepository;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




/**
 * Endpoints para Alumnos:
 * http://localhost:8080/api/alumnos
 * - GET R01 → /api/alumnos/consultar  🧨           → Consultar todos los alumnos
 * - GET R02 → /api/alumnos/consultar/{nif} 🧨      → Consulta por PK (nif)
 * - GET R03 → /api/alumnos/consultar/ordenado      → Consulta ordenada por campo=edad
 * - GET R04 → /api/alumnos/consultar/pagina        → Consulta paginada (LIMIT 0,5)
 * - GET R05 → /api/alumnos/existe/{nif}            → Consulta booleana (¿Existe alumno?)
 * - GET R06 → /api/alumnos/contar                  → Contar todos los alumnos
 * - GET R07 → /api/alumnos/consultar/nombre 🧨         → Listar alumnos que contengan "nombre"
 * - GET R08 → /api/alumnos/consultar/genero/mujeres    → Listar solo alumnas
 * - GET R09 → /api/alumnos/consultar/genero/hombres    → Listar solo alumnos
 * - GET R10 → /api/alumnos/consultar/contar/mujeres    → Contar solo alumnas
 * - GET R11 → /api/alumnos/consultar/contar/hombres    → Contar solo alumnos
 * 
 * - POST - C01 → /api/alumnos/crear/{nif}/{nombre}/{edad}/{genero} 🧨  → Insertar por parámetros
 * - POST - C02 → /api/alumnos/crear 🧨                 → Insertar por JSON
 * - POST - C03 → /api/alumnos/crear/lote 🧨            → insertar por JSON en lote
 * 
 * - PUT - U01 → /api/alumnos/actualizar/{nif}          → Actualiza por param/JSON
 * - PUT - U02 → /api/alumnos/actualizar  🧨            → Actualiza por JSON
 * - PUT - U03 → /api/alumnos/actualizar/{nif}/{nombre}/{edad}/{genero} 🧨
 * 
 * - DELETE - D01 → /api/alumnos/borrar/{nif} 
 * - DELETE - D02 → /api/alumnos/borrar/sencillo/{nif} 🧨
 * - DELETE - D03 → /api/alumnos/borrar 🧨
 * - DELETE - D04 → /api/alumnos/borrar/todo
 */


// Para la API/Rest (en el controlador) se pone la anotación @RestController
// @RequestMapping define el endpoint (la URL que sale en swagger/postman)
@RestController
@RequestMapping("/api/alumnos")
public class AlumnoController {
    // Agregamos el repo
    private final AlumnoRepository alumnoRepo;

    // Constructor del repo
    public AlumnoController(AlumnoRepository alumnoRepo) {
        this.alumnoRepo = alumnoRepo;
    }

    // Por cada endpoint se crea un método
    // Ej: http://localhost:8080/api/alumnos/consultar
    // GET R01 → /api/alumnos/consultar
    @GetMapping("/consultar")
    @Operation(summary = "Lista alumnos")
    public List<Alumno> verTodosAlumnos(){
        return alumnoRepo.findAll();
    }
    
    // Añadimos Swagger CTRL + MAY + P
    // Spring Initializr: Add Starter > SpringDoc Open Api Web
    // Se añade el paquete [Proceed] y se añade al Path
    // En application.properties se añade la url.path de swagger
    // http://localhost:8080/swagger-ui/index.html

    // GET por parámetro PK (findById) -> Parámetros van con { }
    // Ej: http://localhost:8080/api/alumnos/consultar/22B
    // GET R02 → /api/alumnos/consultar/{nif}
    @GetMapping("/consultar/{nif}")
    @Operation(summary = "Ver alumno por NIF")
    public ResponseEntity<Alumno> verAlumnoPorNIF(@PathVariable String nif){
        return alumnoRepo.findById(nif)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // Ej: http://localhost:8080/api/alumnos/consultar/ordenado?campo=edad
    // GET R03 → /api/alumnos/consultar/ordenado
    @GetMapping("/consultar/ordenado")
    @Operation(summary = "Consultar alumnos ordenado por nombre descendente")
    public List<Alumno> verAlumnosOrdenados
        (@RequestParam (defaultValue = "nombre") String campo) {
        return alumnoRepo.findAll(Sort.by(campo).descending());
    }
    
    // Ej: http://localhost:8080/api/alumnos/consultar/pagina?pagina=0&tam=5
    // GET R04 → /api/alumnos/consultar/pagina
    @GetMapping("/consultar/pagina")
    @Operation(summary = "Consultar alumnos paginado por intervalos")
    public Page<Alumno> verAlumnosPaginados 
        (@RequestParam (defaultValue = "0") int pagina,
        @RequestParam (defaultValue = "5") int tam) {
        return alumnoRepo.findAll(PageRequest.of(pagina, tam));
    }
    
    // Ej: http://localhost:8080/api/alumnos/existe/22C
    // GET R05 → /api/alumnos/existe/{nif}
    @GetMapping("/existe/{nif}")
    @Operation(summary = "¿Existe un alumno por su NIF?")
    public boolean existeAlumno (@PathVariable String nif) {
        return alumnoRepo.existsById(nif);
    }
    
    // Ej: http://localhost:8080/api/alumnos/contar
    // GET R06 → /api/alumnos/contar
    @GetMapping("/contar")
    public long contarTodosAlumnos() {
        return alumnoRepo.count();
    }

    // Ej: http://localhost:8080/api/alumnos/consultar/nombre?nombre=alex
    // GET R07 → /api/alumnos/consultar/nombre
    // Paso1: Al no ser método estandar, se añade al repo
    // Paso2: Se agrega el nuevo método del repo aquí en el controlador
    @GetMapping("/consultar/nombre")
    @Operation(summary = "Buscar alumnos que contengan parte de 'nombre'")
    public List<Alumno> buscarPorNombre(@RequestParam String nombre) {
        return alumnoRepo.findByNombreContainingIgnoreCase(nombre);
    }

    // GET R08 → /api/alumnos/consultar/genero/mujeres
    @GetMapping("/consultar/genero/mujeres")
    @Operation(summary = "Sacar Listado Alumnas")
    public List<Alumno> verAlumnas() {
        return alumnoRepo.findByGenero(true);
    }

    // GET R09 → /api/alumnos/consultar/genero/hombres
    @GetMapping("/consultar/genero/hombres")
    @Operation(summary = "Sacar Listado Alumnos")
    public List<Alumno> verAlumnos() {
        return alumnoRepo.findByGenero(false);
    }

    // GET R10 → /api/alumnos/consultar/contar/mujeres
    @GetMapping("/consultar/contar/mujeres")
    @Operation(summary = "Contar Alumnas")
    public long contarAlumnas() {
        return alumnoRepo.countByGenero(true);
    }

    // GET R11 → /api/alumnos/consultar/contar/hombres
    @GetMapping("/consultar/contar/hombres")
    @Operation(summary = "Contar Alumnos")
    public long contarAlumnos() {
        return alumnoRepo.countByGenero(false);
    }
    
    // Ej: http://localhost:8080/api/alumnos/crear/88E/Carlos%20Hidalgo/34/false
    // POST - C01 → /api/alumnos/crear/{nif}/{nombre}/{edad}/{genero}
    // El orden de los parámetros DA IGUAL, pero los nombres deben ser iguales
    // Consejo: En los PathVariable usar siempre OBJETOS (para llegadas nulas)
    // En el SpringBoot en el formulario para rellenar datos sale el orden del @Path
    @PostMapping("/crear/{nif}/{nombre}/{edad}/{genero}")
    @Operation(summary = "Inserta Alumno por parámetros en la URL")
    public Alumno crearAlumnoParametros(
        @PathVariable String nif,
        @PathVariable Boolean genero,
        @PathVariable String nombre,
        @PathVariable Integer edad
    ) {
        Alumno alumno = new Alumno();   // Objeto vacio
        alumno.setNombre(nombre);
        alumno.setEdad(edad);
        alumno.setGenero(genero);
        alumno.setNif(nif);
      
        return alumnoRepo.save(alumno);
    }

    // Ej: http://localhost:8080/api/alumnos/crear
    // POST - C02 → /api/alumnos/crear
    @PostMapping("/crear")
    @Operation(summary = "Inserta alumno por JSON")
    public Alumno crearAlumnoJSON(@RequestBody Alumno alumno) {
        return alumnoRepo.save(alumno);
    }
    
    // Ej: http://localhost:8080/api/alumnos/crear/lote
    // POST - C03 → /api/alumnos/crear/lote
    @PostMapping("/crear/lote")
    @Operation(summary = "Inserta varios alumnos en lote por JSON")
    public List<Alumno> crearAlumnoJSON(@RequestBody List<Alumno> alumnos) {
        return alumnoRepo.saveAll(alumnos);
    }
    
    // Ej: Nif: 33C → { "edad": 30 } 
    // PUT - U01 → /api/alumnos/actualizar/{nif}
    @PutMapping("/actualizar/{nif}")
    @Operation(summary = "Actualizar un alumno por NIF en la URL")
    public Alumno actualizarAlumnoNIF(
        @PathVariable String nif, @RequestBody Alumno alumnoJSON) {
        // Busco el alumno para actualizar
        Alumno alumno = alumnoRepo.findById(nif)
                        .orElseThrow(()-> 
                        new RuntimeException("Alumno no encontrado"));

        if(alumnoJSON.getNombre() != null){
            alumno.setNombre(alumnoJSON.getNombre());}
        if(alumnoJSON.getEdad() != null){
            alumno.setEdad(alumnoJSON.getEdad());}
        if(alumnoJSON.getGenero() != null){
            alumno.setGenero(alumnoJSON.getGenero());}
        
        return alumnoRepo.save(alumno);
    }

    // Ej: { "nif": "11A", "nombre": "Alejandro Párraga","edad": 24 }
    // PUT - U02 → /api/alumnos/actualizar
    @PutMapping("/actualizar")
    @Operation(summary = "Actualizar un alumno por JSON")
    public Alumno actualizarAlumnoJSON(@RequestBody Alumno alumnoJSON) {
        // Busco el alumno para actualizarnif
        Alumno alumno = alumnoRepo.findById(alumnoJSON.getNif())
                        .orElseThrow(()-> 
                        new RuntimeException("Alumno no encontrado"));

        if(alumnoJSON.getNombre() != null){
            alumno.setNombre(alumnoJSON.getNombre());}
        if(alumnoJSON.getEdad() != null){
            alumno.setEdad(alumnoJSON.getEdad());}
        if(alumnoJSON.getGenero() != null){
            alumno.setGenero(alumnoJSON.getGenero());}
        
        return alumnoRepo.save(alumno);
    }

    // Ej: http://localhost:8080/api/alumnos/actualizar/22B/Miguel%20%C3%81ngel%20Rider/39/false
    // PUT - U03 → /api/alumnos/actualizar/{nif}/{nombre}/{edad}/{genero}
    // Los parámetros TODOS son obligatorios
    @PutMapping("/actualizar/{nif}/{nombre}/{edad}/{genero}")
    @Operation(summary = "Actualizar un alumno por NIF en la URL")
    public Alumno actualizarAlumno(
        @PathVariable String nif,
        @PathVariable String nombre,
        @PathVariable Integer edad,
        @PathVariable Boolean genero ) {
        // Busco el alumno para actualizar
        Alumno alumno = alumnoRepo.findById(nif)
                        .orElseThrow(()-> 
                        new RuntimeException("Alumno no encontrado"));

            alumno.setNombre(nombre);
            alumno.setEdad(edad);
            alumno.setGenero(genero);
        
        return alumnoRepo.save(alumno);
    }

    // Ej: http://localhost:8080/api/alumnos/borrar/11A
    // ESTE es el que usa a nivel profesional!
    // DELETE - D01 → /api/alumnos/borrar/{nif}
    @DeleteMapping("/borrar/{nif}")
    @Operation(summary = "Borrar alumno por NIF (PK)")
    public ResponseEntity<String> borrarAlumno(@PathVariable String nif){
        if(!alumnoRepo.existsById(nif)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).
                body("Alumno/a no encontrado");
        }
        alumnoRepo.deleteById(nif);
        return ResponseEntity.ok("Alumno/a borrado de la BBDD");
    }

    // EJ: http://localhost:8080/api/alumnos/borrar/sencillo/14234
    // DELETE - D02 → /api/alumnos/borrar/sencillo/{nif}
    @DeleteMapping("/borrar/sencillo/{nif}")
    @Operation(summary = "Borrar alumno por NIF (PK) - Modo Sencillo")
    public void borrarAlumnoSencillo (@PathVariable String nif){
        alumnoRepo.deleteById(nif);
    }

    // Ej: ["22B", "44D"] → Elimino a Rider y a Jaime
    // DELETE - D03 → /api/alumnos/borrar
    @DeleteMapping("/borrar")
    @Operation(summary = "Borrar alumno por JSON (incluido LOTE)")
    public void borrarVariosAlumnos(@RequestBody List<String> nifs){
        alumnoRepo.deleteAllById(nifs);
    }

    // DELETE - D04 → /api/alumnos/borrar/todo
    @DeleteMapping("/borrar/todo")
    @Operation(summary = "Borrar todos los alumnos")
    public void borrarTodosAlumnos(){
        alumnoRepo.deleteAll();
    }


}
