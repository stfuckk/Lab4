import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        System.out.println("Client Start!");
        Socket socket = new Socket("127.0.0.1", 2222);
        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();
        System.out.println("-Welcome to Calculator-\nInstruction:\n1)Enter the first number;\n" +
                "2)Enter the second number;\n3)Enter the operation(+    -   *   /).\nOr type 'quit' to exit");

        while(true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Number: ");
            String _firstNumber = scanner.nextLine();
            if(_firstNumber.equalsIgnoreCase("quit"))
                break;

            System.out.println("Number: ");
            String _secondNumber = scanner.nextLine();
            if(_secondNumber.equalsIgnoreCase("quit"))
                break;

            System.out.println("Operation: ");
            String _operationSign = scanner.nextLine();
            if(_operationSign.equalsIgnoreCase("quit"))
                break;

            String[] input = new String[]{_firstNumber, _secondNumber, _operationSign};


            if(input[2].equals("/") && input[1].equals("0"))
                System.out.println("Error: /0");

            else {
                byte[] _firstNumberByteArray = new byte[8], _secondNumberByteArray = new byte[8];
                ByteBuffer.wrap(_firstNumberByteArray).putDouble(Double.parseDouble(input[0])); //добавляем в буффер число в виде double
                out.write(_firstNumberByteArray); //отправляем через поток вывода на сервер

                ByteBuffer.wrap(_secondNumberByteArray).putDouble(Double.parseDouble(input[1]));
                out.write(_secondNumberByteArray);
                out.write(input[2].getBytes()); //отправляем оператор на сервер

                byte[] a = new byte[8];
                for(int i = 0; i < 8; i++)
                    a[i] = (byte) in.read(); //читаем входной поток и записываем полученное значение

                double answer = ByteBuffer.wrap(a).getDouble(); //получаем результат вычислений с сервера в буффер
                System.out.println("Answer: " + answer);
            }
        }

        System.out.println("Closing connections...");
        socket.close();
        in.close();
        out.close();
    }
}