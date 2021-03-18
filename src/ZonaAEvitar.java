public class ZonaAEvitar{
    protected double radio;
    protected int tiempoRestante = 500;
    protected double posXzona;
    protected double posYzona;

    public ZonaAEvitar(double _x, double _y, double _radio) {
        posXzona = _x;
        posYzona = _y;
        radio = _radio;
    }

    public double getRadio() {
        return radio;
    }
    public void Actualizar() {
        tiempoRestante--;
    }
    public boolean estaMuerto() {
        return tiempoRestante <= 0;
    }


}

