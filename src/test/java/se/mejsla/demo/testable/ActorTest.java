package se.mejsla.demo.testable;

import java.util.concurrent.TimeUnit;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import akka.testkit.TestActorRef;
import akka.testkit.TestProbe;
import org.junit.Test;
import scala.concurrent.duration.FiniteDuration;
import static org.junit.Assert.*;


public class ActorTest extends JavaTestKit {

    public ActorTest() {
        super(ActorSystem.create("tests"));
    }

    @Test
    public void testActorInteraction() {
        TestProbe probe = new TestProbe(getSystem());
        ActorRef actor = getSystem().actorOf(Props.create(PingPongActor.class), "testA");

        actor.tell("PING", probe.ref());

        probe.expectMsg(new FiniteDuration(1, TimeUnit.SECONDS), "PING");
    }


    @Test
    public void testActorInternalState() {
        Props props = Props.create(ActorWithState.class);
        TestActorRef<ActorWithState> ref = TestActorRef.create(getSystem(), props, "testA");
        ActorWithState actor = ref.underlyingActor();

        ref.tell("Message", ActorRef.noSender());

        assertEquals(actor.messages.size(), 1);
    }

}
