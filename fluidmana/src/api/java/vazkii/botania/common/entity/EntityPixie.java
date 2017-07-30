/**
 * This class was created by <Adubbz>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [? (GMT)]
 */
package vazkii.botania.common.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import vazkii.botania.common.Botania;

public class EntityPixie extends EntityFlyingCreature {

	EntityLivingBase summoner = null;
	float damage = 0;
	PotionEffect effect = null;

	public EntityPixie(World world) {
		super(world);
		setSize(1.0F, 1.0F);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(20, 0);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(2.0);
	}

	public void setType(int type) {
		dataWatcher.updateObject(20, type);
	}

	public int getType() {
		return dataWatcher.getWatchableObjectInt(20);
	}

	public void setProps(EntityLivingBase target, EntityLivingBase summoner, int type, float damage) {
		setAttackTarget(target);
		this.summoner = summoner;
		this.damage = damage;
		setType(type);
	}

	public void setApplyPotionEffect(PotionEffect effect) {
		this.effect = effect;
	}

	@Override
	protected void updateEntityActionState() {
		EntityLivingBase target = getAttackTarget();
		if(target != null) {
			double d0 = target.posX + target.width / 2 - posX;
			double d1 = target.posY + target.height / 2 - posY;
			double d2 = target.posZ + target.width / 2 - posZ;
			double d3 = d0 * d0 + d1 * d1 + d2 * d2;

			float mod = 0.45F;
			if(getType() == 1)
				mod = 0.1F;

			motionX += d0 / d3 * mod;
			motionY += d1 / d3 * mod;
			motionZ += d2 / d3 * mod;

			if(Math.sqrt(d3) < 1F) {
				if(summoner != null) {
					if(summoner instanceof EntityPlayer)
						target.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) summoner), damage);
					else {
						target.attackEntityFrom(DamageSource.causeMobDamage(summoner), damage);
					}
				} else target.attackEntityFrom(DamageSource.causeMobDamage(this), damage);
				if(effect != null && !(target instanceof EntityPlayer))
					target.addPotionEffect(effect);
				die();
			}
		}

		renderYawOffset = rotationYaw = -((float)Math.atan2(motionX, motionZ)) * 180.0F / (float)Math.PI;
	}

	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
		if(getType() == 0 && par1DamageSource.getEntity() != summoner || getType() == 1 && par1DamageSource.getEntity() instanceof EntityPlayer)
			return super.attackEntityFrom(par1DamageSource, par2);
		return false;
	}

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();

		if(getAttackTarget() == null || ticksExisted > 200)
			die();

		boolean dark = getType() == 1;
		if(worldObj.isRemote)
			for(int i = 0; i < 4; i++)
				Botania.proxy.sparkleFX(worldObj, posX + (Math.random() - 0.5) * 0.25, posY + 0.5  + (Math.random() - 0.5) * 0.25, posZ + (Math.random() - 0.5) * 0.25, dark ? 0.1F : 1F, dark ? 0.025F : 0.25F, dark ? 0.09F : 0.9F, 0.1F + (float) Math.random() * 0.25F, 12);
	}

	public void die() {
		setDead();

		if(worldObj.isRemote && getType() == 0)
			for(int i = 0; i < 12; i++)
				Botania.proxy.sparkleFX(worldObj, posX + (Math.random() - 0.5) * 0.25, posY + 0.5  + (Math.random() - 0.5) * 0.25, posZ + (Math.random() - 0.5) * 0.25, 1F, 0.25F, 0.9F, 1F + (float) Math.random() * 0.25F, 5);
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}

}