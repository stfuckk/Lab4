package com.example._3_matches_game.Client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private boolean isClosed = false;

    public Client() throws IOException {
        System.out.println("Client Start!");
        Socket s = new Socket("127.0.0.1", 2222);
        InputStream IS = s.getInputStream();
        OutputStream OS = s.getOutputStream();
        socket = s;
        in = IS;
        out = OS;
    }

    public String Play() throws IOException {
        transfer("play", 4); //команда к началу игры
        return getRules();
    }

    public String getRules() throws IOException{
        char[] a = new char[7]; //аналогично предыдущему заданию, преобразуем команды
        char tempChar;
        int length = 0;
        for(int i = 0; i < 7; i++){
            tempChar = (char) in.read();
            if(tempChar != '0'){
                a[i] = tempChar;
                length++;
            }
        }
        char[] string = new char[length];
        System.arraycopy(a, 0, string, 0, length);
        return new String(string); //возвращает финальную строку
    }

    public void Take(int value) throws IOException {
        out.write(value);
    }
    public int getCountMatches() throws IOException {
        return  in.read();
    }
    public void close() throws IOException {
        isClosed = true;
        socket.close();
    }
    public boolean checkClosed(){
        return isClosed;
    }

    public void transfer(String string, int length){
        char[] a = new char[7];
        for(int i = 0; i < 7; i++){
            a[i] = (i < length) ? string.charAt(i): '0'; //заполняем недостающее пространство нулями
            try{
                out.write(a[i]);
            } catch (IOException e){throw new RuntimeException(e);}
        }
    }
}
