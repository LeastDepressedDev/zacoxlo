package me.qigan.zacoxlo.util.render;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import me.qigan.zacoxlo.ZacoxloGm;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.HashSet;
import java.util.Set;

public class RSect {
    public static class names {

        public static final Set<String> collection = new HashSet<>();

        public static final String ESP_LINE = "elines";
        public static final String DEF_LINES = "dlines";

        public static ResourceLocation gen(String pth) {
            return ResourceLocation.parse("%s:%s".formatted(ZacoxloGm.MOD_ID, pth));
        }
    }

    public static class pipelines {

        public static final Set<RenderPipeline> collection = new HashSet<>();

        public static final RenderPipeline ESP_LINE = RenderPipelines.register(
                RenderPipeline.builder(RenderPipelines.LINES_SNIPPET)
                        .withLocation(names.gen("ppl/%s".formatted(names.ESP_LINE)))
                        .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
                        .withoutBlend()
                        .build()
        );

        public static final RenderPipeline DEF_LINE = RenderPipelines.register(
                RenderPipeline.builder(RenderPipelines.LINES_SNIPPET)
                        .withLocation(names.gen("ppl/%s".formatted(names.DEF_LINES)))
                        .withoutBlend()
                        .build()
        );
    }

    public static class rtypesf {

        public static final Set<RenderType> collection = new HashSet<>();

        public static final RenderType ESP_LINE = RenderType.create("%s:%s".formatted(ZacoxloGm.MOD_ID, names.ESP_LINE),
                RenderType.BIG_BUFFER_SIZE, pipelines.ESP_LINE, RenderType.CompositeState.builder().createCompositeState(false)
        );

        public static final RenderType DEF_LINE = RenderType.create("%s:%s".formatted(ZacoxloGm.MOD_ID, names.DEF_LINES),
                RenderType.BIG_BUFFER_SIZE, pipelines.DEF_LINE, RenderType.CompositeState.builder().createCompositeState(false));
    }

    public static void register() {
        names.collection.add(names.ESP_LINE);
        names.collection.add(names.DEF_LINES);

        rtypesf.collection.add(rtypesf.ESP_LINE);
        rtypesf.collection.add(rtypesf.DEF_LINE);

        pipelines.collection.add(pipelines.ESP_LINE);
        pipelines.collection.add(pipelines.DEF_LINE);
    }

    public static void endBatches() {
        rtypesf.collection.forEach((t) -> {
            Dconsts.getVCP().endBatch(t);
        });
    }
}
