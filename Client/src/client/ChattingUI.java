package client;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import dataclass.UserStatus;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author hesham
 */
public final class ChattingUI extends javax.swing.JFrame {

    ClientController clientControllerObj;

    private JList emotionlist;
    private JList mystatus;
    JList myFriendsJlist;
    DefaultListModel friendsModel;

    String htmlString;
    String htmlChat = "";
    String htmlch = "";
    String contacthtml = "";
    private ManageImageText emos[] = {
        new ManageImageText(":)", "emoticons_icons/27.gif"),
        new ManageImageText(":(", "emoticons_icons/2.gif"),
        new ManageImageText(":D", "emoticons_icons/3.gif"),
        new ManageImageText("(Y)", "emoticons_icons/15.gif"),
        new ManageImageText(":)", "emoticons_icons/27.gif"),
        new ManageImageText(":(", "emoticons_icons/2.gif"),
        new ManageImageText(":D", "emoticons_icons/3.gif"),
        new ManageImageText("(Y)", "emoticons_icons/15.gif"),};

    private final ManageImageText statuses[] = {
        new ManageImageText("available", "emoticons_icons/online.png"),
        new ManageImageText("away", "emoticons_icons/busy.png"),
        new ManageImageText("busy", "emoticons_icons/avaliable.png"),
        new ManageImageText("offline", "emoticons_icons/offline.png")};

