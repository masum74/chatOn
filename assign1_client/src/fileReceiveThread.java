
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;


public class fileReceiveThread implements Runnable
{
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket connection;
    private String serverIP="127.0.0.1";
    
    public final static int FILE_SIZE = 6022386;
    
    String userName;
    
    public fileReceiveThread (String username)
    {        
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
                //System.out.println("chk1");
                output.writeObject("anyfileForMe?");
                output.writeObject(userName);
                System.out.println(userName);
                
                //receive file here
                String confirmation="";
                while(confirmation.equals("")) confirmation=(String) input.readObject();
                if(confirmation.equals("IHaveAFile"))
                {
                    receiveFile();
                }
             
            } 
            catch (IOException ex) {} catch (ClassNotFoundException ex) {
                  Logger.getLogger(fileReceiveThread.class.getName()).log(Level.SEVERE, null, ex);
              }
             
        }
    }
   
     public void receiveFile() throws IOException, ClassNotFoundException
     {
         System.out.println("receiving");
        String from="";
        String extension="";
        while(extension.equals("")) extension =(String) input.readObject(); //receive the extension of file
        while(from.equals("")) from =(String) input.readObject(); //receive to whom file may sent
        String fileName=from;
        
        int bytesRead;
        int current = 0;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
    
        try {
        // receive file
        byte [] mybytearray  = new byte [FILE_SIZE];
        fos = new FileOutputStream(System.getProperty("user.dir")+"\\Files\\"+fileName+"."+extension);
        bos = new BufferedOutputStream(fos);
        bytesRead = input.read(mybytearray,0,mybytearray.length);
        current = bytesRead;
         System.out.println("1");
        do {
           bytesRead = input.read(mybytearray, current, (mybytearray.length-current));
           if(bytesRead >= 0) current += bytesRead;
           System.out.println(bytesRead);
        } while(bytesRead > 0);
        output.writeObject("done");
        bos.write(mybytearray, 0 , current);
        bos.flush();
       }
      finally {
        if (fos != null) fos.close();
        if (bos != null) bos.close();
       }
        System.out.println("finished");
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
