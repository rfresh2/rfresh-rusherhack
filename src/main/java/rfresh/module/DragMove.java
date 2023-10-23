package rfresh.module;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import org.lwjgl.glfw.GLFW;
import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.accessors.gui.IMixinAbstractContainerScreen;
import org.rusherhack.client.api.events.client.input.EventMouse;
import org.rusherhack.client.api.feature.module.ModuleCategory;
import org.rusherhack.client.api.feature.module.ToggleableModule;
import org.rusherhack.client.api.setting.BindSetting;
import org.rusherhack.client.api.utils.InventoryUtils;
import org.rusherhack.core.event.stage.Stage;
import org.rusherhack.core.event.subscribe.Subscribe;

public class DragMove extends ToggleableModule {
    final BindSetting dragMoveBind = new BindSetting("HoldKey", RusherHackAPI.getBindManager().parseKey("LEFT_SHIFT"));
    private boolean dragging = false;

    public DragMove() {
        super("DragMove", "Quick move items while dragging in the inventory", ModuleCategory.MISC);
        registerSettings(dragMoveBind);
    }

    @Override
    public void onDisable() {
        dragging = false;
    }

    @Subscribe
    public void onMouseMove(final EventMouse.Move event) {
        if (!dragging || !dragMoveBind.getValue().isKeyDown()) return;
        if (mc.screen instanceof AbstractContainerScreen handler) {
            Slot hoveredSlot = ((IMixinAbstractContainerScreen) handler).getHoveredSlot();
            if (hoveredSlot == null) return;
            InventoryUtils.clickSlot(hoveredSlot.index, true);
        }
    }

    @Subscribe(stage = Stage.PRE)
    public void onMouseClick(final EventMouse.Key event) {
        if (event.getButton() != 0) return;
        switch (event.getAction()) {
            case GLFW.GLFW_PRESS -> dragging = true;
            case GLFW.GLFW_RELEASE -> dragging = false;
        }
    }
}
