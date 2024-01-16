package rfresh.module;

import net.minecraft.core.Direction;
import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.NotNull;
import org.rusherhack.client.api.events.client.EventUpdate;
import org.rusherhack.client.api.events.render.EventRender3D;
import org.rusherhack.client.api.feature.module.ModuleCategory;
import org.rusherhack.client.api.feature.module.ToggleableModule;
import org.rusherhack.client.api.render.IRenderer3D;
import org.rusherhack.client.api.setting.ColorSetting;
import org.rusherhack.core.event.stage.Stage;
import org.rusherhack.core.event.subscribe.Subscribe;
import org.rusherhack.core.setting.NumberSetting;
import xaeroplus.module.ModuleManager;
import xaeroplus.module.impl.OldChunks;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class XaeroPlusIntegration extends ToggleableModule {
    private final NumberSetting<Double> renderY = new NumberSetting<>("RenderY", 0.0, -64.0, 320.0)
        .incremental(1.0);
    private final ColorSetting chunkColor = new ColorSetting("ChunkColor", new Color(0, 255, 0, 50));
    private final NumberSetting<Integer> radius = new NumberSetting<>("Radius", 25, 1, 100);
    List<ChunkPos> chunks = new CopyOnWriteArrayList<>();

    public XaeroPlusIntegration() {
        super("XaeroPlus", ModuleCategory.RENDER);
        registerSettings(renderY, chunkColor);
    }

    @Subscribe(stage = Stage.POST)
    public void onTick(final EventUpdate event) {
        updateChunks();
    }


    public void updateChunks() {
        OldChunks oldChunks = ModuleManager.getModule(OldChunks.class);
        final ChunkPos currentPos = mc.cameraEntity.chunkPosition();
        final List<ChunkPos> chunkPosList = new ArrayList<>();
        for (int x = currentPos.x - radius.getValue(); x < currentPos.x + radius.getValue(); x++) {
            for (int z = currentPos.z - radius.getValue(); z < currentPos.z + radius.getValue(); z++) {
                if (oldChunks.isHighlighted(x, z, mc.level.dimension())) {
                    chunkPosList.add(new ChunkPos(x, z));
                }
            }
        }
        chunks.clear();
        chunks.addAll(chunkPosList);
    }

    @Subscribe
    private void onRender3D(EventRender3D event) {
        final IRenderer3D renderer = event.getRenderer();

        //begin renderer
        renderer.begin(event.getMatrixStack());
        for (ChunkPos chunkPos : chunks) {
            handleChunkRender(renderer, chunkPos);
        }
        //end renderer
        renderer.end();
    }


    private void handleChunkRender(IRenderer3D renderer, ChunkPos chunk) {
        final int color = chunkColor.getValueRGB();
        final double y = this.renderY.getValue();
        renderChunk(renderer, chunk, y, true, true, color);
    }

    private void renderChunk(@NotNull IRenderer3D renderer, @NotNull ChunkPos chunk, double y, boolean fill, boolean outline, int color) {
        renderer.drawPlane(
            chunk.x * 16,
            y,
            chunk.z * 16,
            16.0,
            16.0,
            Direction.UP,
            fill,
            outline,
            color
        );
    }
}
