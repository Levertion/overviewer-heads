package levertion.overviewericons;

import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = OverviewerIcons.MODID, name = OverviewerIcons.NAME, version = OverviewerIcons.VERSION, clientSideOnly = true)
public class OverviewerIcons {
    public static final String MODID = "overviewericons";
    public static final String NAME = "Minecraft Overviewer Icons";
    public static final String VERSION = "1.0";

    private static OverviewerIconsMain main;
    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        // some example code
        main = new OverviewerIconsMain(logger);
        MinecraftForge.EVENT_BUS.register(main);
    }

}
