/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 14, 2014, 6:46:59 PM (GMT)]
 */
package vazkii.botania.client.gui.lexicon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.ILexicon;
import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.gui.lexicon.button.GuiButtonBack;
import vazkii.botania.client.gui.lexicon.button.GuiButtonInvisible;
import vazkii.botania.client.gui.lexicon.button.GuiButtonPage;
import vazkii.botania.common.lexicon.DLexiconEntry;

public class GuiLexiconIndex extends GuiLexicon implements IParented {

	private static final String TAG_CATEGORY = "category";
	private static final String TAG_PAGE = "page";

	LexiconCategory category;
	String title;
	int page = 0;

	int tutPage = -1;

	GuiButton leftButton, rightButton, backButton;
	GuiLexicon parent;
	GuiTextField searchField;

	GuiButton currentButton;
	LexiconEntry currentEntry;
	float infoTime;

	List<LexiconEntry> entriesToDisplay = new ArrayList();

	public GuiLexiconIndex() {
		parent = new GuiLexicon();
	}

	public GuiLexiconIndex(LexiconCategory category) {
		this.category = category;
		parent = new GuiLexicon();
		setTitle();
	}

	public void setTitle() {
		title = StatCollector.translateToLocal(category == null ? "botaniamisc.lexiconIndex" : category.getUnlocalizedName());
	}

	@Override
	boolean isMainPage() {
		return false;
	}

	@Override
	String getTitle() {
		return title;
	}

	@Override
	boolean isIndex() {
		return true;
	}

	@Override
	boolean isCategoryIndex() {
		return false;
	}

	@Override
	public void onInitGui() {
		super.onInitGui();

		if(!GuiLexicon.isValidLexiconGui(this))	{
			currentOpenLexicon = new GuiLexicon();
			mc.displayGuiScreen(currentOpenLexicon);
			ClientTickHandler.notifyPageChange();
			return;
		}

		buttonList.add(backButton = new GuiButtonBack(12, left + guiWidth / 2 - 8, top + guiHeight + 2));
		buttonList.add(leftButton = new GuiButtonPage(13, left, top + guiHeight - 10, false));
		buttonList.add(rightButton = new GuiButtonPage(14, left + guiWidth - 18, top + guiHeight - 10, true));

		searchField = new GuiTextField(fontRendererObj, left + guiWidth / 2 + 28, top + guiHeight + 6, 200, 10);
		searchField.setCanLoseFocus(false);
		searchField.setFocused(true);
		searchField.setEnableBackgroundDrawing(false);

		updateAll();
	}

	void updateAll() {
		buildEntries();
		updatePageButtons();
		populateIndex();
	}

	void buildEntries() {
		entriesToDisplay.clear();
		ILexicon lex = (ILexicon) stackUsed.getItem();
		for(LexiconEntry entry : category == null ? BotaniaAPI.getAllEntries() : category.entries) {
			if(entry.isVisible() && lex.isKnowledgeUnlocked(stackUsed, entry.getKnowledgeType()) && matchesSearch(entry))
				entriesToDisplay.add(entry);
		}
		Collections.sort(entriesToDisplay);
	}
	
	boolean matchesSearch(LexiconEntry e) {
		String search = searchField.getText().trim();
		if(search.isEmpty())
			return true;
		
		search = search.toLowerCase();
		if(StatCollector.translateToLocal(e.getUnlocalizedName()).toLowerCase().contains(search))
			return true;
		
		for(ItemStack stack : e.getDisplayedRecipes()) {
			String stackName = stack.getDisplayName().toLowerCase().trim();
			if(stackName.contains(search))
				return true;
		}
				
		return false;
	}

	@Override
	void populateIndex() {
		LexiconEntry tutEntry = tutorial != null && !tutorial.isEmpty() ? tutorial.peek() : null;

		for(int i = page * 12; i < (page + 1) * 12; i++) {
			GuiButtonInvisible button = (GuiButtonInvisible) buttonList.get(i - page * 12);
			LexiconEntry entry = i >= entriesToDisplay.size() ? null : entriesToDisplay.get(i);
			if(entry != null) {
				button.displayString = entry.getKnowledgeType().color + "" + (entry.isPriority() ? EnumChatFormatting.ITALIC : "") + StatCollector.translateToLocal(entry.getUnlocalizedName());
				button.displayStack = entry.getIcon();
				if(entry == tutEntry)
					tutPage = page;
				
				if(entry instanceof DLexiconEntry)
					button.dog = true;
			} else button.displayString = "";
		}
	}

