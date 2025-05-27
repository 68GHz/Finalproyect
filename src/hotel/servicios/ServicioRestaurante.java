package hotel.servicios;

public class ServicioRestaurante extends Servicio {
    public static final double PRECIO_DESAYUNO = 10000;
    public static final double PRECIO_ALMUERZO = 12000;
    public static final double PRECIO_CENA = 14000;

    private String comida; // Puede ser "desayuno", "almuerzo" o "cena"

    public ServicioRestaurante(int codigo, String comida) {
        super(codigo, "Restaurante - " + comida, 0, TipoServicio.RESTAURANTE);
        this.comida = comida;
        // El precio base se establece en 0 y se calculará en calcularPrecio()
    }

    public String getComida() {
        return comida;
    }

    @Override
    public double calcularPrecio() {
        switch (comida.toLowerCase()) {
            case "desayuno":
                return PRECIO_DESAYUNO;
            case "almuerzo":
                return PRECIO_ALMUERZO;
            case "cena":
                return PRECIO_CENA;
            default:
                return 0; // O lanzar una excepción si la comida no es válida
        }
    }

    @Override
    public String toString() {
        return super.toString() + ", Comida: " + comida + ", Precio: $" + String.format("%.2f", calcularPrecio());
    }
}