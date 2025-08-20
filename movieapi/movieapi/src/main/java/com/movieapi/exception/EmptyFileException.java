package com.movieapi.exception;

public class EmptyFileException extends RuntimeException{
    public EmptyFileException(String message){
        super(message);
    }
}
