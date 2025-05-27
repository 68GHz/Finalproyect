package hotel.servicios;

public class ServicioCajaFuerte extends Servicio {
    public static final double PRECIO_FIJO = 15000;

    public ServicioCajaFuerte(int codigo) {
        super(codigo, "Caja Fuerte", PRECIO_FIJO, TipoServicio.CAJA_FUERTE);
    }

    @Override
    public double calcularPrecio() {
        return getPrecioBase();
    }
}