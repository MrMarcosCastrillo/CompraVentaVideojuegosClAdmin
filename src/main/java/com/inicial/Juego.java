package com.inicial;

import java.io.Serializable;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Juego implements Serializable {

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