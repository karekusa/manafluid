/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Nov 24, 2014, 5:58:16 PM (GMT)]
 */
package vazkii.botania.common.item.rod;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import vazkii.botania.api.item.IAvatarTile;
import vazkii.botania.api.item.IAvatarWieldable;
import vazkii.botania.api.item.IManaProficiencyArmor;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.Botania;
import vazkii.botania.common.entity.EntityMagicMissile;
import vazkii.botania.common.item.ItemMod;
import vazkii.botania.common.lib.LibItemNames;

public class ItemMissileRod extends ItemMod implements IManaUsingItem, IAvatarWieldable {

	private static final ResourceLocation avatarOverlay = new ResourceLocation(LibResources.MODEL_AVATAR_MISSILE);

	private static final int COST_PER = 120;
	private static final int COST_AVATAR = 40;

	public ItemMissileRod() {
		super();
		setMaxStackSize(1);
		setUnlocalizedName(LibItemNames.MISSILE_ROD);
	}

	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.bow;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
		return 72000;
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
		if(count != getMaxItemUseDuration(stack) && count % (IManaProficiencyArmor.Helper.hasProficiency(player) ? 1 : 2) == 0 && !player.worldObj.isRemote && ManaItemHandler.requestManaExactForTool(stack, player, COST_PER, false)) {
			if(spawnMissile(player.worldObj, player, player.posX + (Math.random() - 0.5 * 0.1), player.posY + 2.4 + (Math.random() - 0.5 * 0.1), player.posZ + (Math.random() - 0.5 * 0.1)))
				ManaItemHandler.requestManaExactForTool(stack, player, COST_PER, true);

			Botania.proxy.sparkleFX(player.worldObj, player.posX, player.posY + 2.4, player.posZ, 1F, 0.4F, 1F, 6F, 6);
		}
	}

	public boolean spawnMissile(World world, EntityLivingBase thrower, double x, double y, double z) {
		EntityMagicMissile missile;
		if(thrower != null)
			missile = new EntityMagicMissile(thrower, false);
		else missile = new EntityMagicMissile(world);

		missile.setPosition(x, y, z);
		if(missile.getTarget()) {
			if(!world.isRemote) {
				world.playSoundEffect(x, y, z, "botania:missile", 0.6F, 0.8F + (float) Math.random() * 0.2F);
				world.spawnEntityInWorld(missile);
			}

			return true;
		}
		return false;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		par3EntityPlayer.setItemInUse(par1ItemStack, getMaxItemUseDuration(par1ItemStack));
		return par1ItemStack;
	}

	@Override
	public boolean isFull3D() {
		return true;
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@Override
	public void onAvatarUpdate(IAvatarTile tile, ItemStack stack) {
		TileEntity te = (TileEntity) tile;
		World world = te.getWorldObj();
		if(tile.getCurrentMana() >= COST_AVATAR && tile.getElapsedFunctionalTicks() % 3 == 0 && tile.isEnabled())
			if(spawnMissile(world, null, te.xCoord + 0.5 + (Math.random() - 0.5 * 0.1), te.yCoord + 2.5 + (Math.random() - 0.5 * 0.1), te.zCoord + (Math.random() - 0.5 * 0.1))) {
				if(!world.isRemote)
					tile.recieveMana(-COST_AVATAR);
				Botania.proxy.sparkleFX(world, te.xCoord + 0.5, te.yCoord + 2.5, te.zCoord + 0.5, 1F, 0.4F, 1F, 6F, 6);
			}
	}

	@Override
	public ResourceLocation getOverlayResource(IAvatarTile tile, ItemStack stack) {
		return avatarOverlay;
	}
}
