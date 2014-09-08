package se.mejsla.demo.microbench;

import akka.actor.UntypedActor;

public class Destination extends UntypedActor {

    public void onReceive(Object message) throws Exception {
        if (message == Messages.MSG) {
            getSender().tell(Messages.MSG, getSelf());
        }
    }

}
