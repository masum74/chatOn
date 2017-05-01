
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

public class fileSendThread implements Runnable {
    
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket connection;
    private String name;
    private String extension;
    private File selectedFile;

 public void startSendThread(String userId,Socket connection,ObjectInputStream input,ObjectOutputStream output,String extension,File s)
   
 {
      this.input= input;
        this.output=output;
        this.connection=connection;
        this.name=userId;
        this.extension=extension;
        this.selectedFile=s;
        
       //System.out.println(userId);
        
        Thread t;
        t = new Thread((Runnable) this, "Demo Thread");
        t.start();
  }
 
 public void run()
 {
           
            FileInputStream fis = null;
            BufferedInputStream bis = null;

            


               while (true) {
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
                 }
               }
 }
 

}
