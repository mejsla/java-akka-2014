package se.mejsla.demo.futures;

import akka.actor.ActorSystem;
import akka.dispatch.Futures;
import akka.dispatch.OnSuccess;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import scala.concurrent.Promise;


/**
 * Basic hello world example using a future to print the hello world message
 * from another thread
 */
public class HelloWorld {

    private static OnSuccess<String> println = new OnSuccess<String>() {
        @Override
        public void onSuccess(String result) throws Throwable {
            System.out.println(result);
        }
    };


    public static void main(String[] args) {
        // we need an ExecutionContext to run async operations on
        // it is basically a threadpool with some extra sugar on top
        ActorSystem system = ActorSystem.create("my-system");
        ExecutionContext context = system.dispatcher();

        // create an unfulfilled promise
        Promise<String> promise = Futures.promise();

        // to talk about what we will do with it when the value
        // "arrives" we need the future of that promise
        Future<String> future = promise.future();


        // when the future becomes successful, do this
        future.onSuccess(println, context);

        // fulfil the promise
        System.out.println("Just before completing the promise");
        promise.success("Hello world");
        System.out.println("After completing the promise");

    }
}