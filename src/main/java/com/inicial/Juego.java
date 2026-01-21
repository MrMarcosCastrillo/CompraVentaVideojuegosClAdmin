package com.inicial;

import java.io.Serializable;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//Pojo de Juego

@JsonIgnoreProperties(ignoreUnknown = true) //Ignora los campos del JSON recibido del server si no los tiene
public class Juego implements Serializable { //Sirve para poder convertir el objeto en bytes para guardar como JSON (no lo usamos pero por si acaso)

    private Long id;
    private Long vendedor_id;
    private Long comprador_id;
    private String nombre;
    private String imagen;
    private BigDecimal precio;

    public Juego() {}

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    @Override
    public String toString() {
        return "Id: " + id + " - " + nombre + " (" + precio + " €)";
    }
}