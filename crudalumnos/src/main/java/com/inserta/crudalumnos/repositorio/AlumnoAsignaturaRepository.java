package com.inserta.crudalumnos.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.inserta.crudalumnos.modelo.AlumnoAsignatura;
import com.inserta.crudalumnos.modelo.Asignatura;

import jakarta.transaction.Transactional;

// Añadimos la etiqueta @Repository (sobre todo si se va a añadir consultas manuales)
@Repository
public interface AlumnoAsignaturaRepository extends JpaRepository<AlumnoAsignatura, Long> {
        // Paso 1a. Definir el método abstracto de contar asignaturas por NIF
        @Transactional
        long countByAlumnoNif(String nif);

        // Paso 2a. Definir el método abstracto de borrado de asignaturas
        // Operaciones con la BBDD que implica tocar una tabla relacionada
        @Transactional
        void deleteByAlumnoNif(String nif);

        // Ver si un alumno (por nif) tiene asignaturas matriculadas
        /**
         * Verifica si existe una matrícula de un alumno en una asignatura concreta
         * @Query(""" """) → Anotación de Query desde v15 de Java
         * FROM AlumnoAsignatura aa → Alias de Tabla
         * 
         * @param nif          → nif del alumno para buscar
         * @param idAsignatura → id de la asignatura de matriculación
         * @return → true si está matriculado, false si no lo está
         */
        @Query("""
                        SELECT CASE WHEN COUNT(aa) > 0
                            THEN TRUE
                            ELSE FALSE END
                        FROM AlumnoAsignatura AS aa
                        WHERE aa.alumno.nif = :nif
                            AND aa.asignatura.id = :idAsignatura
                        """)
        boolean existeMatricula(
                        @Param("nif") String nif,
                        @Param("idAsignatura") Integer idAsignatura);

        /**
         * Borrado de matricula personalizado
         * 
         * @param nif
         * @param idAsignatura
         */
        @Modifying // Operar en la BBDD, no consultar
        @Transactional // Ejecuta una transacción
        @Query("""
                        DELETE FROM AlumnoAsignatura aa
                            WHERE aa.alumno.nif = :nif
                                AND aa.asignatura.id = :idAsignatura
                        """)
        void borrarMatricula(@Param("nif") String nif,
                        @Param("idAsignatura") Integer idAsignatura);

        //
        /*
         * Ej: SELECT asignaturas.denominacion
         * FROM asignaturas, alumnos_asignaturas, alumnos
         * WHERE asignaturas.id = alumnos_asignaturas.asignatura_id
         * AND alumnos.nif = alumnos_asignaturas.alumno_nif
         * AND alumnos.nif = '44D'
         * ORDER BY asignaturas.denominacion ASC;
         */

        /**
         * Vamos a sacar todas las matriculas de un alumno (nif) JOIN IMPLICITO
         * 
         * @param nif
         * @return List<String>
         */
        @Query("""
                        SELECT aa.asignatura.denominacion
                            FROM AlumnoAsignatura aa
                            WHERE aa.alumno.nif = :nif
                            ORDER BY aa.asignatura.denominacion ASC
                        """)
        List<String> asignaturasPorAlumno(@Param("nif") String nif);

        @Query("""
                        SELECT aa.asignatura
                            FROM AlumnoAsignatura aa
                            WHERE aa.alumno.nif = :nif
                            ORDER BY aa.asignatura.denominacion ASC
                        """)
        List<Asignatura> asignaturasCompletaPorAlumno(@Param("nif") String nif);

}
