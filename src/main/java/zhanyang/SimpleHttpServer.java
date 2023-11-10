package zhanyang;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


// 更健壮的版本

public class SimpleHttpServer {
    private static int i = 0;
    private static final ExecutorService executorService = Executors.newFixedThreadPool(10); // 创建一个固定大小的线程池

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(8080));
        System.out.println("服务在8080端口启动");

        while (!serverSocket.isClosed()) {
            try {
                Socket socket = serverSocket.accept();
                // 使用线程池来管理线程
                executorService.submit(() -> handleRequest(socket));
            } catch (IOException e) {
                if (!serverSocket.isClosed()) {
                    e.printStackTrace();
                    break;
                }
            }
        }
        executorService.shutdown();
    }

    private static void handleRequest(Socket socket) {
        try (OutputStream outputStream = socket.getOutputStream();
             socket) { // 使用 try-with-resources 自动关闭资源
            synchronized (SimpleHttpServer.class) {
                i++;
            }
            String httpResponse = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: text/html; charset=UTF-8\r\n" +
                    "Connection: close\r\n" + // 明确告知连接将关闭
                    "\r\n" +
                    "<html>" +
                    "<head><title>Simple HTTP Server</title></head>" +
                    "<body>" +
                    "<h1>Hello World</h1>" +
                    "<p>Request count: " + i + "</p>" +
                    "</body>" +
                    "</html>";
            outputStream.write(httpResponse.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



