package zhanyang;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SimpleHttpServer {
    private static int i = 0;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(8080));
        System.out.println("服务在8080端口启动");

        while (true) {
            try (Socket socket = serverSocket.accept()) {
                OutputStream outputStream = socket.getOutputStream();
                // 这是一个简单的HTTP响应。
                String httpResponse = "HTTP/1.1 200 OK\r\n\r\nHello World" + " i=" + i;
                outputStream.write(httpResponse.getBytes("UTF-8"));
                outputStream.flush();
                i++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}






// 更健壮的版本

//import java.net.*;
//import java.nio.charset.StandardCharsets;
//import java.io.*;
//
//public class SimpleHttpServer {
//    public static void main(String[] args) throws IOException {
//        ServerSocket serverSocket = new ServerSocket(8080); // 直接指定端口号
//        System.out.println("服务在8080端口启动");
//
//        while (true) {
//            Socket socket = null;
//            OutputStream outputStream = null;
//
//            try {
//                socket = serverSocket.accept();
//                outputStream = socket.getOutputStream();
//                String httpResponse = "HTTP/1.1 200 OK\r\n" +
//                        "Content-Length: 11\r\n" +
//                        "Content-Type: text/plain\r\n\r\n" +
//                        "Hello World";
//                outputStream.write(httpResponse.getBytes(StandardCharsets.UTF_8));
//                outputStream.flush();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                if (outputStream != null) {
//                    try {
//                        outputStream.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                if (socket != null) {
//                    try {
//                        socket.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//        }
//    }
//}
//
