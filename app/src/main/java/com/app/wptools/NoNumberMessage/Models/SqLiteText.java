package com.app.wptools.NoNumberMessage.Models;


/**
 * Created by Baljeet on 27-01-2018.
 */

public class SqLiteText {
    private int id;

    private String number;

    private String code;

    public SqLiteText() {
    }

    public SqLiteText(int id, String number, String code) {
        this.id = id;
        this.number = number;
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
