package com.kylegoodale.keval.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Class used for handling reading and writing to a KeVal server via
 * the Asynchronous socket channel it is passed when constructed.
 * @author: Kyle Goodale (KyleGoodale.com)
 * @date: 12/3/2016
 */
public class ServerConnection implements CompletionHandler<Integer, Integer>{

    // ENUMS for the different states
    private final int READ = 0;
    private final int WRITE = 1;
    private int state;
    // The socket channel used to communicate with the server
    private AsynchronousSocketChannel socketChannel;
    // The buffer used for reading and writing to the socket channel
    private ByteBuffer buffer;


    /**
     * A connection to a remote server. Handles reading and writing to the
     * remote server via the socket channel passed
     * @param channel The socket channel to be used for communication
     */
    public ServerConnection(AsynchronousSocketChannel channel ){
        this.socketChannel = channel;
        this.state = READ;
        this.buffer = ByteBuffer.allocate(2048);
        this.socketChannel.read(this.buffer, 1, this);
    }


    @Override
    public void completed(Integer status, Integer none) {
        if( this.state == READ ){

            // Read the data from the buffer into a byte array.
            this.buffer.flip();
            int bufferLimit = this.buffer.limit();
            byte[] data = new byte[bufferLimit];
            this.buffer.get(data, 0, bufferLimit);

            // Convert the data into a string
            String message = new String(data);
            System.out.println(message);

            // Wait for the users next command ( this is blocking)
            String command = "";
            try {
                command = this.getClientInput();
            } catch(IOException e){
                System.out.println("Failed to read from standard input. Terminating...");
                System.exit(-1);
            }

            // Write the command to the socket channel
            this.sendCommand(command);

        } else if( this.state == WRITE ){
            // Write has been completed, read the response from the socket channel
            this.state = READ;
            this.buffer.clear();
            this.socketChannel.read(this.buffer, 1, this);
        } else{ // This should never happen, but just in case...
            System.out.println("Error: Invalid state detected. Unable to process request.");
            System.exit(-1);
        }
    }

    @Override
    public void failed(Throwable e, Integer none) {
        System.out.println("Error: Lost connection to server.");
        System.exit(1);
    }

    /**
     * Writes the command to the buffer and then sends it to the server via
     * the socket channel
     * @param cmd The command to be sent to the KeVal Server
     */
    private void sendCommand(String cmd){
        this.state = WRITE;
        // Empty the buffer so we can add new data
        this.buffer.clear();
        // Insert the message into the buffer
        this.buffer.put(cmd.getBytes());
        this.buffer.flip();
        // Write the message to the socket channel
        this.socketChannel.write(this.buffer, 1, this);
    }

    // Get the clients input from standard input
    private String getClientInput() throws IOException{
        System.out.print(":> ");
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        return input.readLine();
    }

}
