package com.example.dumitru.lab5;

import org.json.JSONArray;
import org.json.JSONObject;

public class GlobalVariables {
    private static GlobalVariables instance;

    private String TOKEN;
    private int iDocID;
    private boolean bNotifCheck = false;
    private JSONObject JSONNotifInfo;
    private JSONArray JSONADocList;



    private GlobalVariables(){}

    void SetJSONADocList(JSONArray JSONADocList) { this.JSONADocList = JSONADocList; }

    JSONArray GetJSONADocList() { return JSONADocList; }

    void SetJSONNotifInfo(JSONObject JSONNotifInfo) { this.JSONNotifInfo = JSONNotifInfo; }

    JSONObject GetJSONNotifInfo() { return JSONNotifInfo; }

    void SetNotifCheck(boolean Check) { this.bNotifCheck = Check; }

    void SetDocID(int iDocID) { this.iDocID = iDocID; }

    void SetToken(String token){
        this.TOKEN = token;
    }


    String GetToken(){
        return this.TOKEN;
    }

    int GetDocID() {return this.iDocID;}

    boolean CheckNotif() { return  bNotifCheck; }

    public static synchronized GlobalVariables getInstance(){
        if(instance == null)
            instance=new GlobalVariables();
        return instance;
    }
}
