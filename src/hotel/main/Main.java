package hotel.main;

import hotel.huespedes.Genero;
import hotel.huespedes.Huesped;
import hotel.habitaciones.EstadoHabitacion;
import hotel.habitaciones.Habitacion;
import hotel.habitaciones.TipoHabitacion;
import hotel.pagos.MedioPago;
import hotel.pagos.RegistroPago;
import hotel.reservas.CheckIn;
import hotel.reservas.RegistroServicio;
import hotel.servicios.*;
import hotel.util.ListaEnlazada;
import hotel.colas.ColaAtencion;
import hotel.colas.ColaServicioMasaje;
import hotel.pilas.PilaServiciosAdquiridos;
import hotel.reportes.GeneradorReportes;

import java.util.Scanner;

public class Main {

    private static ListaEnlazada<Huesped> huespedes = new ListaEnlazada<>();
    private static ListaEnlazada<Habitacion> habitaciones = new ListaEnlazada<>();
    private static ListaEnlazada<CheckIn> registrosCheckIn = new ListaEnlazada<>();
    private static ListaEnlazada<RegistroPago> registrosPago = new ListaEnlazada<>();
    private static ColaAtencion<Huesped> colaAtencion = new ColaAtencion<>();
    private static ColaServicioMasaje<Huesped> colaMasajes = new ColaServicioMasaje<>(5);
    private static PilaServiciosAdquiridos<CheckIn> pilaServicios = new PilaServiciosAdquiridos<>();
    private static int[] pagosPorMedio = new int[MedioPago.values().length];
    private static Scanner scanner = new Scanner(System.in);
    private static GeneradorReportes generadorReportes; // Instancia única del generador de reportes

    public static void main(String[] args) {
        inicializarHabitaciones();
        generadorReportes = new GeneradorReportes(registrosCheckIn, registrosPago, pilaServicios, colaMasajes, colaAtencion);
        mostrarMenu();
    }

    private static void inicializarHabitaciones() {
        habitaciones.agregarAlFinal(new Habitacion(101, TipoHabitacion.SENCILLA));
        habitaciones.agregarAlFinal(new Habitacion(102, TipoHabitacion.SENCILLA));
        habitaciones.agregarAlFinal(new Habitacion(201, TipoHabitacion.DOBLE));
        habitaciones.agregarAlFinal(new Habitacion(202, TipoHabitacion.DOBLE));
        habitaciones.agregarAlFinal(new Habitacion(301, TipoHabitacion.FAMILIAR));
    }

    private static void mostrarMenu() {
        int opcion;
        do {
            System.out.println("\n--- Menú del Hotel ---");
            System.out.println("1. Llegada de huésped (Cola de atención)");
            System.out.println("2. Atender huésped (Check-in)");
            System.out.println("3. Mostrar habitaciones disponibles");
            System.out.println("4. Huésped adquiere servicio");
            System.out.println("5. Check-out de huésped");
            System.out.println("6. Mostrar reportes");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir la nueva línea

            switch (opcion) {
                case 1:
                    agregarHuespedCola();
                    break;
                case 2:
                    realizarCheckIn();
                    break;
                case 3:
                    mostrarHabitacionesDisponibles();
                    break;
                case 4:
                    adquirirServicio();
                    break;
                case 5:
                    realizarCheckOut();
                    break;
                case 6:
                    mostrarReportesMenu();
                    break;
                case 0:
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción inválida. Intente de nuevo.");
            }
        } while (opcion != 0);
    }

    private static void agregarHuespedCola() {
        System.out.print("Ingrese documento del huésped: ");
        String documento = scanner.nextLine();
        System.out.print("Ingrese nombre del huésped: ");
        String nombre = scanner.nextLine();
        System.out.print("Ingrese edad del huésped: ");
        int edad = scanner.nextInt();
        scanner.nextLine(); // Consumir la nueva línea
        System.out.print("Ingrese género del huésped (MASCULINO, FEMENINO, OTRO): ");
        String generoStr = scanner.nextLine().toUpperCase();
        Genero genero = Genero.valueOf(generoStr);

        Huesped nuevoHuesped = new Huesped(documento, nombre, edad, genero);
        colaAtencion.encolar(nuevoHuesped);
        generadorReportes.registrarTamanoColaAtencion(); // Registrar tamaño al encolar
        System.out.println("Huésped " + nombre + " agregado a la cola de atención.");
    }

