package rfresh;

import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.plugin.Plugin;
import rfresh.hud.CompassHudElement;
import rfresh.module.ElytraFastClose;

public class rPlugin extends Plugin {
    @Override
    public void onLoad() {
        RusherHackAPI.getHudManager().registerFeature(new CompassHudElement());
        RusherHackAPI.getModuleManager().registerFeature(new ElytraFastClose());
        // currently sucks ass
//        RusherHackAPI.getModuleManager().registerFeature(new XaeroPlusIntegration());
        getLogger().info("rfresh plugin loaded");
    }

    @Override
    public void onUnload() {

    }

    @Override
    public String getName() {
        return "rfresh";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String getDescription() {
        return "rfresh random stuff";
    }

    @Override
    public String[] getAuthors() {
        return new String[]{"rfresh2"};
    }
}
