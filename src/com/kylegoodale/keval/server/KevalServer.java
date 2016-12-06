package com.kylegoodale.keval.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * This class is used to create and start a KeVal socket server that will accept
 * new connections and handle any commands sent to the server.
 * @author Kyle Goodale (KyleGoodale.com)
 * @date 12/3/2016
 */
public class KevalServer {

    private String host;
    private int port;
    private Database db;

    /**
     * A server capable of accepting multiple client connection asynchronously
     * via socket connections
     * @param host the IP address to bind this server to
     * @param port the port to bind this server to
     */
    public KevalServer(String host, int port) {
        this.host = host;
        this.port = port;
        this.db = new Database();
    }

    /**
     * Starts the Socket server up and begins accepting connections
     * Warning: This is a blocking operation.
     */
    public void start(){
        System.out.println("KeVal server starting up...");

        try{
            // Create the socket channel that will listen for connections
            final AsynchronousServerSocketChannel socketChannel;
            socketChannel = AsynchronousServerSocketChannel.open();
            // Bind the server to the correct host and port
            InetSocketAddress serverAddress = new InetSocketAddress(this.host, this.port);
            socketChannel.bind( serverAddress );
            System.out.format("KeVal server listening on %s\n", serverAddress);
            ConnectionAttachment attachment = new ConnectionAttachment();
            attachment.server = socketChannel;
            attachment.kevalInstance = this;
            // Start accepting new connections
            socketChannel.accept(attachment,
                new CompletionHandler<AsynchronousSocketChannel, ConnectionAttachment>() {
                    // Logic for handling incoming requests
                    @Override
                    public void completed(
                            AsynchronousSocketChannel clientChannel,
                            ConnectionAttachment attach
                    ){
                        // Accept the next connection
                        attach.server.accept(attach, this);
                        try {
                            // Create a new ClientConnection handler
                            new ClientConnection(clientChannel, attach.kevalInstance);
                        } catch( IOException e ){
                            System.out.println("Client connection failed...");
                        }
                    }

                    @Override
                    public void failed(Throwable e, ConnectionAttachment attach) {
                        System.out.format("Client connection failed...");
                    }
            });

            // This stops the application from terminating prematurely
            try {
                Thread.currentThread().join();
            } catch( InterruptedException e ){
                System.out.println("KeVal Shutting down.");
            }

        } catch( IOException e ){
            e.printStackTrace();
        }
    }

    public String sendCommandToDB( String[] args ){
        return this.db.handleCommand(args);
    }


    public class ConnectionAttachment{
        AsynchronousServerSocketChannel server;
        KevalServer kevalInstance;
    }

}
