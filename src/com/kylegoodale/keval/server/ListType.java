package com.kylegoodale.keval.server;

import java.util.Hashtable;
import java.util.LinkedList;

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

        public String popCommand(){
            return this.data.remove();
        }

        public String popLastCommand(){
            return this.data.remove();
        }

        public String peekCommand(){
            return this.data.peekFirst();
        }

        public String peekLastCommand(){
            return this.data.peekLast();
        }


    public static Hashtable<String, Command> getCommandHooks(int assignedType){

            Hashtable<String, Command>  commands = new Hashtable<>();

            commands.put("lpush", new Command( assignedType, true,
                "Pushes a new element to the end of the list. LPUSH <list_key> <value>",
                (String[] args, DataType dataObject) ->
                        ((ListType)dataObject).pushCommand(args)
            ));

            commands.put("lpop", new Command( assignedType, true,
                    "Removes and returns the first element of the list. LPOP <list_key>",
                    (String[] args, DataType dataObject) ->
                            ((ListType)dataObject).popCommand()
            ));

            commands.put("lpoplast", new Command( assignedType, true,
                    "Removes and returns the last element of the list. LPOPLAST <list_key>",
                    (String[] args, DataType dataObject) ->
                            ((ListType)dataObject).popLastCommand()
            ));

            commands.put("lpeak", new Command( assignedType, true,
                    "Returns the first element of the list without removing it. LPEAK <list_key>",
                    (String[] args, DataType dataObject) ->
                            ((ListType)dataObject).peekCommand()
            ));

            commands.put("lpeaklast", new Command( assignedType, true,
                    "Returns the last element of the list without removing it. LPEAKLAST <list_key>",
                    (String[] args, DataType dataObject) ->
                            ((ListType)dataObject).peekLastCommand()
            ));

            return commands;

        }

}
