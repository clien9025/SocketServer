package zhanyang;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class WebHttpServer {

    public static void main(String[] args) throws IOException {
        int port = 8080;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("服务器在端口 " + port + " 上启动");

        while (true) {
            try (Socket clientSocket = serverSocket.accept()) {
                handleClient(clientSocket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void handleClient(Socket clientSocket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        OutputStream out = clientSocket.getOutputStream();

        // 读取请求行
        String requestLine = in.readLine();
        if (requestLine == null) {
            return;
        }

        // 从请求中提取文件路径
        String requestedFile = requestLine.split(" ")[1];

        // 如果请求是根目录，则默认返回 index.html
        if (requestedFile.equals("/")) {
            requestedFile = "/index.html";
        }

        try {
            // 从磁盘读取文件。这个路径应该是安全的，并限制到一个特定的目录。
            byte[] fileContent = Files.readAllBytes(Paths.get("/Users/apple/IdeaProjects/Serverlet/src/main/resources/static/" + requestedFile));

            // 确定内容类型
            String contentType;
            if (requestedFile.endsWith(".svg")) {
                contentType = "image/svg+xml";
            } else if (requestedFile.endsWith(".html")) {
                contentType = "text/html";
            } else if (requestedFile.startsWith(".css")) {
                contentType = "text/css";
            } else {
                contentType = "text/plain";
            }

            // 发送 HTTP 头
            out.write(("HTTP/1.1 200 OK\r\n").getBytes());
            out.write(("Content-Type: " + contentType + "\r\n").getBytes()); // 这里是修改后的代码
            out.write("\r\n".getBytes());

            // 发送文件内容
            out.write(fileContent);
        } catch (IOException e) {
            // 如果文件未找到，发送404响应
            out.write(("HTTP/1.1 404 Not Found\r\n").getBytes());
            out.write(("ContentType: text/plain\r\n").getBytes());
            out.write("\r\n".getBytes());
            out.write(("文件未找到").getBytes());
        }

        out.flush();
        out.close();
        in.close();
    }
}
