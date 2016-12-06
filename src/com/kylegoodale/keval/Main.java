package com.kylegoodale.keval;

import com.kylegoodale.keval.server.KevalServer;
import com.kylegoodale.keval.client.KevalClient;

/**
 * Created by Kyle on 12/2/2016.
 */
public class Main {

    public static void main( String[] args ){

        // Args: server client
        if( args.length > 0 ){
            if( args[0].equalsIgnoreCase("server") ){
                startServer();
                return;
            } else if( args[0].equalsIgnoreCase("client") ){
                startClient();
                return;
            }
        }

        // default
        startClient();

    }


    public static void startServer(){
        System.out.println("Starting Keval Server.");
        KevalServer server = new KevalServer("127.0.0.1", 9800);
        server.start();
    }

    public static void startClient(){
        System.out.println("Starting Keval Client.");
        new KevalClient("127.0.0.1", 9800);

    }
}
