package com.cappleapple.stackednotbranched;

import com.cappleapple.stackednotbranched.client.CompactBranchHandler;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = StackedNotBranched.MOD_ID, dist = Dist.CLIENT)
public final class StackedNotBranched {
    public static final String MOD_ID = "stackednotbranched";

    public StackedNotBranched() {
        NeoForge.EVENT_BUS.register(CompactBranchHandler.class);
    }
}
