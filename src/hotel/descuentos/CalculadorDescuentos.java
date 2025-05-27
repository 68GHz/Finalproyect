package hotel.descuentos;

import hotel.huespedes.Huesped;

public class CalculadorDescuentos {

    public static double calcularDescuento(Huesped huesped) {
        int edad = huesped.getEdad();
        if (edad < 5) {
            return 0.15; // 15% de descuento
        } else if (edad > 60) {
            return 0.25; // 25% de descuento
        } else {
            return 0.05; // 5% de descuento
        }
    }
}