package hotel.reportes;

import hotel.huespedes.Genero;
import hotel.huespedes.Huesped;
import hotel.pagos.MedioPago;
import hotel.reservas.CheckIn;
import hotel.reservas.RegistroServicio;
import hotel.servicios.TipoServicio;
import hotel.util.ListaEnlazada;
import hotel.pilas.PilaServiciosAdquiridos;
import hotel.colas.ColaServicioMasaje;
import hotel.colas.ColaAtencion;
import hotel.pagos.RegistroPago;

public class GeneradorReportes {

    private ListaEnlazada<CheckIn> registrosCheckIn;
    private ListaEnlazada<RegistroPago> registrosPago;
    private PilaServiciosAdquiridos<CheckIn> pilaServicios;
    private ColaServicioMasaje<?> colaMasajes;
    private ColaAtencion<?> colaAtencion;
    private ListaEnlazada<Integer> tamanosColaAtencion = new ListaEnlazada<>();
    private int serviciosMasajeRealizados = 0;

    public GeneradorReportes(ListaEnlazada<CheckIn> registrosCheckIn, ListaEnlazada<RegistroPago> registrosPago, PilaServiciosAdquiridos<CheckIn> pilaServicios, ColaServicioMasaje<?> colaMasajes, ColaAtencion<?> colaAtencion) {
        this.registrosCheckIn = registrosCheckIn;
        this.registrosPago = registrosPago;
        this.pilaServicios = pilaServicios;
        this.colaMasajes = colaMasajes;
        this.colaAtencion = colaAtencion;
    }

    public void registrarTamanoColaAtencion() {
        this.tamanosColaAtencion.agregarAlFinal(colaAtencion.obtenerTamano());
    }

    public void incrementarServiciosMasajeRealizados() {
        this.serviciosMasajeRealizados++;
    }

    public void mostrarCantidadHuespedesAlojados() {
        System.out.println("Cantidad de huéspedes alojados: " + registrosCheckIn.obtenerTamano());
    }

    public void mostrarCantidadHuespedesPorGenero() {
        ListaEnlazada<ConteoGenero> conteoGenero = new ListaEnlazada<>();
        for (int i = 0; i < registrosCheckIn.obtenerTamano(); i++) {
            Huesped huesped = registrosCheckIn.obtener(i).getHuesped();
            Genero genero = huesped.getGenero();
            boolean encontrado = false;
            for (int j = 0; j < conteoGenero.obtenerTamano(); j++) {
                ConteoGenero item = conteoGenero.obtener(j);
                if (item.genero == genero) {
                    item.cantidad++;
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado) {
                conteoGenero.agregarAlFinal(new ConteoGenero(genero, 1));
            }
        }
        System.out.println("Cantidad de huéspedes por género:");
        for (int i = 0; i < conteoGenero.obtenerTamano(); i++) {
            ConteoGenero item = conteoGenero.obtener(i);
            System.out.println("- " + item.genero + ": " + item.cantidad);
        }
    }

    public void mostrarMontoTotalRecaudadoHotel() {
        double montoTotal = 0;
        for (int i = 0; i < registrosPago.obtenerTamano(); i++) {
            montoTotal += registrosPago.obtener(i).getMonto();
        }
        System.out.println("Monto total recaudado en el hotel: $" + String.format("%.2f", montoTotal));
    }

    public void mostrarPromedioPersonasEnColaAtencion() {
        if (tamanosColaAtencion.estaVacia()) {
            System.out.println("No hay datos para calcular el promedio de la cola de atención.");
            return;
        }
        double suma = 0;
        for (int i = 0; i < tamanosColaAtencion.obtenerTamano(); i++) {
            suma += tamanosColaAtencion.obtener(i);
        }
        double promedio = suma / tamanosColaAtencion.obtenerTamano();
        System.out.println("Promedio de personas que han estado en cola de atención: " + String.format("%.2f", promedio));
    }

    public void mostrarPromedioPersonasHanUsadoMasajes() {
        if (registrosCheckIn.estaVacia()) {
            System.out.println("No hay registros de huéspedes.");
            return;
        }
        int totalHuespedesConMasaje = 0;
        for (int i = 0; i < registrosCheckIn.obtenerTamano(); i++) {
            CheckIn checkIn = registrosCheckIn.obtener(i);
            for (int j = 0; j < checkIn.getServiciosAdquiridos().obtenerTamano(); j++) {
                if (checkIn.getServiciosAdquiridos().obtener(j).getServicio().getTipo() == TipoServicio.MASAJE) {
                    totalHuespedesConMasaje++;
                    break;
                }
            }
        }

        if (totalHuespedesConMasaje > 0) {
            System.out.println("Promedio de personas que han adquirido el servicio de masaje: " +
                    (double) totalHuespedesConMasaje / registrosCheckIn.obtenerTamano());
        } else {
            System.out.println("Ningún huésped ha adquirido el servicio de masaje.");
        }
    }

    // Clase interna para ayudar con el conteo por género
    private static class ConteoGenero {
        Genero genero;
        int cantidad;

        public ConteoGenero(Genero genero, int cantidad) {
            this.genero = genero;
            this.cantidad = cantidad;
        }
    }

    // Puedes ir implementando los otros reportes de manera similar, usando ListaEnlazada
    // para agrupar y contar la información.
    // Por ahora, dejaremos los otros métodos sin implementar para simplificar.

    public void mostrarValorTotalRecaudadoPorGenero() {
        System.out.println("Reporte 'Valor total recaudado por género' no implementado en esta versión simplificada.");
    }

    public void mostrarMontoTotalRecaudadoPorMedioPago() {
        System.out.println("Reporte 'Monto total recaudado por medio de pago' no implementado en esta versión simplificada.");
    }

    public void mostrarMontoTotalRecaudadoPorServicio() {
        System.out.println("Reporte 'Monto total recaudado por servicio' no implementado en esta versión simplificada.");
    }

    public void mostrarMontoTotalDescuentosHechos() {
        System.out.println("Reporte 'Monto total de descuentos hechos' no implementado en esta versión simplificada.");
    }

    public void mostrarMontoTotalPorRangoEdad() {
        System.out.println("Reporte 'Monto total por rango de edad' no implementado en esta versión simplificada.");
    }

    public void mostrarHuespedMasYMenosPago() {
        System.out.println("Reporte 'Huésped que más pagó y el que menos pagó' no implementado en esta versión simplificada.");
    }

    public void mostrarPorcentajeHuespedesPorRangoEdad() {
        System.out.println("Reporte 'Porcentaje de huéspedes atendidos por rango de edad' no implementado en esta versión simplificada.");
    }

    public void mostrarPorcentajeRecaudadoPorServicio() {
        System.out.println("Reporte 'Porcentaje recaudado de cada servicio' no implementado en esta versión simplificada.");
    }

    public void mostrarPorcentajeHuespedesPorMedioPago() {
        System.out.println("Reporte 'Porcentaje de huéspedes por medio de pago' no implementado en esta versión simplificada.");
    }

    public void mostrarPorcentajeHuespedesPorGenero() {
        System.out.println("Reporte 'Porcentaje de huéspedes por género' no implementado en esta versión simplificada.");
    }

    public void mostrarTotalDineroRecaudadoPorRangoEdad() {
        System.out.println("Reporte 'Total de dinero recaudado por rango de edad' no implementado en esta versión simplificada.");
    }

    public void mostrarCantidadPersonasEnColaMasajes() {
        System.out.println("Cantidad de personas en cola para masajes: " + colaMasajes.obtenerTamano());
    }
}