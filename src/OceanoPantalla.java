import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Timer;
import java.util.TimerTask;


public class OceanoPantalla extends JPanel implements MouseListener, PropertyChangeListener{
    protected Oceano oceano;
    protected Timer timer;

    public OceanoPantalla() {
        this.setBackground(new Color(150, 255, 255));
        this.addMouseListener(this);
    }
    public void Ejecutar() {
        oceano = new Oceano(500, this.getWidth(), getHeight());
        oceano.contenedor();
        oceano.agregarChangeListener(this);
        TimerTask tarea = new TimerTask() {
            @Override
            public void run() {
                oceano.ActualizarOceano();
            }
        };
        timer = new Timer();
        timer.scheduleAtFixedRate(tarea, 0, 15);
    }
    protected void DibujarPez(PezComportamiento p, Graphics g) {
        g.drawLine((int) p.posXpez, (int) p.posYpez, (int) (p.posXpez - 10 * p.velocidadX), (int) (p.posYpez - 10 * p.velocidadY));
    }

    protected void DibujarObstaculo(ZonaAEvitar o, Graphics g) {
        g.drawOval((int) (o.posXzona - o.radio), (int) (o.posYzona - o.radio), (int) o.radio * 2, (int) o.radio * 2);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (PezComportamiento p : oceano.peces) {
            DibujarPez(p, g);
        }
        for (ZonaAEvitar o : oceano.obstaculos) {
            DibujarObstaculo(o, g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        oceano.AgregarObstaculo(e.getX(), e.getY(), 10);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
