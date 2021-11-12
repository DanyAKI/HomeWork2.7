import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientHandler {

    private final Socket socket;
    private final ChatServer server;
    private final DataInputStream in;
    private final DataOutputStream out;
    private String name;


    public ClientHandler(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException("Something went wrong during a client connected establishing.");
        }


            doAuthentication();
            listenMessages();
      

    }

    public String getName() {
        return name;
    }

    private void doAuthentication() {
        try {
            performAuthentication();
        } catch (IOException ex) {
            throw new RuntimeException("Something went wrong during a client authentication.");

        }
    }

        private void performAuthentication() throws IOException{
        while (true){
          String inboundMessage = in.readUTF();
            if (inboundMessage.startsWith("- auth")) {
                String[] credentials = inboundMessage.split("\\s");

                AtomicBoolean isSuccess = new AtomicBoolean(false);
                server.getAuthenticationService()
                        .findUsernameByLoginAndPassword(credentials[1], credentials[2])
                        .ifPresentOrElse(
                                username -> {
                                    if (!server.isUsernameOccupied(username)) {
                                        server.broadcastMessage(String.format("User[%s] is logged in", username));
                                        name = username;
                                        server.addClient(this);
                                        isSuccess.set(true);
                                    } else {
                                        sendMessage("Current username is already occupied.");
                                    }

                                },
                                () -> sendMessage("Bad credentials.")
                        );

                if (isSuccess.get()) break;
                    





            }
        }


    }
    public void sendMessage(String outboundMessage){
        try {
            out.writeUTF(outboundMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void readMessage(){

        try {
            server.broadcastMessage(in.readUTF());
            in.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void listenMessages(){
        while (true){
            readMessage();
        }
    }


}


