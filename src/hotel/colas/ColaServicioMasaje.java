package hotel.colas;

import hotel.util.ListaEnlazada;

public class ColaServicioMasaje<T> {
    private ListaEnlazada<T> cola;
    private int capacidadMaxima;

    public ColaServicioMasaje(int capacidadMaxima) {
        this.cola = new ListaEnlazada<>();
        this.capacidadMaxima = capacidadMaxima;
    }

    public boolean estaVacia() {
        return cola.estaVacia();
    }

    public boolean estaLlena() {
        return cola.obtenerTamano() >= capacidadMaxima;
    }

    public void encolar(T elemento) {
        if (!estaLlena()) {
            cola.agregarAlFinal(elemento);
        } else {
            System.out.println("La cola de servicio de masajes está llena. No se puede agregar al huésped.");
        }
    }

    public T desencolar() {
        return cola.eliminarAlInicio();
    }

    public T verPrimero() {
        return cola.obtener(0);
    }

    public int obtenerTamano() {
        return cola.obtenerTamano();
    }

    public int obtenerCapacidadMaxima() {
        return capacidadMaxima;
    }
}