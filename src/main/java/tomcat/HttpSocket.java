package tomcat;

import java.io.*;
import java.net.Socket;

public class HttpSocket {
    public void Socket() throws IOException, InterruptedException {
        Socket(null, 0);
    }

    public void Socket(String host, int port) throws IOException, InterruptedException {
        Socket socket = new Socket("127.0.0.1", 8080);
        OutputStream os = socket.getOutputStream();
        boolean autoFlush = true;
        PrintWriter out = new PrintWriter(socket.getOutputStream(), autoFlush);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // 发送一个 HTTP 请求给到 web 服务器
        out.println("GET /index.jsp HTTP/1.1");
        out.println("Host: localhost:8080");
        out.println("Connection: close");
        out.println();

        // 读响应
        boolean loop = true;
        StringBuffer sb = new StringBuffer(8096);
        while (loop) {
            if (in.ready()) {
                int i = 0;
                while (i != -1) {
                    i = in.read();
                    sb.append((char) i);
                }
                loop = false;
            }
            Thread.currentThread().sleep(50);
        }

        // 在控制台显示响应
        System.out.println(sb.toString());
        socket.close();
    }
}
