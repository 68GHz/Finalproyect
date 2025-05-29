package hotel.huespedes;

public class Huesped {
    private String documento;
    private String nombre;
    private int edad;
    private Genero genero;

    public Huesped(String documento, String nombre, int edad, Genero genero) {
        this.documento = documento;
        this.nombre = nombre;
        this.edad = edad;
        this.genero = genero;
    }

    // Métodos getter para todos los atributos
    public String getDocumento() {
        return documento;
    }

    public String getNombre() {
        return nombre;
    }

    public int getEdad() {
        return edad;
    }

    public Genero getGenero() {
        return genero;
    }

    //setter
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public String toString() {
        return "Documento: " + documento + ", Nombre: " + nombre + ", Edad: " + edad + ", Género: " + genero;
    }
}