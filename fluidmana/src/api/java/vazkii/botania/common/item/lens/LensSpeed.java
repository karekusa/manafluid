/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 24, 2015, 4:32:47 PM (GMT)]
 */
package vazkii.botania.common.item.lens;

import net.minecraft.item.ItemStack;
import vazkii.botania.api.mana.BurstProperties;

public class LensSpeed extends Lens {

	@Override
	public void apply(ItemStack stack, BurstProperties props) {
		props.motionModifier *= 2F;
		props.maxMana *= 0.75F;
		props.ticksBeforeManaLoss /= 3F;
		props.manaLossPerTick *= 2F;
	}

}
