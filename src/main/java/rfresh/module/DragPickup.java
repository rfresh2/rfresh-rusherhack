package rfresh.module;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.inventory.MerchantResultSlot;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;
import org.rusherhack.client.api.accessors.gui.IMixinAbstractContainerScreen;
import org.rusherhack.client.api.events.client.input.EventMouse;
import org.rusherhack.client.api.feature.module.ModuleCategory;
import org.rusherhack.client.api.feature.module.ToggleableModule;
import org.rusherhack.client.api.utils.InventoryUtils;
import org.rusherhack.core.event.stage.Stage;
import org.rusherhack.core.event.subscribe.Subscribe;

public class DragPickup extends ToggleableModule {
    private boolean dragging = false;

    public DragPickup() {
        super("DragPickup", "Pickup items by dragging an item over them", ModuleCategory.MISC);
    }

    @Override
    public void onDisable() {
        dragging = false;
    }

    @Subscribe
    public void onMouseMove(final EventMouse.Move event) {
        if (!dragging) return;
        if (mc.screen instanceof AbstractContainerScreen handler) {
            ItemStack mouseStack = mc.player.containerMenu.getCarried();
            if (mouseStack.isEmpty()) return;
            Slot hoveredSlot = ((IMixinAbstractContainerScreen) handler).getHoveredSlot();
            if (hoveredSlot == null) return;
            if (mouseStack.getCount() + hoveredSlot.getItem().getCount() > mouseStack.getMaxStackSize()) return;
            InventoryUtils.clickSlot(hoveredSlot.index, false);
            if (hoveredSlot instanceof ResultSlot
                || hoveredSlot instanceof FurnaceResultSlot
                || hoveredSlot instanceof MerchantResultSlot) return;
            InventoryUtils.clickSlot(hoveredSlot.index, false);
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
