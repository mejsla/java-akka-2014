package se.mejsla.demo.testable;


import akka.actor.UntypedActor;

public class PingPongActor extends UntypedActor {


    @Override
    public void onReceive(Object message) throws Exception {
        // just bounce it back
        getSender().tell(message, getSelf());
    }
}