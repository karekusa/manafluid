/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Nov 16, 2014, 10:52:21 PM (GMT)]
 */
package vazkii.botania.common.block.tile.string;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockMushroom;
import net.minecraft.tileentity.TileEntity;
import vazkii.botania.api.subtile.ISubTileContainer;

public class TileRedStringRelay extends TileRedString {

	@Override
	public boolean acceptBlock(int x, int y, int z) {
		if(x == xCoord && y == yCoord + 1 && z == zCoord)
			return false;

		Block block = worldObj.getBlock(x, y, z);
		TileEntity tile = worldObj.getTileEntity(x, y, z);
		return (block instanceof BlockFlower || block instanceof BlockMushroom || block instanceof BlockDoublePlant) && (tile == null || !(tile instanceof ISubTileContainer));
	}

}
