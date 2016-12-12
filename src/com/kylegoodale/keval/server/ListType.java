package com.kylegoodale.keval.server;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Created by Kyle on 12/5/2016.
 */
public class ListType implements DataType{

        private LinkedList<String> data;

        public ListType(){
            this.data = new LinkedList<>();
        }


        public String pushCommand(String[] args){
            return this.data.add(args[2]) ? "OK" : "ERROR";
        }

        public String sizeCommand(){ return ""+this.data.size(); }

        public String popCommand(){
            try {
                return this.data.remove();
            } catch( NoSuchElementException e ){
                return "NULL";
            }
        }

        public String popLastCommand(){
            try {
                return this.data.removeLast();
            } catch( NoSuchElementException e ){
                return "NULL";
            }
        }

        public String peekCommand(){
            try {
                return this.data.peekFirst();
            } catch( NoSuchElementException e ){
                return "NULL";
            }
        }

        public String peekLastCommand(){
            try {
                return this.data.peekLast();
            } catch( NoSuchElementException e ){
                return "NULL";
            }
        }

    // Registers all the commands this DataType will respond to ( See DataType Interface )
    public static Hashtable<String, Command> getCommandHooks(){

            Hashtable<String, Command>  commands = new Hashtable<>();

            commands.put("lpush", new Command( true,
                "Pushes a new element to the end of the list. LPUSH <list_key> <value>",
                (String[] args, DataType dataObject) ->
                        ((ListType)dataObject).pushCommand(args)
            ));

            commands.put("lpop", new Command( true,
                    "Removes and returns the first element of the list. LPOP <list_key>",
                    (String[] args, DataType dataObject) ->
                            ((ListType)dataObject).popCommand()
            ));

            commands.put("lpoplast", new Command( true,
                    "Removes and returns the last element of the list. LPOPLAST <list_key>",
                    (String[] args, DataType dataObject) ->
                            ((ListType)dataObject).popLastCommand()
            ));

            commands.put("lpeak", new Command( true,
                    "Returns the first element of the list without removing it. LPEAK <list_key>",
                    (String[] args, DataType dataObject) ->
                            ((ListType)dataObject).peekCommand()
            ));

            commands.put("lpeaklast", new Command( true,
                    "Returns the last element of the list without removing it. LPEAKLAST <list_key>",
                    (String[] args, DataType dataObject) ->
                            ((ListType)dataObject).peekLastCommand()
            ));

            return commands;

        }

}