	public void setHoveredButton(GuiButtonInvisible b) {
		if(b == null)
			currentEntry = null;
		else currentEntry = entriesToDisplay.get(b.id + page * 12);
		currentButton = b;
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		super.drawScreen(par1, par2, par3);

		if(!searchField.getText().isEmpty()) {
			drawBookmark(left + 138, top + guiHeight - 24, "  " + searchField.getText(), false);
			mc.renderEngine.bindTexture(texture);
			GL11.glColor4f(1F, 1F, 1F, 1F);
			drawTexturedModalRect(left + 134, top + guiHeight - 26, 86, 180, 12, 12);
			
			if(entriesToDisplay.size() == 1) {
				boolean unicode = mc.fontRenderer.getUnicodeFlag();
				mc.fontRenderer.setUnicodeFlag(true);
				String s = StatCollector.translateToLocal("botaniamisc.enterToView");
				mc.fontRenderer.drawString(s, left + guiWidth / 2 - mc.fontRenderer.getStringWidth(s) / 2, top + 30, 0x666666);
				mc.fontRenderer.setUnicodeFlag(unicode);
			}
		} else {
			boolean unicode = mc.fontRenderer.getUnicodeFlag();
			mc.fontRenderer.setUnicodeFlag(true);
			String s = StatCollector.translateToLocal("botaniamisc.typeToSearch");
			mc.fontRenderer.drawString(s, left + 120 - mc.fontRenderer.getStringWidth(s), top + guiHeight - 18, 0x666666);
			mc.fontRenderer.setUnicodeFlag(unicode);
		}

		float animationTime = 4F;
		if(isShiftKeyDown()) {
			if(currentButton != null)
				infoTime = Math.min(animationTime, infoTime + timeDelta);
		} else {
			infoTime = Math.max(0, infoTime - timeDelta);

			if(currentButton != null && infoTime == 0) {
				int x = par1 + 10;
				int y = par2;

				x = currentButton.xPosition - 20;
				y = currentButton.yPosition;

				mc.fontRenderer.drawStringWithShadow("?", x, y, 0xFFFFFF);
				GL11.glScalef(0.5F, 0.5F, 1F);
				mc.fontRenderer.drawStringWithShadow(EnumChatFormatting.BOLD + "Shift", x * 2 - 6, y * 2 + 20, 0xFFFFFF);
				GL11.glScalef(2F, 2F, 1F);
			}
		}

		if(currentButton != null && infoTime > 0) {
			float fract = infoTime / animationTime;

			int x = currentButton.xPosition;
			int y = currentButton.yPosition;
			String s = StatCollector.translateToLocal(currentEntry.getTagline());
			boolean unicode = mc.fontRenderer.getUnicodeFlag();
			mc.fontRenderer.setUnicodeFlag(true);
			int width = mc.fontRenderer.getStringWidth(s);

			GL11.glPushMatrix();
			GL11.glTranslatef(x, y, 0);
			GL11.glScalef(fract, 1F, 1F);
			Gui.drawRect(12, -30, width + 20, -2, 0x44000000);
			Gui.drawRect(10, -32, width + 22, -2, 0x44000000);
			drawBookmark(width / 2 + 16, -8, s, true, 0xFFFFFF, 180);
			mc.fontRenderer.setUnicodeFlag(unicode);

			net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			ItemStack paper = new ItemStack(Items.paper, currentEntry.pages.size());

			RenderItem.getInstance().renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, paper, 14, -28);
			RenderItem.getInstance().renderItemOverlayIntoGUI(mc.fontRenderer, mc.renderEngine, paper, 14, -28);
			List<ItemStack> stacks = currentEntry.getDisplayedRecipes();

			if(stacks.size() > 0) {
				int spaceForEach = Math.min(18, (width - 30) / stacks.size());
				for(int i = 0; i < stacks.size(); i++) {
					ItemStack stack = stacks.get(i);
					RenderItem.getInstance().renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, stack, 38 + spaceForEach * i, -28);
				}
			}

			net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();

