package hotel.pilas;

import hotel.util.ListaEnlazada;

public class PilaServiciosAdquiridos<T> {
    private ListaEnlazada<T> pila;

    public PilaServiciosAdquiridos() {
        this.pila = new ListaEnlazada<>();
    }

    public boolean estaVacia() {
        return pila.estaVacia();
    }

    public void apilar(T elemento) {
        pila.agregarAlFinal(elemento); // Simula la parte superior de la pila al final de la lista
    }

    public T desapilar() {
        if (estaVacia()) {
            return null; // O podrías lanzar una excepción
        }
        int ultimoIndice = pila.obtenerTamano() - 1;
        T elemento = pila.obtener(ultimoIndice);
        // Necesitamos un método para eliminar el último elemento de la lista
        eliminarUltimo();
        return elemento;
    }

    private void eliminarUltimo() {
        // Este método requiere modificar la ListaEnlazada
        // Lo implementaremos en la siguiente iteración en ListaEnlazada.java
        // Por ahora, lo dejamos pendiente.
    }

    public T verTope() {
        if (estaVacia()) {
            return null;
        }
        return pila.obtener(pila.obtenerTamano() - 1);
    }

    public int obtenerTamano() {
        return pila.obtenerTamano();
    }
}