package com.songoda.epicspawners.Utils;

import com.songoda.arconix.Arconix;
import com.songoda.epicspawners.EpicSpawners;
import com.songoda.epicspawners.Lang;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by songo on 6/4/2017.
 */
public class SettingsManager implements Listener {

    EpicSpawners plugin = EpicSpawners.pl();

    public Map<Player, Integer> page = new HashMap<>();

    private static ConfigWrapper defs;

    public SettingsManager() {
        plugin.saveResource("SettingDefinitions.yml", true);
        defs = new ConfigWrapper(plugin, "", "SettingDefinitions.yml");
        defs.createNewFile("Loading data file", "EpicSpawners SettingDefinitions file");
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public Map<Player, String> current = new HashMap<>();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getClickedInventory() != null) {
            if (e.getInventory().getTitle().equals("EpicSpawners Settings Editor")) {
                Player p = (Player) e.getWhoClicked();
                if (e.getCurrentItem().getType().equals(Material.STAINED_GLASS_PANE)) {
                    e.setCancelled(true);
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Lang.NEXT.getConfigValue())) {
                    page.put(p, 2);
                    openEditor(p);
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Lang.BACK.getConfigValue())) {
                    page.put(p, 1);
                    openEditor(p);
                } else if (e.getCurrentItem() != null) {
                    e.setCancelled(true);

                    String key = e.getCurrentItem().getItemMeta().getDisplayName().substring(2);

                    if (plugin.getConfig().get(key).getClass().getName().equals("java.lang.Boolean")) {
                        boolean bool = (Boolean) plugin.getConfig().get(key);
                        if (!bool)
                            plugin.getConfig().set(key, true);
                        else
                            plugin.getConfig().set(key, false);
                        finishEditing(p);
                    } else {
                        editObject(p, key);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        if (current.containsKey(p)) {
            if (plugin.getConfig().get(current.get(p)).getClass().getName().equals("java.lang.Integer")) {
                plugin.getConfig().set(current.get(p), Integer.parseInt(e.getMessage()));
            } else if (plugin.getConfig().get(current.get(p)).getClass().getName().equals("java.lang.Double")) {
                plugin.getConfig().set(current.get(p), Double.parseDouble(e.getMessage()));
            } else if (plugin.getConfig().get(current.get(p)).getClass().getName().equals("java.lang.String")) {
                plugin.getConfig().set(current.get(p), e.getMessage());
            }
            finishEditing(p);
            e.setCancelled(true);
        }
    }

    public void finishEditing(Player p) {
        current.remove(p);
        plugin.saveConfig();
        openEditor(p);
    }


    public void editObject(Player p, String current) {
        this.current.put(p, current);
        p.closeInventory();
        p.sendMessage("");
        p.sendMessage(Arconix.pl().format().formatText("&7Please enter a value for &6" + current + "&7."));
        if (plugin.getConfig().get(current).getClass().getName().equals("java.lang.Integer")) {
            p.sendMessage(Arconix.pl().format().formatText("&cUse only numbers."));
        }
        p.sendMessage("");
    }

    public void openEditor(Player p) {
        int pmin = 1;

        if (page.containsKey(p))
            pmin = page.get(p);

        if (pmin != 1)
            pmin = 45;

        int pmax = pmin * 44;

        Inventory i = Bukkit.createInventory(null, 54, "EpicSpawners Settings Editor");

        int num = 0;
        int total = 0;
        for (String key : plugin.getConfig().getDefaultSection().getKeys(true)) {
            if (!key.contains("Entities")) {
                if (total >= pmin - 1 && total <= pmax) {

                    ItemStack item = new ItemStack(Material.DIAMOND_HELMET);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(Arconix.pl().format().formatText("&6" + key));
                    ArrayList<String> lore = new ArrayList<>();
                    if (plugin.getConfig().get(key).getClass().getName().equals("java.lang.Boolean")) {

                        item.setType(Material.LEVER);
                        boolean bool = (Boolean) plugin.getConfig().get(key);

                        if (!bool)
                            lore.add(Arconix.pl().format().formatText("&c" + bool));
                        else
                            lore.add(Arconix.pl().format().formatText("&a" + bool));

                    } else if (plugin.getConfig().get(key).getClass().getName().equals("java.lang.String")) {
                        item.setType(Material.PAPER);
                        String str = (String) plugin.getConfig().get(key);
                        lore.add(Arconix.pl().format().formatText("&9" + str));
                    } else if (plugin.getConfig().get(key).getClass().getName().equals("java.lang.Integer")) {
                        item.setType(Material.WATCH);

                        int in = (Integer) plugin.getConfig().get(key);
                        lore.add(Arconix.pl().format().formatText("&5" + in));
                    }
                    if (defs.getConfig().contains(key)) {
                        String text = defs.getConfig().getString(key);

                        Pattern regex = Pattern.compile("(.{1,28}(?:\\s|$))|(.{0,28})", Pattern.DOTALL);
                        Matcher m = regex.matcher(text);
                        while (m.find()) {
                            if (m.end() != text.length() || m.group().length() != 0)
                                lore.add(Arconix.pl().format().formatText("&7" + m.group()));
                        }
                    }
                    meta.setLore(lore);
                    item.setItemMeta(meta);

                    i.setItem(num, item);
                    num++;
                }
                total++;
            }
        }


        int nu = 45;
        while (nu != 54) {
            i.setItem(nu, Methods.getGlass());
            nu++;
        }


        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
        ItemStack skull = head;
        if (!plugin.v1_7)
            skull = Arconix.pl().getGUI().addTexture(head, "http://textures.minecraft.net/texture/1b6f1a25b6bc199946472aedb370522584ff6f4e83221e5946bd2e41b5ca13b");
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        if (plugin.v1_7)
            skullMeta.setOwner("MHF_ArrowRight");
        skull.setDurability((short) 3);
        skullMeta.setDisplayName(Lang.NEXT.getConfigValue());
        skull.setItemMeta(skullMeta);

        ItemStack head2 = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
        ItemStack skull2 = head2;
        if (!plugin.v1_7)
            skull2 = Arconix.pl().getGUI().addTexture(head2, "http://textures.minecraft.net/texture/3ebf907494a935e955bfcadab81beafb90fb9be49c7026ba97d798d5f1a23");
        SkullMeta skull2Meta = (SkullMeta) skull2.getItemMeta();
        if (plugin.v1_7)
            skull2Meta.setOwner("MHF_ArrowLeft");
        skull2.setDurability((short) 3);
        skull2Meta.setDisplayName(Lang.BACK.getConfigValue());
        skull2.setItemMeta(skull2Meta);

        if (pmin != 1) {
            i.setItem(46, skull2);
        }
        if (pmin == 1) {
            i.setItem(52, skull);
        }

        p.openInventory(i);
    }

    public void updateSettings() {
        for (settings s : settings.values()) {
            if (plugin.getConfig().contains("settings." + s.oldSetting)) {
                plugin.getConfig().addDefault(s.setting, plugin.getConfig().get("settings." + s.oldSetting));
                plugin.getConfig().set("settings." + s.oldSetting, null);
            } else if (s.setting.equals("Main.Upgrade Particle Type")) {
                if (plugin.v1_7 || plugin.v1_8)
                    plugin.getConfig().addDefault(s.setting, "WITCH_MAGIC");
                else
                    plugin.getConfig().addDefault(s.setting, s.option);
            } else
                plugin.getConfig().addDefault(s.setting, s.option);
        }
        ConfigurationSection cs = plugin.getConfig().getConfigurationSection("settings");
        for (String key : cs.getKeys(true)) {
            plugin.getConfig().set("settings." + key, null);
        }
    }

    public static boolean contains(String test) {
        for (settings c : settings.values()) {
            if (c.setting.equals(test)) {
                return true;
            }
        }
        return false;
    }

    public enum settings {
        o1("Spawner-max", "Main.Spawner Max Upgrade", 5),
        o58("Name-format", "Main.Spawner Name Format", "&e{TYPE} &fSpawner [&c{AMT}x]"),
        o2("Force-Combine-Radius", "Main.Force Combine Radius", 0),
        o49("Force-Combine-Deny", "Main.Deny Place On Force Combine", false),
        o3("Search-Radius", "Main.Radius To Search Around Spawner", "8x4x8"),
        o10("Alter-Delay", "Main.Default Minecraft Spawner Cooldowns", true),
        o17("Alert-place-break", "Main.Alerts On Place And Break", true),
        o18("Sneak-for-stack", "Main.Sneak To Recive A Stacked Spawner", true),
        o32("Spawner-holograms", "Main.Spawners Have Holograms", true),
        o4("Only-drop-placed", "Main.Only Drop Placed Spawners", false),
        o5("Only-charge-natural", "Main.Only Charge Natural Spawners", false),
        o43("Random-Low & Random-High", "Main.Random Amount Added To Each Spawn", "1:3"), //                                                 (Done)
        o63("Sounds", "Main.Sounds Enabled", true),
        o23("Display-Level-One", "Main.Display Level In Spawner Title If Level 1", false),
        o19("OmniSpawners", "Main.OmniSpawners Enabled", true),
        o21("Omni-Limit", "Main.Max Spawners Inside A OmniSpawner", 3),
        o215("Eggs-convert-spawners", "Main.Convert Spawners With Eggs", true),
        o42("Helpful-Tips", "Main.Display Helpful Tips For Operators", true),
        o33("Upgrade-with-eco", "Main.Upgrade With Economy", true),
        o34("Upgrade-with-xp", "Main.Upgrade With XP", true),
        o35("Upgrade-xp-cost", "Main.Cost To Upgrade With XP", 50),
        o36("Upgrade-eco-cost", "Main.Cost To Upgrade With Economy", 10000),
        o55("Use-equations", "Main.Use Custom Equations for Upgrade Costs", false),
        o62("spawners-repel-radius", "Main.Spawner Repel Liquid Radius", 1),          //                                                     (Done)
        o60("redstone-activate", "Main.Redstone Power Deactivates Spawners", true),
        o51("Max-Entities-Around-Single-Spawner", "Main.Max Entities Around Single Spawner", 6),
        o523("How-to", "Main.Display Help Button In Spawner Overview", true),
        o24("Inventory-Stacking", "Main.Allow Stacking Spawners In Survival Inventories", true),
        o27("Spawners-dont-explode", "Main.Prevent Spawners From Exploding", false),
        o53("Add-Spawner-To-Inventory-On-Drop", "Main.Add Spawner To Inventory On Drop", false),
        o54("Upgrade-particle-type", "Main.Upgrade Particle Type", "SPELL_WITCH"),

        o56("XP-cost-equation", "Main.Equations.Calculate XP Upgrade Cost", "{XPCost} * {Level}"),
        o57("ECO-cost-equation", "Main.Equations.Calculate Economy Upgrade Cost", "{ECOCost} * {Level}"),
        o554("Spawner-Spawn-Equation", "Main.Equations.Mobs Spawned Per Spawn", "{MULTI} + {RAND}"),
        o6("Spawner-Rate-Equation", "Main.Equations.Cooldown Between Spawns", "{DEFAULT} / {MULTI}"),


        o81("Boost-Multiplier", "Spawner Boosting.Boost Multiplier", "0.5"),
        o82("Max-Player-Boost", "Spawner Boosting.Max Multiplier For A Spawner Boost", 5),
        o83("Boost-cost", "Spawner Boosting.Item Charged For A Boost", "DIAMOND:2"),


        o30("Hostile-mobs-attack-second", "Entity.Hostile Mobs Attack Second", false),
        o50("SpawnEffect", "Entity.Spawn Particle Effect", "EXPLOSION_NORMAL"),
        o52("Large-Entity-Safe-Spawning", "Entity.Use Default Minecraft Spawn Method For Large Entities", true),


        o41("Only-drop-stacked", "Spawner Drops.Only Drop Stacked Spawners", false),
        o31("Mob-kill-counting", "Spawner Drops.Allow Killing Mobs To Drop Spawners", true),
        o40("Count-unnatural-kills", "Spawner Drops.Count Unnatural Kills Towards Spawner Drop", false),
        o623("Goal", "Spawner Drops.Kills Needed for Drop", 100),
        o7("Alert-every", "Spawner Drops.Alert Every X Before Drop", 10),
        o25("Drop-on-creeper-explosion", "Spawner Drops.Drop On Creeper Explosion", true),
        o26("Drop-on-tnt-explosion", "Spawner Drops.Drop On TNT Explosion", true),
        o28("Tnt-explosion-drop-chance", "Spawner Drops.Chance On TNT Explosion", "100%"),
        o29("Creeper-explosion-drop-chance", "Spawner Drops.Chance On Creeper Explosion", "100%"),
        o13("Silktouch-spawners", "Spawner Drops.Drop On SilkTouch", true),
        o14("Silktouch-natural-drop-chance", "Spawner Drops.Chance On Natural Silktouch", "100%"),
        o15("Silktouch-placed-drop-chance", "Spawner Drops.Chance On Placed Silktouch", "100%"),

        o8("Exit-Icon", "Interfaces.Exit Icon", "WOOD_DOOR"),
        o9("Buy-Icon", "Interfaces.Buy Icon", "EMERALD"),
        o37("ECO-Icon", "Interfaces.Economy Icon", "DOUBLE_PLANT"),
        o39("XP-Icon", "Interfaces.XP Icon", "EXP_BOTTLE"),
        o11("Glass-Type-1", "Interfaces.Glass Type 1", 7),
        o112("Glass-Type-2", "Interfaces.Glass Type 2", 11),
        o113("Glass-Type-3", "Interfaces.Glass Type 3", 3),
        o12("Rainbow-Glass", "Interfaces.Replace Glass Type 1 With Rainbow Glass", false),

        o45("Thin-Entity-data", "System.Remove Dead Entities from Data File", true),
        o48("Debug-Mode", "System.Debugger Enabled", false);

        private String setting;
        private String oldSetting;
        private Object option;

        private settings(String oldSetting, String setting, Object option) {
            this.oldSetting = oldSetting;
            this.setting = setting;
            this.option = option;
        }

    }
}
