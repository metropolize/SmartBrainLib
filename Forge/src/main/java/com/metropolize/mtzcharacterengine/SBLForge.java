package com.metropolize.mtzcharacterengine;

import com.metropolize.mtzcharacterengine.api.core.sensor.ExtendedSensor;
import com.metropolize.mtzcharacterengine.registry.SBLMemoryTypes;
import com.metropolize.mtzcharacterengine.registry.SBLSensors;
import com.mojang.serialization.Codec;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;

public final class SBLForge implements SBLLoader {
	public static final DeferredRegister<MemoryModuleType<?>> MEMORY_TYPES = DeferredRegister.create(ForgeRegistries.Keys.MEMORY_MODULE_TYPES, SBLConstants.MOD_ID);
	public static final DeferredRegister<SensorType<?>> SENSORS = DeferredRegister.create(ForgeRegistries.Keys.SENSOR_TYPES, SBLConstants.MOD_ID);

	public void init() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		MEMORY_TYPES.register(modEventBus);
		SENSORS.register(modEventBus);

		SBLMemoryTypes.init();
		SBLSensors.init();
	}

	@Override
	public boolean isDevEnv() {
		return !FMLLoader.isProduction();
	}

	@Override
	public <T> Supplier<MemoryModuleType<T>> registerMemoryType(String id) {
		return registerMemoryType(id, null);
	}

	@Override
	public <T> Supplier<MemoryModuleType<T>> registerMemoryType(String id, @Nullable Codec<T> codec) {
		return MEMORY_TYPES.register(id, () -> new MemoryModuleType<T>(Optional.ofNullable(codec)));
	}

	@Override
	public <T extends ExtendedSensor<?>> Supplier<SensorType<T>> registerSensorType(String id, Supplier<T> sensor) {
		return SENSORS.register(id, () -> new SensorType<>(sensor));
	}
}
