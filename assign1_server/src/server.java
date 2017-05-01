import java.io.*;
import java.net.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;


public class server 
{
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket connection;
    public final static int FILE_SIZE = 6022386;
    
    String userId="";
    
    public void startRunning(Socket connection)
    {
        this.connection= connection;
        
        setupStream();
        while(true)
        {
           receive();
        } 
        
    }
    
    public void setupStream()
    {
        try
        {
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input =new ObjectInputStream(connection.getInputStream());
        }           
        catch(IOException ioException)
        {
            ioException.printStackTrace();
        }
    }
    
    public void receive()
    {
        try
        {
         try
            {
                
               String message = (String) input.readObject();
              
               if(message.equals("signup")) signup();
               else if(message.equals("login")) login();
               else if(message.equals("logout")) logout();
               else if(message.equals("anonymous")) anonymous();
               else if(message.equals("friends")) friends();
               else if(message.equals("frequest")) frequest();
               else if(message.equals("getFReq"))  getFRequest();
               else if(message.equals("faccept"))  acceptFRequest();
               else if(message.equals("sendChat"))  inviteChat();
               else if(message.equals("receiveChat"))  receiveChat();
               else if(message.equals("anyOneWantsToChat?"))  wantsToChat();
               else if(message.equals("client2ServerFile")) client2ServerFile(); 
               else if(message.equals("anyfileForMe?")) fileForMe(); 

            }
            catch(ClassNotFoundException classNotFoundException){}
         
          }
         catch(IOException ioException){}
    }
    
    public void fileForMe() throws IOException, ClassNotFoundException
    {
        String me ="";
        while(me.equals(""))
        {
            me=(String) input.readObject();
        }
        
         int flag=0;

         //receive all the files from my Files folder
        File folder = new File(System.getProperty("user.dir")+"\\Files");
        File[] listOfFiles = folder.listFiles();

           //System.out.println(me);
        String filename="";
        for (int i = 0; i < listOfFiles.length; i++)
        {
            if (listOfFiles[i].isFile()) 
            filename = listOfFiles[i].getName();  //get the name of the file 
            
            StringTokenizer st = new StringTokenizer(filename,"&"); //tokenize it to check if its for me
            while (st.hasMoreTokens()) 
             {
                 String aaa=st.nextToken();
                 StringTokenizer st2 = new StringTokenizer(aaa,"#");
                 String from=st2.nextToken();
                 StringTokenizer st3 = new StringTokenizer(st2.nextToken(),".");

                 String chk=st3.nextToken();
                 if(chk.equals(me))         //if it's for me send it to me
                 {
                     output.writeObject("IHaveAFile");
                     //System.out.println("I have");
                     sendFileToClient(filename,from);
                     System.out.println(System.getProperty("user.dir")+"\\Files\\"+filename);
                     
                     File file = new File(System.getProperty("user.dir")+"\\Files\\"+filename);
                     file.delete();
                     flag=1;

                 }
                 else  output.writeObject("No file");

             }
        }
        //if(flag==0) output.writeObject("noData");
        
    }
    
    public String getFilenameExtension(String filename) 
    {
        String extension = "NoExtension";
        int i = filename.lastIndexOf('.');
        if (i > 0) 
        {
            extension = filename.substring(i + 1);
        }
        return extension;
    }
    
    public void  sendFileToClient(String filename,String from) throws IOException, ClassNotFoundException
    {
         //send file to client
            
            
            File selectedFile = null;            
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            
            String extension = getFilenameExtension(filename);
            output.writeObject(extension);
            output.writeObject(from);
             String chk="";
               while (true) {
                 try {
                    // chk=(String) input.readObject();
                     //if(chk.equals("done")) break;
                   // send file
                   File myFile = new File (System.getProperty("user.dir")+"\\Files\\"+filename);
                   byte [] mybytearray  = new byte [(int)myFile.length()];
                   fis = new FileInputStream(myFile);
                   bis = new BufferedInputStream(fis);
                   bis.read(mybytearray,0,mybytearray.length);
   
                   output.write(mybytearray,0,mybytearray.length);      //send the file
                   output.flush();
                   if(mybytearray.length<=0) break;
                   
                 }
                   catch (FileNotFoundException ex) {
                   } catch (IOException ex) {
                       
                   }                 finally {
                   if (bis != null) try {
                       bis.close();
                   } catch (IOException ex) {

                   }
                 }
               }
    }
    
