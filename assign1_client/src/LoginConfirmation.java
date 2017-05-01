import java.io.*;
import java.net.*;
import javax.swing.JOptionPane;

public class LoginConfirmation 
{
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket connection;
    private String serverIP="192.168.0.106";
   

    public String send(String  userId,String password)
    {
         String message = "$"+userId+"#"+password+"$";
        try
        {
            connectToServer();//connect to one specific server
            setupStreams();
            
            System.out.println("chk1:"+output);
            
            output.writeObject("login");
            output.writeObject(message);
            output.flush();
            
            String Confirmation="";
          
            while(true)
            {
                try
                {
                    Confirmation = (String) input.readObject();
                    
                }
                
                catch(ClassNotFoundException classNotFoundException){}
                
                if(Confirmation.equals("")) continue;
                
                else if(Confirmation.equalsIgnoreCase("success")) 
                {
                    Homepage ob = new Homepage();
                    ob.send(userId,connection,input,output);
                    return "success";
                    
                }
                else if(Confirmation.equalsIgnoreCase("fail")) return "fail";
                
            }
            
        }
        catch(IOException ioException){}
        
        return "";
    }
    
     private void connectToServer() throws IOException
    {
       
        //now we need the ip address to connect to a specific server and a port to connect to a specific app
        connection= new Socket(InetAddress.getByName(serverIP),1234);
        
    }
    
      private void setupStreams() throws IOException
    {
        this.output = new ObjectOutputStream(connection.getOutputStream()); //output messages
        output.flush(); 
        input =new ObjectInputStream(connection.getInputStream());
        
    }
}
