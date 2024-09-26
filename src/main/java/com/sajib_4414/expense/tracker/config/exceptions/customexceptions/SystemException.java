package com.sajib_4414.expense.tracker.config.exceptions.customexceptions;

import lombok.Getter;

@Getter
public class SystemException extends RuntimeException{
    private String code;
    public SystemException(String message) {
        super(message);
        this.code = "system_error";
    }

    public SystemException(String code, String message) {
        super(message);
        this.code = code;
    }
    public SystemException() {
        super("Something went wrong in server, please try again later.");
        this.code = "system_error";
    }

}
