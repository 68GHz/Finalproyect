package hotel.util;

public class ListaEnlazada<T> { //generica
    private Nodo<T> cabeza;

    public ListaEnlazada() {
        this.cabeza = null;
    }

    public boolean estaVacia() {
        return cabeza == null;
    }

    public void agregarAlFinal(T dato) {
        Nodo<T> nuevoNodo = new Nodo<>(dato);
        if (estaVacia()) {
            cabeza = nuevoNodo;
            return;
        }
        Nodo<T> actual = cabeza;
        while (actual.siguiente != null) {
            actual = actual.siguiente;
        }
        actual.siguiente = nuevoNodo;
    }

    public T obtener(int indice) {
        if (indice < 0 || estaVacia()) {
            return null;
        }
        Nodo<T> actual = cabeza;
        int contador = 0;
        while (actual != null && contador < indice) {
            actual = actual.siguiente;
            contador++;
        }
        return (actual != null) ? actual.dato : null;
    }

    public void eliminar(T dato) {
        if (estaVacia()) {
            return;
        }
        if (cabeza.dato.equals(dato)) {
            cabeza = cabeza.siguiente;
            return;
        }
        Nodo<T> actual = cabeza;
        while (actual.siguiente != null && !actual.siguiente.dato.equals(dato)) {
            actual = actual.siguiente;
        }
        if (actual.siguiente != null) {
            actual.siguiente = actual.siguiente.siguiente;
        }
    }

    public int obtenerTamano() {
        int contador = 0;
        Nodo<T> actual = cabeza;
        while (actual != null) {
            contador++;
            actual = actual.siguiente;
        }
        return contador;
    }

    //eliminar al inicio
    public T eliminarAlInicio() {
        if (estaVacia()) {
            return null;
        }
        T datoEliminado = cabeza.dato;
        cabeza = cabeza.siguiente;
        return datoEliminado;
    }

    public T eliminarUltimo() {
        if (estaVacia()) {
            return null;
        }
        if (cabeza.siguiente == null) {
            T datoEliminado = cabeza.dato;
            cabeza = null;
            return datoEliminado;
        }
        Nodo<T> actual = cabeza;
        while (actual.siguiente.siguiente != null) {
            actual = actual.siguiente;
        }
        T datoEliminado = actual.siguiente.dato;
        actual.siguiente = null;
        return datoEliminado;
    }
}