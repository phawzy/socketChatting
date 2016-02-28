package client;

import dataclass.ReceivedFromServerData;
import dataclass.SentToServerData;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.Socket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author phawzy
 */
public class ClientController {

    LoginUI loginUIObj;
    RegisterUI registerUIObj;
    ChattingUI chattingUIObj;
    IpUI ipUIObj;
    Socket mySocket;
    ObjectInputStream objectInputStream;
    ObjectOutputStream objectOutputStream;
    String ip = null;

    public ClientController() {
        ipUIObj = new IpUI(this);
        ipUIObj.setVisible(true);

    }

    class ClientHandler extends Thread {

        public ClientHandler(Socket cs) {
            start();
        }

        @Override
        public void run() {
            while (true) {
                ReceivedFromServerData receivedMsg = new ReceivedFromServerData();
                SentToServerData sentMsg = new SentToServerData();
                try {
                    receivedMsg = (ReceivedFromServerData) objectInputStream.readObject();
                    if (receivedMsg != null) {
                        switch (receivedMsg.type) {
                            case TXTMSG:
                                chattingUIObj.displayMsg(receivedMsg.txtMsg,
                                        receivedMsg.username);
                                break;
                            case SERVERMSG:
                                chattingUIObj.displayServerMsg(receivedMsg.serverMsg);
                                break;
                            case NEWCONTACT:
                                chattingUIObj.addUserToContactList(receivedMsg.username, receivedMsg.status);

                                break;
                            case CONTACTSTATUSCHANGE:
                                System.out.println(receivedMsg.username + "status client controller" + receivedMsg.status);
                                chattingUIObj.changeContactStatus(receivedMsg.username,
                                        receivedMsg.status);
                                break;
                            case CONTACTLIST:
                                chattingUIObj.setContactList(receivedMsg.contactListVector);
                                break;
                            case REGISTERATIONRESPONSE:
                                switch (receivedMsg.registerationResponse) {
                                    case "error":
                                        registerUIObj.handleRegisterationError();
                                        break;
                                    case "ok":

                                        loginUIObj = new LoginUI(ClientController.this);
                                        loginUIObj.setVisible(true);
                                        loginUIObj.setLocation(registerUIObj.getLocation());
                                        registerUIObj.dispose();

                                        break;
                                }
                                break;
                            case LOGINRESPONSE:
                                switch (receivedMsg.loginResponse) {
                                    case "error":
                                        loginUIObj.handleLoginError();
                                        break;
                                    case "ok":

                                        loginUIObj.dispose();
                                        chattingUIObj = new ChattingUI(ClientController.this);
                                        chattingUIObj.setVisible(true);
                                        sentMsg.type = SentToServerData.Type.DEMANDCONTACTLIST;
                                        try {
                                            objectOutputStream.writeUnshared(sentMsg);
                                        } catch (IOException e) {
                                            JOptionPane.showMessageDialog(null, "Sorry The Server Is Down :(");
                                        }

                                        break;
                                }
                                break;
                            case ASKTODOWNLOAD:
                                boolean downloadResponse = chattingUIObj.askToDownload(receivedMsg.fileName,
                                        receivedMsg.fileSize, receivedMsg.username);
                                if (downloadResponse) {
                                    System.out.println("confirm");
                                    sentMsg.type = SentToServerData.Type.CONFIRMDOWNLOAD;
                                    sentMsg.fileName = receivedMsg.fileName;
                                    sentMsg.fileSize = receivedMsg.fileSize;
                                    sentMsg.username = receivedMsg.username;
                                    try {
                                        objectOutputStream.writeUnshared(sentMsg);
                                    } catch (IOException e) {
                                        JOptionPane.showMessageDialog(null, "the serever is down :(");
                                    }

                                }
                                break;
                            case FILE:
                                System.out.println("download client client");
                                FileOutputStream fos = new FileOutputStream(receivedMsg.fileName);
                                BufferedOutputStream bos = new BufferedOutputStream(fos);
                                bos.write(receivedMsg.fileData, 0, receivedMsg.fileSize);
                                bos.flush();
                                if (fos != null) {
                                    fos.close();
                                }
                                if (bos != null) {
                                    bos.close();
                                }
                                break;
                        }
                    }
                } catch (IOException ex) {
                    System.out.println("the server is close1 3lshan at2kd");
                    JOptionPane.showMessageDialog(null, "Sorry The Server Is Down :(");
                    try {
                        objectInputStream.close();
                        objectOutputStream.close();
                        if(chattingUIObj !=null){
                            chattingUIObj.setEnabled(false);
                        }
                        
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                    try {
                        // System.out.println("the server is close");
                        objectInputStream.close();
                    } catch (IOException ex1) {
                        System.out.println("the server is close2");
                        Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                    break;
                } catch (ClassNotFoundException ex) {
                    System.out.println("the server is close3");
                    Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    void setIp(String newIp) {
        ip = newIp;
        initConnection();
    }

    void initConnection() {
        try {
            if (ipUIObj != null) {
                ipUIObj.dispose();
            }
            mySocket = new Socket(ip, 5006);
            System.out.println("socket created");
            objectOutputStream = new ObjectOutputStream(mySocket.getOutputStream());
            objectOutputStream.flush();
            objectInputStream = new ObjectInputStream(mySocket.getInputStream());
            java.awt.EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    ClientHandler clientHandler = new ClientHandler(mySocket);
                    System.out.println("clientHandler created");
                }
            });
            if (loginUIObj == null&&registerUIObj==null&&chattingUIObj==null) {
                loginUIObj = new LoginUI(this);
                loginUIObj.setVisible(true);
            }
            if(chattingUIObj!=null){
                 chattingUIObj.setEnabled(true);
            }

        } catch (IOException ex) {
            System.out.println("the server is close4");
            JOptionPane.showMessageDialog(null, "the server is down");
        }
    }

    void sendLoginData(String username, String password) {
        SentToServerData sentMsg = new SentToServerData();
        sentMsg.type = SentToServerData.Type.LOGINDATA;
        sentMsg.username = username;
        sentMsg.password = password;
        try {
            objectOutputStream.writeUnshared(sentMsg);
        } catch (IOException ex) {
            System.out.println("the server is done5");
            JOptionPane.showMessageDialog(null, "the serever is down :(");
            // Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("");
    }

    void getContactList() {
        try {
            SentToServerData sentMsg = new SentToServerData();
            sentMsg.type = SentToServerData.Type.DEMANDCONTACTLIST;
            objectOutputStream.writeUnshared(sentMsg);
        } catch (IOException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void switchToRegisteration() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                loginUIObj.dispose();
                registerUIObj = new RegisterUI(ClientController.this);
                registerUIObj.setVisible(true);
            }
        });

    }

    void switchToLogin() {
        registerUIObj.dispose();
        loginUIObj = new LoginUI(this);
        loginUIObj.setVisible(true);
    }

    void sendRegisterationData(String name, String username, String password,
            String email, String gender) {
        SentToServerData sentMsg = new SentToServerData();
        sentMsg.type = SentToServerData.Type.REGISTERATIONDATA;
        sentMsg.name = name;
        sentMsg.username = username;
        sentMsg.password = password;
        sentMsg.email = email;
        sentMsg.gender = gender;
        try {
            objectOutputStream.writeUnshared(sentMsg);
        } catch (IOException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void sendTxtMsg(String txtMsg) {
        SentToServerData sentMsg = new SentToServerData();
        sentMsg.type = SentToServerData.Type.TXTMSG;
        sentMsg.txtMsg = txtMsg;
        try {
            objectOutputStream.writeUnshared(sentMsg);
        } catch (IOException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void changeMyStatus(String status) {
        SentToServerData sentMsg = new SentToServerData();
        sentMsg.type = SentToServerData.Type.STATUSCHANGE;
        sentMsg.status = status;
        try {
            objectOutputStream.writeUnshared(sentMsg);
            System.out.println("my status client controller" + sentMsg.status);
        } catch (IOException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void sendFile(String filePath) {
        File sentFile = new File(filePath);
        SentToServerData sentMsg = new SentToServerData();
        sentMsg.type = SentToServerData.Type.FILE;
        sentMsg.fileName = sentFile.getName();
        sentMsg.fileSize = (int) sentFile.length();
        sentMsg.fileData = new byte[(int) sentMsg.fileSize];
        FileInputStream fis;
        try {
            fis = new FileInputStream(sentFile);
            BufferedInputStream bis = new BufferedInputStream(fis);
            bis.read(sentMsg.fileData, 0, sentMsg.fileSize);
            objectOutputStream.writeUnshared(sentMsg);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void signOut() {
        changeMyStatus("offline");
        chattingUIObj.dispose();
        loginUIObj = new LoginUI(this);
        loginUIObj.setVisible(true);
    }

}
