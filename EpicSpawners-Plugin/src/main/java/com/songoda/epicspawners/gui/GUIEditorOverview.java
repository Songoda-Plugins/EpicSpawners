package com.songoda.epicspawners.gui;

import com.songoda.epicspawners.EpicSpawnersPlugin;
import com.songoda.epicspawners.References;
import com.songoda.epicspawners.api.spawner.SpawnerData;
import com.songoda.epicspawners.spawners.spawner.ESpawnerData;
import com.songoda.epicspawners.utils.Methods;
import com.songoda.epicspawners.utils.gui.AbstractAnvilGUI;
import com.songoda.epicspawners.utils.gui.AbstractGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class GUIEditorOverview extends AbstractGUI {

    private final EpicSpawnersPlugin plugin;
    private final AbstractGUI back;
    private SpawnerData spawnerData;

    public GUIEditorOverview(EpicSpawnersPlugin plugin, AbstractGUI abstractGUI, SpawnerData spawnerData, Player player) {
        super(player);
        this.plugin = plugin;
        this.back = abstractGUI;
        this.spawnerData = spawnerData;

        if (spawnerData == null) {
            String type;
            for (int i = 1; true; i++) {
                String temp = "Custom " + i;
                if (!plugin.getSpawnerManager().isSpawnerData(temp)) {
                    type = temp;
                    break;
                }
            }

            this.spawnerData = new ESpawnerData(0, type, new ArrayList<>(), new ArrayList<>(),
                    new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
            ((ESpawnerData) this.spawnerData).addDefaultConditions();
            this.spawnerData.setCustom(true);
            plugin.getSpawnerManager().addSpawnerData(type, this.spawnerData);
        }

        init("&8Editing: " + Methods.compileName(this.spawnerData, 1, false) + "&8.", 54);
    }

    @Override
    public void constructGUI() {
        inventory.clear();
        resetClickables();
        registerClickables();

        int num = 0;
        while (num != 54) {
            inventory.setItem(num, Methods.getGlass());
            num++;
        }

        inventory.setItem(0, Methods.getBackgroundGlass(false));
        inventory.setItem(1, Methods.getBackgroundGlass(false));
        inventory.setItem(2, Methods.getBackgroundGlass(false));
        inventory.setItem(3, Methods.getBackgroundGlass(true));
        inventory.setItem(4, Methods.getBackgroundGlass(true));

        inventory.setItem(9, Methods.getBackgroundGlass(false));
        inventory.setItem(13, Methods.getBackgroundGlass(true));
        inventory.setItem(14, Methods.getBackgroundGlass(false));
        inventory.setItem(15, Methods.getBackgroundGlass(true));
        inventory.setItem(16, Methods.getBackgroundGlass(true));
        inventory.setItem(17, Methods.getBackgroundGlass(true));

        inventory.setItem(18, Methods.getBackgroundGlass(false));
        inventory.setItem(22, Methods.getBackgroundGlass(false));
        inventory.setItem(26, Methods.getBackgroundGlass(true));

        inventory.setItem(27, Methods.getBackgroundGlass(true));
        inventory.setItem(31, Methods.getBackgroundGlass(false));
        inventory.setItem(35, Methods.getBackgroundGlass(false));

        inventory.setItem(36, Methods.getBackgroundGlass(true));
        inventory.setItem(37, Methods.getBackgroundGlass(true));
        inventory.setItem(38, Methods.getBackgroundGlass(false));
        inventory.setItem(39, Methods.getBackgroundGlass(true));
        inventory.setItem(40, Methods.getBackgroundGlass(true));
        inventory.setItem(44, Methods.getBackgroundGlass(false));

        inventory.setItem(49, Methods.getBackgroundGlass(true));
        inventory.setItem(50, Methods.getBackgroundGlass(true));
        inventory.setItem(51, Methods.getBackgroundGlass(false));
        inventory.setItem(52, Methods.getBackgroundGlass(false));
        inventory.setItem(53, Methods.getBackgroundGlass(false));

        createButton(8, Methods.addTexture(new ItemStack(Material.PLAYER_HEAD, 1, (byte) 3),
                "http://textures.minecraft.net/texture/3ebf907494a935e955bfcadab81beafb90fb9be49c7026ba97d798d5f1a23"),
                plugin.getLocale().getMessage("general.nametag.back"));

        ItemStack it = new ItemStack(Material.PLAYER_HEAD, 1, (byte) 3);

        ItemStack item = plugin.getHeads().addTexture(it, spawnerData);
        if (spawnerData.getDisplayItem() != null && spawnerData.getDisplayItem() != Material.AIR) {
            item.setType(spawnerData.getDisplayItem());
        }

        ItemMeta itemmeta = item.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();
        lore.add(Methods.formatText("&7Left-Click to &9Change Spawner Name&7."));
        lore.add(Methods.formatText("&7Right-Click to &bChange Spawner Display Item&7."));
        lore.add(Methods.formatText("&6-----------------------------"));

        lore.add(Methods.formatText("&6Display Name: &7" + spawnerData.getDisplayName() + "&7."));
        if (spawnerData.getDisplayItem() != null) {
            lore.add(Methods.formatText("&6Display Item: &7" + spawnerData.getDisplayItem().name() + "&7."));
        } else {
            if (!spawnerData.isCustom()) {
                lore.add(Methods.formatText("&6Display Item: &7Unavailable&7."));
            } else {
                lore.add(Methods.formatText("&6Display Item: &7Dirt&7."));
            }
        }
        lore.add(Methods.formatText("&6Config Name: &7" + spawnerData.getIdentifyingName() + "&7."));
        itemmeta.setLore(lore);
        itemmeta.setDisplayName(Methods.compileName(spawnerData, 1, false));
        item.setItemMeta(itemmeta);
        inventory.setItem(11, item);

        lore = new ArrayList<>();
        if (spawnerData.isCustom()) lore.add(Methods.formatText("&7Right-Click to: &cDestroy Spawner"));
        lore.add(Methods.formatText("&6---------------------------"));
        lore.add(Methods.formatText(spawnerData.isActive() ? "&6Currently:&a Enabled." : "&6Currently:&c Disabled."));

        createButton(29, Material.TNT, "&7Left-Click to: &cDisable Spawner", lore);

        createButton(23, Material.LEVER, "&9&lGeneral Settings");
        createButton(24, Material.BONE, "&e&lDrop Settings");

        createButton(25, plugin.getHeads().addTexture(new ItemStack(Material.PLAYER_HEAD, 1, (byte) 3), plugin.getSpawnerManager().getSpawnerData("omni")), "&a&lEntity Settings");

        createButton(41, Material.CHEST, "&5&lItem Settings");
        createButton(32, Material.GOLD_BLOCK, "&c&lBlock Settings");
        createButton(34, Material.FIREWORK_ROCKET, "&b&lParticle Settings");
        createButton(43, Material.PAPER, "&6&lCommand Settings");
    }

    @Override
    protected void registerClickables() {

        registerClickable(29, ((player1, inventory1, cursor, slot, type) -> {
            if (type == ClickType.LEFT || !spawnerData.isCustom()) {
                if (spawnerData.isActive())
                    spawnerData.setActive(false);
                else
                    spawnerData.setActive(true);
                constructGUI();
            } else if (type == ClickType.RIGHT) {
                AbstractAnvilGUI gui = new AbstractAnvilGUI(player, event -> {
                    if (event.getName().equalsIgnoreCase("yes")) {
                        player.sendMessage(Methods.formatText("&6" + spawnerData.getIdentifyingName() + " Spawner &7 has been destroyed successfully"));
                        plugin.getSpawnerManager().removeSpawnerData(spawnerData.getIdentifyingName());
                    }
                });

                gui.setOnClose((player, inventory) -> {
                    back.init(back.getSetTitle(), back.getInventory().getSize());
                    back.constructGUI();
                });

                ItemStack item = new ItemStack(Material.PAPER);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName("Are you sure? (Yes/No)");
                item.setItemMeta(meta);

                gui.setSlot(AbstractAnvilGUI.AnvilSlot.INPUT_LEFT, item);
                gui.open();
            }
        }));

        registerClickable(23, ((player1, inventory1, cursor, slot, type) ->
                new GUIEditorGeneral(plugin, this, spawnerData, player)));

        registerClickable(41, ((player1, inventory1, cursor, slot, type) ->
                new GUIEditorEdit(plugin, this, spawnerData, GUIEditorEdit.EditType.ITEM, player)));

        registerClickable(43, ((player1, inventory1, cursor, slot, type) ->
            new GUIEditorEdit(plugin, this, spawnerData, GUIEditorEdit.EditType.COMMAND, player)));

        registerClickable(24, ((player1, inventory1, cursor, slot, type) ->
                new GUIEditorEdit(plugin, this, spawnerData, GUIEditorEdit.EditType.DROPS, player)));

        registerClickable(25, ((player1, inventory1, cursor, slot, type) ->
                new GUIEditorEdit(plugin, this, spawnerData, GUIEditorEdit.EditType.ENTITY, player)));

        registerClickable(32, ((player1, inventory1, cursor, slot, type) ->
                new GUIEditorEdit(plugin, this, spawnerData, GUIEditorEdit.EditType.BLOCK, player)));

        registerClickable(34, ((player1, inventory1, cursor, slot, type) ->
                new GUIEditorParticle(plugin, this, spawnerData, player)));

        registerClickable(8, (player, inventory, cursor, slot, type) -> {
            back.init(back.getSetTitle(), back.getInventory().getSize());
            back.constructGUI();
        });

        registerClickable(11, (player, inventory, cursor, slot, type) -> {
            if (type == ClickType.RIGHT) {
                spawnerData.setDisplayItem(Material.valueOf(player.getInventory().getItemInHand().getType().toString()));
                player.sendMessage(Methods.formatText(References.getPrefix() + "&7Display Item for &6" + spawnerData.getIdentifyingName() + " &7set to &6" + player.getInventory().getItemInHand().getType().toString() + "&7."));
                constructGUI();
            } else if (type == ClickType.LEFT) {
                AbstractAnvilGUI gui = new AbstractAnvilGUI(player, event ->
                        spawnerData.setDisplayName(event.getName()));

                gui.setOnClose((player1, inventory1) -> init("&8Editing: " + Methods.compileName(spawnerData, 1, false) + "&8.", inventory.getSize()));

                ItemStack item = new ItemStack(Material.PAPER);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName("Enter a display name.");
                item.setItemMeta(meta);

                gui.setSlot(AbstractAnvilGUI.AnvilSlot.INPUT_LEFT, item);
                gui.open();
            }
        });
    }

    @Override
    protected void registerOnCloses() {

    }
}
