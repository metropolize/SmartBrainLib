package com.metropolize.mtzcharacterengine.api.core.behaviour.custom.misc;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import com.metropolize.mtzcharacterengine.api.core.behaviour.DelayedBehaviour;
import com.metropolize.mtzcharacterengine.api.core.behaviour.ExtendedBehaviour;

import java.util.List;
import java.util.function.Predicate;

/**
 * A behaviour module that acts as a default implementation of {@link DelayedBehaviour}. <br>
 * Useful for handling custom minor actions that are either too specific to warrant a new behaviour, or not worth implementing into a full behaviour. <br>
 * Set the condition for running via {@link ExtendedBehaviour#startCondition(Predicate)}
 */
public final class CustomDelayedBehaviour<E extends LivingEntity> extends DelayedBehaviour<E> {
	public CustomDelayedBehaviour(int delayTicks) {
		super(delayTicks);
	}

	@Override
	protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
		return List.of();
	}
}
