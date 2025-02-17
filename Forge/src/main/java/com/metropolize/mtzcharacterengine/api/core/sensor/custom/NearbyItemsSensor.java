package com.metropolize.mtzcharacterengine.api.core.sensor.custom;

import com.metropolize.mtzcharacterengine.api.core.sensor.ExtendedSensor;
import com.metropolize.mtzcharacterengine.api.core.sensor.PredicateSensor;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import com.metropolize.mtzcharacterengine.object.SquareRadius;
import com.metropolize.mtzcharacterengine.registry.SBLMemoryTypes;
import com.metropolize.mtzcharacterengine.registry.SBLSensors;
import com.metropolize.mtzcharacterengine.util.BrainUtils;
import com.metropolize.mtzcharacterengine.util.EntityRetrievalUtil;

import java.util.List;

/**
 * A sensor that looks for nearby {@link ItemEntity items} in the surrounding area.<br>
 * Defaults:
 * <ul>
 * <li>32x16x32 radius</li>
 * <li>Only items that return true for {@link Mob#wantsToPickUp(ItemStack)}</li>
 * <li>Only items that return true for
 * {@link net.minecraft.world.entity.LivingEntity#hasLineOfSight(Entity)}</li>
 * </ul>
 *
 * @param <E> The entity
 */
public class NearbyItemsSensor<E extends Mob> extends PredicateSensor<ItemEntity, E> {
	private static final List<MemoryModuleType<?>> MEMORIES = ObjectArrayList.of(SBLMemoryTypes.NEARBY_ITEMS.get());

	protected SquareRadius radius = new SquareRadius(32, 16);

	public NearbyItemsSensor() {
		super((item, entity) -> entity.wantsToPickUp(item.getItem()) && entity.hasLineOfSight(item));
	}

	/**
	 * Set the radius for the item sensor to scan.
	 *
	 * @param radius The coordinate radius, in blocks
	 * @return this
	 */
	public NearbyItemsSensor<E> setRadius(double radius) {
		return setRadius(radius, radius);
	}

	/**
	 * Set the radius for the item sensor to scan.
	 *
	 * @param xz The X/Z coordinate radius, in blocks
	 * @param y  The Y coordinate radius, in blocks
	 * @return this
	 */
	public NearbyItemsSensor<E> setRadius(double xz, double y) {
		this.radius = new SquareRadius(xz, y);

		return this;
	}

	@Override
	public List<MemoryModuleType<?>> memoriesUsed() {
		return MEMORIES;
	}

	@Override
	public SensorType<? extends ExtendedSensor<?>> type() {
		return SBLSensors.NEARBY_ITEMS.get();
	}

	@Override
	protected void doTick(ServerLevel level, E entity) {
		BrainUtils.setMemory(entity, SBLMemoryTypes.NEARBY_ITEMS.get(), EntityRetrievalUtil.getEntities(level, this.radius.inflateAABB(entity.getBoundingBox()), obj -> obj instanceof ItemEntity item && predicate().test(item, entity)));
	}
}


