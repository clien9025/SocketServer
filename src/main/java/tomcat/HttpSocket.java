package tomcat;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class HttpSocket {

    public static void main(String[] args) throws IOException, InterruptedException {

        /*
         * ChatGPT 版本
         * */

        try {
            // 确保web服务器正在这个端口上监听
            // 访问百度的话: new Socket("www.baidu.com", 80)
            Socket socket = new Socket("127.0.0.1", 8080);
            OutputStream os = socket.getOutputStream();
            boolean autoFlush = true;
            PrintWriter out = new PrintWriter(os, autoFlush);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // 发送一个 HTTP 请求到 web 服务器
            // 访问百度的话: Host: www.baidu.com:80
            out.println("GET /index.html HTTP/1.1");
            out.println("Host: localhost:8080"); // 确保这与您的服务器监听的端口一致
            out.println("Connection: close");
            out.println();

            /**
             * How Tomcat Work 书中示例
             *这个逻辑存在问题，因为内部 while 循环设计有误。
             *它应该在读取到 -1 时停止，但是代码中并没有在读取到 -1 之前跳出循环，这将导致无限循环。
             *另外，当 in.read() 返回 -1 时，它会将 -1 作为一个字符添加到 StringBuffer 中，这是不正确的。
             */

//        // read the response
//        boolean loop = true;
//        StringBuilder sb = new StringBuilder(8096);
//
//        while (loop) {
//            if (in.ready()) {
//                int i = 0;
//                while (i != -1) {
//                    i = in.read();
//                    sb.append((char) i);
//                }
//                loop = false;
//            }
//            Thread.currentThread().sleep(50);
//        }

            // 读响应
            StringBuilder sb = new StringBuilder(8096);
            while (!in.ready()) {
                Thread.sleep(50);
            }
            while (in.ready()) {
                sb.append((char) in.read());
            }

            // 在控制台显示响应
            System.out.println(sb.toString());
            socket.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

}