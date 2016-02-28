/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataclass;

import java.io.Serializable;
import java.util.Vector;

/**
 *
 * @author phawzy
 */
public class ReceivedFromServerData implements Serializable {

    public ReceivedFromServerData() {
        username = "";
        txtMsg = "";
        serverMsg = "";
        status = "";
        fileName = "";
        fileSize = 0;
        loginResponse = "";
        registerationResponse = "";
        type = Type.TXTMSG;
        contactListVector = new Vector<UserStatus>();
    }

    public enum Type {

        TXTMSG, SERVERMSG, NEWCONTACT, CONTACTSTATUSCHANGE, REGISTERATIONRESPONSE,
        LOGINRESPONSE, FILE, ASKTODOWNLOAD, CONTACTLIST
    }

    

    public Type type;
    public String username;
    /////in receiving msg and new contact and contact status change and file
    public String txtMsg;  //// in receiving msg
    public String serverMsg;     //// in receiving server msg
    public String status;   /////in contact status change and new contact
    public String registerationResponse;   //// in registeration response
    public String loginResponse;      ///// in  login response

    public int fileSize;
    public String fileName;
    public byte[] fileData;
    public Vector<UserStatus> contactListVector;
}
