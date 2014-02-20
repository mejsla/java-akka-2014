package se.mejsla.demo.worker;

public class OneUnitOfWork {

    public final String data;

    public OneUnitOfWork(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "OneUnitOfWork{" +
                "data='" + data + '\'' +
                '}';
    }
}
