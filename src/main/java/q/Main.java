package q;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) throws IOException {
        final ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress("localhost", 23334));
        while (true) {
            //  Ждем подключения клиента и получаем потоки для дальнейшей работы
            try (SocketChannel socketChannel = serverChannel.accept()) {
                //  Определяем буфер для получения данных
                final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);
                while (socketChannel.isConnected()) {
                    //  читаем данные из канала в буфер
                    int bytesCount = socketChannel.read(inputBuffer);
                    //  если из потока читать нельзя, перестаем работать с этим клиентом
                    if (bytesCount == -1) break;
                    //  получаем переданную отклиента строку в нужной кодировке и очищаем буфер
                    String msg = new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8);
                    inputBuffer.clear();
                    System.out.println("Получено сообщение от клиента: " + msg);
                    //  отправляем сообщение клиента назад с пометкой ЭХО
                    msg = msg.replaceAll(" ", "");
                    socketChannel.write(ByteBuffer.wrap(("Эхо: " + msg).getBytes(StandardCharsets.UTF_8)));
                }
            } catch (IOException err) {
                System.out.println(err.getMessage());
            }
        }
    }
}








        /*
        ServerSocket serverSocket = new ServerSocket(8080);
        Socket clientSocket = serverSocket.accept(); // ждем подключения, используем блокировку потока т.к. ожидаем подключения клиента и
        // получения от него данных для задачи (процесс идет так: клиент дает данные - получает ответ, потом снова дает данные и т.п.)
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));


        String line = null;
        while ((line = in.readLine()) != "end") {
            // Выход если от клиента получили end
            if (line != null) {
                //Вычисления
                String result = fibonachi(line);
                // Отправляем ответ
                out.println(result);
            }
        }
    }

    public static String fibonachi(String inputSting) {
        long input = Long.parseLong(inputSting);
        long prev = 0;
        long next = 1;
        for (long i = 0; i < input; i++) {
            next = prev + next;
            prev = next - prev;
        }
        return String.valueOf(prev);
    }
}
*/