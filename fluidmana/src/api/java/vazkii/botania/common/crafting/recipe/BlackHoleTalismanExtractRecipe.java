/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Apr 9, 2015, 7:48:00 PM (GMT)]
 */
package vazkii.botania.common.crafting.recipe;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import vazkii.botania.common.item.ItemBlackHoleTalisman;
import vazkii.botania.common.item.ModItems;

public class BlackHoleTalismanExtractRecipe implements IRecipe {

	@Override
	public boolean matches(InventoryCrafting var1, World var2) {
		boolean foundTalisman = false;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(stack != null) {
				if(stack.getItem() == ModItems.blackHoleTalisman && !foundTalisman)
					foundTalisman = true;
				else return false;
			}
		}

		return foundTalisman;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting var1) {
		ItemStack talisman = null;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(stack != null)
				talisman = stack;
		}

		int count = ItemBlackHoleTalisman.getBlockCount(talisman);
		if(count > 0) {
			Block block = ItemBlackHoleTalisman.getBlock(talisman);
			int meta = ItemBlackHoleTalisman.getBlockMeta(talisman);
			return new ItemStack(block, Math.min(64, count), meta);
		}

		return null;
	}

	@Override
	public int getRecipeSize() {
		return 10;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return null;
	}

}
