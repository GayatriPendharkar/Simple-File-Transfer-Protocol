import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Helper class used by the serverMain to receive a file from the client.
 *
 */

public class ServerHelper extends Thread
{
	Socket ClientSocket;

    DataInputStream datain;
    DataOutputStream dataout;
    BufferedReader buffer_reader;
    
    /**
     * Constructor to initialize input/output streams from the socket
     * 
     * @param soc server socket.
     */
    
    public ServerHelper(Socket soc)
	{
		try
		{
			ClientSocket = soc;
			
			//Get streams from the socket
			datain = new DataInputStream(ClientSocket.getInputStream());
			dataout = new DataOutputStream(ClientSocket.getOutputStream());
			
			//Buffered reader to read inputs from the clients
			buffer_reader = new BufferedReader(new InputStreamReader(System.in));
			
			System.out.println("\nConnected to client....");
			
			// start the Server thread by calling start() method which calls the run() method implemented in this class
			start();
		}
		catch(IOException ex) 
		{
            System.out.println("\nException occured while initializing streams!");
            System.exit(0);
        }        
	}
    
    /**
     * Method to handle the data from the client
     */
    
    public void ReceiveFile() throws IOException {
    	
    	//Get the filename from the client
        String filename = datain.readUTF();
        
        //If file not found return and exit
        if (filename.contains("requested file doesn't exist on Client Side")) 
        {
            System.out.println("\n" + filename);
            return;
        }
        
        //open the file to write
        File f = new File(filename);
        String choice;
        
        // check if file is already present in the directory
        // if yes -> send a message to client to ask if he wants to over-ride the existing file.
        if (f.exists()) 
        {
            System.out.println("\nFile Already Exists! Want to OverWrite (Y/N) ?");
            choice = buffer_reader.readLine();
            if (choice.equals("N")) 
            {
                dataout.writeUTF("ABORT");
                return;
            } 
            else 
            {
                dataout.writeUTF("SendFile");
            }
        } 
        else 
        {
            dataout.writeUTF("SendFile");
            choice = "Y";
        }
        
        //Write the chunk of the data that is being sent from the client to the file
        if (choice.equals("Y")) 
        {
            FileOutputStream fout=new FileOutputStream(f);
            long fileSize = datain.readLong();
            
            byte[] buffer = new byte[1024];
            int bytesReadSofar = 0;
            int bytesRead;
            
            //Read the file chunks from the client and write to the file
            while (true) 
            {   
                bytesRead = datain.read(buffer);
                bytesReadSofar += bytesRead;
                fout.write(buffer, 0, bytesRead);
                if (fileSize == bytesReadSofar) 
                {
                    break;
                }
            }
            
            fout.close();
            System.out.println("File received succesfully");
            dataout.writeUTF("File Sent Successfully");    
        } 
        else 
        {
            return;
        }
    }
    
    public void run() 
    {
        while (true) 
        {
            try 
            {
                System.out.println("\n\nWaiting for the client to send file ...");
                String cmd = datain.readUTF();
                
                if (cmd.equals("Want to Send a File!")) 
                {
                    System.out.println("\nClient wants to send a file!");
                    System.out.println("\nDo you want to receive the file (Y/N)?");
                    String input = buffer_reader.readLine();
                    
                    if (input.equals("Y")) 
                    {
                        ReceiveFile();    
                    }
                    
                    System.out.println("\nDisconnecting from the client and exiting");
                    dataout.writeUTF("DISCONNECT");
                    System.exit(1);
                }
            } 
            catch (IOException ex) 
            {
                System.out.println("Exception occured while receiving the data");
                continue;
            }
        }
    }

}
