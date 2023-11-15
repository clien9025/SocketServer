package tomcat;

import java.io.IOException;
import java.net.InetAddress;

public class TomcatServerSocket {
    public static void main(String[] args) throws IOException {
        java.net.ServerSocket serverSocket = new java.net.ServerSocket(8080, 1, InetAddress.getByName("127.0.0.1"));
    }

}