    public void client2ServerFile() throws FileNotFoundException, IOException, ClassNotFoundException
    {
        String to="";
        String extension="";
        while(extension.equals("")) extension =(String) input.readObject(); //receive the extension of file
        while(to.equals("")) to =(String) input.readObject(); //receive to whom file may sent
        
        String from=getClientId();                            // from whom file is being sent
        
        String fileName="&"+from+"#"+to;
        
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
        do {
           bytesRead = input.read(mybytearray, current, (mybytearray.length-current));
           if(bytesRead >= 0) current += bytesRead;
        } while(bytesRead > -1);

        bos.write(mybytearray, 0 , current);
        bos.flush();
       }
      finally {
        if (fos != null) fos.close();
        if (bos != null) bos.close();
       }
  }

    
    
    public void wantsToChat() throws IOException, ClassNotFoundException
    {
        
        
        String me ="";
        while(me.equals(""))
        {
            me=(String) input.readObject();
        }
        int flag=0;

        String data=""; //client name who wants to chat with me

       String str = new String(Files.readAllBytes(Paths.get("chatData.txt")));
       StringTokenizer st = new StringTokenizer(str,"&");
        while (st.hasMoreTokens()) 
        {
            String aaa=st.nextToken();
            StringTokenizer st2 = new StringTokenizer(aaa,"#");
            String from=st2.nextToken();
            StringTokenizer st3 = new StringTokenizer(st2.nextToken(),"$");
            
            String chk=st3.nextToken();
            if(chk.equals(me))
            {
               // System.out.println("chk");
                output.writeObject(from);
                flag=1;
                
            }
            
        }
        

        if(flag==0) output.writeObject("noData");
        
    }
    
    public void receiveChat() throws IOException, ClassNotFoundException
    {
        String me = getClientId();
        String from="";
        String data="";
        String replace="";  //replace file data with this
        while(from.equals(""))
        {
             from= (String) input.readObject();
        }
        
        String chk=from+"#"+me; //am I receiving any chat from this particular client?
       
        String str = new String(Files.readAllBytes(Paths.get("chatData.txt")));
        StringTokenizer st = new StringTokenizer(str,"&");
        while (st.hasMoreTokens()) 
        {
            String temp=st.nextToken();
            StringTokenizer st2 = new StringTokenizer(temp,"$");
            String chk2 = st2.nextToken();
            
            if(chk2.equals(chk))
            {
                data = st2.nextToken();
                output.writeObject(data);
                
            }
            else
            {
                replace=replace+"&"+temp;
            }
            
            
        }
        
        
        FileWriter fw2 = new FileWriter("chatData.txt",false); //add user to the online list
        fw2.write(replace);
        fw2.close();
        if(data.equals("")) output.writeObject("noData");
        
    }
    
    public void inviteChat() throws IOException, ClassNotFoundException
    {
        String chatData="";
        while(chatData.equals(""))
        {
             chatData= (String) input.readObject();
        }
        
        String from=getClientId();
        String content="&"+from+chatData;   //chat is in the form of "&from#to$message"
        FileWriter fw = new FileWriter("chatData.txt",true);
        fw.write(content);
        fw.close();
        
        //System.out.println("chk");
        //System.out.println(chatData);
    }
    
    public void acceptFRequest() throws IOException, ClassNotFoundException
    {
        //write in the list who is whose friend
        String newFriend="";
        while(newFriend.equals(""))
        {
           newFriend= (String) input.readObject();
        }
        
        String myId= getClientId();
        
        String token="@"+myId+"#"+newFriend;   //make a token of friend request from who to whom
        FileWriter fw = new FileWriter("friends.txt",true);
        fw.write(token);
        fw.close();
        
        //delete this accepted request from pending list
        String content = new String(Files.readAllBytes(Paths.get("frequest.txt")));
        content=content.replace(token,"");
        FileWriter fw2 = new FileWriter("frequest.txt",false); //add rest of the requests to the list
        fw2.write(content);
        fw2.close();
        
    }
    
    public void  getFRequest() throws IOException
    {
         
        String str = new String(Files.readAllBytes(Paths.get("frequest.txt")));
        
        String userId=getClientId();
        String retVal="";
        
        StringTokenizer st = new StringTokenizer(str,"@");
        while (st.hasMoreTokens()) 
        {
            StringTokenizer st2 = new StringTokenizer(st.nextToken(),"#");
            String to = st2.nextToken();
            String from = st2.nextToken();
            
            if(userId.equals(to))
            {
                retVal=retVal+"#"+from;
            }
            
        }
        if(retVal.equals("")) output.writeObject("no requests");
        else output.writeObject(retVal);
    }
    
    public String getClientId()
    {
        StringTokenizer st = new StringTokenizer(userId,"$");   //tokenize $id#pass$ form
        StringTokenizer st2 = new StringTokenizer(st.nextToken(),"#");  //tokenize id#pass form
        String retVal=st2.nextToken();    //tokenize only the id
        return retVal;
    }
    
    public void frequest() throws IOException, ClassNotFoundException
    {
        String to="";
        while(to.equals(""))
        {
           to= (String) input.readObject();
        }
        
        String from= getClientId();
        
        String token="@"+to+"#"+from;   //make a token of friend request from who to whom
        FileWriter fw2 = new FileWriter("frequest.txt",true);
        fw2.write(token);
        fw2.close();

    }
    
    public void logout()
    {
        //System.out.println("logout");
        try 
        {
            String content = new String(Files.readAllBytes(Paths.get("online.txt")));
            content=content.replace(userId,"");
            FileWriter fw2 = new FileWriter("online.txt",false); //add user to the online list
            fw2.write(content);
            fw2.close();
            connection.close();
        } catch (IOException ex) {}
    }
    
    public void signup()
    {
        try
        {
            try
            {    
                String message="";

                while(message.equals(""))
                {
                   message= (String) input.readObject(); 
                }

                output.writeObject("success"); //send user success notification
                userId=message;

                FileWriter fw = new FileWriter("id#pass.txt",true); //add user to the registered user list
                fw.write(message);
                fw.close();
                
                
                FileWriter fw2 = new FileWriter("online.txt",true); //add user to the online list
                fw2.write(message);
                fw2.close();
                     
             }
             catch(ClassNotFoundException classNotFoundException){}
        }
         catch(IOException ioException){}
            
    }
    
    public void anonymous()
    {
        try
        {
            String str1 = new String(Files.readAllBytes(Paths.get("online.txt")));
            String retVal="";
            
            StringTokenizer stoken = new StringTokenizer(str1,"$");
            while (stoken.hasMoreTokens()) 
            {
                StringTokenizer stoken2 = new StringTokenizer(stoken.nextToken(),"#");
                String str2 = stoken2.nextToken();
                if(str2.equals(getClientId())) str2=""; // if I am online ignore my name
                retVal=retVal+"#"+str2; //get only the user name in form #user1#user2

            }
            
            if(retVal.equals("#")) output.writeObject("No one is online");
            else output.writeObject(retVal);            
            
        }
                    
        catch(IOException ioException){}
    }
    
    public void friends()
    {

        try
        {
            
            String str = new String(Files.readAllBytes(Paths.get("friends.txt")));
            
            String userId=getClientId();
            String retVal="";
        
            StringTokenizer st = new StringTokenizer(str,"@");
            while (st.hasMoreTokens()) 
            {
                StringTokenizer st2 = new StringTokenizer(st.nextToken(),"#");
                String to = st2.nextToken();
                String from = st2.nextToken();

                if(userId.equals(to))
                {
                    retVal=retVal+"#"+from;
                }
                else if(userId.equals(from))
                {
                    retVal=retVal+"#"+to;
                }
            
            }
            
            if(retVal.equals(""))output.writeObject("no friends");
            else
            {
                
                output.writeObject(retVal);
            }
        }
        catch(IOException ioException){}
    }
    
    public void login()
    {
        //System.out.println("login");
        try
        {
            try
            {    
                String message="";

                while(message.equals(""))
                {
                   message= (String) input.readObject(); 
                }

                // check if user is registered or not
                String content = new String(Files.readAllBytes(Paths.get("id#pass.txt"))); 
                if(content.contains(message))
                {
                    userId=message;
                    output.writeObject("success");
                    
                    //if successfully logged in add him to the online list
                    FileWriter fw2 = new FileWriter("online.txt",true); //add user to the online list
                    fw2.write(message);
                    fw2.close();                   
                
                }
                else output.writeObject("fail");
             }
             catch(ClassNotFoundException classNotFoundException){}
        }
         catch(IOException ioException){}
    }
    
}
