package org.lessons.springilmiofotoalbum.exceptions;

public class PhotoNotFoundException extends Exception{
    public PhotoNotFoundException(String message) {
        super(message);
    }
}
