package hotel.servicios;

public class ServicioAlquilerVehiculo extends Servicio {
    public static final double PRECIO_POR_DIA = 30000;
    private int cantidadDias;

    public ServicioAlquilerVehiculo(int codigo, int cantidadDias) {
        super(codigo, "Alquiler de Vehículo", PRECIO_POR_DIA, TipoServicio.ALQUILER_VEHICULO);
        this.cantidadDias = cantidadDias;
    }

    public int getCantidadDias() {
        return cantidadDias;
    }

    public void setCantidadDias(int cantidadDias) {
        this.cantidadDias = cantidadDias;
    }

    @Override
    public double calcularPrecio() {
        return getPrecioBase() * cantidadDias; // Usamos el getter
    }

    @Override
    public String toString() {
        return super.toString() + ", Cantidad de días: " + cantidadDias + ", Precio: $" + String.format("%.2f", calcularPrecio());
    }
}