package se.mejsla.demo.microbench;

import java.util.concurrent.CountDownLatch;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public class Client extends UntypedActor {

    private ActorRef actor;
    private CountDownLatch latch;
    private long repeat;
    private long sent = 0;
    private long received = 0;
    private long initialMessages;


    public Client(ActorRef actor, CountDownLatch latch, long repeat) {
        this.actor = actor;
        this.latch = latch;
        this.repeat = repeat;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message == Messages.MSG) {
            received += 1;
            if (sent < repeat) {
                actor.tell(Messages.MSG, getSelf());
                sent += 1;
            } else if (received >= repeat) {
                latch.countDown();
            }
        } else if (message == Messages.RUN) {
            // the original uses the throughput of the dispatcher as initial messages here,
            // this is to simplify the code a little and make it more naive
            for (int i = 0; i <= 5; i++) {
                actor.tell(Messages.MSG, getSelf());
                sent += 1;
            }
        }
    }
}
