package com.company.ch4.threaded;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ThreadedEchoServer {
    public static void main(String[] args) {
        try(ServerSocket s = new ServerSocket(8189)) {
            int i = 1;
            while (true) {
                Socket incoming = s.accept();
                System.out.println("Spawning " + i);
                Runnable r = new ThreadedEchoHandler(incoming);
                Thread t = new Thread(r);
                t.start();
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
