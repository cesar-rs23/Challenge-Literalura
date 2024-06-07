package com.challenger.Literalura.Repository;
import com.challenger.Literalura.Model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {


    Autor findByNombreContainsIgnoreCase(String nombre);



    @Query("SELECT autor\n" + "FROM Autor autor\n" +
            "WHERE (autor.fechaDeNacimiento <= :fecha)\n" +
            "AND (autor.fechaDeMuerte >= :fecha OR autor.fechaDeMuerte IS NULL)\n")
    List<Autor> buscarAutorVivo(String fecha);




}