/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 14, 2014, 6:44:59 PM (GMT)]
 */
package vazkii.botania.common.core.handler;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import vazkii.botania.api.boss.IBotaniaBoss;
import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.api.corporea.ICorporeaSpark;
import vazkii.botania.api.corporea.IWrappedInventory;
import vazkii.botania.api.internal.DummyMethodHandler;
import vazkii.botania.api.internal.IManaNetwork;
import vazkii.botania.api.lexicon.LexiconPage;
import vazkii.botania.api.lexicon.multiblock.MultiblockSet;
import vazkii.botania.api.recipe.RecipeBrew;
import vazkii.botania.api.recipe.RecipeElvenTrade;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import vazkii.botania.api.subtile.SubTileEntity;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.api.subtile.SubTileGenerating;
import vazkii.botania.client.core.handler.BossBarHandler;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.BlockModFlower;
import vazkii.botania.common.block.BlockSpecialFlower;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.subtile.functional.SubTileSolegnolia;
import vazkii.botania.common.integration.corporea.WrappedDeepStorage;
import vazkii.botania.common.integration.corporea.WrappedIInventory;
import vazkii.botania.common.integration.corporea.WrappedStorageDrawers;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.item.relic.ItemLokiRing;
import vazkii.botania.common.lexicon.page.PageBrew;
import vazkii.botania.common.lexicon.page.PageCraftingRecipe;
import vazkii.botania.common.lexicon.page.PageElvenRecipe;
import vazkii.botania.common.lexicon.page.PageImage;
import vazkii.botania.common.lexicon.page.PageLoreText;
import vazkii.botania.common.lexicon.page.PageManaInfusionRecipe;
import vazkii.botania.common.lexicon.page.PageMultiblock;
import vazkii.botania.common.lexicon.page.PagePetalRecipe;
import vazkii.botania.common.lexicon.page.PageRuneRecipe;
import vazkii.botania.common.lexicon.page.PageText;
import baubles.common.lib.PlayerHandler;
import baubles.common.network.PacketHandler;
import baubles.common.network.PacketSyncBauble;
import buildcraft.api.transport.IPipeTile;
import cpw.mods.fml.common.Optional;

public class InternalMethodHandler extends DummyMethodHandler {

	@Override
	public LexiconPage textPage(String key) {
		return new PageText(key);
	}

	@Override
	public LexiconPage elfPaperTextPage(String key) {
		return new PageLoreText(key);
	}

	@Override
	public LexiconPage imagePage(String key, String resource) {
		return new PageImage(key, resource);
	}

	@Override
	public LexiconPage craftingRecipesPage(String key, List<IRecipe> recipes) {
		return new PageCraftingRecipe(key, recipes);
	}

	@Override
	public LexiconPage craftingRecipePage(String key, IRecipe recipe) {
		return new PageCraftingRecipe(key, recipe);
	}

	@Override
	public IIcon getSubTileIconForName(String name) {
		IIcon icon = (ConfigHandler.altFlowerTextures ? BlockSpecialFlower.iconsAlt : BlockSpecialFlower.icons).get(name);
		return icon == null ? Blocks.red_flower.getIcon(0, 0) : icon;
	}

	@Override
	public void registerBasicSignatureIcons(String name, IIconRegister register) {
		IIcon normal = IconHelper.forName(register, name);
		IIcon alt = IconHelper.forName(register, BlockModFlower.ALT_DIR + "/" + name);
		BlockSpecialFlower.icons.put(name, normal);
		BlockSpecialFlower.iconsAlt.put(name, alt == null ? normal : alt);
	}

	@Override
	public LexiconPage petalRecipesPage(String key, List<RecipePetals> recipes) {
		return new PagePetalRecipe(key, recipes);
	}

	@Override
	public LexiconPage petalRecipePage(String key, RecipePetals recipe) {
		return new PagePetalRecipe(key, recipe);
	}

	@Override
	public LexiconPage runeRecipesPage(String key, List<RecipeRuneAltar> recipes) {
		return new PageRuneRecipe(key, recipes);
	}

	@Override
	public LexiconPage runeRecipePage(String key, RecipeRuneAltar recipe) {
		return new PageRuneRecipe(key, recipe);
	}

	@Override
	public LexiconPage manaInfusionRecipesPage(String key, List<RecipeManaInfusion> recipes) {
		return new PageManaInfusionRecipe(key, recipes);
	}

	@Override
	public LexiconPage manaInfusionRecipePage(String key, RecipeManaInfusion recipe) {
		return new PageManaInfusionRecipe(key, recipe);
	}

	@Override
	public LexiconPage elvenTradePage(String key, List<RecipeElvenTrade> recipes) {
		return new PageElvenRecipe(key, recipes);
	}

	@Override
	public LexiconPage elvenTradesPage(String key, RecipeElvenTrade recipe) {
		return new PageElvenRecipe(key, recipe);
	}

