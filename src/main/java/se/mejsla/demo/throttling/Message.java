package se.mejsla.demo.throttling;

public class Message {

    public final int id;

    public Message(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                '}';
    }
}

