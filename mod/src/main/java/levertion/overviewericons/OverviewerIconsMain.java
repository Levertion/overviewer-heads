package levertion.overviewericons;

import org.apache.logging.log4j.Logger;

import levertion.overviewericons.server.OverviewerIconsServer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;

public class OverviewerIconsMain {
    private Logger logger;
    private OverviewerIconsServer server;

    public OverviewerIconsMain(Logger logger) {
        this.logger = logger;
        try {
            logger.info("Starting websocket server");
            server = new OverviewerIconsServer(logger);
        } catch (Exception e) {
            logger.warn("Starting websocket server failed: " + e.toString());
            // Ignore the exception if the server cannot begin
        }
    }

    @SubscribeEvent
    public void joinServer(EntityJoinWorldEvent event) {
        Entity maybePlayer = event.getEntity();
        if (maybePlayer instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) maybePlayer;
            if (server != null && player.isUser()) {
                logger.info("Connected to server");
                server.set_player(player);
            }

        }
    }

    @SubscribeEvent
    public void leaveServer(ClientDisconnectionFromServerEvent event) {
        logger.info("Disconnected from server");
        logger.info(event.toString());
        if (server != null) {
            server.remove_player();
        }
    }

    @Override
    public void finalize() {
        try {
            server.stop(100);
        } catch (Exception e) {
            // This is fine
        }
    }
}
