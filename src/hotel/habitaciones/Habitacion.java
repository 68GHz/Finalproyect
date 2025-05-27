package hotel.habitaciones;

public class Habitacion {
    private int numero;
    private TipoHabitacion tipo;
    private EstadoHabitacion estado;

    public Habitacion(int numero, TipoHabitacion tipo) {
        this.numero = numero;
        this.tipo = tipo;
        this.estado = EstadoHabitacion.DISPONIBLE; // Inicialmente, todas las habitaciones están disponibles
    }

    // Métodos getter para todos los atributos
    public int getNumero() {
        return numero;
    }

    public TipoHabitacion getTipo() {
        return tipo;
    }

    public EstadoHabitacion getEstado() {
        return estado;
    }

    // Métodos setter
    public void setEstado(EstadoHabitacion estado) {
        this.estado = estado;
    }

    public String toString() {
        return "Número: " + numero + ", Tipo: " + tipo + ", Estado: " + estado;
    }
}