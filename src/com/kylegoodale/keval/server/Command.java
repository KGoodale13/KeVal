package com.kylegoodale.keval.server;

/**
 * Created by Kyle on 12/5/2016.
 */
public class Command {

    CommandHandler handler;
    boolean usesObject;
    String helpText;

    public Command(){};
    public Command( boolean usesObj, String help, CommandHandler handler){
        this.usesObject = usesObj;
        this.helpText = help;
        this.handler = handler;
    }

    public String handle(String[] args, DataType dataObj){
        return this.handler.handle(args, dataObj);
    }
}
