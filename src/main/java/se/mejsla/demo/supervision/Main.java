package se.mejsla.demo.supervision;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class Main {

    public static void main(String[] args) {

        ActorSystem system = ActorSystem.create("system");

        ActorRef actor = system.actorOf(Props.create(Supervisor.class), "supervisor");

        for (int i = 0; i < 20; i++) {
            actor.tell("message", ActorRef.noSender());
        }

        system.awaitTermination();
    }
}
