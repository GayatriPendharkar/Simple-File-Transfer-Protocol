This zip file includes, ServerMain.java,ServerHelper.java ClientMain.java, ClientHelper.java, ClientFile.txt (File to send to server)

To run -> 

1. Open atleast 2 different terminals; one for server and another for client. (you can have more than one client terminals)

2. Compile the code:
   javac ServerMain.java
   javac ClientMain.java

3. Run the code:
   terminal 1: java ServerMain
   terminal 2: java ClientMain -s server_IP -f filename

4. Follow the commands you get

- You will see that the server requires permission to receive the file from client.
- Once permission is given, server accepts the file.
- If the file is already present at server, it asks if you want to over write the existing file.
- The availaibility of file at client side is also checked in case wrong filename is entered while running the code.
