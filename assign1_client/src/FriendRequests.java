import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.StringTokenizer;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

public class FriendRequests extends javax.swing.JFrame {
    
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket connection;

    public FriendRequests() 
    {
        initComponents();
        setVisible(true);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
    }
    
    public void set(String requests,Socket connection,ObjectInputStream input,ObjectOutputStream output)
    {
        this.input= input;
        this.output=output;
        this.connection=connection;
        
        System.out.println(requests);
        
        DefaultListModel dlm = new DefaultListModel(); //introduce model to add data to list anonymous
        StringTokenizer st = new StringTokenizer(requests,"#");  //tokenize the names
        while (st.hasMoreTokens()) 
        {  
             dlm.addElement(st.nextToken());
             requestList.setModel(dlm);
        } 
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        requestList = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("This Persons want to be your friend");

        requestList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                requestListMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(requestList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(47, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void requestListMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_requestListMousePressed
        // TODO add your handling code here:
        Object name = requestList.getSelectedValue();
        int response = JOptionPane.showConfirmDialog(null, "Do you want to add this person as your friend?", "Confirm",
        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.YES_OPTION)
        {
            try 
            {
                output.writeObject("faccept");
                output.writeObject(name);
                JOptionPane.showMessageDialog(null, "Friend Added");
            } 
            catch (IOException ex) {}
        }   
    }//GEN-LAST:event_requestListMousePressed


    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList requestList;
    // End of variables declaration//GEN-END:variables
}
