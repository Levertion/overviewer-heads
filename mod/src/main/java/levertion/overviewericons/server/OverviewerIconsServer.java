package levertion.overviewericons.server;

import java.net.InetSocketAddress;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.Logger;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import net.minecraft.entity.player.EntityPlayer;

public class OverviewerIconsServer extends WebSocketServer {
    private static final int DEFAULT_UPDATE_RATE = 1000;
    private static final int DEFAULT_PORT = 25570;
    private Logger logger;
    private EntityPlayer player;
    private Timer timer;

    class MessageTask extends TimerTask {
        public void run() {
            send_update(); // Terminate the timer thread
        }
    }

    public OverviewerIconsServer(Logger logger) {
        this(DEFAULT_PORT, logger);
    }

    public OverviewerIconsServer(int port, Logger logger) {
        super(new InetSocketAddress(port));
        this.logger = logger;
        this.start();
        logger.info("Started websocket server on: " + this.getAddress().toString());
    }

    private void send_update() {
        this.broadcast(OverviewerIconsMessages.update_message(this.player).toString());
    }

    public void set_player(EntityPlayer player) {
        if (this.player == null) {
            this.player = player;
            this.broadcast(OverviewerIconsMessages.connect_message(this.player).toString());

            int period = DEFAULT_UPDATE_RATE;
            this.timer = new Timer();
            timer.schedule(new MessageTask(), period, period);
        }
    }

    public void remove_player() {
        if (player != null) {
            this.broadcast(OverviewerIconsMessages.disconnect_message(this.player).toString());
            timer.cancel();
            player = null;
        }
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        logger.info(String.format("Connection to client at %s opened at url %s", conn.getRemoteSocketAddress(),
                handshake.getResourceDescriptor()));
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        logger.info(String.format("Connection to client at %s closed for reason: %s", conn.getRemoteSocketAddress(),
                reason));
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        logger.warn(String.format("Recieved message from client at %s, where none was expected: '%s'",
                conn.getRemoteSocketAddress(), message));
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        logger.warn(String.format("Error connecting to client: '%s'", ex));
    }

    @Override
    public void onStart() {
        logger.info("Web socket server started");
    }

}
