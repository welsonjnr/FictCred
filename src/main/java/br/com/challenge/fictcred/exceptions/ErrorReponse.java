package br.com.challenge.fictcred.exceptions;

import java.util.List;

public class ErrorReponse {

    private String message;
    private int code;
    private String status;
    private String objectName;
    private List<ErrorObject> errors;

    public ErrorReponse(){}

    public ErrorReponse(String message, int code, String status, String objectName, List<ErrorObject> errors) {
        this.message = message;
        this.code = code;
        this.status = status;
        this.objectName = objectName;
        this.errors = errors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public List<ErrorObject> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorObject> errors) {
        this.errors = errors;
    }
}
