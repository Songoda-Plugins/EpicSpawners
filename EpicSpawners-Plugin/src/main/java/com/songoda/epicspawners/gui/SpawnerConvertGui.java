package com.songoda.epicspawners.gui;

import com.songoda.core.gui.CustomizableGui;
import com.songoda.core.gui.GuiUtils;
import com.songoda.core.hooks.EconomyManager;
import com.songoda.third_party.com.cryptomorin.xseries.XMaterial;
import com.songoda.epicspawners.EpicSpawners;
import com.songoda.epicspawners.api.spawners.spawner.SpawnerData;
import com.songoda.epicspawners.api.spawners.spawner.SpawnerStack;
import com.songoda.epicspawners.api.utils.HeadUtils;
import com.songoda.epicspawners.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SpawnerConvertGui extends CustomizableGui {
    private final EpicSpawners plugin;
    private final SpawnerStack stack;
    private final Player player;
    private final List<SpawnerData> entities = new ArrayList<>();

    public SpawnerConvertGui(EpicSpawners plugin, SpawnerStack stack, Player player) {
        super(plugin, "convert");
        setRows(6);
        this.plugin = plugin;
        this.stack = stack;
        this.player = player;

        for (SpawnerData spawnerData : plugin.getSpawnerManager().getAllSpawnerData()) {
            if (!spawnerData.isConvertible()
                    || !spawnerData.isActive()
                    || !player.hasPermission("epicspawners.convert." + spawnerData.getIdentifyingName().replace(" ", "_"))) {
                continue;
            }
            this.entities.add(spawnerData);
        }

        setTitle(plugin.getLocale().getMessage("interface.convert.title").toString());
        showPage();
    }

    public void showPage() {
        reset();

        // decorate the edges
        ItemStack glass2 = GuiUtils.getBorderItem(Settings.GLASS_TYPE_2.getMaterial(XMaterial.BLUE_STAINED_GLASS_PANE));
        ItemStack glass3 = GuiUtils.getBorderItem(Settings.GLASS_TYPE_3.getMaterial(XMaterial.LIGHT_BLUE_STAINED_GLASS_PANE));

        // edges will be type 3
        mirrorFill("mirrorfill_1", 0, 2, true, true, glass3);
        mirrorFill("mirrorfill_2", 1, 1, true, true, glass3);

        // decorate corners with type 2
        mirrorFill("mirrorfill_3", 0, 0, true, true, glass2);
        mirrorFill("mirrorfill_4", 1, 0, true, true, glass2);
        mirrorFill("mirrorfill_5", 0, 1, true, true, glass2);

        this.pages = (int) Math.max(1, Math.ceil(this.entities.size() / ((double) 28)));

        // enable page event
        setNextPage(5, 7, GuiUtils.createButtonItem(XMaterial.ARROW, this.plugin.getLocale().getMessage("general.nametag.next").toString()));
        setPrevPage(5, 1, GuiUtils.createButtonItem(XMaterial.ARROW, this.plugin.getLocale().getMessage("general.nametag.back").toString()));
        setOnPage((event) -> showPage());

        // Sort entities by their shopOrder val
        this.entities.sort(Comparator.comparingInt(SpawnerData::getShopOrder));

        List<SpawnerData> data = this.entities.stream().skip((this.page - 1) * 28).limit(28).collect(Collectors.toList());

        int num = 11;
        for (SpawnerData spawnerData : data) {
            if (num == 16 || num == 36) {
                num = num + 2;
            }

            ItemStack item = HeadUtils.getTexturedSkull(spawnerData);

            if (spawnerData.getDisplayItem() != null) {
                XMaterial mat = spawnerData.getDisplayItem();
                if (!mat.equals(XMaterial.AIR)) {
                    item = mat.parseItem();
                }
            }

            ItemMeta itemmeta = item.getItemMeta();
            String name = spawnerData.getFirstTier().getCompiledDisplayName();
            ArrayList<String> lore = new ArrayList<>();
            double price = spawnerData.getConvertPrice();

            lore.add(this.plugin.getLocale().getMessage("interface.shop.buyprice").processPlaceholder("cost", EconomyManager.formatEconomy(price)).toText());
            String loreString = this.plugin.getLocale().getMessage("interface.convert.lore").toText();
            if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                loreString = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(this.player, loreString.replace(" ", "_")).replace("_", " ");
            }
            lore.add(loreString);
            itemmeta.setLore(lore);
            itemmeta.setDisplayName(name);
            item.setItemMeta(itemmeta);
            setButton(num, item, event ->
                    this.stack.convert(spawnerData, this.player, false));
            num++;
        }

        setButton("back", 8, GuiUtils.createButtonItem(Settings.EXIT_ICON.getMaterial(),
                        this.plugin.getLocale().getMessage("general.nametag.back").toString()),
                event -> this.guiManager.showGUI(this.player, new SpawnerOverviewGui(this.plugin, this.stack, this.player)));
    }
}
