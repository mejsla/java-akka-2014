package se.mejsla.demo.helloworld;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Greeter extends UntypedActor {

    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Greeting) {
            log.info("Hello " + ((Greeting) message).who);
        } else unhandled(message);
    }
}
