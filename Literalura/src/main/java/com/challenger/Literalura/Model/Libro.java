package com.challenger.Literalura.Model;
import com.challenger.Literalura.Record.DatosLibro;
import jakarta.persistence.*;


@Entity
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    private String idioma;
    private Double cantidadDeDescargas;
    @ManyToOne
    @JoinColumn(name = "autor_id", nullable = false)
    private Autor autor;

    public Libro(){}

    public Libro(DatosLibro datosLibro) {
        this.titulo = datosLibro.titulo();
        this.idioma = datosLibro.idioma().get(0);
        this.cantidadDeDescargas = datosLibro.cantidadDeDescargas();
        this.autor = new Autor(datosLibro.author().get(0));
    }

    //GYS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Double getCantidadDeDescargas() {
        return cantidadDeDescargas;
    }

    public void setCantidadDeDescargas(Double cantidadDeDescargas) {
        this.cantidadDeDescargas = cantidadDeDescargas;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    @Override
    public String toString() {
        return """
                
                Titulo: %s
                Autor: %s
                Idioma: %s
                Numero de Descargas: %s
                
                """.formatted(this.titulo,this.autor.getNombre(),this.idioma, this.cantidadDeDescargas);
    }
}
