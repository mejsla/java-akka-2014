package se.mejsla.demo.supervision;

import akka.actor.*;
import akka.japi.Function;
import scala.concurrent.duration.Duration;
import akka.actor.SupervisorStrategy.Directive;

public class Supervisor extends UntypedActor {



    private ActorRef child = getContext().actorOf(Props.create(Child.class), "child");

    @Override
    public void onReceive(Object message) throws Exception {
        // send all messages to the child
        child.forward(message, getContext());
    }

    private static SupervisorStrategy strategy = new OneForOneStrategy(10, Duration.create("1 minute"),
            new Function<Throwable, Directive>() {

                @Override
                public Directive apply(Throwable throwable) throws Exception {
                    if (throwable instanceof RuntimeException) {
                        return SupervisorStrategy.restart();
                    } else {
                        return SupervisorStrategy.escalate();
                    }
                }
            }
    );

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return strategy;
    }
}
