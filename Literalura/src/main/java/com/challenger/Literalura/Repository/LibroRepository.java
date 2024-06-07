package com.challenger.Literalura.Repository;
import com.challenger.Literalura.Model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {


    @Query("SELECT libro.idioma as idioma, COUNT(*) as count FROM Libro libro GROUP BY libro.idioma")
    List<idiomaLibro> buscarIdiomasCount();

    List<Libro> findByIdiomaEquals(String codigo);

    List<Libro> findTop10ByOrderByCantidadDeDescargasDesc();

}
