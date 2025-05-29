package hotel.pagos;

import hotel.reservas.CheckIn;

public class RegistroPago {
    private CheckIn checkIn;
    private MedioPago medioPago;
    private double monto;
    private double descuentoAplicado;

    public RegistroPago(CheckIn checkIn, MedioPago medioPago, double monto, double descuentoAplicado) {
        this.checkIn = checkIn;
        this.medioPago = medioPago;
        this.monto = monto;
        this.descuentoAplicado = descuentoAplicado;
    }

    public CheckIn getCheckIn() {
        return checkIn;
    }

    public MedioPago getMedioPago() {
        return medioPago;
    }

    public double getMonto() {
        return monto;
    }

    public double getDescuentoAplicado() {
        return descuentoAplicado;
    }

    @Override
    public String toString() {
        return "Huésped: " + checkIn.getHuesped().getNombre() + ", Medio de Pago: " + medioPago + ", Monto: $" + String.format("%.2f", monto);
    }
}