package com.kylegoodale.keval.server;

import java.util.Hashtable;
import java.util.NoSuchElementException;

/**
 * The Database class handles the storing of the data and processing of commands
 * and maintains a Hashtable of DataType objects
 * Created by Kyle on 12/5/2016.
 */
public class Database {

    // ENUMS for DataTypes
    private final int TYPE_STRING = 1;
    private final int TYPE_LIST = 2;
    private final int TYPE_SET = 3;

    private Hashtable<String, Command> commands; // Map of commands available
    private Hashtable<String, DataType> data; // The actual database

    private String helpTable;

    public Database(){
        // Create the maps for data and commands
        this.commands  = new Hashtable<>();
        this.data = new Hashtable<>();

        // Add commands handled directly by the database
        this.commands.put("help", new Command( false,
            "Displays a list of all commands",
            (String[] args, DataType dataObj) -> this.helpTable
        ));

        this.commands.put("status",  new Command( false,
            "Displays the status of the server and some performance metrics.",
            (String[] args, DataType dataObj) ->
                String.format("Server Online. Keys used: %d", data.size())
        ));

        this.commands.put("echo",  new Command( false,
            "Replies back with the argument sent. ECHO <message>",
            (String[] args, DataType dataObj) ->
                String.format("%s", args[1])
        ));

        this.commands.put("set", new Command( false,
            "Sets the key passed to the value. SET <key> <value>",
            (String[] args, DataType dataObj) ->
                this.setCommand(args, TYPE_STRING)
        ));

        this.commands.put("lcreate", new Command( false,
            "Creates a new list at the specified key. LCREATE <key>",
            (String[] args, DataType dataObj) ->
                this.setCommand(args, TYPE_LIST)
        ));

        this.commands.put("screate", new Command( false,
            "Creates a new set at the specified key. SCREATE <key>",
            (String[] args, DataType dataObj) ->
                this.setCommand(args, TYPE_SET)
        ));

        this.commands.put("del", new Command( false,
            "Deletes the key passed. DEL <key>",
            (String[] args, DataType dataObj) ->
                this.deleteCommand(args)
        ));

        this.commands.put("rename", new Command( false,
            "Renames the key1 to key2. RENAME <key1> <key2>",
            (String[] args, DataType dataObj) ->
                this.renameCommand(args)
        ));

        this.commands.put("size", new Command( false,
            "Returns the size of the object at the key. SIZE <key>",
            (String[] args, DataType dataObj) ->
                this.sizeCommand(args)
        ));


        // Add the commands for the other data types
        this.commands.putAll(StringType.getCommandHooks());
        this.commands.putAll(ListType.getCommandHooks());
        this.commands.putAll(SetType.getCommandHooks());


        /*
            Now we generate the help table so we can cache the result and
            avoid having to iterate over all the commands every time the help
            command is called.
         */
        this.helpTable = "KeVal Commands: \n";
        this.commands.forEach( (k,v) ->
            this.helpTable += String.format( "%14s - %s\n", k.toUpperCase(), v.helpText)
        );
    }

    /**
     * Handles sending commands to their defined handlers
     * @param cmdArgs The command and it's args i.e ["set", "some val", "something"]
     * @return The response resulting from the command being run
     */
    public String handleCommand( String[] cmdArgs ){
        Command command = this.commands.get(cmdArgs[0].toLowerCase());
        try {
            if (command == null) {
                return "Error: Invalid command. Use the 'help' command to view a list of valid commands";
            }

            // If the command uses an existing data object we need to fetch it
            DataType dataObject = null;
            if (command.usesObject) {
                dataObject = this.data.get(cmdArgs[1]);
                if( dataObject == null ){
                    return String.format("Error: No object at key '%s'", cmdArgs[1]);
                }
            }
            return command.handle(cmdArgs, dataObject);

        } catch( NoSuchElementException e) {
            return String.format("Error: No data at key: '%s'", cmdArgs[1]);
        } catch( NullPointerException e ){
            return "Error: Missing arguments. Command Help:\n " + command.helpText;
        } catch( ClassCastException e ){
            return String.format(
                "Error: Command '%s' not supported for the datatype at key '%s'",
                cmdArgs[0].toUpperCase(),
                cmdArgs[1] );
        }
    }

    // Handles deleting objects from the database
    private String deleteCommand(String[] args){
        return this.data.remove(args[1]) != null ? "OK" : "Key not found";
    }

    // Calls the sizeCommand on the datatype at the key and returns the response
    private String sizeCommand(String[] args){
        try {
            return this.data.get(args[1]).sizeCommand();
        } catch( NullPointerException | NoSuchElementException e ){
            return String.format("Error: No data at key: '%s'", args[1]);
        }
    }

    // Renames a key
    private String renameCommand(String[] args){
        if( this.data.containsKey(args[1]) ){
            this.data.put(args[2], this.data.remove(args[1]));
            return "OK";
        } else{
            return "Unable to rename (Key not found)";
        }
    }

    // Creates a new DataType object at the specified key. They type is dependent on
    // the type int parameter
    private String setCommand(String[] args, int type){

        switch( type ){
            case TYPE_STRING:
                this.data.put(args[1], new StringType(args[2]) );
                break;
            case TYPE_LIST:
                this.data.put(args[1], new ListType());
                break;
            case TYPE_SET:
                this.data.put(args[1], new SetType());
                break;
            default:
                return "ERROR: Unable to set key (Unknown Value)";
        }
        return "OK";
    }



}
