package com.example.myapplication;

public class AppInfo {

//    private String number;
    private String titleStr;// 应用名称
    private String userStr;// 包名
    private String pwdStr;
    private String dateStr;
    private String noteStr;

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setTitleStr(String titleStr) {
        this.titleStr = titleStr;
    }

    public void setUserStr(String userStr) {
        this.userStr = userStr;
    }

    public void setPwdStr(String pwdStr) {
        this.pwdStr = pwdStr;
    }

    public void setNoteStr(String noteStr) {
        this.noteStr = noteStr;
    }

//    public void setNumber(String number) {
//        this.number = number;
//    }

    public String getTitleStr() {
        return titleStr;
    }

    public String getUserStr() {
        return userStr;
    }

    public String getPwdStr() {
        return pwdStr;
    }

    public String getNoteStr() {
        return noteStr;
    }

//    public String getNumber() {
//        return number;
//    }



    public AppInfo( String titleStr, String userStr,String pwdStr, String noteStr,String dateStr) {
        super();
        this.titleStr = titleStr;
        this.userStr = userStr;
        this.pwdStr = pwdStr;
        this.noteStr = noteStr;
        this.dateStr = dateStr;
    }

    public AppInfo() {
        super();
    }


//    @Override
//    public String toString() {
//        return "AppInfo [number=" + number + ", titleStr=" + titleStr
//                + ", userStr=" + userStr
//                + ", pwdStr=" + pwdStr
//                + ", noteStr=" + noteStr + "]";
//    }

}