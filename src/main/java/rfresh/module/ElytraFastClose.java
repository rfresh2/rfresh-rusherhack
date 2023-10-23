package rfresh.module;

import org.rusherhack.client.api.events.player.EventPlayerUpdate;
import org.rusherhack.client.api.feature.module.ModuleCategory;
import org.rusherhack.client.api.feature.module.ToggleableModule;
import org.rusherhack.core.event.stage.Stage;
import org.rusherhack.core.event.subscribe.Subscribe;
import org.rusherhack.core.setting.NumberSetting;

public class ElytraFastClose extends ToggleableModule {
    final NumberSetting<Double> yThreshold = new NumberSetting<>("Y Distance",
                                                                 "Distance from ground to close elytra",
                                                                 0.05,
                                                                 0.0,
                                                                 1.0);

    public ElytraFastClose() {
        super("ElytraFastClose", "Closes elytra immediately and stops motion when near ground", ModuleCategory.MOVEMENT);
        registerSettings(yThreshold);
    }

    @Subscribe(stage = Stage.PRE)
    public void onPlayerUpdate(final EventPlayerUpdate event) {
        if (!event.getPlayer().isFallFlying()) return;
        if (mc.level.noCollision(event.getPlayer(), event.getPlayer().getBoundingBox().move(0.0, -yThreshold.getValue(), 0.0))) return;
        // force server to teleport us back and make us lose momentum
        event.setY(event.getY() + 100);
        event.setOnGround(true);
    }
}
