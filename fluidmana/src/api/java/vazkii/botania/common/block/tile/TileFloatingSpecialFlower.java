/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Nov 17, 2014, 5:41:58 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import vazkii.botania.common.block.decor.IFloatingFlower;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;

public class TileFloatingSpecialFlower extends TileSpecialFlower implements IFloatingFlower {

	public static final String TAG_ISLAND_TYPE = "islandType";
	IslandType type = IslandType.GRASS;

	@Override
	public boolean isOnSpecialSoil() {
		return false;
	}

	@Override
	public ItemStack getDisplayStack() {
		return ItemBlockSpecialFlower.ofType(subTileName);
	}

	@Override
	public IslandType getIslandType() {
		return type;
	}

	@Override
	public void setIslandType(IslandType type) {
		this.type = type;
	}

	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		super.writeCustomNBT(cmp);
		cmp.setString(TAG_ISLAND_TYPE, type.toString());
	}

	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		super.readCustomNBT(cmp);
		type = IslandType.ofType(cmp.getString(TAG_ISLAND_TYPE));
	}

	@Override
	public int getSlowdownFactor() {
		IslandType type = getIslandType();
		if (type == IslandType.MYCEL)
			return SLOWDOWN_FACTOR_MYCEL;
		else if (type == IslandType.PODZOL)
			return SLOWDOWN_FACTOR_PODZOL;
		return 0;
	}

}