    private static void realizarCheckIn() {
        if (colaAtencion.estaVacia()) {
            System.out.println("La cola de atención está vacía.");
            return;
        }
        Huesped huesped = colaAtencion.desencolar();
        generadorReportes.registrarTamanoColaAtencion(); // Registrar tamaño al desencolar
        System.out.println("Atendiendo al huésped: " + huesped.getNombre());

        ListaEnlazada<Habitacion> habitacionesDisponibles = obtenerHabitacionesDisponibles();

        if (habitacionesDisponibles.estaVacia()) {
            System.out.println("No hay habitaciones disponibles en este momento.");
            colaAtencion.encolar(huesped); // Devolver al huésped a la cola si no hay habitación
            return;
        }

        System.out.println("--- Habitaciones Disponibles ---");
        for (int i = 0; i < habitacionesDisponibles.obtenerTamano(); i++) {
            System.out.println((i + 1) + ". " + habitacionesDisponibles.obtener(i));
        }

        System.out.print("Seleccione el número de la habitación deseada: ");
        int opcionHabitacion = scanner.nextInt();
        scanner.nextLine(); // Consumir la nueva línea

        if (opcionHabitacion > 0 && opcionHabitacion <= habitacionesDisponibles.obtenerTamano()) {
            Habitacion habitacionSeleccionada = habitacionesDisponibles.obtener(opcionHabitacion - 1);
            habitacionSeleccionada.setEstado(EstadoHabitacion.OCUPADA);
            CheckIn nuevoCheckIn = new CheckIn(huesped, habitacionSeleccionada);
            registrosCheckIn.agregarAlFinal(nuevoCheckIn);
            System.out.println("Check-in realizado para " + huesped.getNombre() + " en la habitación " + habitacionSeleccionada.getNumero());
        } else {
            System.out.println("Opción de habitación inválida. El huésped vuelve a la cola de atención.");
            colaAtencion.encolar(huesped); // Devolver al huésped a la cola por selección inválida
        }
    }

    private static ListaEnlazada<Habitacion> obtenerHabitacionesDisponibles() {
        ListaEnlazada<Habitacion> disponibles = new ListaEnlazada<>();
        for (int i = 0; i < habitaciones.obtenerTamano(); i++) {
            Habitacion habitacion = habitaciones.obtener(i);
            if (habitacion.getEstado() == EstadoHabitacion.DISPONIBLE) {
                disponibles.agregarAlFinal(habitacion);
            }
        }
        return disponibles;
    }

    private static Habitacion encontrarHabitacionDisponible() {
        for (int i = 0; i < habitaciones.obtenerTamano(); i++) {
            Habitacion habitacion = habitaciones.obtener(i);
            if (habitacion.getEstado() == EstadoHabitacion.DISPONIBLE) {
                return habitacion;
            }
        }
        return null;
    }

    private static void mostrarHabitacionesDisponibles() {
        System.out.println("--- Habitaciones Disponibles ---");
        for (int i = 0; i < habitaciones.obtenerTamano(); i++) {
            Habitacion habitacion = habitaciones.obtener(i);
            if (habitacion.getEstado() == EstadoHabitacion.DISPONIBLE) {
                System.out.println(habitacion);
            }
        }
    }

