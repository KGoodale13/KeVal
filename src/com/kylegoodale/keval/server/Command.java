package com.kylegoodale.keval.server;

/**
 * Command object, Each command has a unique object and is stored in the
 * databases commands table. When a command is invoked the CommandHandler is
 * passed the args and a reference to the DataType Object if usesObj is true.
 * Created by Kyle on 12/5/2016.
 */
public class Command {

    CommandHandler handler;
    boolean usesObject;
    String helpText;

    /**
     * Command object used to add commands to the database
     * @param usesObj Does the command require a reference to the DataType object?
     *                If so the object located at the key passed will be passed to the
     *                handler when invoked.
     * @param help A short description of the command and its parameters.
     * @param handler The CommandHandler that will be run when the command is
     *                invoked.
     */
    public Command( boolean usesObj, String help, CommandHandler handler){
        this.usesObject = usesObj;
        this.helpText = help;
        this.handler = handler;
    }

    /**
     * Call the CommandHandler's handle method to process the command
     * @param args The commands arguments passed by the client
     * @param dataObj The DataType object located at args[1] if usesObject is true
     * @return The response resulting from running the command.
     */
    public String handle(String[] args, DataType dataObj){
        return this.handler.handle(args, dataObj);
    }
}
