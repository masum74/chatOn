import java.net.*;

public class main implements Runnable 
{
    private ServerSocket myserver;
    private Socket connection;
    
    public static void main(String[] args) 
    {
        main ob =new main();
        ob.call();
    }
    
    public void call()
    {
        try
        {
            myserver = new ServerSocket(1234,100); //port number(whre is this app) & backlog(how many people can excess it)
            while(true)
            {
                connection = myserver.accept();
                newThread(); 
            }
        }
        catch(Exception e){}
    }
    
  public void newThread()
  {
        Thread t;
        t = new Thread(this, "Demo Thread");
        t.start();
    }
  public void run() 
  { 
    server ob=new server();
    ob.startRunning(connection);   
  }  
}
