package rfresh.module;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.accessors.gui.IMixinAbstractContainerScreen;
import org.rusherhack.client.api.events.client.input.EventMouse;
import org.rusherhack.client.api.feature.module.ModuleCategory;
import org.rusherhack.client.api.feature.module.ToggleableModule;
import org.rusherhack.client.api.setting.BindSetting;
import org.rusherhack.client.api.utils.InventoryUtils;
import org.rusherhack.core.event.stage.Stage;
import org.rusherhack.core.event.subscribe.Subscribe;

public class QuickMove extends ToggleableModule {
    final BindSetting quickMoveBind = new BindSetting("HoldKey", RusherHackAPI.getBindManager().parseKey("LEFT_CONTROL"));

    public QuickMove() {
        super("QuickMove", "Quick move items from containers by control clicking", ModuleCategory.MISC);
        registerSettings(quickMoveBind);
    }

    @Subscribe(stage = Stage.PRE)
    public void onMouseClick(final EventMouse.Key event) {
        if (event.getAction() != 0) return;;
        if (mc.screen instanceof AbstractContainerScreen handler && event.getButton() == 0 && quickMoveBind.getValue().isKeyDown()) {
            Slot hoveredSlot = ((IMixinAbstractContainerScreen) handler).getHoveredSlot();
            if (hoveredSlot == null) return;
            ItemStack mouseStack = mc.player.containerMenu.getCarried();
            if (mouseStack.isEmpty()) {
                getLogger().info("click1");
                InventoryUtils.clickSlot(hoveredSlot.index, false);
                mouseStack = mc.player.containerMenu.getCarried();
            }
            for(Slot slot : handler.getMenu().slots) {
                if (slot != null
                    && slot.mayPickup(mc.player)
                    && slot.hasItem()
                    && slot.container == hoveredSlot.container
                    && AbstractContainerMenu.canItemQuickReplace(slot, mouseStack, true)) {
                    InventoryUtils.clickSlot(slot.index, true);
                }
            }
            InventoryUtils.clickSlot(hoveredSlot.index, false);
            InventoryUtils.clickSlot(hoveredSlot.index, true);
        }
    }
}
