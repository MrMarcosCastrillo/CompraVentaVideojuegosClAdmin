package com.inicial;

import java.net.ConnectException;
import java.util.List;
import java.util.Scanner;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {

	private static Scanner sc = new Scanner(System.in);
	private static ObjectMapper mapper = new ObjectMapper();
	private static Long idAdmin;

	public static void main(String[] args) {
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
					submenuAprobados();
					break;
				case 3:
					subirJuegoTienda();
					break;
				case 0:
					System.out.println("Saliendo del menú");
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

		sc.close();
	}

	private static boolean login() {
		try {
			System.out.print("Usuario admin: ");
			String user = sc.nextLine().trim().replace(" ", "%20");
			System.out.print("Password: ");
			String pwd = sc.nextLine().trim().replace(" ", "%20");

			String json = ApiCliente.get("/login/" + user + "/" + pwd);

			if (json == null || json.isBlank() || json.equals("null")) {
				System.out.println("Login incorrecto");
				return false;
			}

			Usuario u = mapper.readValue(json, Usuario.class);

			if (!u.isAdmin()) {
				System.out.println("Acceso denegado: no eres administrador");
				return false;
			}

			idAdmin = u.getId();
			System.out.println("Login correcto");
			return true;

		} catch (ConnectException e) {
			System.out.println("Error de conexión con el servidor");
			return false;
		} catch (Exception e) {
			System.out.println("Error al hacer login");
			e.printStackTrace();
			return false;
		}
	}

	private static void submenuPendientes() {
		try {
			String json = ApiCliente.get("/juegosPendientes");
			List<Juego> juegos = mapper.readValue(json, new TypeReference<List<Juego>>() {
			});

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

								if (!existeJuegoPorId(juegos, idJuego)) {
									System.out.println("No existe ningún juego con ese id");
								} else {
									if (Boolean
											.parseBoolean(ApiCliente.get("/aprobarJuego/" + idJuego + "/" + idAdmin))) {
										System.out.println("Juego aprobado con exito");
										json = ApiCliente.get("/juegosPendientes");
										juegos = mapper.readValue(json, new TypeReference<List<Juego>>() {
										});
									} else {
										System.out.println("No se pudo aprobar el juego");
									}
								}
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

								if (!existeJuegoPorId(juegos, idJuego)) {
									System.out.println("No existe ningún juego con ese id");
								} else {
									if (Boolean.parseBoolean(
											ApiCliente.get("/rechazarJuego/" + idJuego + "/" + idAdmin))) {
										System.out.println("Juego rechazado con exito");
										json = ApiCliente.get("/juegosPendientes");
										juegos = mapper.readValue(json, new TypeReference<List<Juego>>() {
										});
									} else {
										System.out.println("No se pudo rechazar el juego");
									}
								}
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
		} catch (ConnectException e) {
			System.out.println("Error de conexión con el servidor");
		} catch (Exception e) {
			System.out.println("Error en la gestión de juegos pendientes");
			e.printStackTrace();
		}
	}

	private static void submenuAprobados() {
		boolean volver = false;
		try {
			String json = ApiCliente.get("/juegos");
			List<Juego> juegos = mapper.readValue(json, new TypeReference<List<Juego>>() {
			});

			while (!volver) {
				if (juegos.isEmpty()) {
					System.out.println("No hay juegos aprobados");
					volver = true;
				} else {
					System.out.println("Juegos aprobados:");
					for (Juego j : juegos) {
						System.out.println(j);
					}

					System.out.println("1. Retirar juego subido por la tienda");
					System.out.println("0. Volver");

					if (sc.hasNextInt()) {
						int op = sc.nextInt();
						sc.nextLine();

						switch (op) {
						case 0:
							volver = true;
							break;
						case 1:
							System.out.print("ID del juego a borrar: ");
							if (sc.hasNextLong()) {
								Long idJuego = sc.nextLong();
								sc.nextLine();

								if (!existeJuegoPorId(juegos, idJuego)) {
									System.out.println("No existe ningún juego con ese id");
								} else {
									Long ID_TIENDA = 1L; // id que conocemos nosotros de la tienda

									if (Boolean.parseBoolean(
											ApiCliente.get("/borrarJuego/" + idJuego + "/" + ID_TIENDA))) {
										System.out.println("Juego borrado con exito");
										json = ApiCliente.get("/juegos");
										juegos = mapper.readValue(json, new TypeReference<List<Juego>>() {
										});
									} else {
										System.out.println("El juego no fue subido por la tienda");
									}
								}
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
		} catch (ConnectException e) {
			System.out.println("Error de conexión con el servidor");
		} catch (Exception e) {
			System.out.println("Error en la gestión de juegos aprobados");
			e.printStackTrace();
		}
	}

	private static void subirJuegoTienda() {
		try {
			System.out.print("Nombre: ");
			String nombre = sc.nextLine().trim().replace(" ", "%20");
			System.out.print("Imagen: ");
			String imagen = sc.nextLine().trim().replace(" ", "%20").replace("/", "🗿");
			System.out.println(imagen);

			if (sc.hasNextDouble()) {
				double precio = sc.nextDouble();
				sc.nextLine();

				Long ID_TIENDA = 1L; // id que conocemos nosotros de la tienda

				if (precio >= 0) {
					if (Boolean.parseBoolean(
							ApiCliente.get("/subirJuego/" + ID_TIENDA + "/" + nombre + "/" + imagen + "/" + precio))) {
						System.out.println("Juego subido con exito");
					} else {
						System.out.println("No se pudo subir el juego");
					}
				} else {
					System.out.println("El precio no puede ser igual o menor que 0");
				}
			} else {
				System.out.println("Precio no válido");
				sc.nextLine();
			}
		} catch (ConnectException e) {
			System.out.println("Error de conexión con el servidor");
		} catch (Exception e) {
			System.out.println("Error al subir un juego");
			e.printStackTrace();
		}
	}

	// Metodo compartido para comprobar el id en una lista de juegos

	private static boolean existeJuegoPorId(List<Juego> juegos, Long id) {
		for (Juego j : juegos) {
			if (j.getId().equals(id)) {
				return true;
			}
		}
		return false;
	}
}