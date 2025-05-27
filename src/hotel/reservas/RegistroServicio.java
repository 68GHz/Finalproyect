package hotel.reservas;

import hotel.servicios.Servicio;

public class RegistroServicio {
    private Servicio servicio;
    // Podríamos añadir más información como la fecha y hora de adquisición

    public RegistroServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public double getPrecio() {
        return servicio.calcularPrecio();
    }

    @Override
    public String toString() {
        return "Servicio: " + servicio.getNombre() + ", Precio: $" + String.format("%.2f", getPrecio());
    }
}