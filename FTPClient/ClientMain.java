import java.io.IOException;
import java.net.Socket;

public class ClientMain
{
	public static void main(String args[])
	{
		//Parse input arguements
		if(args[0].equals("-h help"))
		{
			echo("Execution commands: java ClientMain -s server_IP -f filename");
			echo("where:" + "\n" + "a. 'java ClientMain' is for running the executable of client source code");
			echo("b. server_IP is the IP adddress of the server");
			echo("c. filename is the name of the file you wish to send");
			System.exit(0);
		}
		//Get the IpAddress of the server
		String serverIP = args[1];
		
		//Get the filename to be sent to the server.
		String filename = args[3];
		
		try
		{
			//Create a socket to connect to the server with the given IP 
			Socket soc = new Socket(serverIP, 13572);
			
			//Create an object for the helper class
			ClientHelper c = new ClientHelper(soc, filename);
			
			//Call startClient which is an entry point to the helper class
			c.StartClientMethod();
		}
		catch(IOException io)
		{
			System.out.println(io.getMessage());
            System.exit(0);
		}
	}
	public static void echo(String message)
	{
	       System.out.println(message);
	}
}
