package com.app.whatsapptools.TextRepeater.Models;


/**
 * Created by Baljeet on 27-01-2018.
 */

public class SqLiteText {
    private int id;

    private String rep_text;

    public SqLiteText(int id, String rep_text) {
        this.id = id;
        this.rep_text = rep_text;
    }

    public SqLiteText() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRep_text() {
        return rep_text;
    }

    public void setRep_text(String rep_text) {
        this.rep_text = rep_text;
    }
}