			GL11.glPopMatrix();
		}

		setHoveredButton(null);
	}

	@Override
	public void positionTutorialArrow() {
		LexiconEntry entry = tutorial.peek();
		LexiconCategory category = entry.category;
		if(category != this.category) {
			orientTutorialArrowWithButton(backButton);
			return;
		}

		if(tutPage != -1 && tutPage != page) {
			orientTutorialArrowWithButton(tutPage < page ? leftButton : rightButton);
			return;
		}

		List<GuiButton> buttons = buttonList;
		for(GuiButton button : buttons) {
			int id = button.id;
			int index = id + page * 12;
			if(index >= entriesToDisplay.size())
				continue;

			if(entry == entriesToDisplay.get(index)) {
				orientTutorialArrowWithButton(id >= 12 ? rightButton : button);
				break;
			}
		}
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton) {
		if(par1GuiButton.id >= BOOKMARK_START)
			handleBookmark(par1GuiButton);
		else if(par1GuiButton.id == NOTES_BUTTON_ID)
			notesEnabled = !notesEnabled;
		else
			switch(par1GuiButton.id) {
			case 12 :
				mc.displayGuiScreen(parent);
				ClientTickHandler.notifyPageChange();
				break;
			case 13 :
				page--;
				updatePageButtons();
				populateIndex();
				ClientTickHandler.notifyPageChange();
				break;
			case 14 :
				page++;
				updatePageButtons();
				populateIndex();
				ClientTickHandler.notifyPageChange();
				break;
			default :
				if(par1GuiButton instanceof GuiButtonInvisible && ((GuiButtonInvisible) par1GuiButton).dog)
					((GuiButtonInvisible) par1GuiButton).click();
				else {
					int index = par1GuiButton.id + page * 12;
					openEntry(index);
				}
			}
	}

	void openEntry(int index) {
		if(index >= entriesToDisplay.size())
			return;

		LexiconEntry entry = entriesToDisplay.get(index);
		mc.displayGuiScreen(new GuiLexiconEntry(entry, this));
		ClientTickHandler.notifyPageChange();
	}

	public void updatePageButtons() {
		leftButton.enabled = page != 0;
		rightButton.enabled = page < (entriesToDisplay.size() - 1) / 12;
		putTutorialArrow();
	}

	@Override
	public void setParent(GuiLexicon gui) {
		parent = gui;
	}

	int fx = 0;
	boolean swiped = false;

	@Override
	protected void mouseClickMove(int x, int y, int button, long time) {
		if(button == 0 && Math.abs(x - fx) > 100 && mc.gameSettings.touchscreen && !swiped) {
			double swipe = (x - fx) / Math.max(1, (double) time);
			if(swipe < 0.5) {
				nextPage();
				swiped = true;
			} else if(swipe > 0.5) {
				prevPage();
				swiped = true;
			}
		}
	}

	@Override
	protected void mouseClicked(int par1, int par2, int par3) {
		super.mouseClicked(par1, par2, par3);

		searchField.mouseClicked(par1, par2, par3);
		fx = par1;
		switch(par3) {
		case 1:
			back();
			break;
		case 3:
			nextPage();
			break;
		case 4:
			prevPage();
			break;
		}
	}

	@Override
	public void handleMouseInput() {
		super.handleMouseInput();

		if(Mouse.getEventButton() == 0)
			swiped = false;

		int w = Mouse.getEventDWheel();
		if(w < 0)
			nextPage();
		else if(w > 0)
			prevPage();
	}

	@Override
	boolean closeScreenOnInvKey() {
		return false;
	}

	@Override
	protected void keyTyped(char par1, int par2) {
		if(par2 == 203 || par2 == 200 || par2 == 201) // Left, Up, Page Up
			prevPage();
		else if(par2 == 205 || par2 == 208 || par2 == 209) // Right, Down Page Down
			nextPage();
		else if(par2 == 14 && !notesEnabled && searchField.getText().isEmpty()) // Backspace
			back();
		else if(par2 == 199) { // Home
			mc.displayGuiScreen(new GuiLexicon());
			ClientTickHandler.notifyPageChange();
		} else if(par2 == 28 && entriesToDisplay.size() == 1) // Enter
			openEntry(0);

		if(!notesEnabled) {
			String search = searchField.getText();
			searchField.textboxKeyTyped(par1, par2);
			if(!searchField.getText().equalsIgnoreCase(search))
				updateAll();
		}

		super.keyTyped(par1, par2);
	}

	void back() {
		if(backButton.enabled) {
			actionPerformed(backButton);
			backButton.func_146113_a(mc.getSoundHandler());
		}
	}

	void nextPage() {
		if(rightButton.enabled) {
			actionPerformed(rightButton);
			rightButton.func_146113_a(mc.getSoundHandler());
		}
	}

	void prevPage() {
		if(leftButton.enabled) {
			actionPerformed(leftButton);
			leftButton.func_146113_a(mc.getSoundHandler());
		}
	}

	@Override
	public void serialize(NBTTagCompound cmp) {
		super.serialize(cmp);
		cmp.setString(TAG_CATEGORY, category == null ? "" : category.getUnlocalizedName());
		cmp.setInteger(TAG_PAGE, page);
	}

	@Override
	public void load(NBTTagCompound cmp) {
		super.load(cmp);
		String categoryStr = cmp.getString(TAG_CATEGORY);
		if(categoryStr.isEmpty())
			category = null;
		else for(LexiconCategory cat : BotaniaAPI.getAllCategories())
			if(cat.getUnlocalizedName().equals(categoryStr)) {
				category = cat;
				break;
			}
		page = cmp.getInteger(TAG_PAGE);
		setTitle();
	}

	@Override
	public GuiLexicon copy() {
		GuiLexiconIndex gui = new GuiLexiconIndex(category);
		gui.page = page;
		gui.setTitle();
		return gui;
	}

	@Override
	public String getNotesKey() {
		return "category_" + (category == null ? "lexindex" : category.unlocalizedName);
	}
}

