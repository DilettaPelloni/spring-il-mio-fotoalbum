package org.lessons.springilmiofotoalbum.exceptions;

public class UserNotAllowedException extends Exception{
    public UserNotAllowedException(String message) {
        super(message);
    }
}
