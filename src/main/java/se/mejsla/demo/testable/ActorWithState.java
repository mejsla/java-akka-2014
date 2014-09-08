package se.mejsla.demo.testable;

import java.util.ArrayList;
import java.util.List;

import akka.actor.UntypedActor;

public class ActorWithState extends UntypedActor {

    final List<Object> messages = new ArrayList<Object>();

    @Override
    public void onReceive(Object message) throws Exception {
        messages.add(message);
    }

}
