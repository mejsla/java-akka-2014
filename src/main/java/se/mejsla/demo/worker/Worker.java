package se.mejsla.demo.worker;

import java.util.Random;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Worker extends UntypedActor {

    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof OneUnitOfWork) {
            log.info("Got some work " + message);
            // this simulates that the worker is busy for a little while
            Thread.sleep(Math.abs(new Random().nextInt() % 200));
            log.info("Completed doing work" + message);

            getSender().tell(new ResultOfWork(((OneUnitOfWork) message).data + " by " + getSelf().path().name()), getSelf());
        }
    }
}
