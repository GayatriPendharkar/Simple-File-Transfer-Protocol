import java.io.IOException;
import java.net.ServerSocket;

public class ServerMain 
{
	public static void main(String args[])
	{
		ServerSocket soc = null;
		try
		{
			// create a server socket to run on 13572 port which client will communicate
			soc = new ServerSocket(13572);
		}
		catch(IOException io)
		{
			System.out.println("Error while creating the server socket!");
		}
		
		System.out.println("\nServer socket started on port number 13572");
		System.out.println("\nWaiting for connections......");
		while(true)
		{
			try
			{
				// create the object of the helper class which runs the start() method
				new ServerHelper(soc.accept());
			}
			catch(IOException io)
			{
				System.out.println(io.getMessage());
				continue;
			}
		}
	}

}
