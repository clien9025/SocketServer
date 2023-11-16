package tomcat;
import java.io.*;
import java.net.Socket;

public class BaiduSocket {

    public static void main(String[] args) {
        try (Socket socket = new Socket("www.baidu.com", 80);
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // 发送 HTTP GET 请求
            out.println("GET / HTTP/1.1");
            out.println("Host: www.baidu.com");
            out.println("Connection: close");
            out.println();

            // 读取响应并打印
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
