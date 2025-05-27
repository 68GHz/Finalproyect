package hotel.servicios;

public class ServicioLavanderia extends Servicio {
    public static final double PRECIO_POR_PRENDA = 2000;
    private int cantidadPrendas;

    public ServicioLavanderia(int codigo, int cantidadPrendas) {
        super(codigo, "Lavandería", PRECIO_POR_PRENDA, TipoServicio.LAVANDERIA);
        this.cantidadPrendas = cantidadPrendas;
    }

    public int getCantidadPrendas() {
        return cantidadPrendas;
    }

    public void setCantidadPrendas(int cantidadPrendas) {
        this.cantidadPrendas = cantidadPrendas;
    }

    @Override
    public double calcularPrecio() {
        return getPrecioBase() * cantidadPrendas;
    }

    @Override
    public String toString() {
        return super.toString() + ", Cantidad de prendas: " + cantidadPrendas + ", Precio: $" + String.format("%.2f", calcularPrecio());
    }
}