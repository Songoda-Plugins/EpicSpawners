package com.songoda.epicspawners.spawners.condition;

import com.songoda.epicspawners.EpicSpawners;
import com.songoda.epicspawners.api.spawners.condition.SpawnCondition;
import com.songoda.epicspawners.api.spawners.spawner.PlacedSpawner;
import org.bukkit.Location;

public class SpawnConditionLightDark implements SpawnCondition {
    private final Type lightDark;

    public SpawnConditionLightDark(Type lightDark) {
        this.lightDark = lightDark;
    }

    @Override
    public String getName() {
        return "lightdark";
    }

    @Override
    public String getDescription() {
        switch (this.lightDark) {
            case LIGHT:
                return EpicSpawners.getInstance().getLocale().getMessage("interface.spawner.conditionLight").toString();
            case DARK:
                return EpicSpawners.getInstance().getLocale().getMessage("interface.spawner.conditionDark").toString();
            default:
                return null;
        }
    }

    @Override
    public boolean isMet(PlacedSpawner spawner) {
        Location location = spawner.getLocation();
        switch (this.lightDark) {
            case LIGHT:
                return !isDark(location);
            case DARK:
                return isDark(location);
        }
        return true;
    }

    public boolean isDark(Location location) {
        return location.getBlock().getLightLevel() <= 7;
    }

    public Type getType() {
        return this.lightDark;
    }

    public enum Type {LIGHT, DARK, BOTH}
}
