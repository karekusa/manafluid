/**
 * This class was created by <Lazersmoke>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [May 6, 2015, 9:45:56 PM (GMT)]
 */
package vazkii.botania.common.crafting.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.ModItems;

public class HelmRevealingRecipe implements IRecipe {

	@Override
	public boolean matches(InventoryCrafting var1, World var2) {
		Item goggles = (Item) Item.itemRegistry.getObject("Thaumcraft:ItemGoggles");
		if(goggles == null)
			return false; // NO TC loaded

		boolean foundGoggles = false;
		boolean foundHelm = false;
		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(stack != null) {
				if(checkHelm(stack))
					foundHelm = true;
				else if(stack.getItem() == goggles)
					foundGoggles = true;
				else return false; // Found an invalid item, breaking the recipe
			}
		}
		return foundGoggles && foundHelm;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting var1) {
		ItemStack helm = null;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(stack != null && checkHelm(stack))
				helm = stack;
		}

		if(helm == null)
			return null;

		ItemStack helmCopy = helm.copy();
		Item helmItem = helmCopy.getItem();

		ItemStack newHelm;

		if(helmItem == ModItems.manasteelHelm)
			newHelm = new ItemStack(ModItems.manasteelHelmRevealing);
		else if(helmItem == ModItems.terrasteelHelm)
			newHelm = new ItemStack(ModItems.terrasteelHelmRevealing);
		else if(helmItem == ModItems.elementiumHelm)
			newHelm = new ItemStack(ModItems.elementiumHelmRevealing);
		else return null;

		//Copy Ancient Wills
		for(int i = 0; i < 6; i++)
			if(ItemNBTHelper.getBoolean(helmCopy, "AncientWill" + i, false))
				ItemNBTHelper.setBoolean(newHelm, "AncientWill" + i, true);

		//Copy Enchantments
		NBTTagList enchList = ItemNBTHelper.getList(helmCopy, "ench", 10, true);
		if(enchList != null)
			ItemNBTHelper.setList(newHelm, "ench", enchList);

		//Copy Runic Hardening
		byte runicHardening = ItemNBTHelper.getByte(helmCopy, "RS.HARDEN", (byte)0);
		ItemNBTHelper.setByte(newHelm, "RS.HARDEN", runicHardening);

		return newHelm;
	}

	@Override
	public int getRecipeSize() {
		return 10;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(ModItems.manasteelHelmRevealing);
	}

	private boolean checkHelm(ItemStack helmStack) {
		Item helmItem = helmStack.getItem();
		return helmItem == ModItems.manasteelHelm || helmItem == ModItems.terrasteelHelm || helmItem == ModItems.elementiumHelm;
	}

}