package org.lessons.springilmiofotoalbum.messages;

public class AlertMessage {
    //ATTRIBUTI ------------------------------------------------------------------
    private final AlertMessageType type;
    private final String message;

    //COSTRUTTORE ------------------------------------------------------------------
    public AlertMessage(AlertMessageType type, String message) {
        this.type = type;
        this.message = message;
    }

    //GETTERS ------------------------------------------------------------------
    public AlertMessageType getType() {
        return type;
    }
    public String getMessage() {
        return message;
    }
}