    private static void adquirirServicio() {
        if (registrosCheckIn.estaVacia()) {
            System.out.println("No hay huéspedes registrados.");
            return;
        }

        System.out.println("--- Huéspedes Registrados ---");
        for (int i = 0; i < registrosCheckIn.obtenerTamano(); i++) {
            System.out.println((i + 1) + ". " + registrosCheckIn.obtener(i).getHuesped().getNombre());
        }
        System.out.print("Seleccione el número del huésped: ");
        int indiceHuesped = scanner.nextInt() - 1;
        scanner.nextLine(); // Consumir la nueva línea

        if (indiceHuesped >= 0 && indiceHuesped < registrosCheckIn.obtenerTamano()) {
            CheckIn checkInHuesped = registrosCheckIn.obtener(indiceHuesped);
            System.out.println("\n--- Servicios Disponibles ---");
            System.out.println("1. Restaurante (Desayuno: $" + ServicioRestaurante.PRECIO_DESAYUNO +
                    ", Almuerzo: $" + ServicioRestaurante.PRECIO_ALMUERZO +
                    ", Cena: $" + ServicioRestaurante.PRECIO_CENA + ")");
            System.out.println("2. Lavandería ($" + ServicioLavanderia.PRECIO_POR_PRENDA + " por prenda)");
            System.out.println("3. Masaje ($" + ServicioMasaje.PRECIO_POR_PERSONA + ")");
            System.out.println("4. Caja Fuerte ($" + ServicioCajaFuerte.PRECIO_FIJO + ")");
            System.out.println("5. Alquiler de Vehículo ($" + ServicioAlquilerVehiculo.PRECIO_POR_DIA + " por día)");
            System.out.print("Seleccione el número del servicio que desea adquirir: ");
            int opcionServicio = scanner.nextInt();
            scanner.nextLine(); // Consumir la nueva línea

            Servicio servicioAdquirido = null;
            double costoServicio = 0.0;
            switch (opcionServicio) {
                case 1:
                    System.out.print("Seleccione la comida (DESAYUNO, ALMUERZO, CENA): ");
                    String comida = scanner.nextLine().toUpperCase();
                    servicioAdquirido = new ServicioRestaurante(101, comida);
                    costoServicio = servicioAdquirido.calcularPrecio();
                    break;
                case 2:
                    System.out.print("Ingrese la cantidad de prendas: ");
                    int cantidadPrendas = scanner.nextInt();
                    scanner.nextLine();
                    servicioAdquirido = new ServicioLavanderia(201, cantidadPrendas);
                    costoServicio = servicioAdquirido.calcularPrecio();
                    break;
                case 3:
                    if (!colaMasajes.estaLlena()) {
                        colaMasajes.encolar(checkInHuesped.getHuesped());
                        servicioAdquirido = new ServicioMasaje(301);
                        costoServicio = servicioAdquirido.calcularPrecio();
                        System.out.println("Huésped " + checkInHuesped.getHuesped().getNombre() + " en cola para masaje.");
                    } else {
                        System.out.println("La cola de masajes está llena.");
                    }
                    break;
                case 4:
                    servicioAdquirido = new ServicioCajaFuerte(401);
                    costoServicio = servicioAdquirido.calcularPrecio();
                    break;
                case 5:
                    System.out.print("Ingrese la cantidad de días de alquiler: ");
                    int cantidadDias = scanner.nextInt();
                    scanner.nextLine();
                    servicioAdquirido = new ServicioAlquilerVehiculo(501, cantidadDias);
                    costoServicio = servicioAdquirido.calcularPrecio();
                    break;
                default:
                    System.out.println("Opción de servicio inválida.");
            }

            if (servicioAdquirido != null) {
                RegistroServicio registro = new RegistroServicio(servicioAdquirido);
                checkInHuesped.agregarServicio(registro);
                pilaServicios.apilar(checkInHuesped); // Registrar que este huésped adquirió un servicio
                System.out.println("Servicio " + servicioAdquirido.getNombre() + " adquirido por " + checkInHuesped.getHuesped().getNombre());
            }
        } else {
            System.out.println("Número de huésped inválido.");
        }
    }

    private static void realizarCheckOut() {
        if (registrosCheckIn.estaVacia()) {
            System.out.println("No hay huéspedes registrados para realizar check-out.");
            return;
        }

        System.out.println("--- Huéspedes Registrados ---");
        for (int i = 0; i < registrosCheckIn.obtenerTamano(); i++) {
            System.out.println((i + 1) + ". " + registrosCheckIn.obtener(i).getHuesped().getNombre());
        }
        System.out.print("Seleccione el número del huésped que se retira: ");
        int indiceHuesped = scanner.nextInt() - 1;
        scanner.nextLine(); // Consumir la nueva línea

        if (indiceHuesped >= 0 && indiceHuesped < registrosCheckIn.obtenerTamano()) {
            CheckIn checkInHuesped = registrosCheckIn.obtener(indiceHuesped);
            System.out.print("Ingrese la cantidad de noches de estadía: ");
            int numeroNoches = scanner.nextInt();
            scanner.nextLine(); // Consumir la nueva línea

            double costoEstadia = checkInHuesped.calcularCostoEstadia(numeroNoches);
            double costoServicios = checkInHuesped.calcularCostoServicios();
            double descuento = hotel.descuentos.CalculadorDescuentos.calcularDescuento(checkInHuesped.getHuesped());
            double costoTotalSinDescuento = costoEstadia + costoServicios;
            double descuentoAplicado = costoTotalSinDescuento * descuento;
            double costoTotalConDescuento = costoTotalSinDescuento - descuentoAplicado;

            System.out.println("\n--- Resumen de la Estadía de " + checkInHuesped.getHuesped().getNombre() + " ---");
            System.out.println("Costo de la habitación (" + numeroNoches + " noches): $" + String.format("%.2f", costoEstadia));
            System.out.println("Costo de los servicios adquiridos: $" + String.format("%.2f", costoServicios));
            System.out.println("Descuento aplicado (" + String.format("%.2f", descuento * 100) + "%): $" + String.format("%.2f", descuentoAplicado));
            System.out.println("Valor total a cancelar: $" + String.format("%.2f", costoTotalConDescuento));

            System.out.print("Seleccione el medio de pago (EFECTIVO, TARJETA_DEBITO, TARJETA_CREDITO): ");
            String medioPagoStr = scanner.nextLine().toUpperCase();
            MedioPago medioPago = MedioPago.valueOf(medioPagoStr);
            registrosPago.agregarAlFinal(new RegistroPago(checkInHuesped, medioPago, costoTotalConDescuento, descuentoAplicado));
            pagosPorMedio[medioPago.ordinal()]++;

            checkInHuesped.getHabitacion().setEstado(EstadoHabitacion.DISPONIBLE);
            registrosCheckIn.eliminar(checkInHuesped); // Eliminar al huésped de los registros activos

            System.out.println("Check-out realizado para " + checkInHuesped.getHuesped().getNombre() + ". ¡Gracias por su visita!");

        } else {
            System.out.println("Número de huésped inválido.");
        }
    }

