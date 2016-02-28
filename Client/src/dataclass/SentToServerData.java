package dataclass;

import java.io.Serializable;

/**
 *
 * @author phawzy
 */



public class SentToServerData implements Serializable{
    public enum Type{
        TXTMSG,STATUSCHANGE,REGISTERATIONDATA,LOGINDATA,FILE,CONFIRMDOWNLOAD,DEMANDCONTACTLIST
    }
    public SentToServerData(){
        type=Type.TXTMSG;
        txtMsg="";
        status="";
        username="";
        name="";
        password="";
        email="";
        gender="";
        fileSize=0;
        fileName="";
    }
    public Type type;
    public String txtMsg;   //// in sending msg
    public String status;   /// in changing status
    public String username, name;   //// in login and sign up
    public String password, email, gender; ///// in sign up
    
    public int fileSize;
    public String fileName;
    public byte[] fileData;    
}




/*
 json object types    
 from client to server
 1-"type"="txtMsg"  ==> "content" : String
 2-"type"="statusChange"  ===> "status" :String
 3-"type"="registerationData"  ===> "name","username","password","email","gender" :String
 4-"type"="loginData"  ===> "username","password" :String
 5-"type"="file" =====>  

 from server to client
 1-"type"="txtMsg"  ==> "content","username" : String
 2-"type"="newContact"  ==> "username","status" : String
 3-"type"="contactStatusChange"  ===> "username","status" :String
 4-"type"="registerationResponse"  ===> "response" :String
 5-"type"="loginResponse"  ===> "response" :String
 6-"type"="file" =====>  
 7-"type"="serverMsg"  ==> "content": String

 */

/*
 status values : available,busy,away,offline
 response values : ok,error
 */