package com.kylegoodale.keval.server;


import java.util.Hashtable;

/**
 * Created by Kyle on 11/28/2016.
 */
public class StringType implements DataType {


    private String data;

    public StringType(String value){
        this.data = value;
    }

    public String sizeCommand( ){
        return "" + this.data.length();
    }

    public String getValue(){
        return this.data;
    }



    // Registers all the commands this Datatype will respond to ( See DataType Interface )
    public static Hashtable<String, Command> getCommandHooks(){

        Hashtable<String, Command> commands = new Hashtable<>();

        commands.put("len", new Command( true,
            "Retrieves the length of the string stored at the key. LEN <key>",
            (String args[], DataType dataObj) ->
                ((StringType)dataObj).sizeCommand()
        ));

        commands.put("get", new Command( true,
            "Retrieves the string value at the key. GET <key> <value>",
            (String args[], DataType dataObj) ->
                ((StringType)dataObj).getValue()
        ));

        return commands;
    }

}
