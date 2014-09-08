package se.mejsla.demo.microbench;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

/* NOTE this is a pretty stupid and naive microbenchmark, you shouldn't really base anything on it
 * except the fact that you can push messages quickly between actors, System.nanoseconds cannot be
 * trusted, the payload is purely artificial etc etc.
 *
 * Based on this test from the akka test suite:
 * https://github.com/akka/akka/blob/v2.0/akka-actor-tests/src/test/scala/akka/performance/microbench/TellThroughputComputationPerformanceSpec.scala
 */
public class Main {

    public static void main(String[] args) throws Exception {
        runScenario(10, 10000000);
    }

    private static void runScenario(int numberOfClients, long repeats) throws Exception {

        ActorSystem system = ActorSystem.create("my-system");

        CountDownLatch latch = new CountDownLatch(numberOfClients);
        long repeatsPerClient = repeats / numberOfClients;

        List<ActorRef> destinations = new ArrayList<ActorRef>();
        List<ActorRef> clients = new ArrayList<ActorRef>();
        for (int i = 0; i < numberOfClients; i++) {
            ActorRef destination = system.actorOf(Props.create(Destination.class));
            destinations.add(destination);
            ActorRef client = system.actorOf(Props.create(Client.class, destination, latch, repeatsPerClient));
            clients.add(client);
        }

        long start = System.nanoTime();
        for (ActorRef client: clients) {
            client.tell(Messages.RUN, ActorRef.noSender());
        }
        latch.await();
        double durationS = (System.nanoTime() - start) / 1000000000D;

        System.out.print("Total time to send " + repeats + " messages: " + durationS + " s");


        system.shutdown();
    }

}
