package se.mejsla.demo.futures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import akka.actor.ActorSystem;
import akka.dispatch.Futures;
import scala.concurrent.Await;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

public class TransformData {

    private static Callable<String> delayedReturn(final String data) {
        return new Callable<String>() {
            public String call() throws Exception {
                // we just fake heavy processing in this thread for 100 ms
                Thread.sleep(100);
                // and then return the string
                return data;
            }
        };
    }

    public static void main(String[] args) throws Exception {
        // we need an ExecutionContext to run async operations on
        // it is basically a threadpool with some extra sugar on top
        ActorSystem system = ActorSystem.create("my-system");
        ExecutionContext context = system.dispatcher();

        List<String> indatas = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");

        List<Future<String>> listOfFutureStrings = new ArrayList<Future<String>>();
        for (String indata: indatas) {
            Future<String> futureString = Futures.future(delayedReturn(indata), context);
            listOfFutureStrings.add(futureString);
        }

        // we want a future that is not completed until
        // all the future strings has arrived
        Future<Iterable<String>> futureListOfStrings = Futures.sequence(listOfFutureStrings, context);

        // and then we want to block the main thread until
        // those arrive
        Iterable<String> result = Await.result(futureListOfStrings, Duration.apply("20 seconds"));

        System.out.println(result);
    }

}
