package com.songoda.epicspawners.gui;

import com.songoda.core.gui.Gui;
import com.songoda.core.gui.GuiUtils;
import com.songoda.core.lootables.gui.GuiLootableEditor;
import com.songoda.core.lootables.loot.LootManager;
import com.songoda.core.lootables.loot.Lootable;
import com.songoda.third_party.com.cryptomorin.xseries.XMaterial;
import com.songoda.core.utils.ItemUtils;
import com.songoda.epicspawners.EpicSpawners;
import com.songoda.epicspawners.api.spawners.spawner.SpawnerTier;
import com.songoda.epicspawners.settings.Settings;
import org.bukkit.inventory.ItemStack;

public class EditorDropsGui extends Gui {
    private final EpicSpawners plugin;
    private final SpawnerTier spawnerTier;

    public EditorDropsGui(EpicSpawners plugin, SpawnerTier spawnerTier, Gui back) {
        super(3);
        this.plugin = plugin;
        this.spawnerTier = spawnerTier;


        // decorate the edges
        ItemStack glass2 = GuiUtils.getBorderItem(Settings.GLASS_TYPE_2.getMaterial(XMaterial.BLUE_STAINED_GLASS_PANE));
        ItemStack glass3 = GuiUtils.getBorderItem(Settings.GLASS_TYPE_3.getMaterial(XMaterial.LIGHT_BLUE_STAINED_GLASS_PANE));

        setDefaultItem(Settings.GLASS_TYPE_1.getMaterial().parseItem());

        mirrorFill(0, 0, true, true, glass2);
        mirrorFill(0, 1, true, true, glass2);
        mirrorFill(0, 2, true, true, glass3);
        mirrorFill(1, 0, false, true, glass2);
        mirrorFill(1, 1, false, true, glass3);

        setTitle(spawnerTier.getGuiTitle());

        setButton(8, GuiUtils.createButtonItem(XMaterial.ARROW,
                        plugin.getLocale().getMessage("general.nametag.back")),
                (event) -> this.guiManager.showGUI(event.player, back));

        paint();
    }

    public void paint() {
        LootManager lootManager = this.plugin.getLootablesManager().getLootManager();

        setButton(1, 3, GuiUtils.createButtonItem(XMaterial.BARRIER, "Remove & Reset Custom Drops"),
                (event) -> {
                    lootManager.removeLootable(this.spawnerTier.getFullyIdentifyingName());
                    paint();
                });

        boolean dropExists = lootManager.getRegisteredLootables().containsKey(this.spawnerTier.getFullyIdentifyingName());

        setButton(1, 5, GuiUtils.createButtonItem(XMaterial.LIME_DYE, dropExists ? "Edit Drops" : "Create & Enable Custom Drops"),
                (event) -> {
                    if (!dropExists) {
                        lootManager.addLootable(new Lootable(this.spawnerTier.getFullyIdentifyingName()));
                    }
                    Lootable lootable = lootManager.getRegisteredLootables().get(this.spawnerTier.getFullyIdentifyingName());
                    this.plugin.getGuiManager().showGUI(event.player,
                            new GuiLootableEditor(this.plugin.getLootablesManager().getLootManager(), lootable, this));
                });
    }
}
