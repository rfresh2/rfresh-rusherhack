package rfresh.hud;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import org.rusherhack.client.api.feature.hud.ResizeableHudElement;
import org.rusherhack.client.api.render.RenderContext;
import org.rusherhack.core.setting.BooleanSetting;
import org.rusherhack.core.utils.MathUtils;

import java.awt.*;

public class CompassHudElement extends ResizeableHudElement {

    final BooleanSetting axis = new BooleanSetting("Axis", true);

    private enum Direction {
        N("-Z"),
        W("-X"),
        S("+Z"),
        E("+X");
        private final String axis;

        Direction(final String axis) {
            this.axis = axis;
        }

        public String getAxis() {
            return this.axis;
        }
    }

    public CompassHudElement() {
        super("Compass");
        registerSettings(axis);
    }

    @Override
    public void renderContent(final RenderContext context, final int mouseX, final int mouseY) {
        for (Direction dir : Direction.values()) {
            var rad = getPosOnCompass(dir);
            context.pose().pushPose();
            // lol there's definitely a more sane way to select these numbers
            context.graphics().pose().scale(1.5f, 1.5f, 0f);
            context.pose().translate((getWidth() / 3) + getX(rad) - (getFontRenderer().getStringWidth(dir.name())), (getHeight() / 3) + getY(rad) - (getFontRenderer().getFontHeight()), 0.0);
            var textComponent = Component.literal(axis.getValue() ? dir.getAxis() : dir.name());
            textComponent.setStyle(dir == Direction.N ? Style.EMPTY.withColor(ChatFormatting.RED) : Style.EMPTY.withColor(ChatFormatting.WHITE));
            context.graphics().drawString(mc.font, textComponent, 0, 0, new Color(255, 255, 255).getRGB());
            context.pose().popPose();
        }
    }

    private double getX(final double radians) {
        return Math.sin(radians) * (getScale() * 20.0);
    }

    private double getY(final double radians) {
        var pitchRadians = Math.toRadians(
            MathUtils.clamp(
                mc.cameraEntity.getXRot() + 30.0,
                -90.0,
                90.0));
        return Math.cos(radians) * Math.sin(pitchRadians) * (getScale() * 20.0);
    }

    private double getPosOnCompass(Direction dir) {
        var yaw = Math.toRadians(
            Mth.wrapDegrees(mc.cameraEntity.getYRot()));
        var index = dir.ordinal();
        return yaw + (index * Math.PI / 2);
    }

    @Override
    public double getWidth() {
        return 100;
    }

    @Override
    public double getHeight() {
        return 100;
    }
}
