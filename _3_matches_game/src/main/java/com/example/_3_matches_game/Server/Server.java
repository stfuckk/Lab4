package com.example._3_matches_game.Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static ServerSocket _serverSocket;

    private static InputStream in_1;
    private static OutputStream out_1; //входные и выходные потоки под разных клиентов
    private static InputStream in_2;
    private static OutputStream out_2;

    private static int counterMatches = 37; //изначальное число спичек
    private static boolean isEnd = false;

    public static void main(String[] args) throws IOException
    {
        System.out.println("Server Start!");
        ServerSocket ss = new ServerSocket(2222);

        System.out.println("Waiting for the Client1...");
        Socket s = ss.accept();
        System.out.println("Client1 has been connected!");
        InputStream IN_1 = s.getInputStream();
        OutputStream OUT_1 = s.getOutputStream();

        System.out.println("Waiting for the Client2...");
        s = ss.accept();
        System.out.println("Client2 has been connected!");
        InputStream IN_2 = s.getInputStream();
        OutputStream OUT_2 = s.getOutputStream();

        _serverSocket = ss;
        in_1 = IN_1;
        out_1 = OUT_1;
        in_2 = IN_2;
        out_2 = OUT_2;

        String string1 = getRules(in_1); //принимаем информацию от первого клиента
        String string2 = getRules(in_2); //принимаем информацию от второго клиента
        if(string1.equals("play") && string2.equals("play")){
            transfer("play", 4, out_1); //состояние: ходит первый игрок
            transfer("wait", 4, out_2); //состояние: второй игрок ждет хода первого
        }

        while (!_serverSocket.isClosed())
        {
            gameProcess(in_1, out_1); //запускаем игровой процесс для обоих клиентов
            gameProcess(in_2, out_2);

            if (isEnd)
                _serverSocket.close(); //закрываем соединение, когда игра закончится
        }
    }

    public static void gameProcess(InputStream in, OutputStream out) throws IOException{
        int textField = in.read(); //получаем ввод клиента из текстового поля
        counterMatches -= textField; //уменьшаем число спичек
        out_1.write(counterMatches);
        out_2.write(counterMatches); //отправляем число оставшихся спичек клиентам
        if(counterMatches <= 0){
            isEnd = true;
            if(out == out_1){
                transfer("win", 3, out_1);
                transfer("lose", 4, out_2);  //если спичек не осталось, то отправляем итог
            }
            else {
                transfer("win", 3, out_2);
                transfer("lose", 4, out_1);
            }
        }
        else {
            if (out == out_1) { //если спички остались, то передаем информацию о следующем ходе
                transfer("wait", 4, out_1);
                transfer("play", 4, out_2);
            } else {
                transfer("wait", 4, out_2);
                transfer("play", 4, out_1);
            }
        }
    }


    public static void transfer(String string, int length, OutputStream out){
        char[] a = new char[7];
        for(int i = 0; i < 7; i++){
            a[i] = (i < length) ? string.charAt(i): '0';
            try{
                out.write(a[i]);
            } catch(IOException e){throw new RuntimeException(e);}
        }
    }

    public static String getRules(InputStream in){
        char[] a = new char[7];
        char tempChar;
        int length = 0;
        for(int i = 0; i < 7; i++){
            try{
                tempChar = (char) in.read();
                if(tempChar != '0'){
                    a[i] = tempChar;
                    length++;
                }
            } catch (IOException e) {throw new RuntimeException(e);}
        }
        char[] string = new char[length];
        System.arraycopy(a, 0, string, 0, length);
        return new String(string);
    }
}
