package se.mejsla.demo.worker;

public class ResultOfWork {

    public final String data;

    public ResultOfWork(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultOfWork{" +
                "data='" + data + '\'' +
                '}';
    }
}
