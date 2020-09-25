package com.songoda.epicspawners.spawners.spawner;

import com.google.common.base.Preconditions;
import com.songoda.core.compatibility.ServerVersion;
import com.songoda.core.nms.NmsManager;
import com.songoda.core.nms.nbt.NBTItem;
import com.songoda.core.utils.TextUtils;
import com.songoda.epicspawners.particles.ParticleDensity;
import com.songoda.epicspawners.particles.ParticleEffect;
import com.songoda.epicspawners.particles.ParticleType;
import com.songoda.epicspawners.settings.Settings;
import com.songoda.epicspawners.spawners.condition.SpawnCondition;
import com.songoda.epicspawners.spawners.condition.SpawnConditionBiome;
import com.songoda.epicspawners.spawners.condition.SpawnConditionHeight;
import com.songoda.epicspawners.spawners.condition.SpawnConditionLightDark;
import com.songoda.epicspawners.spawners.condition.SpawnConditionNearbyEntities;
import com.songoda.epicspawners.spawners.condition.SpawnConditionNearbyPlayers;
import com.songoda.epicspawners.spawners.condition.SpawnConditionStorm;
import com.songoda.epicspawners.spawners.spawner.option.SpawnOption;
import com.songoda.epicspawners.spawners.spawner.option.SpawnOptionBlock;
import com.songoda.epicspawners.spawners.spawner.option.SpawnOptionCommand;
import com.songoda.epicspawners.spawners.spawner.option.SpawnOptionEntity_1_12;
import com.songoda.epicspawners.spawners.spawner.option.SpawnOptionEntity_1_13;
import com.songoda.epicspawners.spawners.spawner.option.SpawnOptionItem;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class SpawnerData {

    private final String name;
    private int uuid;

    private boolean custom = false;

    private double pickupCost = 0.0;
    private List<Material> spawnBlocks = Collections.singletonList(Material.AIR);
    private boolean active = true, inShop = true;
    private boolean spawnOnFire = false, upgradeable = true, convertible = true;
    private double shopPrice = 1000.0;
    private int shopOrder = 0;
    private String convertRatio = "45%";
    private double upgradeCostEconomy = 0.0;
    private int upgradeCostExperience = 0;
    private int killGoal = 0;
    private int spawnLimit = -1;
    private short pickDamage = 1;
    private String displayName;
    private Material displayItem = null;

    private boolean craftable = false;
    private String recipe = "AAAABAAAA";
    private List<String> recipeIngredients = Arrays.asList("A, IRON_BARS", "B, SPAWN_EGG");

    private String tickRate = "800:200";

    private ParticleEffect particleEffect = ParticleEffect.HALO;
    private ParticleType spawnEffectParticle = ParticleType.REDSTONE;
    private ParticleType entitySpawnParticle = ParticleType.SMOKE;
    private ParticleType spawnerSpawnParticle = ParticleType.FIRE;

    private ParticleDensity particleDensity = ParticleDensity.NORMAL;

    private boolean particleEffectBoostedOnly = true;

    private List<EntityType> entities;
    private List<Material> blocks;
    private List<ItemStack> items;
    private List<String> commands;

    private Set<SpawnOption> spawnOptions = new HashSet<>();

    private List<SpawnCondition> spawnConditions = new ArrayList<>();

    public SpawnerData(int uuid, String name, List<EntityType> entities, List<Material> blocks, List<ItemStack> items, List<String> commands) {
        Preconditions.checkNotNull(name, "Name cannot be null");

        this.uuid = uuid == 0 ? (new Random()).nextInt(9999) : uuid;
        this.name = name;
        this.displayName = name;

        this.entities = entities;
        this.blocks = blocks;
        this.items = items;
        this.commands = commands;
        reloadSpawnMethods();
    }

    public SpawnerData(String name) {
        this(0, name, Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
    }

    public void addDefaultConditions() {
        addCondition(new SpawnConditionNearbyPlayers(16, 1));
        addCondition(new SpawnConditionHeight(0, 265));
        addCondition(new SpawnConditionBiome(Biome.values()));
        addCondition(new SpawnConditionLightDark(SpawnConditionLightDark.Type.BOTH));
        addCondition(new SpawnConditionStorm(false));
        addCondition(new SpawnConditionNearbyEntities(6));
    }

    public void reloadSpawnMethods() {
        spawnOptions.clear();
        if (ServerVersion.isServerVersionAtLeast(ServerVersion.V1_13)) {
            if (!entities.isEmpty()) spawnOptions.add(new SpawnOptionEntity_1_13(entities));
        } else {
            if (!entities.isEmpty()) spawnOptions.add(new SpawnOptionEntity_1_12(entities));
        }
        if (!blocks.isEmpty()) spawnOptions.add(new SpawnOptionBlock(blocks));
        if (!items.isEmpty()) spawnOptions.add(new SpawnOptionItem(items));
        if (!commands.isEmpty()) spawnOptions.add(new SpawnOptionCommand(commands));
    }

    public void spawn(Spawner spawner, SpawnerStack stack) {
        for (SpawnOption spawnOption : spawnOptions) {
            spawnOption.spawn(this, stack, spawner);
        }
    }

    public int getUUID() {
        return uuid;
    }


    public void setUUID(int uuid) {
        this.uuid = uuid;
    }


    public ItemStack toItemStack() {
        return toItemStack(1);
    }


    public ItemStack toItemStack(int amount) {
        return toItemStack(amount, 1);
    }


    public ItemStack toItemStack(int amount, int stackSize) {
        Preconditions.checkArgument(stackSize >= 0, "Stack size must be greater than or equal to 0");

        ItemStack item = new ItemStack(ServerVersion.isServerVersionAtLeast(ServerVersion.V1_13) ? Material.SPAWNER : Material.valueOf("MOB_SPAWNER"), amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getCompiledDisplayName(stackSize));
        item.setItemMeta(meta);

        NBTItem nbtItem = NmsManager.getNbt().of(item);
        nbtItem.set("type", name);
        nbtItem.set("size", stackSize);
        return nbtItem.finish();
    }


    public String getIdentifyingName() {
        return name;
    }


    public double getPickupCost() {
        return pickupCost;
    }


    public void setPickupCost(double pickupCost) {
        this.pickupCost = pickupCost;
    }


    public Material[] getSpawnBlocks() {
        return spawnBlocks.toArray(new Material[spawnBlocks.size()]);
    }

    public void setSpawnBlocks(String[] spawnBlock) {
        this.spawnBlocks = new ArrayList<>();
        for (String block : spawnBlock) {
            if (block.toUpperCase().trim().equals("")) continue;
            this.spawnBlocks.add(Material.valueOf(block.toUpperCase().trim()));
        }
    }


    public void setSpawnBlocks(List<Material> spawnBlock) {
        this.spawnBlocks = spawnBlock;
    }


    public List<Material> getSpawnBlocksList() {
        return Collections.unmodifiableList(spawnBlocks);
    }


    public boolean isActive() {
        return active;
    }


    public void setActive(boolean active) {
        this.active = active;
    }


    public boolean isInShop() {
        return inShop;
    }


    public int getShopOrder() {
        return shopOrder;
    }


    public void setShopOrder(int slot) {
        this.shopOrder = slot;
    }


    public void setInShop(boolean inShop) {
        this.inShop = inShop;
    }


    public boolean isSpawnOnFire() {
        return spawnOnFire;
    }


    public void setSpawnOnFire(boolean spawnOnFire) {
        this.spawnOnFire = spawnOnFire;
    }


    public boolean isUpgradeable() {
        return upgradeable;
    }


    public void setUpgradeable(boolean upgradeable) {
        this.upgradeable = upgradeable;
    }


    public boolean isConvertible() {
        return convertible;
    }


    public void setConvertible(boolean convertible) {
        this.convertible = convertible;
    }


    public double getShopPrice() {
        return shopPrice;
    }


    public void setShopPrice(double shopPrice) {
        this.shopPrice = shopPrice;
    }


    public String getConvertRatio() {
        return convertRatio;
    }


    public void setConvertRatio(String convertRatio) {
        this.convertRatio = convertRatio;
    }


    public double getUpgradeCostEconomy() {
        return upgradeCostEconomy;
    }


    public void setUpgradeCostEconomy(double upgradeCostEconomy) {
        this.upgradeCostEconomy = upgradeCostEconomy;
    }


    public int getUpgradeCostExperience() {
        return upgradeCostExperience;
    }


    public void setUpgradeCostExperience(int upgradeCostExperience) {
        this.upgradeCostExperience = upgradeCostExperience;
    }


    public int getKillGoal() {
        return killGoal;
    }


    public void setKillGoal(int killGoal) {
        this.killGoal = killGoal;
    }


    public String getDisplayName() {
        return displayName;
    }

    public String getCompiledDisplayName() {
        return getCompiledDisplayName(1);
    }

    public String getCompiledDisplayName(int multi) {
        String nameFormat = Settings.NAME_FORMAT.getString();
        String displayName = getDisplayName();

        nameFormat = nameFormat.replace("{TYPE}", displayName);

        if ((multi > 1 || Settings.DISPLAY_LEVEL_ONE.getBoolean() || Settings.NAMED_SPAWNER_TIERS.getBoolean()) && multi >= 0) {
            if (Settings.NAMED_SPAWNER_TIERS.getBoolean() && Settings.TIER_NAMES.getStringList().size() >= multi) {
                nameFormat = nameFormat.replace("{AMT}", Settings.TIER_NAMES.getStringList().get(multi - 1));
            } else {
                nameFormat = nameFormat.replace("{AMT}", Integer.toString(multi));
            }
            nameFormat = nameFormat.replace("[", "").replace("]", "");
        } else {
            nameFormat = nameFormat.replaceAll("\\[.*?]", "");
        }

        return TextUtils.formatText(nameFormat).trim();
    }


    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }


    public Material getDisplayItem() {
        return displayItem == null ? Material.AIR : displayItem;
    }


    public void setDisplayItem(Material displayItem) {
        this.displayItem = displayItem;
    }


    public List<EntityType> getEntities() {
        return Collections.unmodifiableList(entities);
    }


    public void setEntities(List<EntityType> entities) {
        this.entities = entities;
    }


    public double getConvertPrice() {
        return (int) (shopPrice * (Double.valueOf(convertRatio.substring(0, convertRatio.length() - 1)) / 100.0f));
    }


    public List<Material> getBlocks() {
        return Collections.unmodifiableList(blocks);
    }


    public void setBlocks(List<Material> blocks) {
        this.blocks = blocks;
    }

    public List<ItemStack> getItems() {
        return Collections.unmodifiableList(items);
    }


    public void setItems(List<ItemStack> items) {
        this.items = items;
    }


    public List<String> getCommands() {
        return Collections.unmodifiableList(commands);
    }


    public void setCommands(List<String> commands) {
        this.commands = commands;
    }

    public short getPickDamage() {
        return pickDamage;
    }

    public void setPickDamage(short pickDamage) {
        this.pickDamage = pickDamage;
    }

    public String getTickRate() {
        return tickRate;
    }


    public void setTickRate(String tickRate) {
        this.tickRate = tickRate;
    }


    public ParticleEffect getParticleEffect() {
        return particleEffect;
    }


    public void setParticleEffect(ParticleEffect particleEffect) {
        this.particleEffect = particleEffect;
    }


    public ParticleType getSpawnEffectParticle() {
        return spawnEffectParticle;
    }


    public void setSpawnEffectParticle(ParticleType spawnEffectParticle) {
        this.spawnEffectParticle = spawnEffectParticle;
    }

    public void setSpawnLimit(int spawnLimit) {
        this.spawnLimit = spawnLimit;
    }

    public int getSpawnLimit() {
        return spawnLimit;
    }

    public ParticleType getEntitySpawnParticle() {
        return entitySpawnParticle;
    }


    public void setEntitySpawnParticle(ParticleType entitySpawnParticle) {
        this.entitySpawnParticle = entitySpawnParticle;
    }


    public ParticleType getSpawnerSpawnParticle() {
        return spawnerSpawnParticle;
    }


    public void setSpawnerSpawnParticle(ParticleType spawnerSpawnParticle) {
        this.spawnerSpawnParticle = spawnerSpawnParticle;
    }


    public ParticleDensity getParticleDensity() {
        return particleDensity;
    }


    public void setParticleDensity(ParticleDensity particleDensity) {
        this.particleDensity = particleDensity;
    }


    public boolean isParticleEffectBoostedOnly() {
        return particleEffectBoostedOnly;
    }


    public void setParticleEffectBoostedOnly(boolean particleEffectBoostedOnly) {
        this.particleEffectBoostedOnly = particleEffectBoostedOnly;
    }


    public boolean isCustom() {
        return custom;
    }


    public void setCustom(boolean custom) {
        this.custom = custom;
    }


    public void addCondition(SpawnCondition spawnCondition) {
        spawnConditions.add(spawnCondition);
    }


    public void removeCondition(SpawnCondition spawnCondition) {
        spawnConditions.remove(spawnCondition);
    }


    public List<SpawnCondition> getConditions() {
        return Collections.unmodifiableList(spawnConditions);
    }


    public boolean isCraftable() {
        return craftable;
    }


    public void setCraftable(boolean craftable) {
        this.craftable = craftable;
    }


    public String getRecipe() {
        return recipe;
    }


    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }


    public List<String> getRecipeIngredients() {
        return recipeIngredients;
    }


    public void setRecipeIngredients(List<String> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }


    public int hashCode() {
        return 31 * name.hashCode();
    }


    public boolean equals(Object object) {
        if (object == this) return true;
        if (!(object instanceof SpawnerData)) return false;

        SpawnerData other = (SpawnerData) object;
        return Objects.equals(name, other.name);
    }


    public String toString() {
        return "SpawnerData:{Name:\"" + name + "\"}";
    }

    public int getStackSize(ItemStack item) {
        Preconditions.checkNotNull(item, "Cannot get stack size of null item");
        NBTItem nbtItem = NmsManager.getNbt().of(item);

        if (nbtItem.has("size"))
            return nbtItem.getNBTObject("size").asInt();

        // Legacy
        if (!item.hasItemMeta() && !item.getItemMeta().hasDisplayName()) return 1;

        String name = item.getItemMeta().getDisplayName();
        if (!name.contains(":")) return 1;

        String amount = name.replace(String.valueOf(ChatColor.COLOR_CHAR), "").replace(";", "").split(":")[1];
        return NumberUtils.toInt(amount, 1);
    }
}
