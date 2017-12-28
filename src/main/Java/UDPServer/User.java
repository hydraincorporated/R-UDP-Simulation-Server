package UDPServer;


import com.esotericsoftware.kryonet.Connection;

public class User {

    private int sequenceNumber;
    private Connection connection;

    public User(Connection connection){
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }


    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
}
