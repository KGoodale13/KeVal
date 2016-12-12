package com.kylegoodale.keval.server;

import java.util.Hashtable;

/**
 * DataTypes are used to allow multiple different types of values to be
 * stored in the database and for them to each have their own unique commands
 * without the need for additional logic in the command handler.
 * Created by Kyle on 12/5/2016.
 */
public interface DataType {

    /**
     * Returns all the commands the DataType implements which is then merged
     * with the Databases existing command table.
     * @return A Hashtable containing all the commands this DataType implements
     */
    static Hashtable<String, Command> getCommandHooks(){
        return new Hashtable<>();
    };

    /**
     * Returns the size of the DataType
     * @return String the size of the DataType
     */
    String sizeCommand();

}
