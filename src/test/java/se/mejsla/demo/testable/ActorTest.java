package se.mejsla.demo.testable;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import akka.testkit.TestActorRef;
import akka.testkit.TestProbe;
import org.junit.Test;
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

        probe.expectMsg("PING");
    }


    @Test
    public void testActorInternalState() {
        Props props = Props.create(ActorWithState.class);
        TestActorRef<ActorWithState> ref = TestActorRef.create(getSystem(), props, "testA");
        ActorWithState actor = ref.underlyingActor();

        ref.tell("Message", ActorRef.noSender());

        assertEquals(actor.getMessages().size(), 1);
    }

}
