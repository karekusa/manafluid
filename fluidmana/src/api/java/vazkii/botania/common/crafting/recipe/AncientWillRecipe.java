/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 30, 2015, 11:24:08 PM (GMT)]
 */
package vazkii.botania.common.crafting.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import vazkii.botania.api.item.IAncientWillContainer;
import vazkii.botania.common.item.ModItems;

public class AncientWillRecipe implements IRecipe {

	@Override
	public boolean matches(InventoryCrafting var1, World var2) {
		boolean foundWill = false;
		boolean foundItem = false;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(stack != null) {
				if(stack.getItem() == ModItems.ancientWill && !foundWill)
					foundWill = true;
				else if(!foundItem) {
					if(stack.getItem() instanceof IAncientWillContainer)
						foundItem = true;
					else return false;
				}
			}
		}

		return foundWill && foundItem;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting var1) {
		ItemStack item = null;
		int will = -1;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(stack != null) {
				if(stack.getItem() instanceof IAncientWillContainer && item == null)
					item = stack;
				else will = stack.getItemDamage();
			}
		}

		IAncientWillContainer container = (IAncientWillContainer) item.getItem();
		if(container.hasAncientWill(item, will))
			return null;

		ItemStack copy = item.copy();
		container.addAncientWill(copy, will);
		return copy;
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