    //Vector<ManageImageText> myFriends = new Vector<ManageImageText>();
    public ChattingUI(ClientController Controller) {
        initComponents();
        clientControllerObj = Controller;

        friendsModel = new DefaultListModel();
        // addUserToContactList("mohmed", "online");
        // addUserToContactList("mohmed", "offline");
        // addUserToContactList("mohmed", "busy");
        //changeContactStatus("hesham", "Busy");
        //friendsModel.addElement(new ManageImageText( "hesham","3.gif"));
        myFriendsList.setCellRenderer(new ContactCellRenderer());
        myFriendsList.setModel(friendsModel);

        //////////////////////////////////show ads////////////////////////////////
        //////////////////////////////////save in file////////////////////////////////////////////
        JMenuItem save = new JMenuItem("save");
        tollBar.add(save);
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                if (fc.showSaveDialog(ChattingUI.this) == JFileChooser.APPROVE_OPTION) {
                    try {
                        String path = fc.getSelectedFile().getPath();
                        try (FileOutputStream fos = new FileOutputStream(path)) {
                            byte[] b = chatArea.getText().getBytes();
                            fos.write(b);
                        }
                    } catch (Exception ex) {
                    }

                }
            }
        });
        ///////////////////////////////for upload file//////////////////////////////////////////
        JMenuItem uploadfile = new JMenuItem("UploadFile");
        tollBar.add(uploadfile);
        uploadfile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                JFileChooser fc = new JFileChooser();
                if (fc.showOpenDialog(ChattingUI.this) == JFileChooser.APPROVE_OPTION) {
                    try {
                        String path = fc.getSelectedFile().getPath();
                        clientControllerObj.sendFile(path);
                    } catch (Exception ex) {
                    }

                }
            }
        });
        ////////////////////////////my status///////////////////////////////
        final JComboBox mystatus = new JComboBox(statuses);
        mystatus.setRenderer(new StatusCellRenderer());
        tollBar.add(mystatus);
        //mystatus2.setModel(mystatus);
        //mystatus2.setRenderer(new StatusCellRenderer());
        mystatus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selected = mystatus.getSelectedIndex();
                System.out.println("Selected Elements:");
                ManageImageText element = (ManageImageText) mystatus.getSelectedItem();
                System.out.println("my status ui" + element.getTitle());
                clientControllerObj.changeMyStatus(element.getTitle());
            }
        });
        //////////////////////////////for emotion///////////////////////////////////////
        emotionlist = new JList(emos);
        emotionlist.setCellRenderer(new EmotionCellRenderer());
        emotionlist.setVisibleRowCount(4);
        final JScrollPane pane = new JScrollPane(emotionlist);
        emotionMenu.add(pane);

        emotionlist.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                String imgPath = "";
                int selected = emotionlist.getSelectedIndex();
                System.out.println("Selected Elements:");
                ManageImageText element = (ManageImageText) emotionlist.getModel()
                        .getElementAt(selected);
                System.out.println("  " + element.getTitle());
                String getMsgAreaText = msgArea.getText();
                getMsgAreaText += " " + element.getTitle();
                msgArea.setText(getMsgAreaText);
            }
        });

        ////////////////////////////////my test should remove/////////////////////////////////////
        //addUserToContactList("mohmed", "busy");
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        chatArea = new javax.swing.JTextPane();
        sendMsg = new javax.swing.JButton();
        msgArea = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        showAds = new javax.swing.JEditorPane();
        contactPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        myFriendsList = new javax.swing.JList<String>();
        jButton1 = new javax.swing.JButton();
        tollBar = new javax.swing.JMenuBar();
        emotionMenu = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        chatArea.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                chatAreaCaretUpdate(evt);
            }
        });
        jScrollPane2.setViewportView(chatArea);

        sendMsg.setText("Send");
        sendMsg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendMsgActionPerformed(evt);
            }
        });

        showAds.setEditable(false);
        jScrollPane3.setViewportView(showAds);

        jScrollPane1.setViewportView(myFriendsList);

        jButton1.setText("Sign Out");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout contactPanelLayout = new javax.swing.GroupLayout(contactPanel);
        contactPanel.setLayout(contactPanelLayout);
        contactPanelLayout.setHorizontalGroup(
            contactPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contactPanelLayout.createSequentialGroup()
                .addGroup(contactPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        contactPanelLayout.setVerticalGroup(
            contactPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, contactPanelLayout.createSequentialGroup()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 466, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        emotionMenu.setText("Emotions");
        tollBar.add(emotionMenu);

        setJMenuBar(tollBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(msgArea)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sendMsg, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(contactPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(msgArea)
                    .addComponent(sendMsg, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3)
                .addContainerGap())
            .addComponent(contactPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void sendMsgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendMsgActionPerformed
        // TODO add your handling code here:
        String msg=msgArea.getText();
        msg=msg.trim();
        if (msg.length()>0) {
            clientControllerObj.sendTxtMsg(msgArea.getText());
            msgArea.setText("");
        }
    }//GEN-LAST:event_sendMsgActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        clientControllerObj.signOut();
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void chatAreaCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_chatAreaCaretUpdate
        // TODO add your handling code here:
    }//GEN-LAST:event_chatAreaCaretUpdate

    /**
     * @param args the command line arguments
     */
    void displayServerMsg(String txtMsg) {
        String object = new String();
        String htmlobj = "";
        htmlobj = txtMsg;

        showAds.setText(htmlobj);
    }

    void displayMsg(String txtMsg, String username) {
        //da ely gay mn el server we bezhr 3ala textarea 
        int count = 0;
        String img = " ";
        System.out.println("te receved msg");
        htmlch += "<b>"+username + ": ";
        //ImageIcon img = new ImageIcon();
        for (String retval : txtMsg.split("\\s+")) {
            for (int i = 0; i < emos.length; i++) {
                //System.out.println("founded:"+retval);
                // System.out.println("founded:"+emos[i].getTitle()+"||retval"+retval);
                if (emos[i].getTitle().equals(retval)) {
                    retval = " ";
                    System.out.println("founded:" + emos[i].getTitle());
                    img = emos[i].getImagePath();
                    java.net.URL urlimg = getClass().getResource(img);
                    System.out.println("chatapp.ChatRoomForm.displayMsg():" + img);
                    htmlch += "<img src=\"" + urlimg + "\" />";
                    System.out.println("retval||" + retval);
                    System.out.println("retval with html||" + retval);
                    System.out.println("text msg||" + htmlch);
                }
            }
            htmlch += " " + retval +"</b>";
        }
        StringBuilder newLine = new StringBuilder();
        newLine.append("<br>");
        htmlch += newLine.toString();
        chatArea.setContentType("text/html");
        chatArea.setText(htmlch+"</b>");
   
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
        //chatArea.setText(txtMsg);
    }

    void addUserToContactList(String username, String status) {
        String imgpath = " ";
        friendsModel = (DefaultListModel) myFriendsList.getModel();
        for (int j = 0; j < statuses.length; j++) {
            if (status.equals(statuses[j].getTitle())) {
                System.out.println("user name and status::" + username + "::" + status);
                imgpath = statuses[j].getImagePath();
                System.out.println("user get image path" + statuses[j].getImagePath());
            }
        }
        friendsModel.addElement(new ManageImageText(username, imgpath));
    }

    void setContactList(Vector<UserStatus> clientList) {
        DefaultListModel allContactList = new DefaultListModel();
        String imgpath = "";
        for (UserStatus user : clientList) {
            for (int i = 0; i < statuses.length; i++) {
                System.out.println(" user.status ::" + user.status);
                System.out.println("statuses[i].getTitle()::" + statuses[i].getTitle());
                if (user.status.equals(statuses[i].getTitle())) {
                    System.out.println("set contact list in if statement ::" + statuses[i]);
                    imgpath = statuses[i].getImagePath();
                    allContactList.addElement(new ManageImageText(user.username, imgpath));
                }
            }
        }
        myFriendsList.setModel(allContactList);
        friendsModel = allContactList;
    }
    
    void changeContactStatus(String username, String status) {
        //  System.out.println("addUserToContactList:"+username+":"+status);
        String img = "";
        String name = " ";
        boolean isExist = false;
        friendsModel = (DefaultListModel) myFriendsList.getModel();

        for (int i = 0; i < statuses.length; i++) {
            if (status.equals(statuses[i].getTitle())) {
                img = statuses[i].getImagePath();
            }
        }
        for (int i = 0; i < friendsModel.size(); i++) {
            if (username.equals(friendsModel.getElementAt(i).toString())) {
                System.out.println("client.ChattingUI.changeContactStatus()");
                friendsModel.set(i, new ManageImageText(username, img));
                isExist = true;
                break;
            } else {
                //isExist = false;
            }

        }
        if (!isExist) {
            addUserToContactList(username, status);
        }
    }

    boolean askToDownload(String fileName, int fileSize, String username) {
        int answer = JOptionPane.showConfirmDialog(null,
                username + " has sent file(size : " + fileSize / 1024 + " kb, name : " + fileName + " \n "
                + "Do you want to download it?",
                "Download File", JOptionPane.YES_NO_OPTION);
        if (answer == JOptionPane.YES_OPTION) {
            return true;
        }else{
            return false;
        }
    }

    


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextPane chatArea;
    private javax.swing.JPanel contactPanel;
    private javax.swing.JMenu emotionMenu;
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField msgArea;
    private javax.swing.JList<String> myFriendsList;
    private javax.swing.JButton sendMsg;
    private javax.swing.JEditorPane showAds;
    private javax.swing.JMenuBar tollBar;
    // End of variables declaration//GEN-END:variables
}

class ManageImageText {

    private final String title;

    private String imagePath;

    private ImageIcon image;

    public ManageImageText(String title, String imagePath) {
        this.title = title;
        this.imagePath = imagePath;
    }

    public String getTitle() {
        return title;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public ImageIcon getImage() {
        if (image == null) {
            java.net.URL url = getClass().getResource(imagePath);
            BufferedImage pathurl;
            try {
                pathurl = ImageIO.read(url);
                image = new ImageIcon(pathurl);
            } catch (IOException ex) {
                // Logger.getLogger(ManageImageText.class.getName()).log(Level.SEVERE, null, ex);
            }
            // image = new ImageIcon(url);
        }
        return image;
    }

    public void setImage(String path) {
        java.net.URL url = getClass().getResource(path);
        image = new ImageIcon(url);
    }

    public String toString() {
        return title;
    }
}

class Friends {

    private final String name;
    private String status;

    public Friends(String name, String status) {
        this.name = name;
        this.status = status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return status;
    }

    public String getUserName() {
        return name;
    }

    public String toString() {
        return name;
    }

}

class StatusCellRenderer implements ListCellRenderer {

    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
        // System.out.println("test.StatusCellRenderer.getListCellRendererComponent()");
        ManageImageText entry = (ManageImageText) value;
        JLabel label = new JLabel();
        label.setOpaque(true);
        label.setIcon(entry.getImage());
        label.setText(entry.getTitle());
        if (isSelected) {
            label.setForeground(Color.white);
        } else {
            label.setBackground(Color.white);
            label.setForeground(Color.black);
        }
        return label;
    }
}

class EmotionCellRenderer extends JLabel implements ListCellRenderer {

    private static final Color HIGHLIGHT_COLOR = new Color(0, 0, 128);
    Icon icon = UIManager.getIcon("html.pendingImage");

    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
        System.out.println("test.BookCellRenderer.getListCellRendererComponent()");
        ManageImageText entry = (ManageImageText) value;
        JLabel label = new JLabel(icon);
        label.setIcon(entry.getImage());
        label.setOpaque(true);
        //label.setIconTextGap(12);
        //Dimension dim = new Dimension(90, 90);
        //label.setPreferredSize(dim);
        //label.setHorizontalTextPosition(JLabel.CENTER);
        //label.setVerticalTextPosition(JLabel.BOTTOM);
        setIcon(entry.getImage());
        if (isSelected) {
            setBackground(HIGHLIGHT_COLOR);
            setForeground(Color.white);
        } else {
            setBackground(Color.white);
            setForeground(Color.black);
        }
        return label;
    }
}

class ContactCellRenderer implements ListCellRenderer {

    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
        // System.out.println("test.StatusCellRenderer.getListCellRendererComponent()");
        ManageImageText entry = (ManageImageText) value;
        JLabel label = new JLabel();
        label.setOpaque(true);
        label.setIcon(entry.getImage());
        label.setText(entry.getTitle());
        if (isSelected) {
            label.setForeground(Color.white);
        } else {
            label.setBackground(Color.white);
            label.setForeground(Color.black);
        }
        return label;
    }
}
