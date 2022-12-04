package example.lab.model.api;

public interface ModelAPI {
    double calcProgress(double inp, double max);
    void start();
    void stop();
    void pause();
    void resume();
    void interrupt();
}
