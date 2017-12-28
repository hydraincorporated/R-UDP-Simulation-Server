package UDPServer;

import Packets.PacketContainer;
import Packets.Packet;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

import java.util.concurrent.CopyOnWriteArrayList;


public class PacketRegistry {

    static void register (EndPoint endPoint)
    {
        Kryo kryo = endPoint.getKryo();

        kryo.register(CopyOnWriteArrayList.class);
        kryo.register(PacketContainer.class);
        kryo.register(Packet.class);
        kryo.register(InitialPacket.class);
    }

    static public class InitialPacket
    {
        public int sequenceNumber = 0;
    }

}
