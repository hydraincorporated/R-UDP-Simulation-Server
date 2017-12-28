package UDPServer;

import Logging.Logger;

/**
 * Created by Jakub on 28.12.2017.
 */
public class Launch {

    public static void main(String[] args)
    {
        Logger.initialize();
        Logger.info("Starting Server");

        UDPServer server = new UDPServer();
    }
}
