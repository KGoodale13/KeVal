package com.kylegoodale.keval;

import com.kylegoodale.keval.server.KevalServer;
import com.kylegoodale.keval.client.KevalClient;

/**
 * KeVal - A Key-Value in-memory database.
 * To launch a KeVal Client use the 'client' argument
 * To launch a KeVal Server use the 'server' argument
 * All connections are localhost only due to the lack of authentication support
 * KeVal runs on localhost port 9800
 * Created By: Kyle Goodale on 12/13/2016
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

        // No args specified print some help info and then start a client
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("Welcome to KeVal!\nNo environment was specified! Defaulting to client");
        System.out.println("To launch a KeVal Client use the `client` argument when running this application");
        System.out.println("To launch a KeVal Server use the `server` argument when running this application");
        System.out.println("All connections are on localhost:9800 by default");
        System.out.println("------------------------------------------------------------------------------------");
        startClient();
    }

    public static void startServer(){
        System.out.println("Starting KeVal Server.");
        KevalServer server = new KevalServer("127.0.0.1", 9800);
        server.start();
    }

    public static void startClient(){
        System.out.println("Starting KeVal Client.");
        new KevalClient("127.0.0.1", 9800);

    }
}
