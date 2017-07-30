/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jun 11, 2014, 1:16:59 AM (GMT)]
 */
package vazkii.botania.common.item.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.block.decor.slabs.BlockModSlab;

public class ItemBlockModSlab extends ItemSlab {

	public ItemBlockModSlab(Block par1) {
		super(par1, ((BlockModSlab)par1).getSingleBlock(), ((BlockModSlab)par1).getFullBlock(), false);
	}

	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		return field_150939_a.getUnlocalizedName().replaceAll("tile.", "tile.botania:");
	}

}
