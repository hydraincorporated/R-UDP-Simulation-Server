package UDPServer;

import Logging.Logger;
import Packets.Packet;
import Packets.PacketContainer;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import java.util.HashMap;

public class UDPServer extends Server {

    private static final int TCP_PORT = 5555;
    private static final int UDP_PORT = 5556;
    private static final int TIMEOUT = 3000;

    private HashMap<Connection, User> connections = new HashMap<Connection, User>();

    public UDPServer(){
        super(30000,6000);

        PacketRegistry.register(this);
        start();

        try {
            bind(TCP_PORT, UDP_PORT);
        } catch (IOException e) {
            Logger.error("Failed to bind sockets", e);
        }

        this.addListener(new Listener() {

            @Override
            public void disconnected(Connection connection) {
                super.disconnected(connection);
                final User user = connections.remove(connection);

                Logger.info("[Client Disconnected]");
            }


            @Override
            public void connected(Connection connection) {
                super.connected(connection);

                if(connections.containsKey(connection))
                    connections.remove((connection));

                final User user = new User(connection);
                connections.put(connection, user);

                Logger.info("[New Client Connected]: " + connection.getRemoteAddressTCP().toString());
            }

            public void received (Connection connection, Object object) {

                final User user = connections.get((connection));

                if(user == null)
                {
                    Logger.info("Closing connection");
                    connection.close();
                    return;
                }

                if (object instanceof PacketRegistry.InitialPacket)
                {
                    Logger.info("[Initial Packet Received]: " + connection);
                    user.setSequenceNumber(((PacketRegistry.InitialPacket) object).sequenceNumber);
                    connection.sendTCP(object);
                }

                if(object instanceof Packet)
                {
                    final Packet pckt = (Packet) object;
                    user.setSequenceNumber(user.getSequenceNumber() + 1);
                    pckt.setServerSequence(user.getSequenceNumber());

                    Logger.info("[Packet Received]: " + connection + " Message: " + pckt.getMessage());
                    connection.sendTCP(pckt);
                }

                if(object instanceof PacketContainer)
                {
                    final PacketContainer container = (PacketContainer) object;

                    Logger.info("[Packet Container Received]: " + connection + " Size: " + container.getSize());

                    container.getElements()
                            .stream().filter(p -> p.getSequenceNumber() == user.getSequenceNumber())
                            .forEach(p -> {
                                user.setSequenceNumber(user.getSequenceNumber() + 1);
                                p.setServerSequence(user.getSequenceNumber());
                                connection.sendTCP(p);
                            });
                }
            }
        });
    }
}
