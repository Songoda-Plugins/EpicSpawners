package com.songoda.epicspawners.spawners.condition;

import com.songoda.epicspawners.EpicSpawners;
import com.songoda.epicspawners.api.spawners.condition.SpawnCondition;
import com.songoda.epicspawners.api.spawners.spawner.PlacedSpawner;

public class SpawnConditionHeight implements SpawnCondition {
    private final int min, max;

    public SpawnConditionHeight(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public String getName() {
        return "height";
    }

    @Override
    public String getDescription() {
        return EpicSpawners.getInstance().getLocale().getMessage("interface.spawner.conditionHeight")
                .processPlaceholder("min", this.min)
                .processPlaceholder("max", this.max)
                .toString();
    }

    @Override
    public boolean isMet(PlacedSpawner spawner) {
        double y = spawner.getLocation().getY();
        return y >= this.min && y <= this.max;
    }

    public int getMin() {
        return this.min;
    }

    public int getMax() {
        return this.max;
    }
}
