
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class chat extends javax.swing.JFrame implements Runnable  {
    
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket connection;
    private String userId;
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        chatWindow = new javax.swing.JTextArea();
        userName = new javax.swing.JLabel();
        chatVal = new javax.swing.JTextField();
        send = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        chatWindow.setColumns(20);
        chatWindow.setRows(5);
        jScrollPane1.setViewportView(chatWindow);

        userName.setText("jLabel1");

        send.setText("Send");
        send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1)
                            .addComponent(chatVal)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(80, 80, 80)
                        .addComponent(send)))
                .addContainerGap(30, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(userName, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(67, 67, 67))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(userName, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(chatVal, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(send)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void sendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendActionPerformed
        // TODO add your handling code here:
        chatWindow.append("\nME:  "+chatVal.getText()); 
        try 
        {
            output.writeObject("sendChat");
            output.writeObject("#"+userId+"$"+chatVal.getText());
        } 
        catch (IOException ex) {}
        
        chatVal.setText("");
        
    }//GEN-LAST:event_sendActionPerformed

    public void startChatThread(String userId,Socket connection,ObjectInputStream input,ObjectOutputStream output)
    {
        this.input= input;
        this.output=output;
        this.connection=connection;
        this.userId=userId;
        
       //System.out.println(userId);
        
        Thread t;
        t = new Thread((Runnable) this, "Demo Thread");
        t.start();
    }
    
    
  public void run() 
  {    
      //userId is the person to whom chat is being send
             
        initComponents();
        setVisible(true); 
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        userName.setText(userId);
        
        while(true)
        {
            try 
            {
                Thread.sleep(1000);
            } 
            catch (InterruptedException ex) {}
            
            String receivedData="";
            try 
            {
                output.writeObject("receiveChat");
                output.writeObject(userId);
                
                String data="";
                while(data.equals(""))
                {
                    try 
                    {
                        data=(String) input.readObject();
                    } 
                    catch (ClassNotFoundException ex) {}
                }
                if(!data.equals("noData")) chatWindow.append("\n"+userId+":   "+data); ;
            } 
            catch (IOException ex) {}
             
        }
  }  


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField chatVal;
    private javax.swing.JTextArea chatWindow;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton send;
    private javax.swing.JLabel userName;
    // End of variables declaration//GEN-END:variables
}

