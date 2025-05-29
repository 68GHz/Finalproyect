package hotel.colas;

import hotel.util.ListaEnlazada;

public class ColaAtencion<T> {
    private ListaEnlazada<T> cola; //tipo generico <T>

    public ColaAtencion() {
        this.cola = new ListaEnlazada<>(); //el constructor de ListaEnlazada también es generico
    }

    public boolean estaVacia() {
        return cola.estaVacia();
    }

    public void encolar(T elemento) {
        cola.agregarAlFinal(elemento);
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
}