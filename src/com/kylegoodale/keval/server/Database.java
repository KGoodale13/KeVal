package com.kylegoodale.keval.server;

import javax.xml.crypto.Data;
import java.util.Hashtable;
import java.util.Map;

/**
 * The Database class handles the storing of the data and processing of commands
 *
 * Created by Kyle on 12/5/2016.
 */
public class Database {

    private final int TYPE_SYSTEM = 0;
    private final int TYPE_STRING = 1;
    private final int TYPE_LIST = 2;
    private final int TYPE_QUEUE = 3;

    private Map<String, Command> commands;
    private Map<String, DataType> data;

    private String helpTable;

    public Database(){
        // Create the maps for data and commands
        this.commands  = new Hashtable<>();
        this.data = new Hashtable<>();

        // Add commands handled directly by the database

        this.commands.put("help", new Command( TYPE_SYSTEM, false,
                "Displays a list of all commands",
                (String[] args, DataType dataObj) -> this.helpTable
        ));

        this.commands.put("status",  new Command( TYPE_SYSTEM, false,
            "Displays the status of the server and some performance metrics.",
            (String[] args, DataType dataObj) ->
                String.format("Server Online. Keys used: %d", data.size())
        ));

        this.commands.put("echo",  new Command( TYPE_SYSTEM, false,
            "Replies back with the argument sent. ECHO <message>",
            (String[] args, DataType dataObj) ->
                String.format("%s", args[1])
        ));

        this.commands.put("set", new Command( TYPE_SYSTEM, false,
            "Sets the key passed to the value. SET <key> <value>",
            (String[] args, DataType dataObj) ->
                this.setCommand(args, TYPE_STRING)
        ));

        this.commands.put("del", new Command( TYPE_SYSTEM, false,
            "Deletes the key passed. DEL <key>",
            (String[] args, DataType dataObj) ->
                this.deleteCommand(args)
        ));

        this.commands.put("rename", new Command( TYPE_SYSTEM, false,
            "Renames the key1 to key2. RENAME <key1> <key2>",
            (String[] args, DataType dataObj) ->
                this.renameCommand(args)
        ));



        // Add the commands for the other data types
        this.commands.putAll(StringType.getCommandHooks(TYPE_STRING));


        // After all the commands have been added we need to generate the help table
        this.helpTable = "KeVal Commands: \n";
        this.commands.forEach((k,v) -> this.helpTable += k + ":" + v.helpText + "\n");
    }


    public String handleCommand(String[] cmdArgs){
        Command command = this.commands.get(cmdArgs[0]);
        DataType dataObject = null;

        if( command.usesObject ){
            dataObject = this.data.get(cmdArgs[1]);
        }

        return command.handle(args, dataObject);
    }


    public String deleteCommand(String[] args){
        return this.data.remove(args[1]) != null ? "OK" : "Key not found";
    }

    public String renameCommand(String[] args){
        if( this.data.containsKey(args[1]) ){
            this.data.put(args[2], this.data.remove(args[1]));
            return "OK";
        } else{
            return "Unable to rename (Key not found)";
        }
    }

    public String setCommand(String[] args, int type){

        switch( type ){
            case TYPE_STRING:
                this.data.put(args[1], (DataType) new StringType(args[2]) );
                break;
            default:
                return "ERROR: Unable to set key (Unknown Value)";
        }
        return "OK";
    }



}
