package com.example.application.Client;

public class ClienttoClientManagementMessage {
    String userName;
    int passWord;
    String taskName;
    boolean authenticationFlag;
    int gameRecord;

    public ClienttoClientManagementMessage(String userName,int passWord,String taskName,boolean authenticationFlag,int gameRecord){
this.userName=userName;
this.passWord=passWord;
this.taskName=taskName;
this.authenticationFlag=authenticationFlag;
this.gameRecord=gameRecord;
    }
    
}
