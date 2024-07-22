package ganymedes01.etfuturum.client.gui.inventory;

import com.google.common.collect.Lists;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ganymedes01.etfuturum.api.EnchantingFuelRegistry;
import ganymedes01.etfuturum.client.OpenGLHelper;
import ganymedes01.etfuturum.core.utils.Utils;
import ganymedes01.etfuturum.inventory.ContainerEnchantment;
import ganymedes01.etfuturum.lib.Reference;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.lwjgl.util.glu.Project;

import java.util.ArrayList;
import java.util.Random;

@SideOnly(Side.CLIENT)
public class GuiEnchantment extends GuiContainer {

	private final ResourceLocation TEXTURE;
	private static final ResourceLocation field_147070_D = Utils.getResource("textures/entity/enchanting_table_book.png");
	private static final ModelBook field_147072_E = new ModelBook();
	private final InventoryPlayer field_175379_F;
	private final Random field_147074_F = new Random();
	private final ContainerEnchantment field_147075_G;
	public int field_147073_u;
	public float field_147071_v;
	public float field_147069_w;
	public float field_147082_x;
	public float field_147081_y;
	public float field_147080_z;
	public float field_147076_A;
	ItemStack field_147077_B;
	private final String field_175380_I;

	/**
	 * How long has the player seen the "lapis lazuli" text?
	 */
	private int viewingTicks = 0;
	private ItemStack displayItem;
	private final ArrayList<ItemStack> displayStacks; //Stores the stacks in an array so we can more easily pick random elements and stuff
	private final Random displayRand = new Random();
	private static final ItemStack LAPIS = new ItemStack(Items.dye, 1, 4); //This is used to match with lapis to put it at the front of the list

	public GuiEnchantment(InventoryPlayer p_i45502_1_, World worldIn, String p_i45502_3_) {
		super(new ContainerEnchantment(p_i45502_1_, worldIn, 0, 0, 0));
		field_175379_F = p_i45502_1_;
		field_147075_G = (ContainerEnchantment) inventorySlots;
		field_175380_I = p_i45502_3_;

		TEXTURE = Utils.getResource(
				field_147075_G.noFuel ? "textures/gui/container/enchanting_table.png" :
						Reference.MOD_ID + ":textures/gui/container/enchanting_table.png");

		displayStacks = new ArrayList<>(EnchantingFuelRegistry.getFuels().keySet());
		if (displayStacks.size() > 1) {
			for (int i = 1; i < displayStacks.size(); i++) {
				ItemStack stack = displayStacks.get(i);
				if (LAPIS.isItemEqual(stack)) {
					displayStacks.remove(i);
					displayStacks.add(0, stack);
					break;
				}
			}
		}
		displayItem = field_147075_G.noFuel ? LAPIS : displayStacks.get(0);
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items). Args : mouseX, mouseY
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		fontRendererObj.drawString(field_175380_I == null ? I18n.format("container.enchant") : field_175380_I, 12, 5, 4210752);
		fontRendererObj.drawString(I18n.format(field_175379_F.getInventoryName()), 8, ySize - 96 + 2, 4210752);
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void updateScreen() {
		super.updateScreen();
		if (displayStacks.size() > 1) { //We only need this if there are 2 or more elements in the list
			viewingTicks++;
		}
		func_147068_g();
	}

	/**
	 * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
	 */
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		int var4 = (width - xSize) / 2;
		int var5 = (height - ySize) / 2;

