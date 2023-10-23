package rfresh.module;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.accessors.gui.IMixinAbstractContainerScreen;
import org.rusherhack.client.api.events.client.input.EventMouse;
import org.rusherhack.client.api.feature.module.ModuleCategory;
import org.rusherhack.client.api.feature.module.ToggleableModule;
import org.rusherhack.client.api.setting.BindSetting;
import org.rusherhack.core.event.subscribe.Subscribe;

public class QuickMove extends ToggleableModule {
    final BindSetting quickMoveBind = new BindSetting("HoldKey", RusherHackAPI.getBindManager().parseKey("LEFT_CONTROL"));

    public QuickMove() {
        super("QuickMove", "Quick move items from containers by control clicking", ModuleCategory.MISC);
        registerSettings(quickMoveBind);
    }

    @Subscribe
    public void onMouseClick(final EventMouse.Key event) {
        if (event.getAction() != 0) return;
        final Screen screen = mc.screen;
        if (screen instanceof AbstractContainerScreen handler && event.getButton() == 0 && quickMoveBind.getValue().isKeyDown()) {
            Slot hoveredSlot = ((IMixinAbstractContainerScreen) handler).getHoveredSlot();
            if (hoveredSlot == null) return;
            ItemStack mouseStack = mc.player.containerMenu.getCarried();
//            if (mouseStack.isEmpty()) {
//                // todo: i need an accessor for this method on AbstractContainerScreen
//                handler.slotClicked(hoveredSlot, hoveredSlot.index, 0, ClickType.PICKUP);
//                mouseStack = mc.player.containerMenu.getCarried();
//            }
//            for(Slot slot : handler.getMenu().slots) {
//                if (slot != null
//                    && slot.mayPickup(mc.player)
//                    && slot.hasItem()
//                    && slot.container == hoveredSlot.container
//                    && AbstractContainerMenu.canItemQuickReplace(slot, mouseStack, true)) {
//                    handler.slotClicked(slot, slot.index, 0, ClickType.QUICK_MOVE);
//                }
//            }
//            handler.slotClicked(hoveredSlot, hoveredSlot.index, 0, ClickType.PICKUP);
//            handler.slotClicked(hoveredSlot, hoveredSlot.index, 0, ClickType.QUICK_MOVE);
        }
    }
}
