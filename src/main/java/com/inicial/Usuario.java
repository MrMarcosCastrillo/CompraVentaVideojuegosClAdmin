package com.inicial;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//Pojo de Usuario

@JsonIgnoreProperties(ignoreUnknown = true)
public class Usuario implements Serializable {

    private Long id;
    private String nombre;
    private BigDecimal saldo;
    private boolean admin;

    public Usuario() {}

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public boolean isAdmin() {
        return admin;
    }
    
    @Override
    public String toString() {
        return "nombre" + nombre + " (" + saldo + " €)";
    }
}
