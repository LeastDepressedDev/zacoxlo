package me.qigan.zacoxlo.util.render;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import me.qigan.zacoxlo.ZacoxloGm;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class RSect {
    public static class names {
        public static final String ESP_LINE = "elines";

        public static ResourceLocation gen(String pth) {
            return ResourceLocation.parse("%s:%s".formatted(ZacoxloGm.MOD_ID, pth));
        }
    }

    public static class pipelines {
        public static final RenderPipeline ESP_LINE = RenderPipelines.register(
                RenderPipeline.builder(RenderPipelines.LINES_SNIPPET)
                        .withLocation(names.gen("ppl/%s".formatted(names.ESP_LINE)))
                        .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
                        .withoutBlend()
                        .build());
    }

    public static class rtypesf {
        public static final RenderType ESP_LINE = RenderType.create("%s:%s".formatted(ZacoxloGm.MOD_ID, names.ESP_LINE),
                RenderType.BIG_BUFFER_SIZE, pipelines.ESP_LINE,  RenderType.CompositeState.builder().createCompositeState(false)
                );
    }
}
