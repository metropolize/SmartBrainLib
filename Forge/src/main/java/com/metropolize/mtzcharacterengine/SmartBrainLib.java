package com.metropolize.mtzcharacterengine;

import net.minecraftforge.fml.common.Mod;

@Mod(SBLConstants.MOD_ID)
public class SmartBrainLib {
	public SmartBrainLib() {
		SBLConstants.SBL_LOADER.init();
	}
}
