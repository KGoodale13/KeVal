package com.kylegoodale.keval.server;

import java.util.Hashtable;

/**
 * The Set DataType, this creates a map of key value pairs
 * Similar to the regular set and get function but adds some organization
 * Created by Kyle on 12/5/2016.
 */
public class SetType implements DataType{

    private Hashtable<String, String> data;

    public SetType(){
        this.data = new Hashtable<>();
    }


    public String insertCommand(String[] args){
        // Put the data in the hashtable and make sure it returns the value inserted
        this.data.put(args[2], args[3]);
        return "OK";
    }

    public String getCommand(String[] args){
        // Put the data in the hashtable and make sure it returns the value inserted
        return this.data.getOrDefault(args[2], "NULL");
    }

    public String removeCommand(String[] args){
        String removeResponse = this.data.remove(args[2]);
        return removeResponse == null ? "NULL" : removeResponse;
    }

    public String containsValueCommand(String[] args){
        return this.data.containsValue(args[2]) ? "TRUE" : "FALSE";
    }

    public String containsKeyCommand(String[] args){
        return this.data.containsKey(args[2]) ? "TRUE" : "FALSE";
    }

    public String sizeCommand(){ return ""+this.data.size(); }


    // Registers all the commands this Datatype will respond to ( See DataType Interface )
    public static Hashtable<String, Command> getCommandHooks(){

        Hashtable<String, Command>  commands = new Hashtable<>();

        commands.put("sinsert", new Command( true,
            "Adds a new element to the set at the passed key. SINSERT <set_key> <object_key> <object_value>",
            (String[] args, DataType dataObject) ->
                ((SetType)dataObject).insertCommand(args)
        ));

        commands.put("sremove", new Command( true,
            "Removes and returns the value at the passed object_key. SREMOVE <set_key> <object_key>",
            (String[] args, DataType dataObject) ->
                ((SetType)dataObject).removeCommand(args)
        ));

        commands.put("sget", new Command( true,
            "Returns the value in the set at set_key at object_key. SGET <set_key> <object_key>",
            (String[] args, DataType dataObject) ->
                ((SetType)dataObject).getCommand(args)
        ));

        commands.put("shasval", new Command( true,
            "Checks if the value passed exists in the set. SHASVAL <set_key> <object_value>",
            (String[] args, DataType dataObject) ->
                ((SetType)dataObject).containsValueCommand(args)
        ));

        commands.put("shaskey", new Command(  true,
            "Checks if the key exists in the set. SHASKEY <list_key> <object_key>",
            (String[] args, DataType dataObject) ->
                ((SetType)dataObject).containsKeyCommand(args)
        ));

        return commands;

    }

}
