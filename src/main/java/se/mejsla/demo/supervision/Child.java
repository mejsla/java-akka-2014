package se.mejsla.demo.supervision;

import java.util.Random;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import scala.Option;

public class Child extends UntypedActor {

    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public void preRestart(Throwable reason, Option<Object> message) throws Exception {
        log.info("Got restarted by supervisor");
    }

    @Override
    public void onReceive(Object message) throws Exception {

        // interact with service that we cannot trust
        if (Math.abs((new Random().nextInt() % 4)) == 1)  {
            log.info("Blowing up");
            throw new RuntimeException("Kabooom");

        } else {
            log.info("Handled message ok: " + message);
        }

    }
}
