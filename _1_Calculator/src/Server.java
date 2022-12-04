import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Server{
    public static void main(String[] args) throws IOException{
        System.out.println("Server Start!");
        System.out.println("Waiting for the client...");
        ServerSocket _serverSocket = new ServerSocket(2222);
        Socket socket = _serverSocket.accept();
        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();
	System.out.println("Connected!");

        while(true){
            //Принимаем и записываем первое число в буффер
            System.out.println("Waiting for user input...");
            byte[] a = new byte[8];
            for(int i = 0; i < 8; i++)
                a[i] = (byte) in.read();
            double number1 = ByteBuffer.wrap(a).getDouble();
            System.out.println("First number: " + number1);
            //Принимаем и записываем второе число в буффер
            byte[] b = new byte[8];
            for(int i = 0; i < 8; i++)
                b[i] = (byte) in.read();
            double number2 = ByteBuffer.wrap(b).getDouble();
            System.out.println("Second number: " + number2);

            //Принимаем и записываем знак оператора
            int operation = in.read();
            System.out.println("Operator: " + (char) operation);

            //подготовка данных к вычислениям
            double[] numbers = new double[]{number1, number2};
            double num1 = numbers[0];
            double num2 = numbers[1];
            char operator = (char) operation;
            double answer = 0;

            //вычисляем
            if(operator == '+')
                answer = num1 + num2;
            else if(operator == '-')
                answer = num1 - num2;
            else if(operator == '*')
                answer = num1 * num2;
            else if (operator == '/')
                answer = num1 / num2;
            System.out.println("Answer: " + answer);

            //отправляем ответ клиенту
            byte[] _byteAnswer = new byte[8];
            ByteBuffer.wrap(_byteAnswer).putDouble(answer);
            out.write(_byteAnswer);
        }
    }
}
