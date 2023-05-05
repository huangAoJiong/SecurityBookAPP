package com.example.myapplication;

public class Password {
//    private long id;
    private String title;
    private String username;
    private String password;
    private String note;
    private String date;

    public Password() {

    }

    public Password( String title, String username, String password, String note,String date) {
//        this.id = id;
        this.title = title;
        this.username = username;
        this.password = password;
        this.note = note;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