    private static void mostrarReportesMenu() {
        int opcionReporte;
        do {
            System.out.println("\n--- Menú de Reportes ---");
            System.out.println("1. Cantidad de huéspedes alojados");
            System.out.println("2. Cantidad de huéspedes por género");
            System.out.println("3. Valor total recaudado por género");
            System.out.println("4. Monto total recaudado en el hotel");
            System.out.println("5. Monto total recaudado por medio de pago");
            System.out.println("6. Monto total recaudado por servicio");
            System.out.println("7. Monto total de descuentos hechos");
            System.out.println("8. Monto total por rango de edad");
            System.out.println("9. Huésped que más pagó y el que menos pagó");
            System.out.println("10. Porcentaje de huéspedes atendidos por rango de edad");
            System.out.println("11. Porcentaje recaudado de cada servicio");
            System.out.println("12. Porcentaje de huéspedes por medio de pago");
            System.out.println("13. Porcentaje de huéspedes por género");
            System.out.println("14. Total de dinero recaudado por rango de edad");
            System.out.println("15. Cantidad de personas en cola para masajes");
            System.out.println("16. Promedio de personas que han usado el servicio de masajes (requiere lógica adicional)");
            System.out.println("17. Promedio de personas en cola de atención (requiere lógica adicional)");
            System.out.println("0. Volver al menú principal");
            System.out.print("Seleccione una opción de reporte: ");
            opcionReporte = scanner.nextInt();
            scanner.nextLine(); // Consumir la nueva línea

            switch (opcionReporte) {
                case 1:
                    generadorReportes.mostrarCantidadHuespedesAlojados();
                    break;
                case 2:
                    generadorReportes.mostrarCantidadHuespedesPorGenero();
                    break;
                case 3:
                    generadorReportes.mostrarValorTotalRecaudadoPorGenero();
                    break;
                case 4:
                    generadorReportes.mostrarMontoTotalRecaudadoHotel();
                    break;
                case 5:
                    generadorReportes.mostrarPorcentajeRecaudadoPorMedioPago();
                    break;
                case 6:
                    generadorReportes.mostrarMontoTotalRecaudadoPorServicio();
                    break;
                case 7:
                    generadorReportes.mostrarMontoTotalDescuentosHechos();
                    break;
                case 8:
                    generadorReportes.mostrarMontoTotalPorRangoEdad();
                    break;
                case 9:
                    generadorReportes.mostrarHuespedMasYMenosPago();
                    break;
                case 10:
                    generadorReportes.mostrarPorcentajeHuespedesPorRangoEdad();
                    break;
                case 11:
                    generadorReportes.mostrarPorcentajeRecaudadoPorServicio();
                    break;
                case 12:
                    generadorReportes.mostrarMontoTotalRecaudadoPorMedioPago();
                    break;
                case 13:
                    generadorReportes.mostrarPorcentajeHuespedesPorGenero();
                    break;
                case 14:
                    generadorReportes.mostrarTotalDineroRecaudadoPorRangoEdad();
                    break;
                case 15:
                    generadorReportes.mostrarCantidadPersonasEnColaMasajes();
                    break;
                case 16:
                    generadorReportes.mostrarPromedioPersonasHanUsadoMasajes();
                    break;
                case 17:
                    generadorReportes.mostrarPromedioPersonasEnColaAtencion();
                    break;
                case 0:
                    System.out.println("Volviendo al menú principal...");
                    break;
                default:
                    System.out.println("Opción de reporte inválida. Intente de nuevo.");
            }
        } while (opcionReporte != 0);
    }
}