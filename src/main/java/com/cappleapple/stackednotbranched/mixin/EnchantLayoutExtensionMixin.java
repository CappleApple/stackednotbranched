package com.cappleapple.stackednotbranched.mixin;

import com.cappleapple.stackednotbranched.client.CompactNodeAccess;
import com.cappleapple.stackednotbranched.client.CompactRemoveHintLine;
import com.cappleapple.stackednotbranched.client.CurrentLevelLine;
import me.alfie.immersiveenchanting.api.description.DescriptionHelper;
import me.alfie.immersiveenchanting.api.description.DescriptionLayout;
import me.alfie.immersiveenchanting.api.description.internal.EnchantLayoutExtension;
import me.alfie.immersiveenchanting.api.description.internal.lines.EquippedLine;
import me.alfie.immersiveenchanting.api.description.internal.lines.RemoveProgressLine;
import me.alfie.immersiveenchanting.api.description.internal.lines.RemovingLine;
import me.alfie.immersiveenchanting.api.description.internal.lines.UnavailableEnchantmentLine;
import me.alfie.immersiveenchanting.api.node.internal.EnchantmentNodeData;
import me.alfie.immersiveenchanting.gui.EnchantingTableScreen;
import me.alfie.immersiveenchanting.gui.tab.enchanting.node.Node;
import me.alfie.immersiveenchanting.gui.tab.enchanting.node.NodeState;
import me.alfie.immersiveenchanting.gui.tab.enchanting.tooltip.NodeTooltip;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnchantLayoutExtension.class)
public abstract class EnchantLayoutExtensionMixin {
    @Inject(method = "extendLayout", at = @At("HEAD"), cancellable = true)
    private void stackedNotBranched$buildCompactTooltip(DescriptionLayout description,
                                                            NodeTooltip tooltip,
                                                            CallbackInfo ci) {
        if(!tooltip.node().isDataType(EnchantmentNodeData.TYPE)) return;

        description.widthPadding = 16;
        Node node = tooltip.node();
        CompactNodeAccess compactNode = (CompactNodeAccess)node;
        EnchantingTableScreen screen = tooltip.screen();

        description.insertLine(0, new CurrentLevelLine(tooltip));
        if(screen.tooltipManager().isHoldingTooltip()) {
            description.insertLine(1, new RemovingLine(tooltip));
            description.insertLine(2, new RemoveProgressLine(tooltip));
        } else if(node.isState(NodeState.LOCKED)) {
            description.insertLine(1, new UnavailableEnchantmentLine(tooltip));
        } else if(compactNode.stackedNotBranched$canUpgrade()) {
            int costLine = 1;
            if(node.canRemove()) {
                description.insertLine(costLine++, new CompactRemoveHintLine(tooltip));
            }
            DescriptionHelper.insertCostLines(tooltip, description, costLine);
        } else if(node.isState(NodeState.OBTAINED)) {
            description.insertLine(1, new EquippedLine(tooltip));
            if(node.canRemove()) description.insertLine(2, new CompactRemoveHintLine(tooltip));
        }

        ci.cancel();
    }
}
