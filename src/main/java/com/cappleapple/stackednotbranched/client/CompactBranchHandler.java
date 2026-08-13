package com.cappleapple.stackednotbranched.client;

import me.alfie.alfinolib.networking.Networking;
import me.alfie.alfinolib.util.ResourceId;
import me.alfie.immersiveenchanting.api.node.BranchBuilder;
import me.alfie.immersiveenchanting.api.node.BuildBranchesEvent;
import me.alfie.immersiveenchanting.api.node.NodeData;
import me.alfie.immersiveenchanting.api.node.NodeTemplate;
import me.alfie.immersiveenchanting.api.node.SpriteIcon;
import me.alfie.immersiveenchanting.api.node.internal.EnchantmentNodeData;
import me.alfie.immersiveenchanting.gui.EnchantingTableScreen;
import me.alfie.immersiveenchanting.gui.tab.enchanting.node.Node;
import me.alfie.immersiveenchanting.gui.tab.enchanting.node.NodeBranch;
import me.alfie.immersiveenchanting.gui.tab.enchanting.node.NodeState;
import me.alfie.immersiveenchanting.gui.tab.enchanting.node.NodeTier;
import me.alfie.immersiveenchanting.networking.EnchantPacket;
import me.alfie.immersiveenchanting.util.EnchantmentTextureHelper;
import me.alfie.immersiveenchanting.util.EnchantmentUtil;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;

import java.util.ListIterator;

public final class CompactBranchHandler {
    private CompactBranchHandler() { }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onBuildBranches(BuildBranchesEvent event) {
        ListIterator<NodeBranch> branches = event.getBranches().listIterator();
        while(branches.hasNext()) {
            NodeBranch branch = branches.next();
            if(branch.nodes().isEmpty()) continue;
            if(!branch.nodes().getFirst().dataType().equals(EnchantmentNodeData.TYPE)) continue;
            if(!(branch.nodes().getFirst().data().value() instanceof EnchantmentNodeData data)) continue;

            Holder<Enchantment> enchantment = EnchantmentUtil.toHolder(
                    data.enchantmentId(), event.getCanvas().screen().registryAccess());
            branches.set(buildCompactBranch(event, enchantment));
        }
    }

    private static NodeBranch buildCompactBranch(BuildBranchesEvent event, Holder<Enchantment> enchantment) {
        int maxLevel = event.costRegistry().isRegistered(enchantment)
                ? event.costRegistry().get(enchantment).levelCosts().maxLevel()
                : enchantment.value().getMaxLevel();

        int equippedLevel = EnchantmentUtil.getEnchantmentLevel(event.getStack(), enchantment);
        int displayedLevel = equippedLevel < maxLevel ? equippedLevel + 1 : Math.max(equippedLevel, 1);
        NodeState state = equippedLevel > 0 ? NodeState.OBTAINED : NodeState.UNOBTAINED;
        if(!event.getCanvas().screen().getMenu().isEnchantmentAvailable(enchantment)) state = NodeState.LOCKED;

        ResourceId enchantmentId = EnchantmentUtil.toId(enchantment);
        NodeTemplate node = new NodeTemplate(
                Enchantment.getFullname(enchantment, displayedLevel),
                displayedLevel - 1,
                state,
                displayedLevel >= maxLevel ? NodeTier.ELITE : NodeTier.BASIC,
                new SpriteIcon(EnchantmentTextureHelper.getTexture(enchantmentId)),
                createCompactNodeData(enchantmentId, displayedLevel)
        );

        return BranchBuilder.of(event.getCanvas(), enchantmentId).node(node).build();
    }

    private static NodeData<EnchantmentNodeData> createCompactNodeData(ResourceId enchantmentId, int level) {
        return new NodeData<>(
                EnchantmentNodeData.TYPE,
                new EnchantmentNodeData(enchantmentId, level),
                (data, context) -> {
                    Node node = context.node();
                    EnchantingTableScreen screen = context.screen();
                    if(node.isState(NodeState.LOCKED)) return;

                    CompactNodeAccess compactNode = (CompactNodeAccess)node;
        if(node.canRemove() && (!compactNode.stackedNotBranched$canUpgrade() || Screen.hasShiftDown())) {
                        screen.tooltipManager().startHold(node);
        } else if(compactNode.stackedNotBranched$canUpgrade()) {
                        Holder<Enchantment> enchantment = EnchantmentUtil.toHolder(
                                data.enchantmentId(), screen.registryAccess());
                        Networking.sendToServer(new EnchantPacket(enchantment.getKey(), data.level()));
                    }
                }
        );
    }
}
