package hotel.servicios;

public abstract class Servicio {
    private int codigo;
    private String nombre;
    private double precioBase;
    private TipoServicio tipo;

    public Servicio(int codigo, String nombre, double precioBase, TipoServicio tipo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precioBase = precioBase;
        this.tipo = tipo;
    }

    //getter
    public int getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public TipoServicio getTipo() {
        return tipo;
    }

    // metodo abstracto que las clases concretas implementarán para calcular el precio final
    public abstract double calcularPrecio();

    @Override
    public String toString() {
        return "Código: " + codigo + ", Nombre: " + nombre + ", Precio Base: $" + String.format("%.2f", precioBase);
    }
}