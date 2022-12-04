package example.lab.Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Server {
    static calcProgress calc;
    static double value;

    static ServerSocket SS;
    static Socket S;
    static InputStream IN;
    static OutputStream OUT;

    public static void main(String[] args) throws IOException{
        System.out.println("Server Start!");
        ServerSocket _serverSocket = new ServerSocket(2222);
        System.out.println("Waiting for the client...");
        Socket socket = _serverSocket.accept();
        System.out.println("Connected!");
        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();

        SS = _serverSocket;
        S = socket;
        IN = in;
        OUT = out;

        calc = new calcProgress();
        calc.start();

        while(true){ //заворачиваем наше значение в виде byte
            if(calc.getValue() != value){
                value = calc.getValue();
                byte[] a = new byte[8];
                ByteBuffer.wrap(a).putDouble(value);
                out.write(a);
            }

            new Thread(()-> {
                synchronized (System.out){
                    //здесь мы будем получать информацию из входного потока
                    char[] a = new char[8];
                    char tempChar;
                    int length = 0;
                    for(int i = 0; i<8; i++){ //посимвольно читаем входные данные
                        try {
                            tempChar = (char) in.read();
                            if (tempChar != '0') //записываем ненулевые символы
                            {
                                a[i] = tempChar;
                                length++;
                            }
                        } catch (IOException e) {throw new RuntimeException(e);}
                    }
                    char[] b = new char[length];
                    for(int i = 0; i<length; i++)
                        b[i] = a[i]; //помещаем отобранные символы
                    String s = new String(b); //преобразуем в строку

                    //выполняем действия относительно прочитанной команды
                    if(s.equals("Restart")){
                        calc.Restart();
                        calc = new calcProgress(); //сбрасываем счет
                        calc.start();
                    }
                    if(s.equals("Resume"))
                        calc.Resume();
                    if(s.equals("Pause"))
                        calc.Pause();
                    if(s.equals("Stop"))
                        calc.Stop();
                }
            }).start();

        }
    }



}
class calcProgress extends Thread{
    private static boolean isStopped = false, isPaused = false;
    private static double value;

    void Pause(){
        isPaused = true;
        System.out.println("USER: pause");
    }
    void Restart(){
        isPaused = false;
        this.interrupt();
        System.out.println("USER: restart");
    }
    void Resume(){
        isPaused = false;
        System.out.println("USER: resume");
    }
    void Stop(){
        System.out.println("USER: stop");
        isPaused = false;
        isStopped = true;
        synchronized (System.out){System.out.notifyAll();}
    }

    public double getValue(){
        return value;
    }

    @Override
    public void run(){
        for(int i = 0; i < 1000; i++){
            while(isPaused)
                Thread.yield();
            if(isStopped){
                value = 0;
                isStopped = false;
                break;
            }
            value = (double) i / 1000;
            try {
                sleep(100);
            } catch (InterruptedException e) {
                this.interrupt();
                return;
            }
        }
    }
}