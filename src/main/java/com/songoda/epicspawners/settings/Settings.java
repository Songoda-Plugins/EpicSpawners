package com.songoda.epicspawners.settings;

import com.songoda.core.compatibility.CompatibleMaterial;
import com.songoda.core.configuration.Config;
import com.songoda.core.configuration.ConfigSetting;
import com.songoda.core.hooks.EconomyManager;
import com.songoda.core.hooks.HologramManager;
import com.songoda.epicspawners.EpicSpawners;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Settings {

    private static final Config config = EpicSpawners.getInstance().getCoreConfig();

    public static final ConfigSetting SPAWNERS_MAX = new ConfigSetting(config, "Main.Spawner Max Upgrade", 5,
            "The maximum level a spawner can be upgraded to.");

    public static final ConfigSetting NAME_FORMAT = new ConfigSetting(config, "Main.Spawner Name Format", "&e{TYPE} &fSpawner [&c{AMT}x]",
            "The text displayed in the hologram positioned above every spawner.");

    public static final ConfigSetting FORCE_COMBINE_RADIUS = new ConfigSetting(config, "Main.Force Combine Radius", 0,
            "Spawners placed next to each other within this radius will automatically",
            "combine with each other.");

    public static final ConfigSetting FORCE_COMBINE_DENY = new ConfigSetting(config, "Main.Deny Place On Force Combine", false,
            "Prevent spawners from being placed next to each other within the specified radius.");

    public static final ConfigSetting SEARCH_RADIUS = new ConfigSetting(config, "Main.Radius To Search Around Spawners", "8x4x8",
            "The radius checked around a spawner before spawning entities.",
            "By default this is used to make sure there are no more than 7 entities",
            "around any single spawner.");

    public static final ConfigSetting ALERT_PLACE_BREAK = new ConfigSetting(config, "Main.Alerts On Place And Break", true,
            "Toggle an alerting chat message after triggered by placing or breaking a spawner.");

    public static final ConfigSetting SNEAK_FOR_STACK = new ConfigSetting(config, "Main.Sneak To Receive A Stacked Spawner", true,
            "Toggle ability to receive a stacked spawner when breaking a spawner while sneaking.");

    public static final ConfigSetting SPAWNER_HOLOGRAMS = new ConfigSetting(config, "Main.Spawners Have Holograms", true,
            "Toggle holograms showing above spawners.");

    public static final ConfigSetting ONLY_DROP_PLACED = new ConfigSetting(config, "Main.Only Drop Placed Spawner", false,
            "Should natural mob spawners drop upon being broken?");

    public static final ConfigSetting ONLY_CHARGE_NATURAL = new ConfigSetting(config, "Main.Only Charge Natural Spawners", false,
            "Should map generated spawners charge a price in order to be broken?",
            "You can configure the cost for each spawner type in the Spawners.yml.");

    public static final ConfigSetting CUSTOM_SPAWNER_TICK_RATE = new ConfigSetting(config, "Main.Custom Spawner Tick Rate", 10,
            "The tick rate in which spawners will attempt to spawn.",
            "Making this smaller or larger will not effect a spawners spawn rate as",
            "this value only effects the frequency in which a spawn attempt is triggered.");

    public static final ConfigSetting SOUNDS_ENABLED = new ConfigSetting(config, "Main.Sounds Enabled", true,
            "Toggles various sound effects used throughout the plugin.");

    public static final ConfigSetting DISPLAY_LEVEL_ONE = new ConfigSetting(config, "Main.Display Level In Spawner Title If Level 1", false,
            "Should a spawners hologram display its level if it's level one?");

    public static final ConfigSetting OMNI_SPAWNERS = new ConfigSetting(config, "Main.OmniSpawners Enabled", true,
            "Should spawners of different mob types be stackable into a single spawner?");

    public static final ConfigSetting EGGS_CONVERT_SPAWNERS = new ConfigSetting(config, "Main.Convert Spawners With Eggs", true,
            "Ability to change mob spawner type with spawn eggs.");

    public static final ConfigSetting UPGRADE_WITH_ECO_ENABLED = new ConfigSetting(config, "Main.Upgrade With Economy", true,
            "Can spawners be upgraded with money?");

    public static final ConfigSetting UPGRADE_WITH_XP_ENABLED = new ConfigSetting(config, "Main.Upgrade With XP", true,
            "Can spawners be upgraded with XP levels?");

    public static final ConfigSetting UPGRADE_COST_ECONOMY = new ConfigSetting(config, "Main.Cost To Upgrade With Economy", 10000,
            "Cost to upgrade a spawners level.");

    public static final ConfigSetting UPGRADE_COST_EXPERIANCE = new ConfigSetting(config, "Main.Cost To Upgrade With XP", 50,
            "Experience cost to upgrade a spawners level.");

    public static final ConfigSetting USE_CUSTOM_UPGRADE_EQUATION = new ConfigSetting(config, "Main.Use Custom Equations for Upgrade Costs", false,
            "Should custom equations be used to generate upgrade costs?");

    public static final ConfigSetting LIQUID_REPEL_RADIUS = new ConfigSetting(config, "Main.Spawner Repel Liquid Radius", 1,
            "Prevent water from flowing next to or on top of a spawner within the here declared radius.",
            "Set to 0 to disable.");

    public static final ConfigSetting REDSTONE_ACTIVATE = new ConfigSetting(config, "Main.Redstone Power Deactivates Spawners", true,
            "Does redstone power disable a spawner?");

    public static final ConfigSetting DISPLAY_HELP_BUTTON = new ConfigSetting(config, "Main.Display Help Button In Spawner Overview", true,
            "should the button be visible in each spawners overview GUI.");

    public static final ConfigSetting SPAWNERS_DONT_EXPLODE = new ConfigSetting(config, "Main.Prevent Spawners From Exploding", false,
            "Should spawners not break when blown up?");

    public static final ConfigSetting SPAWNERS_TO_INVENTORY = new ConfigSetting(config, "Main.Add Spawners To Inventory On Drop", false,
            "Should broken spawners be added directly to the players inventory?",
            "Alternatively they will drop to the ground?");

    public static final ConfigSetting UPGRADE_PARTICLE_TYPE = new ConfigSetting(config, "Main.Upgrade Particle Type", "SPELL_WITCH",
            "The name of the particle shown when upgrading a spawner.");

    public static final ConfigSetting EXTRA_SPAWN_TICKS = new ConfigSetting(config, "Main.Extra Ticks Added To Each Spawn", 0,
            "After every spawner successfully spawns, a new delay is added to it.",
            "That delay is different for every spawner type and can be configured in the Spawners.yml",
            "The number configured here is then added to that delay.");

    public static final ConfigSetting MAX_SPAWNERS = new ConfigSetting(config, "Main.Max Spawners Per Player", -1,
            "The maximum amount of spawners a player can place. Set to -1 to allow unlimited",
            "spawner placement.");

    public static final ConfigSetting AUTOSAVE = new ConfigSetting(config, "Main.Auto Save Interval In Seconds", 15,
            "The amount of time in between saving to file.",
            "This is purely a safety function to prevent against unplanned crashes or",
            "restarts. With that said it is advised to keep this enabled.",
            "If however you enjoy living on the edge, feel free to turn it off.");

    public static final ConfigSetting DISABLE_NATURAL_SPAWNERS = new ConfigSetting(config, "Main.Disable Natural Spawners", false,
            "Should natural spawners be disabled?");

    public static final ConfigSetting NO_AI = new ConfigSetting(config, "Main.Nerf Spawner Mobs", false,
            "If enabled mobs spawned by spawners will not move or attack.");

    public static final ConfigSetting COST_EQUATION_EXPERIANCE = new ConfigSetting(config, "Main.Equations.Calculate XP Upgrade Cost", "{XPCost} * {Level}",
            "The equation used to calculate the experience upgrade cost.");

    public static final ConfigSetting COST_EQUATION_ECONOMY = new ConfigSetting(config, "Main.Equations.Calculate Economy Upgrade Cost", "{ECOCost} * {Level}",
            "The equation used to calculate the economy upgrade cost.");

    public static final ConfigSetting SPAWNER_SPAWN_EQUATION = new ConfigSetting(config, "Main.Equations.Mobs Spawned Per Single Spawn", "{RAND}",
            "The equation that defines the amount of mobs a spawner will spawn each time it is triggered.",
            "This is ran once for each spawner in the stack then summed up after. You may use the variable {STACK_SIZE}",
            "or {RAND} If you like. For more information about how to make equations for this option look up the",
            "Java ScriptEngine.");

    public static final ConfigSetting RANDOM_LOW_HIGH = new ConfigSetting(config, "Main.Equations.Random Amount Variable", "1:4",
            "This value depicts the variable {RAND} in equations used by this plugin",
            "It generates a random number between (by default) 1 and 4.");

    public static final ConfigSetting NAMED_SPAWNER_TIERS = new ConfigSetting(config, "Main.Named Spawners Tiers", false,
            "Whether or not spawners will have names rather than numbers.");

    public static final ConfigSetting TIER_NAMES = new ConfigSetting(config, "Main.Tier Names", Arrays.asList("&7Common", "&6Uncommon", "&4Rare", "&5Mythic"),
            "The names of each spawner tier.",
            "Where one spawner is common, two is uncommon, three is rare, and four is mythic.");

    public static final ConfigSetting ECONOMY_PLUGIN = new ConfigSetting(config, "Main.Economy", EconomyManager.getEconomy() == null ? "Vault" : EconomyManager.getEconomy().getName(),
            "Which economy plugin should be used?",
            "Supported plugins you have installed: \"" + EconomyManager.getManager().getRegisteredPlugins().stream().collect(Collectors.joining("\", \"")) + "\".");

    public static final ConfigSetting HOLOGRAM_PLUGIN = new ConfigSetting(config, "Main.Hologram",
            HologramManager.getHolograms() == null ? "HolographicDisplays" : HologramManager.getHolograms().getName(),
            "Which hologram plugin should be used?",
            "You can choose from \"" + HologramManager.getManager().getRegisteredPlugins().stream().collect(Collectors.joining(", ")) + "\".");

    public static final ConfigSetting CHARGE_FOR_CREATIVE = new ConfigSetting(config, "Main.Charge For Creative", false,
            "Should players in creative have to pay for perks like upgrades and boosting?");

    public static final ConfigSetting MAX_PLAYER_BOOST = new ConfigSetting(config, "Spawner Boosting.Max Multiplier For A Spawner Boost", 5,
            "The highest multiplier a spawner can be boosted to.");

    public static final ConfigSetting ALWAYS_REMEMBER_PLACER = new ConfigSetting(config, "Main.Always Remember Placer", true,
            "Should the person who placed a spawner be remembered under every",
            "circumstance? Disabling this can greatly decrease save file size.");

    public static final ConfigSetting BOOST_COST = new ConfigSetting(config, "Spawner Boosting.Item Charged For A Boost", "DIAMOND:2",
            "The cost required when a player boosts their own spawner.",
            "If you would rather charge experience or economy then enter respectively",
            "ECO or XP in place of the default DIAMOND.");

    public static final ConfigSetting HOSTILE_MOBS_ATTACK_SECOND = new ConfigSetting(config, "entity.Hostile Mobs Attack Second", false,
            "Should hostile mobs attack only if attacked first?");

    public static final ConfigSetting ONLY_DROP_STACKED = new ConfigSetting(config, "Spawner Drops.Only Drop Stacked Spawners", false,
            "Should stacked spawners always drop their whole stack when broken?");

    public static final ConfigSetting MOB_KILLING_COUNT = new ConfigSetting(config, "Spawner Drops.Allow Killing Mobs To Drop Spawners", true,
            "Should spawners drop when enough mobs of that spawners type are killed?");

    public static final ConfigSetting COUNT_UNNATURAL_KILLS = new ConfigSetting(config, "Spawner Drops.Count Unnatural Kills Towards Spawner Drop", false,
            "Can mobs from spawners count towards the spawner drop count?");

    public static final ConfigSetting KILL_GOAL = new ConfigSetting(config, "Spawner Drops.Kills Needed for Drop", 100,
            "Amount of mob kills required to drop a spawner.");

    public static final ConfigSetting ALERT_INTERVAL = new ConfigSetting(config, "Spawner Drops.Alert Every X Before Drop", 10,
            "Alert players every x amount of kills before dropping spawner.");

    public static final ConfigSetting EXPLOSION_DROP_CHANCE_TNT = new ConfigSetting(config, "Spawner Drops.Chance On TNT Explosion", "100%",
            "Chance of a TNT explosion dropping a spawner.");

    public static final ConfigSetting EXPLOSION_DROP_CHANCE_CREEPER = new ConfigSetting(config, "Spawner Drops.Chance On Creeper Explosion", "100%",
            "Chance of a creeper explosion dropping a spawner.");

    public static final ConfigSetting SILKTOUCH_SPAWNERS = new ConfigSetting(config, "Spawner Drops.Drop On SilkTouch", true,
            "Do spawners drop when broken with a pick enchanted with silk touch?");

    public static final ConfigSetting SILKTOUCH_MIN_LEVEL = new ConfigSetting(config, "Spawner Drops.Minimum Required Silktouch Level", 1,
            "What level of silk touch is required to drop a spawner?");

    public static final ConfigSetting SILKTOUCH_NATURAL_SPAWNER_DROP_CHANCE = new ConfigSetting(config, "Spawner Drops.Chance On Natural Silktouch", "100%",
            "Chance of a natural spawner dropping with silk touch.");

    public static final ConfigSetting SILKTOUCH_PLACED_SPAWNER_DROP_CHANCE = new ConfigSetting(config, "Spawner Drops.Chance On Placed Silktouch", "100%",
            "Chance of a placed spawner dropping with silk touch.");

    public static final ConfigSetting EXIT_ICON = new ConfigSetting(config, "Interfaces.Exit Icon", CompatibleMaterial.OAK_DOOR.getMaterial().name(),
            "Item to be displayed as the icon for exiting the interface.");

    public static final ConfigSetting BUY_ICON = new ConfigSetting(config, "Interfaces.Buy Icon", "EMERALD",
            "Item to be displayed as the icon for buying a spawner.");

    public static final ConfigSetting ECO_ICON = new ConfigSetting(config, "Interfaces.Economy Icon", CompatibleMaterial.SUNFLOWER.getMaterial().name(),
            "Item to be displayed as the icon for economy upgrades.");

    public static final ConfigSetting XP_ICON = new ConfigSetting(config, "Interfaces.XP Icon", CompatibleMaterial.EXPERIENCE_BOTTLE.getMaterial().name(),
            "Item to be displayed as the icon for XP upgrades.");

    public static final ConfigSetting CONVERT_ICON = new ConfigSetting(config, "Interfaces.Convert Icon", "EGG");

    public static final ConfigSetting BOOST_ICON = new ConfigSetting(config, "Interfaces.Boost Icon", "BLAZE_POWDER");

    public static final ConfigSetting GLASS_TYPE_1 = new ConfigSetting(config, "Interfaces.Glass Type 1", 7);
    public static final ConfigSetting GLASS_TYPE_2 = new ConfigSetting(config, "Interfaces.Glass Type 2", 11);
    public static final ConfigSetting GLASS_TYPE_3 = new ConfigSetting(config, "Interfaces.Glass Type 3", 3);
    public static final ConfigSetting RAINBOW_GLASS = new ConfigSetting(config, "Interfaces.Replace Glass Type 1 With Rainbow Glass", false);

    public static final ConfigSetting LANGUGE_MODE = new ConfigSetting(config, "System.Language Mode", "en_US",
            "The enabled language file.",
            "More language files (if available) can be found in the plugins data folder.");

    /**
     * In order to set dynamic economy comment correctly, this needs to be
     * called after EconomyManager load
     */
    public static void setupConfig() {
        config.load();
        config.setAutoremove(true).setAutosave(true);

        // convert economy settings
        if (config.getBoolean("Economy.Use Vault Economy") && EconomyManager.getManager().isEnabled("Vault")) {
            config.set("Main.Economy", "Vault");
        } else if (config.getBoolean("Economy.Use Reserve Economy") && EconomyManager.getManager().isEnabled("Reserve")) {
            config.set("Main.Economy", "Reserve");
        } else if (config.getBoolean("Economy.Use Player Points Economy") && EconomyManager.getManager().isEnabled("PlayerPoints")) {
            config.set("Main.Economy", "PlayerPoints");
        }

        config.saveChanges();
    }
}
