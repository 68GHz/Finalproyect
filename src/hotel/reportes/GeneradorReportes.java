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

    //clase interna para ayudar con el conteo por genero
    private static class ConteoGenero {
        Genero genero;
        int cantidad;

        public ConteoGenero(Genero genero, int cantidad) {
            this.genero = genero;
            this.cantidad = cantidad;
        }
    }

    public void mostrarValorTotalRecaudadoPorGenero() {
        ListaEnlazada<ConteoMontoGenero> montosPorGenero = new ListaEnlazada<>();

        for (int i = 0; i < registrosPago.obtenerTamano(); i++) {
            RegistroPago registro = registrosPago.obtener(i);
            Genero generoHuesped = registro.getCheckIn().getHuesped().getGenero();
            double montoPagado = registro.getMonto();

            boolean encontrado = false;
            for (int j = 0; j < montosPorGenero.obtenerTamano(); j++) {
                ConteoMontoGenero item = montosPorGenero.obtener(j);
                if (item.genero == generoHuesped) {
                    item.monto += montoPagado;
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado) {
                montosPorGenero.agregarAlFinal(new ConteoMontoGenero(generoHuesped, montoPagado));
            }
        }

        System.out.println("--- Valor total recaudado por género ---");
        for (int i = 0; i < montosPorGenero.obtenerTamano(); i++) {
            ConteoMontoGenero item = montosPorGenero.obtener(i);
            System.out.println("- " + item.genero + ": $" + String.format("%.2f", item.monto));
        }
    }

    //clase interna para ayudar con el conteo y monto por genero
    private static class ConteoMontoGenero {
        Genero genero;
        double monto;

        public ConteoMontoGenero(Genero genero, double monto) {
            this.genero = genero;
            this.monto = monto;
        }
    }

    public void mostrarPorcentajeRecaudadoPorMedioPago() {
        double[] montosPorMedioPago = new double[MedioPago.values().length];
        double montoTotalHotel = 0;

        for (int i = 0; i < registrosPago.obtenerTamano(); i++) {
            RegistroPago registro = registrosPago.obtener(i);
            MedioPago medioPago = registro.getMedioPago();
            montosPorMedioPago[medioPago.ordinal()] += registro.getMonto();
            montoTotalHotel += registro.getMonto();
        }

        System.out.println("--- Porcentaje recaudado por medio de pago ---");
        if (montoTotalHotel > 0) {
            for (MedioPago medioPago : MedioPago.values()) {
                double porcentaje = (montosPorMedioPago[medioPago.ordinal()] / montoTotalHotel) * 100;
                System.out.println("- " + medioPago + ": " + String.format("%.2f", porcentaje) + "%");
            }
        } else {
            System.out.println("No hay ingresos registrados para calcular el porcentaje por medio de pago.");
        }
    }

    public void mostrarMontoTotalRecaudadoPorServicio() {
        ListaEnlazada<ConteoServicio> montosPorServicio = new ListaEnlazada<>();

        for (int i = 0; i < registrosCheckIn.obtenerTamano(); i++) {
            CheckIn checkIn = registrosCheckIn.obtener(i);
            for (int j = 0; j < checkIn.getServiciosAdquiridos().obtenerTamano(); j++) {
                RegistroServicio registroServicio = checkIn.getServiciosAdquiridos().obtener(j);
                TipoServicio tipoServicio = registroServicio.getServicio().getTipo();
                double costoServicio = registroServicio.getServicio().calcularPrecio();

                boolean encontrado = false;
                for (int k = 0; k < montosPorServicio.obtenerTamano(); k++) {
                    ConteoServicio item = montosPorServicio.obtener(k);
                    if (item.tipoServicio == tipoServicio) {
                        item.monto += costoServicio;
                        encontrado = true;
                        break;
                    }
                }
                if (!encontrado) {
                    montosPorServicio.agregarAlFinal(new ConteoServicio(tipoServicio, costoServicio));
                }
            }
        }

        System.out.println("--- Monto total recaudado por servicio ---");
        for (int i = 0; i < montosPorServicio.obtenerTamano(); i++) {
            ConteoServicio item = montosPorServicio.obtener(i);
            System.out.println("- " + item.tipoServicio + ": $" + String.format("%.2f", item.monto));
        }
    }

    //clase interna para ayudar con el conteo de servicios
    private static class ConteoServicio {
        TipoServicio tipoServicio;
        double monto;

        public ConteoServicio(TipoServicio tipoServicio, double monto) {
            this.tipoServicio = tipoServicio;
            this.monto = monto;
        }
    }

    public void mostrarMontoTotalDescuentosHechos() {
        double totalDescuentos = 0;
        for (int i = 0; i < registrosPago.obtenerTamano(); i++) {
            totalDescuentos += registrosPago.obtener(i).getDescuentoAplicado();
        }
        System.out.println("Monto total de descuentos hechos: $" + String.format("%.2f", totalDescuentos));
    }

    public void mostrarMontoTotalPorRangoEdad() {
        double[] montosPorRango = new double[4]; // Ejemplo de 4 rangos de edad
        // Rangos de edad: 0-12 (Niños), 13-25 (Jovenes), 26-60 (Adultos), >60 (Adultos Mayores)

        for (int i = 0; i < registrosPago.obtenerTamano(); i++) {
            RegistroPago registro = registrosPago.obtener(i);
            int edadHuesped = registro.getCheckIn().getHuesped().getEdad();
            double montoPagado = registro.getMonto();

            if (edadHuesped <= 12) {
                montosPorRango[0] += montoPagado;
            } else if (edadHuesped <= 25) {
                montosPorRango[1] += montoPagado;
            } else if (edadHuesped <= 60) {
                montosPorRango[2] += montoPagado;
            } else {
                montosPorRango[3] += montoPagado;
            }
        }

        System.out.println("--- Monto total recaudado por rango de edad ---");
        System.out.println("- Niños (0-12 años): $" + String.format("%.2f", montosPorRango[0]));
        System.out.println("- Jóvenes (13-25 años): $" + String.format("%.2f", montosPorRango[1]));
        System.out.println("- Adultos (26-60 años): $" + String.format("%.2f", montosPorRango[2]));
        System.out.println("- Adultos Mayores (>60 años): $" + String.format("%.2f", montosPorRango[3]));
    }

    public void mostrarHuespedMasYMenosPago() {
        if (registrosPago.estaVacia()) {
            System.out.println("No hay registros de pago para generar este reporte.");
            return;
        }

        RegistroPago pagoMaximo = registrosPago.obtener(0);
        RegistroPago pagoMinimo = registrosPago.obtener(0);

        for (int i = 1; i < registrosPago.obtenerTamano(); i++) {
            RegistroPago pagoActual = registrosPago.obtener(i);
            if (pagoActual.getMonto() > pagoMaximo.getMonto()) {
                pagoMaximo = pagoActual;
            }
            if (pagoActual.getMonto() < pagoMinimo.getMonto()) {
                pagoMinimo = pagoActual;
            }
        }

        System.out.println("--- Huésped que más pagó y el que menos pagó ---");
        System.out.println("Huésped que más pagó: " + pagoMaximo.getCheckIn().getHuesped().getNombre() + " - Monto: $" + String.format("%.2f", pagoMaximo.getMonto()));
        System.out.println("Huésped que menos pagó: " + pagoMinimo.getCheckIn().getHuesped().getNombre() + " - Monto: $" + String.format("%.2f", pagoMinimo.getMonto()));
    }

    public void mostrarPorcentajeHuespedesPorRangoEdad() {
        int[] conteoPorRango = new int[4]; // ejm
        int totalHuespedes = registrosCheckIn.obtenerTamano();

        if (totalHuespedes == 0) {
            System.out.println("No hay registros de check-in para generar este reporte.");
            return;
        }

        for (int i = 0; i < registrosCheckIn.obtenerTamano(); i++) {
            int edadHuesped = registrosCheckIn.obtener(i).getHuesped().getEdad();
            if (edadHuesped <= 12) {
                conteoPorRango[0]++;
            } else if (edadHuesped <= 25) {
                conteoPorRango[1]++;
            } else if (edadHuesped <= 60) {
                conteoPorRango[2]++;
            } else {
                conteoPorRango[3]++;
            }
        }

        System.out.println("--- Porcentaje de huéspedes atendidos por rango de edad ---");
        System.out.println("- Niños (0-12 años): " + String.format("%.2f", (double) conteoPorRango[0] / totalHuespedes * 100) + "%");
        System.out.println("- Jóvenes (13-25 años): " + String.format("%.2f", (double) conteoPorRango[1] / totalHuespedes * 100) + "%");
        System.out.println("- Adultos (26-60 años): " + String.format("%.2f", (double) conteoPorRango[2] / totalHuespedes * 100) + "%");
        System.out.println("- Adultos Mayores (>60 años): " + String.format("%.2f", (double) conteoPorRango[3] / totalHuespedes * 100) + "%");
    }

    public void mostrarPorcentajeRecaudadoPorServicio() {
        ListaEnlazada<ConteoServicio> montosPorServicio = new ListaEnlazada<>();
        double montoTotalHotel = 0;

        //calcular el monto total recaudado por cada servicio
        for (int i = 0; i < registrosCheckIn.obtenerTamano(); i++) {
            CheckIn checkIn = registrosCheckIn.obtener(i);
            for (int j = 0; j < checkIn.getServiciosAdquiridos().obtenerTamano(); j++) {
                RegistroServicio registroServicio = checkIn.getServiciosAdquiridos().obtener(j);
                TipoServicio tipoServicio = registroServicio.getServicio().getTipo();
                double costoServicio = registroServicio.getPrecio();

                boolean encontrado = false;
                for (int k = 0; k < montosPorServicio.obtenerTamano(); k++) {
                    ConteoServicio item = montosPorServicio.obtener(k);
                    if (item.tipoServicio == tipoServicio) {
                        item.monto += costoServicio;
                        encontrado = true;
                        break;
                    }
                }
                if (!encontrado) {
                    montosPorServicio.agregarAlFinal(new ConteoServicio(tipoServicio, costoServicio));
                }
            }
        }

        //calcular el monto total recaudado en el hotel
        for (int i = 0; i < registrosPago.obtenerTamano(); i++) {
            montoTotalHotel += registrosPago.obtener(i).getMonto();
        }

        System.out.println("--- Porcentaje recaudado de cada servicio ---");
        if (montoTotalHotel > 0) {
            for (int i = 0; i < montosPorServicio.obtenerTamano(); i++) {
                ConteoServicio item = montosPorServicio.obtener(i);
                double porcentaje = (item.monto / montoTotalHotel) * 100;
                System.out.println("- " + item.tipoServicio + ": " + String.format("%.2f", porcentaje) + "%");
            }
        } else {
            System.out.println("No hay ingresos registrados para calcular el porcentaje por servicio.");
        }
    }

    public void mostrarMontoTotalRecaudadoPorMedioPago() {
        double[] montosPorMedioPago = new double[MedioPago.values().length];
        for (int i = 0; i < registrosPago.obtenerTamano(); i++) {
            RegistroPago registro = registrosPago.obtener(i);
            MedioPago medioPago = registro.getMedioPago();
            montosPorMedioPago[medioPago.ordinal()] += registro.getMonto();
        }

        System.out.println("--- Monto total recaudado por medio de pago ---");
        for (MedioPago medioPago : MedioPago.values()) {
            System.out.println("- " + medioPago + ": $" + String.format("%.2f", montosPorMedioPago[medioPago.ordinal()]));
        }
    }

    public void mostrarPorcentajeHuespedesPorGenero() {
        int totalHuespedes = registrosCheckIn.obtenerTamano();
        if (totalHuespedes == 0) {
            System.out.println("No hay registros de check-in para generar este reporte.");
            return;
        }

        ListaEnlazada<ConteoPorcentajeGenero> conteoPorcentajeGenero = new ListaEnlazada<>();
        for (int i = 0; i < registrosCheckIn.obtenerTamano(); i++) {
            Genero genero = registrosCheckIn.obtener(i).getHuesped().getGenero();
            boolean encontrado = false;
            for (int j = 0; j < conteoPorcentajeGenero.obtenerTamano(); j++) {
                ConteoPorcentajeGenero item = conteoPorcentajeGenero.obtener(j);
                if (item.genero == genero) {
                    item.cantidad++;
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado) {
                conteoPorcentajeGenero.agregarAlFinal(new ConteoPorcentajeGenero(genero, 1));
            }
        }

        System.out.println("--- Porcentaje de huéspedes por género ---");
        for (int i = 0; i < conteoPorcentajeGenero.obtenerTamano(); i++) {
            ConteoPorcentajeGenero item = conteoPorcentajeGenero.obtener(i);
            double porcentaje = (double) item.cantidad / totalHuespedes * 100;
            System.out.println("- " + item.genero + ": " + String.format("%.2f", porcentaje) + "%");
        }
    }

    //clase interna para ayudar con el conteo y porcentaje por genero
    private static class ConteoPorcentajeGenero {
        Genero genero;
        int cantidad;

        public ConteoPorcentajeGenero(Genero genero, int cantidad) {
            this.genero = genero;
            this.cantidad = cantidad;
        }
    }

    public void mostrarTotalDineroRecaudadoPorRangoEdad() {
        this.mostrarMontoTotalPorRangoEdad();
    }

    public void mostrarCantidadPersonasEnColaMasajes() {
        System.out.println("Cantidad de personas en cola para masajes: " + colaMasajes.obtenerTamano());
    }
}