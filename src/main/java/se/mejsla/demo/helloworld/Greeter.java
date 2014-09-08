package se.mejsla.demo.helloworld;

import akka.actor.UntypedActor;

public class Greeter extends UntypedActor {

    public void onReceive(Object message) throws Exception {
        if (message instanceof Greeting) {
            System.out.println("Hello " + ((Greeting) message).who);
        }
    }
}
