package se.mejsla.demo.helloworld;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import scala.Console;
import se.mejsla.demo.helloworld.Greeter;
import se.mejsla.demo.helloworld.Greeting;

public class Main {

    /* NOTE we could also use akka.Main which will start a system and let it live as long as the given
     * supervisor actor lives
     */
    public static void main(String[] args) {

        ActorSystem system = ActorSystem.create("my-system");

        ActorRef greeter = system.actorOf(Props.create(Greeter.class), "greeter");


        greeter.tell(new Greeting("Johan"), ActorRef.noSender());
        greeter.tell(new Greeting("Stina"), ActorRef.noSender());
        greeter.tell(new Greeting("Nils"), ActorRef.noSender());

        System.out.println("Press enter to complete");
        Console.readLine();
        system.shutdown();
    }

}
