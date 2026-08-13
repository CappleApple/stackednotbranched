package com.cappleapple.stackednotbranched.client;

import me.alfie.alfinolib.gui.GuiGraphicsX;
import me.alfie.alfinolib.gui.util.MousePos;
import me.alfie.immersiveenchanting.api.description.DescriptionHelper;
import me.alfie.immersiveenchanting.api.description.DescriptionLine;
import me.alfie.immersiveenchanting.gui.tab.enchanting.tooltip.NodeTooltip;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public record CurrentLevelLine(NodeTooltip tooltip) implements DescriptionLine {
    @Override
    public void render(GuiGraphicsX gx, int lineX, int lineY, MousePos mousePos) {
        DescriptionHelper.text(gx, getText(), lineX, lineY);
    }

    @Override
    public @NotNull Component getText() {
        CompactNodeAccess node = (CompactNodeAccess)tooltip.node();
        return Component.translatable(
                "stackednotbranched.tooltip.current_level",
                node.stackedNotBranched$getEquippedEnchantmentLevel()
        ).withStyle(ChatFormatting.LIGHT_PURPLE);
    }
}
