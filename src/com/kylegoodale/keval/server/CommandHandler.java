package com.kylegoodale.keval.server;

public interface CommandHandler {

    String handle(String[] args, DataType dataObject);

}
