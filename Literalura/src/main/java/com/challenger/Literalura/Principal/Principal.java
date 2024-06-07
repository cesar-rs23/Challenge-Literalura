package com.challenger.Literalura.Principal;
import com.challenger.Literalura.Services.ConvierteDatos;
import com.challenger.Literalura.API.ConsumoAPI;
import com.challenger.Literalura.Model.Autor;
import com.challenger.Literalura.Model.Libro;
import com.challenger.Literalura.Record.DatosApi;
import com.challenger.Literalura.Repository.AutorRepository;
import com.challenger.Literalura.Repository.idiomaLibro;
import com.challenger.Literalura.Repository.LibroRepository;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {

    Scanner teclado = new Scanner(System.in);
    private static final String URL_BASE = "https://gutendex.com/books/";
    private final ConsumoAPI consumoApi = new ConsumoAPI();
    private final ConvierteDatos convierteDatos = new ConvierteDatos();
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;


    public Principal() {
    }

    public Principal(LibroRepository repository, AutorRepository autorRepository) {
        this.libroRepository = repository;
        this.autorRepository = autorRepository;
    }



  public void getDatosLibro()  {
        System.out.println("Ingrese el nombre del libro que desea buscar");
        String libroSolicitado = teclado.nextLine();

        //llamada a la api por titulo
        String json = consumoApi.obtenerDatos(URL_BASE + "?search=" + libroSolicitado.replace(" ", "+"));
        DatosApi datosApi = convierteDatos.obtenerDatos(json, DatosApi.class);

        Optional<Libro> libroEncontrado = datosApi.libros().stream()
                .map(Libro::new)
                .findFirst();


        if (libroEncontrado.isPresent()) {

            Autor autor = autorRepository.findByNombreContainsIgnoreCase(libroEncontrado.get().getAutor().getNombre());

            if (autor == null) {

                Autor nuevoAutor = libroEncontrado.get().getAutor();
                autor = autorRepository.save(nuevoAutor);
            }

            Libro libro = libroEncontrado.get();

            try {
                libro.setAutor(autor);
                libroRepository.save(libro);
                System.out.println(libro);
            } catch (DataIntegrityViolationException ex) {

                System.out.println("El libro ya existe en la base de datos");
            }

        } else {
            System.out.println("El libro no se encuentra.");
        }

    }


    public void listarLibros() {
        List<Libro> libros = libroRepository.findAll();
        libros.stream()
                .forEach(System.out::println);
    }


    public void listarAutores() {
        List<Autor> autores = autorRepository.findAll();
        autores.stream().forEach(System.out::println);
    }


    public void listarAutoresVivos() {
        System.out.println("Ingrese el año de nacimiento del autor:");
        int fechaBuscada;
        String fecha;

        try {
            fechaBuscada = teclado.nextInt();

            fecha = String.valueOf(fechaBuscada);

            List<Autor> autoresVivos = autorRepository.buscarAutorVivo(fecha);

            if (autoresVivos.isEmpty()) {
                System.out.println("No se encontraron registros");
            } else {
                autoresVivos.stream().forEach(System.out::println);
            }

        } catch (Exception e) {
            System.out.println("Escriba un año valido");
            teclado.nextLine();
        }

    }


    public void listarIdiomas() {
        List<idiomaLibro> idiomas = libroRepository.buscarIdiomasCount();
        idiomas.stream().forEach(i -> System.out.println(
                """
                        Codigo idioma: %s, Cantidad de libros: %d""".formatted(i.getIdioma(), i.getCount())
        ));

        System.out.println("Ingresa el idioma a dos digitos para listar los libros");

        try {
            String codigo = teclado.nextLine();

            for (idiomaLibro idioma : idiomas) {
                if (idioma.getIdioma().equals(codigo)) {
                    libroRepository.findByIdiomaEquals(codigo).stream().forEach(System.out::println);
                    return;
                } else if (codigo.length() > 2) {
                    throw new InputMismatchException("Los Codigos contiene 2 caracteres, Ejemplo: es");
                }
            }
            System.out.println("Codigo invalido!");
        } catch (InputMismatchException e){
            System.out.println(e.getMessage());
        }

    }


    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {

            var menu = """
                    
                        Elije una Opcion...
                    
                        1-  Buscar libros por Título
                        2-  Listar libros registrados   
                        3-  Listar autores registrados
                        4-  Listar autores vivos en determinado año 
                        5-  Listar libros por idioma 
                        6-  Salir 
                      
                    """;


            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    getDatosLibro();
                    break;
                case 2:
                    listarLibros();
                    break;
                case 3:
                    listarAutores();
                    break;
                case 4:
                    listarAutoresVivos();
                    break;
                case 5:
                    listarIdiomas();
                    break;
                case 6:
                    System.out.println("Cerrando la aplicación...");
                    System.exit(0);
                    default:
                    System.out.println("La Opción NO es inválida!");
            }
        }

    }




}
