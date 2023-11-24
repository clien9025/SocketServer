package webserver;

import java.io.*;

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

    /*
    修改逻辑部分是为了跑通本地项目
     */
    public void sendStaticResource() throws IOException {
        /*
        为了替换路径
         */
        String resourcePath = "static" + request.getUri();

        try (InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath)) {

            if (is != null) {
                byte[] buffer = new byte[1024];
                int byteReads;
                while ((byteReads = is.read(buffer)) != -1) {
                    output.write(buffer, 0, byteReads);
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
        }
    }


    // 获取内容类型
    private String getContentType(String fileName) {
        if (fileName.endsWith(".htm") || fileName.endsWith(".html")) {
            return "text/html";
        } else if (fileName.endsWith(".css")) {
            return "text/css";
        } else if (fileName.endsWith(".svg")) {
            return "image/svg+xml";
        } else if (fileName.endsWith(".js")) {
            return "application/javascript";
        } else if (fileName.endsWith(".jpg")) {
            return "image/jpeg";
        } else if (fileName.endsWith(".png")) {
            return "image/png";
        } else if (fileName.endsWith(".ttf") && fileName.startsWith("flaticon.ttf")) {
            return "font/ttf";
        } else if (fileName.endsWith(".woff2") && fileName.startsWith("flaticon.woff") && fileName.startsWith("flaticon.woff2")) {
            return "font/woff2";
        } else {
            return "text/plain";
        }
    }

}
