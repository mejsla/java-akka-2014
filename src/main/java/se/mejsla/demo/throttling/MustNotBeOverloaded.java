package se.mejsla.demo.throttling;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * Actor that cannot be sent too many messages per second
 * (for some definition of too many)
 */
public class MustNotBeOverloaded extends UntypedActor {

    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Message) {
            log.info("Got message " + message);

        } else unhandled(message);
    }
}
