package com.kylegoodale.keval.server;

/**
 * Created by Kyle on 11/30/2016.
 */
public interface CommandHandler {

    String handle(String[] args, DataType dataObject);

}
