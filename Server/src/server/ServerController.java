package server;

import dataclass.ReceivedFromServerData;
import dataclass.SentToServerData;
import dataclass.UserStatus;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author phawzy
 */
public class ServerController {

    static Vector<ClientHandler> clientsLoggedInVector = new Vector<ClientHandler>();
    static Vector<ClientHandler> clientsConnectedVector = new Vector<ClientHandler>();
    boolean isChange;
    ServerModel serverModelObj;
    ServerUI serverUIObj;
    ServerSocketListener serverSocketListener;
    ServerSocket serverSocket;
    boolean serverRun = true;

    public ServerController() {
        isChange = true;
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                serverModelObj = new ServerModel(ServerController.this);
                serverUIObj = new ServerUI(ServerController.this);
                serverUIObj.setVisible(true);
            }
        });

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                serverSocketListener = new ServerSocketListener();
            }
        });

    }

    void stopServer() {
        System.out.println("server.ServerController.stopServer()");
        if (serverRun == true) {
            serverRun = false;
            // serverSocketListener.run();
            try {
                serverSocket.close();
                serverSocketListener.run();
            } catch (IOException ex) {
                Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (!clientsLoggedInVector.isEmpty()) {
            for (int i = 0; i < clientsLoggedInVector.size(); i++) {
                clientsLoggedInVector.get(i).stop();
                try {
                    if (clientsLoggedInVector.get(i).objectInputStream != null) {
                        clientsLoggedInVector.get(i).objectInputStream.close();
                    }
                    if (clientsLoggedInVector.get(i).objectOutputStream != null) {
                        clientsLoggedInVector.get(i).objectOutputStream.close();
                    }
                    /* if(clientsLoggedInVector.get(i).myServerControllerObj.serverSocket!=null)
                     {
                     clientsLoggedInVector.get(i).myServerControllerObj.serverSocket.close();
                     }*/

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    void startServer() {
        System.out.println("server.ServerController.startServer()");
        if (serverRun == false) {
            serverRun = true;
            serverSocketListener.run();
            try {
                if (serverSocket == null) {
                    serverSocket.setReuseAddress(true);
                }
            } catch (SocketException ex) {
                Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
            }
            new ServerSocketListener();

        }
    }

    void broadcastMsg(String msg) {
        ReceivedFromServerData sentMsg = new ReceivedFromServerData();
        sentMsg.serverMsg = msg;
        sentMsg.type = ReceivedFromServerData.Type.SERVERMSG;
        for (ClientHandler ch : clientsLoggedInVector) {
            try {
                ch.objectOutputStream.writeUnshared(sentMsg);
            } catch (Exception e) {
                try {
                    serverModelObj.changeStatus(ch.username, ch.status);
                    ch.objectInputStream.close();
                    ch.objectOutputStream.close();
                    ch.clientSocket.close();
                    ch.isClosed = false;
                } catch (IOException ex) {
                    Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }

    }

    void displayChart() {
        int totalNum = serverModelObj.getNumOfUsers();
        int availableNum = getStatusNum("available");
        int busyNum = getStatusNum("busy");
        int awayNum = getStatusNum("away");
        int offlineNum = totalNum - availableNum - busyNum - awayNum;
        int onlineNum = totalNum - offlineNum;
        serverUIObj.displayNumOfOnlineOfflineUsers(onlineNum, offlineNum);
        serverUIObj.displayUsersStatusChart(availableNum, busyNum, awayNum, offlineNum);
        System.out.println(availableNum + " " + busyNum + " " + awayNum + " " + offlineNum);
    }

    int getStatusNum(String status) {
        int num = 0;
        for (ClientHandler ch : clientsLoggedInVector) {
            if (status.equals(ch.status)) {
                num++;
            }
        }
        return num;
    }

    class ServerSocketListener extends Thread {

        public ServerSocketListener() {
            try {
                serverSocket = new ServerSocket(5006);
            } catch (IOException ex) {
                Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
            }
            start();
        }

        @Override
        public void run() {
            System.out.println("server run||" + serverRun);
            System.out.println("test server 2");
            while (serverRun) {
                try {
                    final Socket s = serverSocket.accept();
                    System.out.println("test server 1");
                    // if(serverRun)
                    // {
                    java.awt.EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            ClientHandler clientHandler = new ClientHandler(s);
                        }
                    });
                    // }
                } catch (IOException ex) {
                    break;
                }
            }
        }
    }

    class ClientHandler extends Thread {

        boolean isClosed;
        ObjectInputStream objectInputStream;
        ObjectOutputStream objectOutputStream;
        String username;
        String status;
        Socket clientSocket;

        ServerController myServerControllerObj;

        public ClientHandler(Socket cs) {
            try {
                username = "";
                status = "";
                System.out.println("hello");
                isClosed = true;
                clientSocket = cs;
                objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                objectOutputStream.flush();
                objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
                clientsConnectedVector.add(this);
                start();
            } catch (IOException ex) {
                try {
                    objectInputStream.close();
                    objectOutputStream.close();
                } catch (IOException ex1) {
                    Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }

        @Override
        public void run() {
            while (true) {
                SentToServerData receivedMsg = new SentToServerData();
                ReceivedFromServerData sentMsg = new ReceivedFromServerData();
                try {
                    receivedMsg = (SentToServerData) objectInputStream.readUnshared();
                    if (!isClosed) {
                        break;
                    }
                    if (receivedMsg != null) {
                        switch (receivedMsg.type) {
                            case TXTMSG:
                                sentMsg.type = ReceivedFromServerData.Type.TXTMSG;
                                sentMsg.txtMsg = receivedMsg.txtMsg;
                                sentMsg.username = this.username;
                                sendMessageToAll(sentMsg);
                                break;
                            case STATUSCHANGE:
                                sentMsg.type = ReceivedFromServerData.Type.CONTACTSTATUSCHANGE;
                                sentMsg.username = this.username;
                                sentMsg.status = receivedMsg.status;
                                this.status = receivedMsg.status;
                                System.out.println("my status server controller" + sentMsg.status);
                                serverModelObj.changeStatus(this.username, this.status);
                                sendMessageToAllExceptOne(sentMsg, this.username);
                                if (this.status.equals("offline")) {
                                    if (clientsLoggedInVector.contains(this)) {
                                        clientsLoggedInVector.removeElement(this);
                                    }
                                }
                                break;
                            case REGISTERATIONDATA:
                                boolean canRegister = serverModelObj.authenticateUsernameRegister(receivedMsg.username);
                                sentMsg.type = ReceivedFromServerData.Type.REGISTERATIONRESPONSE;
                                boolean isAdded;
                                if (canRegister) {
                                    isAdded = serverModelObj.addNewUser(receivedMsg.name, receivedMsg.username,
                                            receivedMsg.password, receivedMsg.email, receivedMsg.gender);
                                    if (isAdded) {
                                        sentMsg.registerationResponse = "ok";   /// user added
                                    } else {
                                        sentMsg.registerationResponse = "error";   ///db error
                                    }
                                } else {
                                    isAdded = false;
                                    sentMsg.registerationResponse = "error";  /// username already exists
                                }
                                try {
                                    this.objectOutputStream.writeUnshared(sentMsg);
                                } catch (IOException ex) {
                                    this.objectInputStream.close();
                                    this.objectOutputStream.close();
                                    this.clientSocket.close();
                                    this.isClosed = false;
                                    if (clientsLoggedInVector.contains(this)) {
                                        clientsLoggedInVector.removeElement(this);
                                    }
                                    clientsConnectedVector.removeElement(this);
                                    Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                if (isAdded) {
                                    sentMsg.type = ReceivedFromServerData.Type.NEWCONTACT;
                                    sentMsg.username = receivedMsg.username;
                                    sentMsg.status = "offline";
                                    sendMessageToAllExceptOne(sentMsg, sentMsg.username);
                                    System.out.println("server add client" + sentMsg.username);
                                }

                                break;
                            case LOGINDATA:
                                boolean canLogin = authenticateUserLogin(
                                        receivedMsg.username, receivedMsg.password);
                                sentMsg.type = ReceivedFromServerData.Type.LOGINRESPONSE;
                                if (canLogin) {
                                    sentMsg.loginResponse = "ok";
                                    clientsLoggedInVector.add(this);
                                    this.username = receivedMsg.username;
                                    this.status = "available";
                                    serverModelObj.changeStatus(this.username, this.status);
                                } else {
                                    sentMsg.loginResponse = "error";
                                }

                                try {
                                    this.objectOutputStream.writeUnshared(sentMsg);
                                    sentMsg.type = ReceivedFromServerData.Type.CONTACTSTATUSCHANGE;
                                    sentMsg.status = "available";
                                    sentMsg.username = this.username;
                                    sendMessageToAllExceptOne(sentMsg, this.username);
                                } catch (IOException ex) {
                                    serverModelObj.changeStatus(this.username, "offline");
                                    this.objectInputStream.close();
                                    this.objectOutputStream.close();
                                    this.clientSocket.close();
                                    this.isClosed = false;
                                    if (clientsLoggedInVector.contains(this)) {
                                        clientsLoggedInVector.removeElement(this);
                                    }
                                    clientsConnectedVector.removeElement(this);
                                    Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
                                }

                                break;
                            case DEMANDCONTACTLIST:
                                sentMsg.type = ReceivedFromServerData.Type.CONTACTLIST;
                                int i = 0;
                                UserStatus[] clientsStatus = serverModelObj.getAllUsernamesStatus();
                                for (UserStatus userStatus : clientsStatus) {
                                    if (!(this.username.equals(userStatus.username))) {
                                        UserStatus user = new UserStatus();
                                        user.username = userStatus.username;
                                        user.status = userStatus.status;
                                        sentMsg.contactListVector.add(user);
                                    }
                                    System.out.println("i is " + i);
                                    i++;
                                }
                                try {
                                    this.objectOutputStream.writeUnshared(sentMsg);
                                } catch (IOException ex) {
                                    serverModelObj.changeStatus(this.username, "offline");
                                    this.objectInputStream.close();
                                    this.objectOutputStream.close();
                                    this.clientSocket.close();
                                    this.isClosed = false;
                                    if (clientsLoggedInVector.contains(this)) {
                                        clientsLoggedInVector.removeElement(this);
                                    }
                                    clientsConnectedVector.removeElement(this);
                                    Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
                                }

                                break;
                            case FILE:
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
                                sentMsg.type = ReceivedFromServerData.Type.ASKTODOWNLOAD;
                                sentMsg.fileName = receivedMsg.fileName;
                                sentMsg.fileSize = (int) receivedMsg.fileSize;
                                sentMsg.username = this.username;
                                sendMessageToAllExceptOne(sentMsg, this.username);

                                break;
                            case CONFIRMDOWNLOAD:
                                sentMsg.type = ReceivedFromServerData.Type.FILE;
                                sentMsg.fileName = receivedMsg.fileName;
                                sentMsg.fileSize = (int) receivedMsg.fileSize;
                                sentMsg.fileData = new byte[(int) sentMsg.fileSize];
                                sentMsg.username = this.username;
                                System.out.println("confirm server");
                                FileInputStream fis;
                                try {
                                    fis = new FileInputStream(sentMsg.fileName);
                                    System.out.println(sentMsg.fileName);
                                    BufferedInputStream bis = new BufferedInputStream(fis);
                                    bis.read(sentMsg.fileData, 0, sentMsg.fileSize);
                                    System.out.println("file read server");
                                    try {
                                        this.objectOutputStream.writeUnshared(sentMsg);
                                    } catch (IOException ex) {
                                        serverModelObj.changeStatus(this.username, "offline");
                                        this.objectInputStream.close();
                                        this.objectOutputStream.close();
                                        this.clientSocket.close();
                                        this.isClosed = false;
                                        if (clientsLoggedInVector.contains(this)) {
                                            clientsLoggedInVector.removeElement(this);
                                        }
                                        clientsConnectedVector.removeElement(this);
                                        Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                } catch (FileNotFoundException ex) {
                                    Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (IOException ex) {
                                    Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                break;
                        }
                    }
                } catch (IOException e) {
                    try {
                        serverModelObj.changeStatus(this.username, "offline");
                        this.isClosed = false;
                        this.objectInputStream.close();
                        this.objectOutputStream.close();
                        this.clientSocket.close();

                        if (clientsLoggedInVector.contains(this)) {
                            clientsLoggedInVector.removeElement(this);
                        }
                        clientsConnectedVector.removeElement(this);

                    } catch (IOException ex1) {
                    }
                } catch (ClassNotFoundException e) {

                }

            }
        }

        boolean authenticateUserLogin(String username, String password) {
            boolean isLoggedIn = false;
            boolean canLogin = false;
            try {
                for (ClientHandler ch : clientsLoggedInVector) {
                    if (username.equals(ch.username)) {
                        isLoggedIn = true;
                    }

                }
            } catch (Exception ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    serverModelObj.changeStatus(this.username, "offline");
                    this.objectInputStream.close();
                    this.objectOutputStream.close();
                    this.clientSocket.close();
                    this.isClosed = false;
                    clientsLoggedInVector.removeElement(this);
                    clientsConnectedVector.removeElement(this);
                } catch (IOException ex1) {
                    Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
            if (!isLoggedIn) {
                canLogin = serverModelObj.authenticateUserLogin(username, password);
            }
            return canLogin;
        }

        void sendMessageToOneClient(ReceivedFromServerData msg, String username) {
            try {
                for (ClientHandler ch : clientsLoggedInVector) {
                    if (username.equals(ch.username)) {
                        ch.objectOutputStream.writeUnshared(msg);
                    }

                }
            } catch (Exception ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    serverModelObj.changeStatus(this.username, "offline");
                    this.objectInputStream.close();
                    this.objectOutputStream.close();
                    this.clientSocket.close();
                    this.isClosed = false;
                    clientsLoggedInVector.removeElement(this);
                    clientsConnectedVector.removeElement(this);
                } catch (IOException ex1) {
                    Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }

        void sendMessageToAll(ReceivedFromServerData msg) {
            try {
                for (ClientHandler ch : clientsLoggedInVector) {
                    ch.objectOutputStream.writeUnshared(msg);
                }
            } catch (Exception ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    serverModelObj.changeStatus(this.username, "offline");
                    this.objectInputStream.close();
                    this.objectOutputStream.close();
                    this.clientSocket.close();
                    this.isClosed = false;
                    clientsLoggedInVector.removeElement(this);
                    clientsConnectedVector.removeElement(this);
                } catch (IOException ex1) {
                    Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }

        void sendMessageToAllExceptOne(ReceivedFromServerData msg, String username) {
            try {
                for (ClientHandler ch : clientsLoggedInVector) {
                    if (!(username.equals(ch.username))) {
                        ch.objectOutputStream.writeUnshared(msg);
                    }

                }
            } catch (Exception ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    serverModelObj.changeStatus(this.username, "offline");
                    this.objectInputStream.close();
                    this.objectOutputStream.close();
                    this.clientSocket.close();
                    clientsLoggedInVector.removeElement(this);
                    clientsConnectedVector.removeElement(this);
                } catch (IOException ex1) {
                    Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }
    }

}
