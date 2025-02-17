package com.metropolize.mtzcharacterengine.api.core.sensor.custom;

import com.metropolize.mtzcharacterengine.api.core.sensor.ExtendedSensor;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import com.metropolize.mtzcharacterengine.registry.SBLMemoryTypes;
import com.metropolize.mtzcharacterengine.registry.SBLSensors;
import com.metropolize.mtzcharacterengine.util.BrainUtils;

import java.util.List;

/**
 * A sensor to attempt to track whether the entity's target is currently obstructed either by blocks or a wall/tower of blocks. <br>
 * This is differentiated from {@link MemoryModuleType#CANT_REACH_WALK_TARGET_SINCE} in that it only stores state if the entity is actively blocked, and not just completing a previous path that may have been blocked. <br>
 * The contract of the memory (when this sensor is used) is as follows:<br>
 * <ul>
 *     <li>If not present: <i>entity is not blocked</i></li>
 *     <li>If false: <i>entity is blocked at a similar or lower y-coordinate (wall-blocked)</i></li>
 *     <li>If true: <i>entity is blocked at a higher y-coordinate (target has towered up, or is on cliff)</i></li>
 * </ul>
 * @param <E> The entity
 */
public class UnreachableTargetSensor<E extends LivingEntity> extends ExtendedSensor<E> {
	private static final List<MemoryModuleType<?>> MEMORIES = ObjectArrayList.of(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.ATTACK_TARGET, SBLMemoryTypes.TARGET_UNREACHABLE.get());

	protected long lastUnpathableTime = 0;

	@Override
	public List<MemoryModuleType<?>> memoriesUsed() {
		return MEMORIES;
	}

	@Override
	public SensorType<? extends ExtendedSensor<?>> type() {
		return SBLSensors.UNREACHABLE_TARGET.get();
	}

	@Override
	protected void doTick(ServerLevel level, E entity) {
		Brain<?> brain = entity.getBrain();
		LivingEntity target = BrainUtils.getTargetOfEntity(entity);

		if (target == null) {
			resetState(brain);

			return;
		}

		Long unpathableTime = BrainUtils.getMemory(brain, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);

		if (unpathableTime == null) {
			resetState(brain);

			return;
		}

		if (this.lastUnpathableTime == 0) {
			this.lastUnpathableTime = unpathableTime;
		}
		else if (this.lastUnpathableTime == unpathableTime) {
			BrainUtils.clearMemory(brain, SBLMemoryTypes.TARGET_UNREACHABLE.get());
		}
		else if (this.lastUnpathableTime < unpathableTime) {
			this.lastUnpathableTime = unpathableTime;

			BrainUtils.setMemory(brain, SBLMemoryTypes.TARGET_UNREACHABLE.get(), target.getY() > entity.getEyeY());
		}
	}

	private void resetState(Brain<?> brain) {
		if (this.lastUnpathableTime > 0)
			BrainUtils.clearMemory(brain, SBLMemoryTypes.TARGET_UNREACHABLE.get());

		this.lastUnpathableTime = 0;
	}
}
