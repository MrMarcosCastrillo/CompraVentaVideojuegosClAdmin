package com.inicial;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiCliente {
	
    private static final String BASE_URL = "http://localhost:8080"; //Principio del endpoint como base para las llamadas del servidor spring boot
    private static HttpClient client = HttpClient.newHttpClient(); //Creación del cliente http

    public static String get(String endpoint) throws Exception { //Para meter por parametro la estructura de los endpoints

    	//Convierte en una petición http el uri creado a partir de la base del endpoint y del resto de la estructura
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + endpoint)).build();

        //Envia la petición y guarda como string la respuesta http a string
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body(); //Return de la respuesta
    }
}