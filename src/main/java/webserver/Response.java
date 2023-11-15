package webserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Response {
    private static final int BUFFER_SIZE = 1024;
    Request request;
    OutputStream output;

    public Response(OutputStream output) {
        this.output = output;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public void sendStaticResource() throws IOException {
//        byte[] bytes = new byte[BUFFER_SIZE];
        FileInputStream fis = null;
        try {
//            File file = new File(HttpServer.WEB_ROOT, request.getUri());
//            if (file.exists()) {
//                fis = new FileInputStream(file);
//                int ch = fis.read(bytes, 0, BUFFER_SIZE);
//                while (ch != -1) {
//                    output.write(bytes, 0, ch);
//                    ch = fis.read(bytes, 0, BUFFER_SIZE);
//                }
            File file = new File(HttpServer.WEB_ROOT, request.getUri());
            if (file.exists()) {
                fis = new FileInputStream(file);
                long length = file.length();  // 获取文件长度
                byte[] bytes = new byte[(int) length];  // 创建一个与文件大小匹配的字节数组
                int readBytes = fis.read(bytes);// 一次性读取文件内容到字节数组

                /*
                添加响应体格式以及数据处理的逻辑
                 */
                // 发送 HTTP 响应头部
                String headerMessage = "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: " + getContentType(file.getName()) + "\r\n" +
                        "Content-Length: " + length + "\r\n" +
                        "\r\n";
                output.write(headerMessage.getBytes());

                // 根据实际读取的字节数写入输出流
                if (readBytes > 0) {
                    output.write(bytes, 0, readBytes);
                }
            } else {
                // 文件没找到
                String errorMessage = "HTTP/1.1 404 File Not Found\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-length: 23\r\n" +
                        "\r\n" +
                        "<h1>File Not Found<h1>";
                output.write(errorMessage.getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (fis!=null) {
                fis.close();
            }
        }
    }


    // 获取内容类型
    private String getContentType(String fileName) {
        if (fileName.endsWith(".htm") || fileName.endsWith(".html")) {
            return "text/html";
        }
        else if (fileName.endsWith(".css")) {
            return "text/css";
        } else if (fileName.endsWith(".svg")) {
            return "image/svg+xml";
        } else {
            return "text/plain";
        }
    }
}
