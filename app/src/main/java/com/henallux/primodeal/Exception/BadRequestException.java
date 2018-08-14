package com.henallux.primodeal.Exception;

/**
 * Created by bil on 01-08-18.
 */

public class BadRequestException extends Exception {
    public String message;
    public BadRequestException(String msg){
        message = msg;
    }
}
