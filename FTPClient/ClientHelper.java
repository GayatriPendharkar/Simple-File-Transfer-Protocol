import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/* Helper class for the clientMain which contains all the methods used by client.*/

public class ClientHelper {
	
	Socket ClientSocket;
	
	DataInputStream datain;
	DataOutputStream dataout;
	BufferedReader buffer_reader;
	String filename;
	
	/**
     * Constructor for the ClientHelper
     * @param soc ClientSocket used for getting Streams
     * @param filename file to transfer to the server.
     */
	
	//Initialize 
	public ClientHelper(Socket soc, String filename)
	{
		this.filename = filename;
		
		try
		{
			ClientSocket = soc;
			
			//Get the input/output streams from the socket
			datain = new DataInputStream(ClientSocket.getInputStream());
			dataout = new DataOutputStream(ClientSocket.getOutputStream());
			
			 //Buffered reader to read inputs from the client
			buffer_reader = new BufferedReader(new InputStreamReader(System.in));
		}
		catch(IOException ex) 
		{
            System.out.println("\nException occured while initializing streams!");
            System.exit(0);
        }        
	}
	
	/**
     * Method to send file to server in chucks of 1024 bytes.
     */
	
	public void SendFile() throws IOException
	{
		//Open the file that contains the data to be sent to server	
		File file = new File(filename);
		
		//Check if the file exists
		if(!file.exists())
		{
			System.out.println("\nFile doesn't exist! Please check the file name");
			dataout.writeUTF(filename + "requested file doesn't exist on Client Side");
			return;
		}
		
		System.out.println("\nWant to Send a File!");
		
		//Send the file name to the server
		dataout.writeUTF(filename);
		
		String replyServer = datain.readUTF();
		
		//Check if file server wants to exit because file is already present
		if(replyServer.equals("ABORT"))
		{
			return;
		}
		
		//Open the file stream and buffered stream to read the file contents
		
		FileInputStream file_stream = null;
        BufferedInputStream buffer_client = null;
        file_stream = new FileInputStream(file);
        buffer_client = new BufferedInputStream(file_stream);
        
        //Buffer to store the data chuncks
        byte[] buffer = new byte[1024];
        int bytesRead = 0;
        long fileSize = file.length();
        
        //Send the size of file to the server
		dataout.writeLong(fileSize);
		
		//Keep reading from the file while there is content, if not, return -1
		while ((bytesRead = buffer_client.read(buffer)) != -1) 
		{
            dataout.write(buffer, 0, bytesRead);
        }
		
		buffer_client.close();
		System.out.println("\n");
		System.out.println(datain.readUTF());
	}
	
	/**
     * Starts the client method to send file.
     * 
     */
	public void StartClientMethod() throws IOException
	{
		while(true)
		{
			dataout.writeUTF("Want to Send a File!");
			SendFile();
			String ack = datain.readUTF();
			if(ack.equals("DISCONNECT"))
			{
				System.out.println("\n-------Disconnecting from the Server------");
				System.exit(1);
			}
		}
		
	}
}
