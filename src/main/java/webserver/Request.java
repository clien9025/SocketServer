package webserver;

import java.io.IOException;
import java.io.InputStream;

public class Request {
    private InputStream input;
    private String uri;

    public Request(InputStream input) {
        this.input = input;
    }

    public void parse() {
        // 从 socket 读取一组字符
        StringBuffer request = new StringBuffer(2048);
        int i;
        byte[] buffer = new byte[2048];
        try {
            // 从传递给 Request 对象的套接字的 InputStream 中读取整个字节流并在一个缓冲区中存储字节数组。
            i = input.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            i = -1;
        }
        for (int j = 0; j < i; j++) {
            // 然后它使用缓冲区字节数据的字节来填入一个 StringBuffer 对象，
            request.append((char) buffer[j]);
        }
        System.out.println(request.toString());
        // 并且把 代表 StringBuffer 的字符串传递给 parseUri 方法。
        uri = parseUri(request.toString());
    }

    /**
     * parseUri 方法从请求行里边获得 URI
     * parseUri 方法搜索请求里边的第一个和第二个空格并从中获取 URI。
     * @param requestString
     * @return
     */
    private String parseUri(String requestString) {
        int index1, index2;
        index1 = requestString.indexOf(' ');
        if (index1 != -1) {
            index2 = requestString.indexOf(' ', index1 + 1);
            if (index2 > index1) {
                return requestString.substring(index1 + 1, index2);
            }
        }
        return null;
    }

    public String getUri() {
        return uri;
    }

}
