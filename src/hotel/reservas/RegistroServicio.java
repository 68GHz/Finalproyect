package hotel.reservas;

import hotel.servicios.Servicio;

public class RegistroServicio {
    private Servicio servicio;
    private double costo;

    public RegistroServicio(Servicio servicio) {
        this.servicio = servicio;
        this.costo = servicio.calcularPrecio();
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