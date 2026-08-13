package com.cappleapple.stackednotbranched.mixin;

import com.cappleapple.stackednotbranched.client.CompactNodeAccess;
import me.alfie.immersiveenchanting.api.node.internal.EnchantmentNodeData;
import me.alfie.immersiveenchanting.config.ServerConfig;
import me.alfie.immersiveenchanting.gui.tab.enchanting.node.Node;
import me.alfie.immersiveenchanting.util.EnchantmentUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Node.class)
public abstract class NodeMixin implements CompactNodeAccess {
    @Inject(method = "canRemove", at = @At("HEAD"), cancellable = true)
    private void stackedNotBranched$allowRemovalFromCompactNode(CallbackInfoReturnable<Boolean> cir) {
        Node node = (Node)(Object)this;
        if(!(node.data().value() instanceof EnchantmentNodeData)) return;
        cir.setReturnValue(stackedNotBranched$getEquippedEnchantmentLevel() > 0
                && ServerConfig.isEnchantmentRemovalAllowed());
    }

    @Override
    @Unique
    public int stackedNotBranched$getEquippedEnchantmentLevel() {
        Node node = (Node)(Object)this;
        if(!(node.data().value() instanceof EnchantmentNodeData data)) return 0;

        return EnchantmentUtil.getEnchantmentLevel(
                node.canvas().screen().getMenu().getToolSlot().getItem(),
                EnchantmentUtil.toHolder(data.enchantmentId(), node.canvas().screen().registryAccess())
        );
    }

    @Override
    @Unique
    public boolean stackedNotBranched$canUpgrade() {
        Node node = (Node)(Object)this;
        if(!(node.data().value() instanceof EnchantmentNodeData data)) return false;
        return data.level() > stackedNotBranched$getEquippedEnchantmentLevel();
    }
}
