import jade.core.Agent;


public class PezAgente extends Agent {

    @Override
    protected void setup() {
        addBehaviour(new PezComportamiento());
    }


}