		for (int var6 = 0; var6 < 3; ++var6) {
			int var7 = mouseX - (var4 + 60);
			int var8 = mouseY - (var5 + 14 + 19 * var6);

			if (var7 >= 0 && var8 >= 0 && var7 < 108 && var8 < 19 && field_147075_G.enchantItem(mc.thePlayer, var6))
				mc.playerController.sendEnchantPacket(field_147075_G.windowId, var6);
		}
	}

	/**
	 * Args : renderPartialTicks, mouseX, mouseY
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		OpenGLHelper.colour(1, 1, 1);
		mc.getTextureManager().bindTexture(TEXTURE);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
		OpenGLHelper.pushMatrix();
		OpenGLHelper.matrixMode(5889);
		OpenGLHelper.pushMatrix();
		OpenGLHelper.loadIdentity();
		ScaledResolution var6 = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		OpenGLHelper.viewport((var6.getScaledWidth() - 320) / 2 * var6.getScaleFactor(), (var6.getScaledHeight() - 240) / 2 * var6.getScaleFactor(), 320 * var6.getScaleFactor(), 240 * var6.getScaleFactor());
		OpenGLHelper.translate(-0.34F, 0.23F, 0.0F);
		Project.gluPerspective(90.0F, 1.3333334F, 9.0F, 80.0F);
		float var7 = 1.0F;
		OpenGLHelper.matrixMode(5888);
		OpenGLHelper.loadIdentity();
		RenderHelper.enableStandardItemLighting();
		OpenGLHelper.translate(0.0F, 3.3F, -16.0F);
		OpenGLHelper.scale(var7, var7, var7);
		float var8 = 5.0F;
		OpenGLHelper.scale(var8, var8, var8);
		OpenGLHelper.rotate(180.0F, 0.0F, 0.0F, 1.0F);
		mc.getTextureManager().bindTexture(field_147070_D);
		OpenGLHelper.rotate(20.0F, 1.0F, 0.0F, 0.0F);
		float var9 = field_147076_A + (field_147080_z - field_147076_A) * partialTicks;
		OpenGLHelper.translate((1.0F - var9) * 0.2F, (1.0F - var9) * 0.1F, (1.0F - var9) * 0.25F);
		OpenGLHelper.rotate(-(1.0F - var9) * 90.0F - 90.0F, 0.0F, 1.0F, 0.0F);
		OpenGLHelper.rotate(180.0F, 1.0F, 0.0F, 0.0F);
		float var10 = field_147069_w + (field_147071_v - field_147069_w) * partialTicks + 0.25F;
		float var11 = field_147069_w + (field_147071_v - field_147069_w) * partialTicks + 0.75F;
		var10 = (var10 - MathHelper.truncateDoubleToInt(var10)) * 1.6F - 0.3F;
		var11 = (var11 - MathHelper.truncateDoubleToInt(var11)) * 1.6F - 0.3F;

		if (var10 < 0.0F)
			var10 = 0.0F;

		if (var11 < 0.0F)
			var11 = 0.0F;

		if (var10 > 1.0F)
			var10 = 1.0F;

		if (var11 > 1.0F)
			var11 = 1.0F;

		OpenGLHelper.enableRescaleNormal();
		field_147072_E.render(null, 0.0F, var10, var11, var9, 0.0F, 0.0625F);
		OpenGLHelper.disableRescaleNormal();
		RenderHelper.disableStandardItemLighting();
		OpenGLHelper.matrixMode(5889);
		OpenGLHelper.viewport(0, 0, mc.displayWidth, mc.displayHeight);
		OpenGLHelper.popMatrix();
		OpenGLHelper.matrixMode(5888);
		OpenGLHelper.popMatrix();
		RenderHelper.disableStandardItemLighting();
		OpenGLHelper.colour(1.0F, 1.0F, 1.0F);
		EnchantmentNameParts.instance.reseedRandomGenerator(field_147075_G.enchantmentSeed);
		int var12 = field_147075_G.func_178147_e();

		for (int i1 = 0; i1 < 3; i1++) {
			int var14 = k + 60;
			int var15 = var14 + 20;
			byte var16 = 86;
			String s = EnchantmentNameParts.instance.generateNewRandomName();
			zLevel = 0.0F;
			mc.getTextureManager().bindTexture(TEXTURE);
			int j1 = field_147075_G.enchantLevels[i1];
			OpenGLHelper.colour(1.0F, 1.0F, 1.0F);

			if (j1 == 0)
				drawTexturedModalRect(var14, l + 14 + 19 * i1, 0, 185, 108, 19);
			else {
				String s1 = "" + j1;
				FontRenderer fontrenderer = mc.standardGalacticFontRenderer;
				int k1 = 6839882;

				if ((var12 < i1 + 1 || mc.thePlayer.experienceLevel < j1) && !mc.thePlayer.capabilities.isCreativeMode) {
					drawTexturedModalRect(var14, l + 14 + 19 * i1, 0, 185, 108, 19);
					drawTexturedModalRect(var14 + 1, l + 15 + 19 * i1, 16 * i1, 239, 16, 16);
					fontrenderer.drawSplitString(s, var15, l + 16 + 19 * i1, var16, (k1 & 16711422) >> 1);
					k1 = 4226832;
				} else {
					int l1 = mouseX - (k + 60);
					int i2 = mouseY - (l + 14 + 19 * i1);

					if (l1 >= 0 && i2 >= 0 && l1 < 108 && i2 < 19) {
						drawTexturedModalRect(var14, l + 14 + 19 * i1, 0, 204, 108, 19);
						k1 = 16777088;
					} else
						drawTexturedModalRect(var14, l + 14 + 19 * i1, 0, 166, 108, 19);

					drawTexturedModalRect(var14 + 1, l + 15 + 19 * i1, 16 * i1, 223, 16, 16);
					fontrenderer.drawSplitString(s, var15, l + 16 + 19 * i1, var16, k1);
					k1 = 8453920;
				}

				fontrenderer = mc.fontRenderer;
				fontrenderer.drawStringWithShadow(s1, var15 + 86 - fontrenderer.getStringWidth(s1), l + 16 + 19 * i1 + 7, k1);
				OpenGLHelper.colour(1, 1, 1);
			}
		}
	}

	/**
	 * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
	 */
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		boolean var4 = mc.thePlayer.capabilities.isCreativeMode;
		int var5 = field_147075_G.func_178147_e();

		for (int var6 = 0; var6 < 3; ++var6) {
			int var7 = field_147075_G.enchantLevels[var6];
			int var8 = field_147075_G.field_178151_h[var6];
			int var9 = var6 + 1;

			if (func_146978_c(60, 14 + 19 * var6, 108, 17, mouseX, mouseY) && var7 > 0 && var8 >= 0) {
				ArrayList<String> var10 = Lists.newArrayList();
				String var11;
				Enchantment ench = Enchantment.enchantmentsList[var8 % Enchantment.enchantmentsList.length];
				if (ench != null) {
					var11 = ench.getTranslatedName((var8) >> 8);
					var10.add(EnumChatFormatting.WHITE.toString() + EnumChatFormatting.ITALIC + I18n.format("container.enchant.clue", var11));
				}

				if (!var4) {
					if(ench != null) {
						var10.add("");
					}

					if (mc.thePlayer.experienceLevel < var7)
						var10.add(EnumChatFormatting.RED + I18n.format("container.enchant.level.required") + ": " + field_147075_G.enchantLevels[var6]);
					else {
						if (displayStacks.size() > 1) {
							if (viewingTicks > 40) {
								viewingTicks = 0;
								ArrayList<ItemStack> tempList = new ArrayList<>(displayStacks);
								tempList.remove(displayItem);
								displayItem = tempList.get(displayRand.nextInt(tempList.size()));
								//We don't want to display the same item name twice in a row (because then it looks like it's being displayed for a longer time)
							}
						}
						if (!field_147075_G.noFuel) {
							String name;

							if (displayItem.isItemEqual(LAPIS)) {
								name = StatCollector.translateToLocal("container.enchant.lapis." + (var9 > 1 ? "many" : "one"));
							} else {
								name = "%s " + StatCollector.translateToLocal(displayItem.getUnlocalizedName() + ".name");
							}

							var11 = String.format(name, var9);
							var10.add((var5 >= var9 ? EnumChatFormatting.GRAY : EnumChatFormatting.RED) + var11);
						}

						var11 = I18n.format(var9 == 1 ? "container.enchant.level.one" : "container.enchant.level.many", var9);

						var10.add(EnumChatFormatting.GRAY + var11);
					}
				}

				drawHoveringText(var10, mouseX, mouseY, fontRendererObj);
				break;
			}
		}
	}

	public void func_147068_g() {
		ItemStack var1 = inventorySlots.getSlot(0).getStack();

		if (!ItemStack.areItemStacksEqual(var1, field_147077_B)) {
			field_147077_B = var1;

			do
				field_147082_x += field_147074_F.nextInt(4) - field_147074_F.nextInt(4);
			while (field_147071_v <= field_147082_x + 1.0F && field_147071_v >= field_147082_x - 1.0F);
		}

		++field_147073_u;
		field_147069_w = field_147071_v;
		field_147076_A = field_147080_z;
		boolean var2 = false;

		for (int var3 = 0; var3 < 3; ++var3)
			if (field_147075_G.enchantLevels[var3] != 0) {
				var2 = true;
				break;
			}

		if (var2)
			field_147080_z += 0.2F;
		else
			field_147080_z -= 0.2F;

		field_147080_z = MathHelper.clamp_float(field_147080_z, 0.0F, 1.0F);
		float var5 = (field_147082_x - field_147071_v) * 0.4F;
		float var4 = 0.2F;
		var5 = MathHelper.clamp_float(var5, -var4, var4);
		field_147081_y += (var5 - field_147081_y) * 0.9F;
		field_147071_v += field_147081_y;
	}
}