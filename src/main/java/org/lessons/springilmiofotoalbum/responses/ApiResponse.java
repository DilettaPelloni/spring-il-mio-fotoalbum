package org.lessons.springilmiofotoalbum.responses;

import java.util.ArrayList;
import java.util.List;

public class ApiResponse<T> {

    //ATTRIBUTI ------------------------------------------------------------------------------
    private T content;
    private boolean success;
    private String errorMessage;
    private List<?> errors = new ArrayList<>();

    //caso OK
    public ApiResponse(T content) {
        this.content = content;
        this.success = true;
    }
    //caso BAD REQUEST
    public ApiResponse(T content, List<?> errors) {
        this.content = content;
        this.errors = errors;
        this.success = false;
    }
    //caso errore con solo messaggio
    public ApiResponse(T content, String errorMessage) {
        this.content = content;
        this.errorMessage = errorMessage;
        this.success = false;
    }

    //GETTERE E SETTER ------------------------------------------------------------------------------
    public T getContent() {
        return content;
    }
    public void setContent(T content) {
        this.content = content;
    }
    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }
    public List<?> getErrors() {
        return errors;
    }
    public void setErrors(List<?> errors) {
        this.errors = errors;
    }
    public String getErrorMessage() {
        return errorMessage;
    }
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
