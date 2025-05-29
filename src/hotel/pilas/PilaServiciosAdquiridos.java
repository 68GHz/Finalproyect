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
        pila.agregarAlFinal(elemento); //simula la parte superior de la pila al final de la lista
    }

    public T desapilar() {
        if (estaVacia()) {
            return null;
        }
        int ultimoIndice = pila.obtenerTamano() - 1;
        T elemento = pila.obtener(ultimoIndice);
        //metodo para eliminar el ultimo elemento de la lista
        eliminarUltimo();
        return elemento;
    }

    private void eliminarUltimo() {
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