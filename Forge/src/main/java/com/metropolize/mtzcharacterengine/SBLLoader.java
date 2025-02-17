package com.metropolize.mtzcharacterengine;

import com.metropolize.mtzcharacterengine.api.core.sensor.ExtendedSensor;
import com.mojang.serialization.Codec;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public interface SBLLoader {
	void init();
	boolean isDevEnv();

	<T> Supplier<MemoryModuleType<T>> registerMemoryType(String id);
	<T> Supplier<MemoryModuleType<T>> registerMemoryType(String id, @Nullable Codec<T> codec);

	<T extends ExtendedSensor<?>> Supplier<SensorType<T>> registerSensorType(String id, Supplier<T> sensor);
}
