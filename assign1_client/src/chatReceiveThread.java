
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;


public class chatReceiveThread implements Runnable
{
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket connection;
    private String serverIP="127.0.0.1";
    
      private ObjectOutputStream output2;
    private ObjectInputStream input2;
    private Socket connection2;
    
    String userName;
    
    public chatReceiveThread (String username,Socket s,ObjectInputStream i,ObjectOutputStream o)
    {
        this.input2= i;
        this.output2=o;
        this.connection2=s;
        
         StringTokenizer st = new StringTokenizer(username,"$");   //tokenize $id#pass$ form
        StringTokenizer st2 = new StringTokenizer(st.nextToken(),"#");  //tokenize id#pass form
        String retVal=st2.nextToken(); 
        this.userName=retVal;
        
        Thread t;
        t = new Thread((Runnable) this, "Demo Thread");
        t.start();
    }
    
     public void run() 
    {
        
          try {
              connectToServer();//connect to one specific server
          } catch (IOException ex) {
              Logger.getLogger(chatReceiveThread.class.getName()).log(Level.SEVERE, null, ex);
          }
          try {
              setupStreams();
          } catch (IOException ex) {
              Logger.getLogger(chatReceiveThread.class.getName()).log(Level.SEVERE, null, ex);
          }
        
        String alreadyConnected="";
        
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
                output.writeObject("anyOneWantsToChat?");
                output.writeObject(userName);
                
                String data="";
                while(data.equals(""))
                {
                    try 
                    {
                        data=(String) input.readObject();
                    } 
                    catch (ClassNotFoundException ex) {}
                }
                if(!data.equals("noData") && !alreadyConnected.contains(data)) 
                {
                    System.out.println(data);
                     chat ob = new chat();
                     ob.startChatThread(data,connection2,input2,output2);
                     alreadyConnected=alreadyConnected+data;
                    
                    
                };
                
             
            } 
            catch (IOException ex) {}
             
        }
    }
     
          private void connectToServer() throws IOException
    {
       
        //now we need the ip address to connect to a specific server and a port to connect to a specific app
        connection= new Socket(InetAddress.getByName(serverIP),1234);
        
    }
    
      private void setupStreams() throws IOException
    {
        output = new ObjectOutputStream(connection.getOutputStream()); //output messages
        output.flush(); 
        input =new ObjectInputStream(connection.getInputStream());
    }
}
