package se.mejsla.demo.throttling;

import java.util.concurrent.TimeUnit;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.contrib.throttle.Throttler;
import akka.contrib.throttle.TimerBasedThrottler;
import scala.concurrent.duration.FiniteDuration;

public class Main {

    public static void main(String[] args) {

        ActorSystem system = ActorSystem.create("system");

        ActorRef worker = system.actorOf(Props.create(MustNotBeOverloaded.class), "worker");

        // we put a throttler inbetween our actor that must not be overloaded
        // and the sender that might overload it
        Throttler.Rate rate = new Throttler.Rate(1, new FiniteDuration(1, TimeUnit.SECONDS));
        ActorRef throttler = system.actorOf(Props.create(TimerBasedThrottler.class, rate), "throttler");
        throttler.tell(new Throttler.SetTarget(worker), ActorRef.noSender());

        // and then lets just send 10 messages to it quickly
        for (int i = 0; i < 10; i++) {
            throttler.tell(new Message(i), ActorRef.noSender());
        }
        System.out.println("All messages sent!");

        system.awaitTermination();

    }
}
