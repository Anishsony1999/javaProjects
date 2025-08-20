package com.movieapi.exception;

public class MovieNotFountException extends RuntimeException{
    public MovieNotFountException(String message){
        super(message);
    }
}
