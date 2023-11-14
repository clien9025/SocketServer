package tomcat;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerSocket {
    public static void main(String[] args) throws IOException {
        new java.net.ServerSocket(8080, 1, InetAddress.getByName("127.0.0.1"));
    }

}
