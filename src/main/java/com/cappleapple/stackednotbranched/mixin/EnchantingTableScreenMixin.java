package com.cappleapple.stackednotbranched.mixin;

import com.cappleapple.stackednotbranched.client.CompactLayout;
import me.alfie.immersiveenchanting.gui.EnchantingTableScreen;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnchantingTableScreen.class)
public abstract class EnchantingTableScreenMixin {
    @Inject(method = "init", at = @At("TAIL"))
    private void stackedNotBranched$centerAfterInit(CallbackInfo ci) {
        CompactLayout.centerOnContent((EnchantingTableScreen)(Object)this);
    }

    @Inject(method = "rebuildBranches(Lnet/minecraft/world/item/ItemStack;)V", at = @At("TAIL"))
    private void stackedNotBranched$compactAndCenterAfterRefresh(ItemStack stack, CallbackInfo ci) {
        CompactLayout.refresh((EnchantingTableScreen)(Object)this);
    }
}
