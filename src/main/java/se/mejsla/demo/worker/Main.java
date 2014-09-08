package se.mejsla.demo.worker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.dispatch.*;
import akka.japi.Procedure;
import akka.japi.Util;
import akka.pattern.Patterns;
import akka.routing.RoundRobinRouter;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import static akka.pattern.Patterns.ask;


/**
 * A more complicated example where we create 10 workers that might work on units of work at the
 * same time (they simulate CPU bound work by sleeping the thread). We send of a hundred items of
 * work to the workers using the "ask" pattern where we will get a Future&lt;Response&gt; back.
 *
 * We then wait for all those futures to complete.
 *
 */
public class Main {

    public static void main(String[] args) throws Exception {

        ActorSystem system = ActorSystem.create("worker-system");

        ActorRef workers = system.actorOf(Props.create(Worker.class)
                .withRouter(new RoundRobinRouter(10)), "worker");

        // we wait at most this long for a reply
        Timeout timeout = new Timeout(10, TimeUnit.SECONDS);

        List<Future<ResultOfWork>> responseFutures = new ArrayList<Future<ResultOfWork>>();
        for (int i = 0; i < 100; i++) {
            final OneUnitOfWork workItem = new OneUnitOfWork("Unit " + i);

            // ask allows us to get something back, without the calling site being and actor
            Future<ResultOfWork> reply = Patterns.ask(workers, workItem, timeout)
                    .mapTo(Util.classTag(ResultOfWork.class));

            responseFutures.add(reply);
        }
        System.out.println("Done sending all messages");

        // turn the list of future completed work units into
        // a future list of completed work unit - this future will not be completed
        // until all the individual work items are completed
        Future<Iterable<ResultOfWork>> futureResponse = Futures.sequence(responseFutures, system.dispatcher());

        // this blocks the main thread until all response futures have completed
        Iterable<ResultOfWork> responses = Await.result(futureResponse, Duration.apply("20 seconds"));
        System.out.println("Completed all work: " + responses);

        system.shutdown();
    }
}
