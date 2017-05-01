
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;

public class Homepage extends javax.swing.JFrame implements Runnable {

    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket connection;

    private String freq;
    private String anonyOnlineName = "";
    private String friends;
    private String friendsOnline = "";
    private String friendWantToChat;
    private String OnlineFriends="";
    int notiCount=0;
    public Homepage() {
        initComponents();
        setVisible(true);

        //if user presses close button
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.out.println("exit pressed");
                try {
                    output.writeObject("logout");
                } catch (IOException ex) {
                }
                System.exit(0);
            }
        });

    }
    

    public void send(String username, Socket connection, ObjectInputStream input, ObjectOutputStream output) {
        this.input = input;
        this.output = output;
        this.connection = connection;
        jLabel1.setText("Welcome " + username);

        //new chatReceiveThread(username, connection, input, output);
        new fileReceiveThread(username);
        dataThread();
    }

    public void dataThread() {
        Thread t;
        t = new Thread((Runnable) this, "Demo Thread");
        t.start();
    }

    public void run() {
        while (true) {
            addDataInLists();

        }

    }

    public void addDataInLists() {
        anonyOnlineName = "";
        friendsOnline = "";

        String OnlineName = receive("a"); //recieve all the names who are currently online
        this.friends = receive("f");  // receive friend list
        setOnline(OnlineName, this.friends); //set anonymous and frind online list

        showAnonymousOnline(this.anonyOnlineName);
        showFriends(this.friends);
        showFriendReq(receive("gfr")); //receive friend requests and show them
        showFriendsOnline(this.friendsOnline);

    }

    public void setOnline(String str1, String str2) {

        //tokenize str2 first and then check if they exists in str1
        StringTokenizer st = new StringTokenizer(str2, "#");
        while (st.hasMoreTokens()) {
            String temp = st.nextToken();
            if (str1.contains(temp)) {
                this.friendsOnline = this.friendsOnline + "#" + temp; //add him to the friend online list
                str1 = str1.replace(temp, ""); //remove him from the global online list
            }
        }

        this.anonyOnlineName = str1; //add the remaining global online list to the anonymous

        if (this.friendsOnline.equals("")) {
            this.friendsOnline = "No one is Online";
        }
        //if(this.anonyOnlineName.equals("")) this.anonyOnlineName= "No one is Online";
    }

    public void showFriendReq(String freq) {
        this.freq = freq;
    }

    public void showAnonymousOnline(String anonyOnlineName) {
        DefaultListModel dlm1 = new DefaultListModel(); //introduce model to add data to list anonymous
        StringTokenizer st = new StringTokenizer(anonyOnlineName, "#");  //tokenize the names

        if (!st.hasMoreTokens()) {
            addList(dlm1, anonyOnline, "no one is online");
        }

        while (st.hasMoreTokens()) {
            addList(dlm1, anonyOnline, st.nextToken());                      //and send only the id to add in the list
        }
    }

    public void showFriends(String friends) {
        DefaultListModel dlm1 = new DefaultListModel(); //introduce model to add data to list of friends
        StringTokenizer st = new StringTokenizer(friends, "#");  //tokenize the names
        while (st.hasMoreTokens()) {
            addList(dlm1, friendList, st.nextToken());                      //and send only the id to add in the list
        }
    }

    public void showFriendsOnline(String friendsOnline) {
        DefaultListModel dlm1 = new DefaultListModel(); //introduce model to add data to list of friends
        StringTokenizer st = new StringTokenizer(friendsOnline, "#");  //tokenize the names
        while (st.hasMoreTokens()) {
            String abc=st.nextToken();
            addList(dlm1, friendOnline, abc);                      //and send only the id to add in the list
            OnlineFriends=OnlineFriends+"#"+abc;
            
        }
    }

    public void addList(DefaultListModel dlm, JList list, String data) {
        dlm.addElement(data);
        list.setModel(dlm);
    }

    public String receive(String str) {
        try {
            Thread.sleep(2000);

        } catch (InterruptedException ex) {
            System.out.println("error in thread");
        }

        String retVal = "";
        try {
            try {
                if (str.equals("a")) {
                    output.writeObject("anonymous");
                } else if (str.equals("f")) {
                    output.writeObject("friends");
                } else if (str.equals("gfr")) {
                    output.writeObject("getFReq");
                }

                while (retVal.equals("")) {
                    retVal = (String) input.readObject();
                }

            } catch (ClassNotFoundException classNotFoundException) {
            }
        } catch (IOException ioException) {
        }

        return retVal;
    }
    
    public String getFilenameExtension(String filename) 
    {
        String extension = "";
        int i = filename.lastIndexOf('.');
        if (i > 0) 
        {
            extension = filename.substring(i + 1);
        }
        return extension;
    }
   

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel6 = new javax.swing.JLabel();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jScrollPane1 = new javax.swing.JScrollPane();
        anonyOnline = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        friendOnline = new javax.swing.JList();
        jScrollPane3 = new javax.swing.JScrollPane();
        friendList = new javax.swing.JList();
        inboxNoti = new javax.swing.JLabel();
        broadCast = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        request = new javax.swing.JMenu();
        frequest = new javax.swing.JMenuItem();
        logout = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();

        jLabel6.setText("jLabel6");

        jMenuItem1.setText("jMenuItem1");

        jMenuItem2.setText("jMenuItem2");

        jMenuItem8.setText("jMenuItem8");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        anonyOnline.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                anonyOnlineMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(anonyOnline);

        jLabel1.setText("Admin");

        jLabel2.setText("     Online (Anonymous)");

        jLabel3.setText("          Online (Friends)");

        jLabel4.setText("             Friend List");

        friendOnline.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                friendOnlineMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(friendOnline);

        friendList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                friendListMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(friendList);

        broadCast.setText("broadcast Message");
        broadCast.setToolTipText("");
        broadCast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                broadCastActionPerformed(evt);
            }
        });

        jMenu1.setText("Home");

        jMenuItem7.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem7.setText("Home");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem7);

        jMenuItem9.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0));
        jMenuItem9.setText("Exit");
        jMenu1.add(jMenuItem9);

        jMenuBar1.add(jMenu1);

        request.setText("Requests");

        frequest.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
        frequest.setText("Friend Requests");
        frequest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                frequestActionPerformed(evt);
            }
        });
        request.add(frequest);

        jMenuBar1.add(request);

        logout.setText("Logout");

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem5.setText("Logout");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        logout.add(jMenuItem5);

        jMenuBar1.add(logout);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(inboxNoti, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(132, 132, 132)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(broadCast)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                        .addContainerGap(27, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inboxNoti))
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(broadCast)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void anonyOnlineMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_anonyOnlineMousePressed

        Object name = anonyOnline.getSelectedValue();
        int response = JOptionPane.showConfirmDialog(null, "Do you want to send this person friend request?", "Confirm",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            try {
                output.writeObject("frequest");
                output.writeObject(name);
                JOptionPane.showMessageDialog(null, "Request Sent");
            } catch (IOException ex) {
            }
        }
    }//GEN-LAST:event_anonyOnlineMousePressed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        // TODO add your handling code here:
        try {
            output.writeObject("logout");
            new Login().setVisible(true);
            setVisible(false);
            connection.close();
        } catch (IOException ioException) {
        }
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void frequestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_frequestActionPerformed
        // TODO add your handling code here:
        FriendRequests ob = new FriendRequests();
        ob.set(freq, connection, input, output);

    }//GEN-LAST:event_frequestActionPerformed

    private void friendOnlineMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_friendOnlineMouseClicked
        // TODO add your handling code here:
        Object name = friendOnline.getSelectedValue();
        chat ob = new chat();
        ob.startChatThread(name.toString(), connection, input, output);
    }//GEN-LAST:event_friendOnlineMouseClicked

    private void friendListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_friendListMouseClicked
        // TODO add your handling code here:
        Object name = friendList.getSelectedValue();

        Object stringArray[] = {"Send Message", "Send File", "Cancel"};

        int response = JOptionPane.showOptionDialog(null, "Press any", "Select an Option",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, stringArray,
                stringArray[2]);

        if (response == 0) {
            chat ob = new chat();
            ob.startChatThread(name.toString(), connection, input, output);
        } 
        else if (response == 1)
        {
            File selectedFile = null;
            String extension = "";
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                extension = getFilenameExtension(selectedFile.getAbsolutePath());
            }

            //send file to server
           // fileSendThread ob = new fileSendThread();
           // ob.startSendThread(name.toString(), connection, input, output,extension,selectedFile);

                 FileInputStream fis = null;
            BufferedInputStream bis = null;

            


               //while (true) {
                 try {
                   // send file
                   File myFile = new File (selectedFile.getAbsolutePath());
                   byte [] mybytearray  = new byte [(int)myFile.length()];
                   fis = new FileInputStream(myFile);
                   bis = new BufferedInputStream(fis);
                   bis.read(mybytearray,0,mybytearray.length);
                   
                   output.writeObject("client2ServerFile");             //tell the server that a file will be sending
                   output.writeObject(extension);                       //tell serever the extension of file
                   output.writeObject(name);   //tell the server to whom it may sent
                   output.write(mybytearray,0,mybytearray.length);      //send the file
                   output.flush();
                 }
                   catch (FileNotFoundException ex) {
                       Logger.getLogger(Homepage.class.getName()).log(Level.SEVERE, null, ex);
                   } catch (IOException ex) {
                       Logger.getLogger(Homepage.class.getName()).log(Level.SEVERE, null, ex);
                   }                 finally {
                   if (bis != null) try {
                       bis.close();
                   } catch (IOException ex) {
                       Logger.getLogger(Homepage.class.getName()).log(Level.SEVERE, null, ex);
                   }
                 //}
               }
           
         }
            




    }//GEN-LAST:event_friendListMouseClicked

    private void broadCastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_broadCastActionPerformed
        // TODO add your handling code here:
        //chat ob = new chat();
        //ob.startBroadcastChatThread(OnlineFriends, connection, input, output);
        
    }//GEN-LAST:event_broadCastActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList anonyOnline;
    private javax.swing.JButton broadCast;
    private javax.swing.JMenuItem frequest;
    private javax.swing.JList friendList;
    private javax.swing.JList friendOnline;
    private javax.swing.JLabel inboxNoti;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JMenu logout;
    private javax.swing.JMenu request;
    // End of variables declaration//GEN-END:variables
}
