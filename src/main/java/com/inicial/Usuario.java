package com.inicial;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//Pojo de Usuario

@JsonIgnoreProperties(ignoreUnknown = true) //Ignora los campos del JSON recibido del server si no los tiene
public class Usuario implements Serializable { //Sirve para poder convertir el objeto en bytes para guardar como JSON (no lo usamos pero por si acaso)

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
