package com.challenger.Literalura.Services;

public interface IConvierteDatos {

    default <T> T obtenerDatos(String json, Class<T> clase){
        return null;
    }

}
