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

    /*
    修改逻辑部分是为了跑通本地项目
     */
    public void sendStaticResource() throws IOException {
        // TODO 我想在下面代码中添加判断 file.getName() 是否含有"?"这个标识符,
        //  如果有的话那么就把包含"?"以及问号后面的路径全部去除,只返回"?"之前
        //  的 file.getName().我举个例子:
        //  file.getName() 的值是:flaticon.ttf?5b9bc942fd3d9c02b6b08441492dd015
        //  但是我只需要这个部分:flaticon.ttf
        //  然后再将得到的 flaticon.ttf 部分添加到 HttpServer.WEB_ROOT 后面,得到文件真实的路径 file
        //  之后再去调用本地的资源,发送相应的响应格式

        FileInputStream fis = null;
        try {
            String fileName = request.getUri();
            // 检查文件名中是否包含 "?"
            int queryIndex = fileName.indexOf('?');
            if (queryIndex != -1) {
                // 如果存在，去掉 "?" 及其之后的所有内容
                fileName = fileName.substring(0, queryIndex);
            }
            File file = new File(HttpServer.WEB_ROOT, fileName);

            // 全局响应头变量
            String headerMessage = null;

            if (file.exists()) {
                fis = new FileInputStream(file);
                long length = file.length();  // 获取文件长度
                byte[] bytes = new byte[(int) length];  // 创建一个与文件大小匹配的字节数组
                int readBytes = fis.read(bytes);// 一次性读取文件内容到字节数组

                /*
                添加响应体格式以及数据处理的逻辑
                 */
                // 发送 HTTP 响应头部
                headerMessage = "HTTP/1.1 200 OK\r\n" +
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
        } finally {
            if (fis != null) {
                fis.close();
            }
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
