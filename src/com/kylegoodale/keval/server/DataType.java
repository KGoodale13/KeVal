package com.kylegoodale.keval.server;

import java.util.Hashtable;

/**
 * Created by Kyle on 12/5/2016.
 */
public interface DataType {

    static Hashtable<String, Command> getCommandHooks(int type){
        return new Hashtable<>();
    };
}
