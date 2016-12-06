package com.kylegoodale.keval.server;


import java.util.Hashtable;

/**
 * Created by Kyle on 11/28/2016.
 */
public class StringType implements DataType {


    private String value;

    public StringType(String value){
        this.value = value;
    }

    public String lengthCommand( ){
        return ""+this.value.length();
    }

    public String getValue(){
        return this.value;
    }

    public static Hashtable<String, Command> getCommandHooks(int assignedType){

        Hashtable<String, Command> commands = new Hashtable<>();

        commands.put("len", new Command( assignedType, true,
            "Retrieves the length of the string stored at the key. LEN <key>",
            (String args[], DataType dataObj) ->
                ((StringType)dataObj).lengthCommand()
        ));

        commands.put("get", new Command( assignedType, true,
            "Retrieves the string value at the key. GET <key> <value>",
            (String args[], DataType dataObj) ->
                    ((StringType)dataObj).getValue()
        ));

        return commands;
    }

}
