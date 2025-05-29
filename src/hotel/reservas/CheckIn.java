package hotel.reservas;

import hotel.huespedes.Huesped;
import hotel.habitaciones.Habitacion;
import hotel.util.ListaEnlazada;

public class CheckIn {
    private Huesped huesped;
    private Habitacion habitacion;
    private ListaEnlazada<RegistroServicio> serviciosAdquiridos;

    public CheckIn(Huesped huesped, Habitacion habitacion) {
        this.huesped = huesped;
        this.habitacion = habitacion;
        this.serviciosAdquiridos = new ListaEnlazada<>();
    }

    public Huesped getHuesped() {
        return huesped;
    }

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public ListaEnlazada<RegistroServicio> getServiciosAdquiridos() {
        return serviciosAdquiridos;
    }

    public void agregarServicio(RegistroServicio servicio) {
        this.serviciosAdquiridos.agregarAlFinal(servicio);
    }

    public double calcularCostoEstadia(int numeroNoches) {
        double costoHabitacion = 0;
        switch (habitacion.getTipo()) {
            case SENCILLA:
                costoHabitacion = 155000;
                break;
            case DOBLE:
                costoHabitacion = 80000;
                break;
            case FAMILIAR:
                costoHabitacion = 55000;
                break;
        }
        return costoHabitacion * numeroNoches;
    }

    public double calcularCostoServicios() {
        double costoTotalServicios = 0;
        for (int i = 0; i < serviciosAdquiridos.obtenerTamano(); i++) {
            RegistroServicio registro = serviciosAdquiridos.obtener(i);
            if (registro != null) {
                costoTotalServicios += registro.getPrecio();
            }
        }
        return costoTotalServicios;
    }

    public double calcularCostoTotal(int numeroNoches) {
        return calcularCostoEstadia(numeroNoches) + calcularCostoServicios();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Huésped: ").append(huesped.getNombre()).append(" (").append(huesped.getDocumento()).append(")\n");
        sb.append("Habitación: ").append(habitacion.getNumero()).append(" (").append(habitacion.getTipo()).append(")\n");
        sb.append("Servicios Adquiridos:\n");
        if (serviciosAdquiridos.estaVacia()) {
            sb.append("Ninguno\n");
        } else {
            for (int i = 0; i < serviciosAdquiridos.obtenerTamano(); i++) {
                RegistroServicio servicio = serviciosAdquiridos.obtener(i);
                if (servicio != null) {
                    sb.append("- ").append(servicio).append("\n");
                }
            }
        }
        return sb.toString();
    }
}