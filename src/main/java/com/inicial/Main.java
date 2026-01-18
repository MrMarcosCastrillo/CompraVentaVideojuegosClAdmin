package com.inicial;

import java.util.List;
import java.util.Scanner;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {

    private static ObjectMapper mapper = new ObjectMapper();
    private static Long idAdmin = null;

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        while (!login()) {
            // Solo para repetir el login hasta que se meta correcto
        }

        int opcion = -1;

        while (opcion != 0) {
            System.out.println("\n--- MENÚ ADMIN ---");
            System.out.println("1. Gestión de juegos pendientes");
            System.out.println("2. Ver juegos aprobados");
            System.out.println("3. Subir juego como tienda");
            System.out.println("0. Salir");

            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1:
                    submenuPendientes();
                    break;
                case 2:
					verJuegosAprobados();
                    break;
                case 3:
                    subirJuegoTienda();
                    break;
                case 0:
                    System.out.println("Saliendo");
                    break;
                default:
                    System.out.println("Opción no válida");
            }
        }
    }

    // Login

    private static boolean login() {
    	Scanner sc = new Scanner(System.in);
    	
        try {
            System.out.print("Usuario admin: ");
            String user = sc.nextLine();
            System.out.print("Password: ");
            String pwd = sc.nextLine();

            String json = ApiCliente.get("/login/" + user + "/" + pwd);

            if (json.equals("null")) {
            	System.out.println("Login incorrecto");
                return false;
            }

            Usuario u = mapper.readValue(json, Usuario.class);
            idAdmin = u.getId();
            return true;

        } catch (Exception e) {
        	System.out.println("Error al hacer login");
            return false;
        }
    }

    // Submenú para juegos pendientes

    private static void submenuPendientes() {
    	Scanner sc = new Scanner(System.in);
    	
        try {
            String json = ApiCliente.get("/juegosPendientes");

            List<Juego> juegos = mapper.readValue(
                    json, new TypeReference<List<Juego>>() {});

            for (Juego j : juegos) {
                System.out.println(j);
            }

            System.out.println("1. Aprobar juego");
            System.out.println("2. Rechazar juego");
            System.out.println("0. Volver");

            int op = sc.nextInt();
            sc.nextLine();

            if (op == 0) return;

            System.out.print("ID del juego: ");
            Long idJuego = sc.nextLong();
            sc.nextLine();

            if (op == 1) {
                System.out.println(
                    ApiCliente.get("/admin/aprobarJuego/" + idJuego + "/" + idAdmin)
                );
            } else if (op == 2) {
                System.out.println(
                    ApiCliente.get("/admin/rechazarJuego/" + idJuego + "/" + idAdmin)
                );
            }

        } catch (Exception e) {
            System.out.println("Error en la gestión de juegos pendientes");
        }
    }

    // Ver juegos aprobados

    private static void verJuegosAprobados() {
        try {
			String json = ApiCliente.get("/juegos");

			List<Juego> juegos = mapper.readValue(
			        json, new TypeReference<List<Juego>>() {});

			for (Juego j : juegos) {
			    System.out.println(j);
			}
		} catch (Exception e) {
			System.out.println("Error al ver los juegos aprobados");
		}
    }

    //Subir juegos
    
    private static void subirJuegoTienda() {
    	Scanner sc = new Scanner(System.in);
    	
        try {
            System.out.print("Nombre: ");
            String nombre = sc.nextLine();
            System.out.print("Imagen: ");
            String imagen = sc.nextLine();
            System.out.print("Precio: ");
            double precio = sc.nextDouble();
            sc.nextLine();
            System.out.print("Clave: ");
            String clave = sc.nextLine();

            Long ID_TIENDA = 1L; // id de ejemplo para la tienda como vendedora

            System.out.println(
                ApiCliente.get("/subirjuego/" + ID_TIENDA + "/" +
                        nombre + "/" + imagen + "/" + precio + "/" + clave)
            );

        } catch (Exception e) {
            System.out.println("Error al subir un juego");
        }
    }
}