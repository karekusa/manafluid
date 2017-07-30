/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Nov 1, 2014, 7:02:29 PM (GMT)]
 */
package vazkii.botania.common.brew;

import net.minecraft.potion.PotionEffect;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.brew.Brew;

public class BrewMod extends Brew {

	public BrewMod(String key, int color, int cost, PotionEffect... effects) {
		super(key, key, color, cost, effects);
		BotaniaAPI.registerBrew(this);
	}

	@Override
	public String getUnlocalizedName() {
		return "botania.brew." + super.getUnlocalizedName();
	}

}
