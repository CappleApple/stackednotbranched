# Stacked Not Branched

A client-side NeoForge 1.21.1 addon for Immersive Enchanting 6.0.2 and newer.

The addon is intentionally configuration-free. When installed, it always:

- represents each enchantment with one stacked upgrade node;
- shows the current level and the next upgrade cost together;
- recenters the enchanting canvas on its visible item and nodes whenever the interface refreshes; and
- keeps removal available with Shift + hold while a stacked node can still be upgraded.

Remove the addon to return to Immersive Enchanting's original per-level interface and camera behavior.

## Requirements

- Minecraft 1.21.1
- NeoForge 21.1.209 or newer
- Immersive Enchanting 6.0.2 or newer
- Alfinolib 1.2.0 or newer, inherited from Immersive Enchanting

## Building

Keep this repository beside the `immersive-enchanting` repository, then run:

```powershell
.\gradlew.bat clean build --offline
```

The composite build compiles against the sibling Immersive Enchanting project.
