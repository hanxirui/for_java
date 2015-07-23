package com.hxr.javatone.concurrency.sevenweeks.firstmodel.thirdday;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * {1.5后如何更优雅的创建线程}
 * <br>
 *  
 * <p>
 * Create on : 2015年7月22日<br>
 * <p>
 * </p>
 * <br>
 * @author hanxirui<br>
 * @version javastone v1.0
 * <p>
 *<br>
 * <strong>Modify History:</strong><br>
 * user     modify_date    modify_content<br>
 * -------------------------------------------<br>
 * <br>
 */
public class EchoServer {

    public static void main(final String[] args) throws IOException {

        class ConnectionHandler implements Runnable {
            InputStream in;
            OutputStream out;

            ConnectionHandler(final Socket socket) throws IOException {
                in = socket.getInputStream();
                out = socket.getOutputStream();
            }

            public void run() {
                try {
                    int n;
                    byte[] buffer = new byte[1024];
                    while ((n = in.read(buffer)) != -1) {
                        out.write(buffer, 0, n);
                        out.flush();
                    }
                } catch (IOException e) {
                }
            }
        }

        ServerSocket server = new ServerSocket(4567);
        int threadPoolSize = Runtime.getRuntime().availableProcessors() * 2;
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);
        while (true) {
            Socket socket = server.accept();
            executor.execute(new ConnectionHandler(socket));
        }
    }
}
