package webserver;

import java.io.IOException;
import java.io.InputStream;
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
        // 添加 8080/index.html 和 / 两个路径访同一资源的逻辑

        String resourcePath;
        if (request.getUri().equals("/")) {
            resourcePath = "static/index.html";
        } else {
            resourcePath = "static" + request.getUri();
        }

        System.out.println("resourcePath++++++++++++++" + resourcePath);
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (is != null) {
                // 发送正确的 HTTP 响应头
                String headerMessage = "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: " + getContentType(resourcePath) + "\r\n" +
                        "\r\n";
                output.write(headerMessage.getBytes());

                // 读取静态资源内容并发送
                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
            } else {
                // 资源未找到，发送 404 错误
                String errorMessage = "HTTP/1.1 404 File Not Found\r\n" +
                        "Content-Type: text/html\r\n" +
                        "\r\n" +
                        "<h1>File Not Found</h1>";
                output.write(errorMessage.getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 确定文件类型
    private String getContentType(String resourcePath) {
        if (resourcePath.endsWith(".html")) {
            return "text/html";
        } else if (resourcePath.endsWith(".css")) {
            return "text/css";
        } else if (resourcePath.endsWith(".js")) {
            return "application/javascript";
        } else if (resourcePath.endsWith(".png")) {
            return "image/png";
        } else if (resourcePath.endsWith(".jpg") || resourcePath.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (resourcePath.endsWith(".svg")) {
            return "image/svg+xml";
        } else if (resourcePath.endsWith(".ttf")) {
            return "font/ttf";
        } else if (resourcePath.endsWith(".woff")) {
            return "font/woff";
        } else if (resourcePath.endsWith(".woff2")) {
            return "font/woff2";
        } else {
            return "text/plain";
        }
    }
}
