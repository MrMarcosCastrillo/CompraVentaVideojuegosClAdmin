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
        boolean logueado = false;

        // Bucle de login hasta que sea correcto
        while (!logueado) {
            logueado = login();
        }

        int opcion = -1;
        boolean salir = false;

        // Bucle menú
        while (!salir) {
            System.out.println("\n--- MENÚ ADMIN ---");
            System.out.println("1. Gestión de juegos pendientes");
            System.out.println("2. Ver juegos aprobados");
            System.out.println("3. Subir juego como tienda");
            System.out.println("0. Salir");

            if (sc.hasNextInt()) {
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
                        System.out.println("Saliendo del menú...");
                        salir = true;
                        break;
                    default:
                        System.out.println("Opción no válida");
                        break;
                }
            } else {
                System.out.println("Por favor, introduce un número");
                sc.nextLine();
            }
        }
    }

    private static boolean login() {
        Scanner sc = new Scanner(System.in);
        boolean correcto = false;

        try {
            System.out.print("Usuario admin: ");
            String user = sc.nextLine();
            System.out.print("Password: ");
            String pwd = sc.nextLine();

            String json = ApiCliente.get("/login/" + user + "/" + pwd);

            if (!json.equals("null")) {
                Usuario u = mapper.readValue(json, Usuario.class);
                idAdmin = u.getId();
                System.out.println("Login correcto: " + u.getNombre());
                correcto = true;
            } else {
                System.out.println("Login incorrecto, prueba de nuevo");
            }
        } catch (Exception e) {
            System.out.println("Error al hacer login: " + e.getMessage());
        }

        return correcto;
    }

    private static void submenuPendientes() {
        Scanner sc = new Scanner(System.in);

        try {
            String json = ApiCliente.get("/juegosPendientes");
            List<Juego> juegos = mapper.readValue(json, new TypeReference<List<Juego>>() {});

            boolean volver = false;

            while (!volver) {
                if (juegos.isEmpty()) {
                    System.out.println("No hay juegos pendientes");
                    volver = true;
                } else {
                    System.out.println("Juegos pendientes:");
                    for (Juego j : juegos) {
                        System.out.println(j);
                    }

                    System.out.println("1. Aprobar juego");
                    System.out.println("2. Rechazar juego");
                    System.out.println("0. Volver");

                    if (sc.hasNextInt()) {
                        int op = sc.nextInt();
                        sc.nextLine();

                        switch (op) {
                            case 0:
                                volver = true;
                                break;
                            case 1:
                                System.out.print("ID del juego a aprobar: ");
                                if (sc.hasNextLong()) {
                                    Long idJuego = sc.nextLong();
                                    sc.nextLine();
                                    System.out.println(
                                            ApiCliente.get("/aprobarJuego/" + idJuego + "/" + idAdmin));
                                } else {
                                    System.out.println("ID no válido");
                                    sc.nextLine();
                                }
                                break;
                            case 2:
                                System.out.print("ID del juego a rechazar: ");
                                if (sc.hasNextLong()) {
                                    Long idJuego = sc.nextLong();
                                    sc.nextLine();
                                    System.out.println(
                                            ApiCliente.get("/rechazarJuego/" + idJuego + "/" + idAdmin));
                                } else {
                                    System.out.println("ID no válido");
                                    sc.nextLine();
                                }
                                break;
                            default:
                                System.out.println("Opción no válida");
                                break;
                        }
                    } else {
                        System.out.println("Introduce un número válido");
                        sc.nextLine();
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Error en la gestión de juegos pendientes");
            e.printStackTrace();
        }
    }

    private static void verJuegosAprobados() {
        try {
            String json = ApiCliente.get("/juegos");
            List<Juego> juegos = mapper.readValue(json, new TypeReference<List<Juego>>() {});

            if (juegos.isEmpty()) {
                System.out.println("No hay juegos aprobados");
            } else {
                System.out.println("Juegos aprobados:");
                for (Juego j : juegos) {
                    System.out.println(j);
                }
            }
        } catch (Exception e) {
            System.out.println("Error al ver los juegos aprobados: " + e.getMessage());
        }
    }

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

            Long ID_TIENDA = 1L; // id que conocemos nosotros de la tienda

            System.out.println(ApiCliente.get("/subirJuego/" + ID_TIENDA + "/" +
                    nombre + "/" + imagen + "/" + precio + "/" + clave));

        } catch (Exception e) {
            System.out.println("Error al subir un juego: " + e.getMessage());
        }
    }
}