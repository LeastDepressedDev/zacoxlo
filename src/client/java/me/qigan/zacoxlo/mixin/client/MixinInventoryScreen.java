package me.qigan.zacoxlo.mixin.client;

import me.qigan.zacoxlo.Zacoxlo;
import me.qigan.zacoxlo.fr.GuiModify;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.gui.screens.inventory.EffectsInInventory;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public abstract class MixinInventoryScreen extends AbstractRecipeBookScreen<InventoryMenu> {
    @Shadow
    @Final
    private EffectsInInventory effects;

    @Shadow
    private float xMouse;

    @Shadow
    private float yMouse;

    public MixinInventoryScreen(InventoryMenu recipeBookMenu, RecipeBookComponent<?> recipeBookComponent, Inventory inventory, Component component) {
        super(recipeBookMenu, recipeBookComponent, inventory, component);
    }

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void render(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo ci) {
        if (Zacoxlo.MAIN_CFG.getBoolVal("gui_modify") && GuiModify.rtCfg.get("gui_modify").get("Hide inventory effects").getAsBoolean()) {
            super.render(guiGraphics, i, j, f);
            this.effects.renderTooltip(guiGraphics, i, j);
            this.xMouse = i;
            this.yMouse = j;
            ci.cancel();
        }
    }
}
