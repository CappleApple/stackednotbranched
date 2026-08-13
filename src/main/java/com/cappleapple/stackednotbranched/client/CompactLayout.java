package com.cappleapple.stackednotbranched.client;

import me.alfie.immersiveenchanting.gui.EnchantingTableScreen;
import me.alfie.immersiveenchanting.gui.canvas.CanvasCamera;
import me.alfie.immersiveenchanting.gui.tab.enchanting.node.Node;

public final class CompactLayout {
    private CompactLayout() { }

    public static void refresh(EnchantingTableScreen screen) {
        int maxBranchDepth = screen.enchantingTab().branchManager().branches().stream()
                .mapToInt(branch -> branch.nodes().size())
                .max()
                .orElse(1);

        screen.canvas().setSizeToFitNodes(maxBranchDepth);
        screen.enchantingTab().branchManager().positionBranches();
        centerOnContent(screen);
    }

    public static void centerOnContent(EnchantingTableScreen screen) {
        CanvasCamera camera = screen.camera();
        if(camera == null) return;

        camera.setZoom(1f);

        final float centralHalfSize = 16f;
        float minX = screen.canvas().getCenter().x() - centralHalfSize;
        float minY = screen.canvas().getCenter().y() - centralHalfSize;
        float maxX = screen.canvas().getCenter().x() + centralHalfSize;
        float maxY = screen.canvas().getCenter().y() + centralHalfSize;

        for(Node node : screen.enchantingTab().branchManager().getAllNodes()) {
            minX = Math.min(minX, node.canvasX());
            minY = Math.min(minY, node.canvasY());
            maxX = Math.max(maxX, node.canvasX() + node.getScaledLength(Node.WIDTH));
            maxY = Math.max(maxY, node.canvasY() + node.getScaledLength(Node.HEIGHT));
        }

        camera.setPos(
                (minX + maxX) / 2f - (float)camera.VIEWPORT_WIDTH / (2 * camera.zoom()),
                (minY + maxY) / 2f - (float)camera.VIEWPORT_HEIGHT / (2 * camera.zoom())
        );
        camera.clampPosition();
    }
}
