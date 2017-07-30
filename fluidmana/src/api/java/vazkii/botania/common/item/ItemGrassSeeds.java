/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Apr 9, 2014, 5:11:34 PM (GMT)]
 */
package vazkii.botania.common.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.decor.IFloatingFlower.IslandType;
import vazkii.botania.common.lib.LibItemNames;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemGrassSeeds extends ItemMod implements IFloatingFlowerVariant {

	/**
	 * Represents a map of dimension IDs to a set of all block swappers
	 * active in that dimension.
	 */
	private static Map<Integer, Set<BlockSwapper>> blockSwappers = new HashMap<Integer, Set<BlockSwapper>>();

	private static final IslandType[] ISLAND_TYPES = {
		IslandType.GRASS, IslandType.PODZOL, IslandType.MYCEL,
		IslandType.DRY, IslandType.GOLDEN, IslandType.VIVID,
		IslandType.SCORCHED, IslandType.INFUSED, IslandType.MUTATED
	};

	private static final int SUBTYPES = 9;
	IIcon[] icons;

	public ItemGrassSeeds() {
		super();
		setUnlocalizedName(LibItemNames.GRASS_SEEDS);
		setHasSubtypes(true);
		FMLCommonHandler.instance().bus().register(this);
	}

	@Override
	public void getSubItems(Item par1, CreativeTabs par2, List par3) {
		for(int i = 0; i < SUBTYPES; i++)
			par3.add(new ItemStack(par1, 1, i));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
		icons = new IIcon[SUBTYPES];
		for(int i = 0; i < SUBTYPES; i++)
			icons[i] = IconHelper.forItem(par1IconRegister, this, i);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int par1) {
		return icons[Math.min(icons.length - 1, par1)];
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName() + stack.getItemDamage();
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
		Block block = par3World.getBlock(par4, par5, par6);
		int bmeta = par3World.getBlockMetadata(par4, par5, par6);

		if((block == Blocks.dirt || block == Blocks.grass && par1ItemStack.getItemDamage() != 0) && bmeta == 0) {
			int meta = par1ItemStack.getItemDamage();

			BlockSwapper swapper = addBlockSwapper(par3World, par4, par5, par6, meta);
			par3World.setBlock(par4, par5, par6, swapper.blockToSet, swapper.metaToSet, 1 | 2);
			for(int i = 0; i < 50; i++) {
				double x = (Math.random() - 0.5) * 3;
				double y = Math.random() - 0.5 + 1;
				double z = (Math.random() - 0.5) * 3;

				float r = 0F;
				float g = 0.4F;
				float b = 0F;
				switch(meta) {
				case 1: {
					r = 0.5F;
					g = 0.37F;
					b = 0F;
					break;
				}
				case 2: {
					r = 0.27F;
					g = 0F;
					b = 0.33F;
					break;
				}
				case 3: {
					r = 0.4F;
					g = 0.5F;
					b = 0.05F;
					break;
				}
				case 4: {
					r = 0.75F;
					g = 0.7F;
					b = 0F;
					break;
				}
				case 5: {
					r = 0F;
					g = 0.5F;
					b = 0.1F;
					break;
				}
				case 6: {
					r = 0.75F;
					g = 0F;
					b = 0F;
					break;
				}
				case 7: {
					r = 0F;
					g = 0.55F;
					b = 0.55F;
					break;
				}
				case 8: {
					r = 0.4F;
					g = 0.1F;
					b = 0.4F;
					break;
				}
				}

				float velMul = 0.025F;

				Botania.proxy.wispFX(par3World, par4 + 0.5 + x, par5 + 0.5 + y, par6 + 0.5 + z, r, g, b, (float) Math.random() * 0.15F + 0.15F, (float) -x * velMul, (float) -y * velMul, (float) -z * velMul);
			}

			par1ItemStack.stackSize--;
		}

		return true;
	}

	@SubscribeEvent
	public void onTickEnd(TickEvent.WorldTickEvent event) {
		// Block swapper updates should only occur on the server
		if(event.world.isRemote)
			return;

		if(event.phase == Phase.END) {
			int dim = event.world.provider.dimensionId;
			if(blockSwappers.containsKey(dim)) {
				Set<BlockSwapper> swappers = blockSwappers.get(dim);

				Iterator<BlockSwapper> iter = swappers.iterator();

				while(iter.hasNext()) {
					BlockSwapper next = iter.next();
					if(next == null || !next.tick())
						iter.remove();
				}
			}
		}
	}

	/**
	 * Adds a grass seed block swapper to the world at the provided positiona
	 * and with the provided meta (which designates the type of the grass
	 * being spread).
	 * 
	 * Block swappers are only actually created on the server, so a client
	 * calling this method will recieve a marker block swapper which contains
	 * the provided information but is not ticked.
	 * @param world The world the swapper will be in.
	 * @param x The x-position of the swapper.
	 * @param y The y-position of the swapper.
	 * @param z The z-position of the swapper.
	 * @param meta The meta value representing the type of block being swapped.
	 * @return The created block swapper.
	 */
	private static BlockSwapper addBlockSwapper(World world, int x, int y, int z, int meta) {
		BlockSwapper swapper = swapperFromMeta(world, x, y, z, meta);

		// Block swappers are only registered on the server
		if(world.isRemote)
			return swapper;

		// If a set for the dimension doesn't exist, create it.
		int dim = world.provider.dimensionId;
		if(!blockSwappers.containsKey(dim))
			blockSwappers.put(dim, new HashSet<BlockSwapper>());

		// Add the block swapper
		blockSwappers.get(dim).add(swapper);

		return swapper;
	}

	private static BlockSwapper swapperFromMeta(World world, int x, int y, int z, int meta) {
		switch(meta) {
		case 1 : return new BlockSwapper(world, new ChunkCoordinates(x, y, z),  Blocks.dirt, 2);
		case 2 : return new BlockSwapper(world, new ChunkCoordinates(x, y, z),  Blocks.mycelium, 0);
		case 3 : return new BlockSwapper(world, new ChunkCoordinates(x, y, z),  ModBlocks.altGrass, 0);
		case 4 : return new BlockSwapper(world, new ChunkCoordinates(x, y, z),  ModBlocks.altGrass, 1);
		case 5 : return new BlockSwapper(world, new ChunkCoordinates(x, y, z),  ModBlocks.altGrass, 2);
		case 6 : return new BlockSwapper(world, new ChunkCoordinates(x, y, z),  ModBlocks.altGrass, 3);
		case 7 : return new BlockSwapper(world, new ChunkCoordinates(x, y, z),  ModBlocks.altGrass, 4);
		case 8 : return new BlockSwapper(world, new ChunkCoordinates(x, y, z),  ModBlocks.altGrass, 5);
		default : return new BlockSwapper(world, new ChunkCoordinates(x, y, z),  Blocks.grass, 0);
		}
	}

	/**
	 * A block swapper for the Pasture Seeds, which swaps dirt and grass Blocks
	 * centered around a provided point to a provided block/metadata.
	 */
	private static class BlockSwapper {

		/**
		 * The range of the block swapper, in Blocks.
		 */
		public static final int RANGE = 3;

		/**
		 * The range around which a block can spread in a single tick.
		 */
		public static final int TICK_RANGE = 1;

		private final World world;
		private final Random rand;
		private final Block blockToSet;
		private final int metaToSet;

		private ChunkCoordinates startCoords;
		private int ticksExisted = 0;

		/**
		 * Constructs a new block swapper with the provided world, starting
		 * coordinates, target block, and target metadata.
		 * @param world The world to swap Blocks in.
		 * @param coords The central coordinates to swap Blocks around.
		 * @param block The target block to swap dirt and grass to.
		 * @param meta The metadata of the target block to swap dirt and grass to.
		 */
		public BlockSwapper(World world, ChunkCoordinates coords, Block block, int meta) {
			this.world = world;
			this.blockToSet = block;
			this.metaToSet = meta;
			this.rand = new Random(coords.posX ^ coords.posY ^ coords.posZ);
			this.startCoords = coords;
		}

		/**
		 * Ticks this block swapper, allowing it to make an action during
		 * this game tick. This method should return "false" when the swapper
		 * has finished operation and should be removed from the world.
		 * @return true if the swapper should continue to exist, false if it
		 * should be removed.
		 */
		public boolean tick() {
			++ticksExisted;

			// Go through all Blocks in the specified RANGE, and then
			// try and spread around that block if it is our target block already
			for(int i = -RANGE; i <= RANGE; i++) {
				for(int j = -RANGE; j <= RANGE; j++) {
					int x = startCoords.posX + i;
					int y = startCoords.posY;
					int z = startCoords.posZ + j;
					Block block = world.getBlock(x, y, z);
					int meta = world.getBlockMetadata(x, y, z);

					if(block == blockToSet && meta == metaToSet) {
						// Only make changes every 20 ticks
						if(ticksExisted % 20 != 0) continue;

						tickBlock(x, y, z);
					}
				}
			}

			// This swapper should exist for 80 ticks
			return ticksExisted < 80;
		}

		/**
		 * Tick a specific block position, finding the valid Blocks
		 * immediately adjacent to it and then replacing one at random.
		 * @param x The x-coordinate to use.
		 * @param y The y-coordinate to use.
		 * @param z The z-coordinate to use.
		 */
		public void tickBlock(int x, int y, int z) {
			List<ChunkCoordinates> validCoords = new ArrayList<ChunkCoordinates>();

			// Go around this block and aggregate valid Blocks.
			for(int xOffset = -TICK_RANGE; xOffset <= TICK_RANGE; xOffset++) {
				for(int zOffset = -TICK_RANGE; zOffset <= TICK_RANGE; zOffset++) {
					// Skip the current block
					if(xOffset == 0 && zOffset == 0) continue;

					if(isValidSwapPosition(x + xOffset, y, z + zOffset))
						validCoords.add(new ChunkCoordinates(x + xOffset, y, z + zOffset));
				}
			}

			// If we can make changes, and have at least 1 block to swap,
			// then swap a random block from the valid Blocks we could swap.
			if(!validCoords.isEmpty() && !world.isRemote) {
				ChunkCoordinates toSwap = validCoords.get(rand.nextInt(validCoords.size()));

				world.setBlock(toSwap.posX, toSwap.posY, toSwap.posZ, blockToSet, metaToSet, 1 | 2);
			}
		}

		/**
		 * Determines if a given position is a valid location to spread to, which
		 * means that the block must be either dirt or grass (with meta 0),
		 * and have a block above it which does not block grass growth.
		 * @param x The x-coordinate to check.
		 * @param y The y-coordinate to check.
		 * @param z The z-coordinate to check.
		 * @return True if the position is valid to swap, false otherwise.
		 */
		public boolean isValidSwapPosition(int x, int y, int z) {
			Block block = world.getBlock(x, y, z);
			int meta = world.getBlockMetadata(x, y, z);

			Block aboveBlock = world.getBlock(x, y + 1, z);

			// Valid Blocks to spread to are either dirt or grass, and do not
			// have Blocks which block grass growth.

			// See http://minecraft.gamepedia.com/Grass_Block
			// The major rule is that a block which reduces light
			// levels by 2 or more Blocks grass growth.

			return (block == Blocks.dirt || block == Blocks.grass)
				&& (meta == 0)
				&& (aboveBlock.getLightOpacity(world, x, y, z) <= 1);
		}
	}

	public IslandType getIslandType(ItemStack stack) {
		return ISLAND_TYPES[Math.min(stack.getItemDamage(), ISLAND_TYPES.length - 1)];
	}

}
