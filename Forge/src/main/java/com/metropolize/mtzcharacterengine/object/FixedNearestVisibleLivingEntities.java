package com.metropolize.mtzcharacterengine.object;

import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import com.metropolize.mtzcharacterengine.util.SensoryUtils;

import java.util.List;
import java.util.function.Predicate;

/**
 * Wrapper for {@link NearestVisibleLivingEntities} that supports follow range for entities rather than a hardcoded 16-block limit
 */
public class FixedNearestVisibleLivingEntities extends NearestVisibleLivingEntities {
    private FixedNearestVisibleLivingEntities() {
        super();
    }

    public FixedNearestVisibleLivingEntities(LivingEntity entity, List<LivingEntity> entities) {
        super();

        this.nearbyEntities = entities;
        this.lineOfSightTest = new Predicate<>() {
            final Object2BooleanOpenHashMap<LivingEntity> cache = new Object2BooleanOpenHashMap<>(entities.size());

            @Override
            public boolean test(LivingEntity target) {
                return this.cache.computeIfAbsent(target, (Predicate<LivingEntity>)target1 -> SensoryUtils.isEntityTargetable(entity, target1));
            }
        };
    }

    public static FixedNearestVisibleLivingEntities empty() {
        return new FixedNearestVisibleLivingEntities();
    }
}