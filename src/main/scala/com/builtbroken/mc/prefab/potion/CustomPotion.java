package com.builtbroken.mc.prefab.potion;

import com.builtbroken.mc.core.References;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;

/**
 * @author Calclavia, Darkguardsman
 */
public abstract class CustomPotion extends Potion
{
	protected static int NEXT_ID = 32;
	public boolean enable = true;
	public boolean disableCreative = true;
	public boolean disablePeaceful = true;

	/**
	 * Creates a new type of potion
	 *
	 * @param color - The color of this potion.
	 * @param name  - The name of this potion.
	 */
	public CustomPotion(int color, String name)
	{
		this(getNextId(), false, color, name);
	}

	/**
	 * Creates a new type of potion
	 *
	 * @param isBadEffect - Is this potion a good potion or a bad one?
	 * @param color       - The color of this potion.
	 * @param name        - The name of this potion.
	 */
	public CustomPotion(boolean isBadEffect, int color, String name)
	{
		this(getNextId(), isBadEffect, color, name);
	}

	/**
	 * Creates a new type of potion
	 *
	 * @param id          - The ID of this potion. Make it greater than 20.
	 * @param isBadEffect - Is this potion a good potion or a bad one?
	 * @param color       - The color of this potion.
	 * @param name        - The name of this potion.
	 */
	public CustomPotion(int id, boolean isBadEffect, int color, String name)
	{
		super(getPotionID(name, id), isBadEffect, color);
		this.setPotionName("potion." + name);
		Potion.potionTypes[this.getId()] = this;
	}

	public static int getPotionID(String name, int id)
	{
		References.CONFIGURATION.load();
		int finalID = References.CONFIGURATION.get("Potion ID", name + " ID", id).getInt(id);
		References.CONFIGURATION.save();
		return finalID;
	}

	/**
	 * Gets the increments a static potion id making it easier to register new potion effects
	 */
	protected static int getNextId()
	{
		return NEXT_ID++;
	}

	@Override
	public void performEffect(EntityLivingBase ent, int amplifier)
	{
		if (shouldRemoveEffect(ent, amplifier))
		{
			removeEffect(ent);
			return;
		}
	}

	/**
	 * Called to check if the effect should be remove before its timer runs out
	 *
	 * @param ent       - entity with the effect
	 * @param amplifier - level of the effect
	 * @return true to remove
	 */
	public boolean shouldRemoveEffect(EntityLivingBase ent, int amplifier)
	{
		// if disabled, dead, or has no hp left(basically dead)
		if (!enable || ent.isDead || ent.getHealth() <= 0)
		{
			return true;
		}

		//Remove any potion effect off of entities by default to avoid issues
		if (disableCreative && ent instanceof EntityPlayer && ((EntityPlayer) ent).capabilities.isCreativeMode)
		{
			return true;
		}

		//Disable mal effects with peaceful on
		if (disablePeaceful && ent.worldObj.difficultySetting.getDifficultyId() == 0)
		{
			return true;
		}
		return false;
	}

	/**
	 * Removes the effect of the entity
	 *
	 * @param entity - entity that may have the effect
	 */
	public void removeEffect(EntityLivingBase entity)
	{
		if (entity.isPotionActive(getId()))
		{
			entity.removePotionEffect(getId());
		}
	}

	@Override
	public CustomPotion setIconIndex(int par1, int par2)
	{
		super.setIconIndex(par1, par2);
		return this;
	}

	@Override
	protected CustomPotion setEffectiveness(double par1)
	{
		super.setEffectiveness(par1);
		return this;
	}

	@Override
	public CustomPotion setPotionName(String name)
	{
		super.setPotionName(name);
		return this;
	}
}