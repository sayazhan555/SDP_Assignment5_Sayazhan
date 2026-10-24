package com.sayazhan;

public class NoSuitableRouteException extends RuntimeException {

    public NoSuitableRouteException(String message) {
        super(message);
    }
}