	@Override
	public LexiconPage brewPage(String key, String bottomText, RecipeBrew recipe) {
		return new PageBrew(recipe, key, bottomText);
	}

	@Override
	public LexiconPage multiblockPage(String key, MultiblockSet mb) {
		return new PageMultiblock(key, mb);
	}

	@Override
	public ItemStack getSubTileAsStack(String subTile) {
		return ItemBlockSpecialFlower.ofType(subTile);
	}

	@Override
	public ItemStack getSubTileAsFloatingFlowerStack(String subTile) {
		return ItemBlockSpecialFlower.ofType(new ItemStack(ModBlocks.floatingSpecialFlower), subTile);
	}

	@Override
	public String getStackSubTileKey(ItemStack stack) {
		return ItemBlockSpecialFlower.getType(stack);
	}

	@Override
	public IManaNetwork getManaNetworkInstance() {
		return ManaNetworkHandler.instance;
	}

	@Override
	public IInventory getBaublesInventory(EntityPlayer player) {
		return PlayerHandler.getPlayerBaubles(player);
	}

	@Override
	public void drawSimpleManaHUD(int color, int mana, int maxMana, String name, ScaledResolution res) {
		HUDHandler.drawSimpleManaHUD(color, mana, maxMana, name, res);
	}

	@Override
	public void drawComplexManaHUD(int color, int mana, int maxMana, String name, ScaledResolution res, ItemStack bindDisplay, boolean properlyBound) {
		HUDHandler.drawComplexManaHUD(color, mana, maxMana, name, res, bindDisplay, properlyBound);
	}

	@Override
	public ItemStack getBindDisplayForFlowerType(SubTileEntity e) {
		return e instanceof SubTileGenerating ? new ItemStack(ModBlocks.spreader) : e instanceof SubTileFunctional ? new ItemStack(ModBlocks.pool) : new ItemStack(ModItems.twigWand);
	}

	@Override
	public void renderLexiconText(int x, int y, int width, int height, String unlocalizedText) {
		PageText.renderText(x, y, width, height, unlocalizedText);
	}

	@Override
	public void sparkleFX(World world, double x, double y, double z, float r, float g, float b, float size, int m) {
		Botania.proxy.sparkleFX(world, x, y, z, r, g, b, size, m);
	}

	@Override
	public ResourceLocation getDefaultBossBarTexture() {
		return BossBarHandler.defaultBossBar;
	}

	@Override
	public void setBossStatus(IBotaniaBoss status) {
		BossBarHandler.setCurrentBoss(status);
	}

	@Override
	public boolean shouldForceCheck() {
		return ConfigHandler.flowerForceCheck;
	}

	@Override
	public int getPassiveFlowerDecay() {
		return ConfigHandler.hardcorePassiveGeneration;
	}

	@Override
	@Optional.Method(modid = "BuildCraft|Transport")
	public boolean isBuildcraftPipe(TileEntity tile) {
		return tile instanceof IPipeTile;
	}

	@Override
	public void breakOnAllCursors(EntityPlayer player, Item item, ItemStack stack, int x, int y, int z, int side) {
		ItemLokiRing.breakOnAllCursors(player, item, stack, x, y, z, side);
	}

	@Override
	public boolean hasSolegnoliaAround(Entity e) {
		return SubTileSolegnolia.hasSolegnoliaAround(e);
	}

	@Override
	public long getWorldElapsedTicks() {
		return Botania.proxy.getWorldElapsedTicks();
	}

	@Override
	public boolean isBotaniaFlower(World world, int x, int y, int z) {
		Block block = world.getBlock(x, y, z);
		return block == ModBlocks.flower || block == ModBlocks.shinyFlower || block == ModBlocks.specialFlower;
	}

	@Override
	public void sendBaubleUpdatePacket(EntityPlayer player, int slot) {
		if(player instanceof EntityPlayerMP)
			PacketHandler.INSTANCE.sendTo(new PacketSyncBauble(player, slot), (EntityPlayerMP) player);
	}
	

	@Override
	public List<IWrappedInventory> wrapInventory(List<IInventory> inventories) {
		ArrayList<IWrappedInventory> arrayList = new ArrayList<IWrappedInventory>();
		for(IInventory inv : inventories) {
			ICorporeaSpark spark = CorporeaHelper.getSparkForInventory(inv);
			IWrappedInventory wrapped = null;
			// try StorageDrawers integration
			if(Botania.storageDrawersLoaded) {
				wrapped = WrappedStorageDrawers.wrap(inv, spark);
			}
			// try DeepStorageUnit
			if(wrapped == null) {
				wrapped = WrappedDeepStorage.wrap(inv, spark);
			}
			// last chance - this will always work
			if(wrapped == null) {
				wrapped = WrappedIInventory.wrap(inv, spark);
			}
			arrayList.add(wrapped);
		}
		return arrayList;
	}
}
