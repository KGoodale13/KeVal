package com.kylegoodale.keval.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Creates a connection to the Keval server and then creates a ServerConnection
 * object that will handle sending commands and receiving messages to/from the server
 * @author: Kyle Goodale (KyleGoodale.com)
 * @date: 12/3/2016
 */
public class KevalClient {

    public KevalClient( String host, int port){

        try {
            SocketAddress serverAddr = new InetSocketAddress(host, port);
            AsynchronousSocketChannel socketChannel = AsynchronousSocketChannel.open();
            // Asynchronously attempts to connect, the result will be stored in the Future object
            Future result = socketChannel.connect(serverAddr);
            try {
                /*
                    Get the result of the connection from the Future object
                    We don't need to check the response because the ServerConnection
                    object will throw an IOException if there isn't a client connected
                */
                result.get();
                System.out.println("Successfully connected to KeVal Server");
                // Creates a new ServerConnection which will handle all reading/writing
                new ServerConnection(socketChannel);
                // This prevents the program from terminating prematurely
                Thread.currentThread().join();

            } catch( InterruptedException e ){
                System.out.println("KeVal Client now terminating...");
            } catch( ExecutionException e ){
                System.out.println("Error: Unable to connect to server");
            }

        } catch( IOException e ){
            System.out.println("Error: Unable to connect to server. ");

        }
    }
}
