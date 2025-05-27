package hotel.servicios;

public class ServicioMasaje extends Servicio {
    public static final double PRECIO_POR_PERSONA = 25000;

    public ServicioMasaje(int codigo) {
        super(codigo, "Masaje", PRECIO_POR_PERSONA, TipoServicio.MASAJE);
    }

    @Override
    public double calcularPrecio() {
        return getPrecioBase();
    }
}