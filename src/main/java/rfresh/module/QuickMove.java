package rfresh.module;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;
import org.rusherhack.client.api.accessors.gui.IMixinAbstractContainerScreen;
import org.rusherhack.client.api.feature.module.ModuleCategory;
import org.rusherhack.client.api.feature.module.ToggleableModule;

public class QuickMove extends ToggleableModule {
    public QuickMove() {
        super("QuickMove", "Quick move items from containers by control clicking", ModuleCategory.MISC);
        // todo: a rusherhack event for this would be preferable
        ScreenEvents.BEFORE_INIT.register(this::beforeInit);
    }

    private void beforeInit(Minecraft client, Screen screen, int scaledWidth, int scaledHeight) {
        ScreenMouseEvents.allowMouseClick(screen).register(this::onMouseClicked);
    }


    public boolean onMouseClicked(final Screen screen, final double mouseX, final double mouseY, final int button) {
        if (!this.isToggled()) return true;
        boolean isLCtrlDown = InputConstants.isKeyDown(mc.getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_CONTROL);
        if (screen instanceof AbstractContainerScreen handler && button == 0 && isLCtrlDown) {
            Slot hoveredSlot = ((IMixinAbstractContainerScreen) handler).getHoveredSlot();
            if (hoveredSlot == null) return true;
            ItemStack stackOnMouse = mc.player.containerMenu.getCarried();

            if (stackOnMouse.isEmpty()) {
                // todo: i need an accessor for this method on AbstractContainerScreen
                handler.slotClicked(hoveredSlot, hoveredSlot.index, 0, ClickType.PICKUP);
                stackOnMouse = mc.player.containerMenu.getCarried();
            }
            for(Slot slot : handler.getMenu().slots) {
                if (slot != null
                    && slot.mayPickup(mc.player)
                    && slot.hasItem()
                    && slot.container == hoveredSlot.container
                    && AbstractContainerMenu.canItemQuickReplace(slot, stackOnMouse, true)) {
                    handler.slotClicked(slot, slot.index, 0, ClickType.QUICK_MOVE);
                }
            }
            handler.slotClicked(hoveredSlot, hoveredSlot.index, 0, ClickType.PICKUP);
            handler.slotClicked(hoveredSlot, hoveredSlot.index, 0, ClickType.QUICK_MOVE);
        }
        return true;
    }
}
