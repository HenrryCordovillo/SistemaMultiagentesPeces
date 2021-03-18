

import jade.core.behaviours.CyclicBehaviour;

import java.util.ArrayList;

public class PezComportamiento extends CyclicBehaviour {
    // Constantes
    public static final double PASO = 2;
    public static final double DISTANCIA_MIN = 5;
    public static final double DISTANCIA_MIN_CUADRADO = 25;
    public static final double DISTANCIA_MAX = 40;
    public static final double DISTANCIA_MAX_CUADRADO = 1600;
    // Atributos
    protected double velocidadX;
    protected double velocidadY;
    protected double posXpez;
    protected double posYpez;

    public PezComportamiento(){

    }



    public PezComportamiento(double _x, double _y, double _dir) {
        posXpez = _x;
        posYpez = _y;
        velocidadX = Math.cos(_dir);
        velocidadY = Math.sin(_dir);
    }



    // Métodos
    public double getVelocidadX() {
        return velocidadX;
    }

    public double getVelocidadY() {
        return velocidadY;
    }

    public double DistanciaCuadradoPez(PezComportamiento p) {
        return (p.posXpez - posXpez) * (p.posXpez - posXpez) + (p.posYpez - posYpez) * (p.posYpez - posYpez);
    }
    public double DistanciaCuadradoZona(ZonaAEvitar o) {
        return (o.posXzona - posXpez) * (o.posXzona - posXpez) + (o.posYzona - posYpez) * (o.posYzona - posYpez);
    }

    protected void ActualizarPosicion() {
        posXpez += PASO * velocidadX;
        posYpez += PASO * velocidadY;
    }

    protected boolean EnAlineacion(PezComportamiento p) {
        double distanciaCuadrado = DistanciaCuadradoPez(p);
        return (distanciaCuadrado < DISTANCIA_MAX_CUADRADO &&
                distanciaCuadrado > DISTANCIA_MIN_CUADRADO);
    }

    protected double DistanciaAlMuro(double muroXMin, double muroYMin, double muroXMax, double muroYMax) {
        double min = Math.min(posXpez - muroXMin, posYpez - muroYMin);
        min = Math.min(min, muroXMax - posXpez);
        min = Math.min(min, muroYMax - posYpez);
        return min;
    }

    protected void Normalizar() {
        double longitud = Math.sqrt(velocidadX * velocidadX +
                velocidadY * velocidadY);
        velocidadX /= longitud;
        velocidadY /= longitud;
    }

    //////////////////////////////////////////////////Comportamiento 1//////////////////////////////////////////////////
    protected boolean EvitarMuros(double muroXMin, double muroYMin, double muroXMax, double muroYMax) {
        PararEnMuro(muroXMin, muroYMin, muroXMax, muroYMax);
        double distancia = DistanciaAlMuro(muroXMin, muroYMin, muroXMax, muroYMax);
        if (distancia < DISTANCIA_MIN) {
            CambiarDireccionMuro(distancia, muroXMin, muroYMin, muroXMax, muroYMax);
            Normalizar();
            return true;
        }
        return false;
    }

    private void PararEnMuro(double muroXMin, double muroYMin, double muroXMax, double muroYMax) {
        if (posXpez < muroXMin) {
            posXpez = muroXMin;
        } else if (posYpez < muroYMin) {
            posYpez = muroYMin;
        } else if (posXpez > muroXMax) {
            posXpez = muroXMax;
        } else if (posYpez > muroYMax) {
            posYpez = muroYMax;
        }
    }

    private void CambiarDireccionMuro(double distancia, double muroXMin, double muroYMin, double muroXMax, double muroYMax) {
        if (distancia == (posXpez - muroXMin)) {
            velocidadX += 0.3;
        } else if (distancia == (posYpez - muroYMin)) {
            velocidadY += 0.3;
        } else if (distancia == (muroXMax - posXpez)) {
            velocidadX -= 0.3;
        } else if (distancia == (muroYMax - posYpez)) {
            velocidadY -= 0.3;
        }
    }

    ////////////////////////////////////////Comportamiento 2////////////////////////////////////////////////////////////
    protected boolean EvitarObstaculos(ArrayList<ZonaAEvitar> obstaculos) {
        if (!obstaculos.isEmpty()) {
            // Búsqueda del obstáculo más cercano
            ZonaAEvitar obstaculoProximo = obstaculos.get(0);
            double distanciaCuadrado =  DistanciaCuadradoZona(obstaculoProximo);
            for (ZonaAEvitar o : obstaculos) {
                if (DistanciaCuadradoZona(o) < distanciaCuadrado) {
                    obstaculoProximo = o;
                    distanciaCuadrado = DistanciaCuadradoZona(o);
                }
            }
            if (distanciaCuadrado < (4 * obstaculoProximo.radio *
                    obstaculoProximo.radio)) {
                // Si colisiona, se calcula el vector diff
                double distancia = Math.sqrt(distanciaCuadrado);
                double diffX = (obstaculoProximo.posXzona - posXpez) / distancia;
                double diffY = (obstaculoProximo.posYzona - posYpez) / distancia;
                velocidadX = velocidadX - diffX / 2;
                velocidadY = velocidadY - diffY / 2;
                Normalizar();
                return true;
            }
        }
        return false;
    }

    ////////////////////////////////////////Comportamiento 3////////////////////////////////////////////////////////////
    protected boolean EvitarPeces(PezComportamiento[] peces) {
        // Búsqueda del pez más cercano
        PezComportamiento p;
        if (!peces[0].equals(this)) {
            p = peces[0];
        } else {
            p = peces[1];
        }
        double distanciaCuadrado = DistanciaCuadradoPez(p);
        for (PezComportamiento pezComportamiento : peces) {
            if (DistanciaCuadradoPez(pezComportamiento) < distanciaCuadrado && !pezComportamiento.equals(this)) {
                p = pezComportamiento;
                distanciaCuadrado = DistanciaCuadradoPez(p);
            }
        }
        // Evitar
        if (distanciaCuadrado < DISTANCIA_MIN_CUADRADO) {
            double distancia = Math.sqrt(distanciaCuadrado);
            double diffX = (p.posXpez - posXpez) / distancia;
            double diffY = (p.posYpez - posYpez) / distancia;
            velocidadX = velocidadX - diffX / 4;
            velocidadY = velocidadY - diffY / 4;
            Normalizar();
            return true;
        }
        return false;
    }

    ////////////////////////////////////////Comportamiento 4////////////////////////////////////////////////////////////
    protected void CalcularDireccionMedia(PezComportamiento[] peces) {
        double velocidadXTotal = 0;
        double velocidadYTotal = 0;
        int numTotal = 0;
        for (PezComportamiento p : peces) {
            if (EnAlineacion(p)) {
                velocidadXTotal += p.velocidadX;
                velocidadYTotal += p.velocidadY;
                numTotal++;
            }
        }
        if (numTotal >= 1) {
            velocidadX = (velocidadXTotal / numTotal + velocidadX) / 2;
            velocidadY = (velocidadYTotal / numTotal + velocidadY) / 2;
            Normalizar();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    protected void Actualizar(PezComportamiento[] peces, ArrayList<ZonaAEvitar> obstaculos, double ancho, double alto) {
        if (!EvitarMuros(0, 0, ancho, alto)) {
            if (!EvitarObstaculos(obstaculos)) {
                if (!EvitarPeces(peces)) {
                    CalcularDireccionMedia(peces);
                }
            }
        }
        ActualizarPosicion();
    }




































    @Override
    public void action() {
        System.out.println("Hola naci soy: "+ myAgent.getName());
        //myAgent.doDelete();
    }





}



