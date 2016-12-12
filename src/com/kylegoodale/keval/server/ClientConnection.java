package com.kylegoodale.keval.server;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class used for handling reading and writing to a connected client via the
 * Asynchronous socket channel it is passed when constructed.
 * @author: Kyle Goodale (KyleGoodale.com)
 * @date: 12/3/2016
 */
public class ClientConnection implements CompletionHandler<Integer, Integer> {

    // Enums used for identifying the current state of the channel
    private final int READ = 0;
    private final int WRITE = 1;
    private int state;

    private ByteBuffer buffer;
    private AsynchronousSocketChannel client;
    private SocketAddress clientAddr; // The clients IP and port
    private Pattern argPattern; // Pattern used to group arguments in a command
    private KevalServer server;

    /**
     * A connection to a client. Handles reading and writing to the client via
     * the socket channel supplied
     * @param client the socket channel the client is connected to
     * @throws IOException When a connection could not be established to the client
     */
    public ClientConnection(AsynchronousSocketChannel client, KevalServer sv) throws IOException {
        this.buffer = ByteBuffer.allocate(2048);
        this.client = client;
        this.server = sv;
        this.clientAddr = this.client.getRemoteAddress();
        this.argPattern = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
        System.out.format("New Connection opened with %s\n", this.clientAddr);
        // Send the client the welcome message
        this.sendClient("Welcome you have successfully connected to a KeVal server.");
    }

    @Override
    public void completed( Integer result, Integer v){

        if( result == -1 ) { // Read/Write failed
           this.closeConnection();
            return;
        }

        if( this.state == READ ){
            // Flip the buffer so we can prepare to read from it
            this.buffer.flip();
            int bufferLimit = this.buffer.limit();
            byte[] data = new byte[bufferLimit];

            // Start reading the data byte by byte into the data array
            this.buffer.get(data, 0, bufferLimit);

            // Convert the byte array into a string
            String cmd = new String(data);
            System.out.format("CommandHandler: \"%s\"  Received from: %s\n", cmd, this.clientAddr);

            // Process the command
            String[] commandArgs = this.parseCommand(cmd);

            // Respond to the client
            this.sendClient(this.server.sendCommandToDB(commandArgs));

        } else if( this.state == WRITE ){
            this.state = READ;
            this.buffer.clear();
            // Wait for the clients next command
            this.client.read(this.buffer, v, this);
        } else{ // This is not possible but it's here just in case
            System.out.println("Error: Invalid state detected. Unable to process request.");
            this.closeConnection();
        }

    }

    @Override
    public void failed(Throwable e, Integer v){
        System.out.format("Connection to client %s", this.clientAddr);

    }

    // Writes the message to the client via the socket channel
    private void sendClient(String msg){
        if( msg.length() == 0 ){
            msg = " ";
        }
        this.state = WRITE;
        // Empty the buffer so we can add new data
        this.buffer.clear();
        // Insert the message into the buffer
        this.buffer.put(msg.getBytes());
        this.buffer.flip();
        // Write the message to the socket channel
        this.client.write(this.buffer, 1, this);
    }

    // Attempts to close the connection to the client
    private void closeConnection(){
        try{
            this.client.close();
            System.out.format("Connection to client %s terminated.\n", this.clientAddr);
        } catch( IOException e ){
            System.out.println("Error occurred while attempting to terminate client connection.");
            e.printStackTrace();
        }

        Thread.currentThread().interrupt();
    }

    // http://stackoverflow.com/a/366532
    private String[] parseCommand(String cmd){
        String[] args = new String[9];
        // Create a new Regex matcher to allow for args to be grouped by spaces or quotes
        Matcher argMatcher = this.argPattern.matcher(cmd);
        int argCount = 0;
        while (argMatcher.find() && argCount < 8  ) {
            if (argMatcher.group(1) != null) {
                // Add double-quoted string without the quotes
                args[argCount] = argMatcher.group(1);
            } else if (argMatcher.group(2) != null) {
                // Add single-quoted string without the quotes
                args[argCount] = argMatcher.group(2);
            } else {
                // Add unquoted word
                args[argCount] = argMatcher.group(0);
            }
            argCount++;
        }
        return args;
    }

}
