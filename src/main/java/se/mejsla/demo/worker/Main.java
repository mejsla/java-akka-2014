package se.mejsla.demo.worker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.dispatch.*;
import akka.japi.Procedure;
import akka.routing.RoundRobinRouter;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import static akka.pattern.Patterns.ask;


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

            // ask allows us to get something back, without us being an actor as well
            Future<ResultOfWork> result = ask(workers, workItem, timeout)
                .map(new Mapper<Object, ResultOfWork>() {
                    @Override
                    public ResultOfWork apply(Object parameter) {
                        if (parameter instanceof ResultOfWork)
                            return (ResultOfWork) parameter;
                        else
                            throw new RuntimeException("Got something unexpected back");
                    }
                }, system.dispatcher());
            
            responseFutures.add(result);
        }
        System.out.println("Done sending all messages");

        Future<Iterable<ResultOfWork>> futureResponse = Futures.sequence(responseFutures, system.dispatcher());


        Iterable<ResultOfWork> responses = Await.result(futureResponse, Duration.apply("20 seconds"));
        System.out.println("Completed all work: " + responses);

        system.shutdown();
    }
}
