package webserver;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
    // 本地文件路径
    public static final String WEB_ROOT =
            System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "static";
    // shutdown
    private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";
    // shutdown command received
    private boolean shutdown = false;

    public static void main(String[] args) {
        HttpServer server = new HttpServer();
        server.await();
    }

    public void await() {
        ServerSocket serverSocket = null;
        int port = 8080;
        try {
            serverSocket = new ServerSocket(port, 1,
                    InetAddress.getByName("127.0.0.1"));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        // 用循环来等待请求
        while (!shutdown) {
            Socket socket = null;
            InputStream input = null;
            OutputStream output = null;
            try {
                socket = serverSocket.accept();
                input = socket.getInputStream();
                output = socket.getOutputStream();
                // 创建请求对象并解析
                Request request = new Request(input);
                request.parse();
                // 创建响应对象
                Response response = new Response(output);
                response.setRequest(request);
                response.sendStaticResource();
                // 关闭 socket
                socket.close();
                // 检查前缀URI为一个关机命令
                shutdown = request.getUri().equals(SHUTDOWN_COMMAND);

            } catch (Exception e) {
                e.printStackTrace();
                continue;

            }
        }
    }
}
