package example.lab.Client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Client implements IClient{
    Updatable updater;
    Socket s;
    InputStream in;
    OutputStream out;

    private static boolean isCreated = false;

    @Override //подключаем клиента, если он не подключен и запускаем progress()
    public void startCalcProgress(Updatable updater) throws IOException {
        this.updater = updater;
        if(!isCreated){
            try {
                System.out.println("Client Start!");
                Socket socket = new Socket("127.0.0.1", 2222);
                InputStream IN = socket.getInputStream();
                OutputStream OUT = socket.getOutputStream();

                s = socket;
                in = IN;
                out = OUT;
            } catch (IOException e){throw new RuntimeException(e);}

            isCreated = true;
            this.progress();
        }
        else restart();
    }

    @Override
    public void restart() throws IOException {
        transfer("Restart", 7);
    }

    @Override
    public void stop() throws IOException {
        transfer("Stop", 4);
    }

    @Override
    public void pause() throws IOException {
        transfer("Pause", 5);
    }

    @Override
    public void resume() throws IOException{
        transfer("Resume", 6);
    }

    @Override //создаем символьный массив, который будет преобразовывать команды и записывать выходной поток
    public void transfer(String string, int length) throws  IOException{
        char[] a = new char[8]; //если длина команды менее 8 символов, то пустые места заполним нулями
        for(int i = 0; i < 8; i++){
            a[i] = (i < length) ? string.charAt(i):'0';
            out.write(a[i]);
        }
    }

    @Override //читаем в отдельном потоке данные в виде byte и заворачивам в виде double
    public void progress() throws IOException {
        new Thread(()->{
            // начало тела потока
            while(true){
                byte[] a = new byte[8];
                for(int i = 0; i < 8; i++){
                    try {
                        a[i] = (byte) in.read();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                updater.update(ByteBuffer.wrap(a).getDouble());
            }
            //конец тела потока
        }).start();
    }
}
