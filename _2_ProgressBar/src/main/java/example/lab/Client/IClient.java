package example.lab.Client;

import java.io.IOException;

public interface IClient
{
    void startCalcProgress(Updatable updater) throws IOException;
    void restart() throws IOException;
    void stop() throws IOException;
    void pause() throws IOException;
    void resume() throws IOException;
    void transfer(String string, int length) throws  IOException;

    void progress() throws IOException;
}
