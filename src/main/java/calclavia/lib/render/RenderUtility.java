package calclavia.lib.render;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_FLAT;
import static org.lwjgl.opengl.GL11.GL_LINE_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_POLYGON_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glShadeModel;
import static org.lwjgl.opengl.GL11.glTranslatef;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeDirection;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

public class RenderUtility
{
	public static RenderBlocks renderBlocks = new RenderBlocks();
	public static final ResourceLocation PARTICLE_RESOURCE = new ResourceLocation("textures/particle/particles.png");

	public static void setTerrainTexture()
	{
		setSpriteTexture(0);
	}

	public static void setSpriteTexture(ItemStack itemStack)
	{
		setSpriteTexture(itemStack.getItemSpriteNumber());
	}

	public static void setSpriteTexture(int sprite)
	{
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(FMLClientHandler.instance().getClient().renderEngine.getResourceLocation(sprite));
	}

	/**
	 * Enables blending.
	 */
	public static void enableBlending()
	{
		glShadeModel(GL_SMOOTH);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

	/**
	 * Disables blending.
	 */
	public static void disableBlending()
	{
		glShadeModel(GL_FLAT);
		glDisable(GL_LINE_SMOOTH);
		glDisable(GL_POLYGON_SMOOTH);
		glDisable(GL_BLEND);
	}

	public static void enableLighting()
	{
		RenderHelper.enableStandardItemLighting();
	}

	/**
	 * Disables lighting and turns glow on.
	 */
	public static void disableLighting()
	{
		RenderHelper.disableStandardItemLighting();
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
	}

	public static void disableLightmap()
	{
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}

	public static void enableLightmap()
	{
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}

	public static void renderNormalBlockAsItem(Block block, int metadata, RenderBlocks renderer)
	{
		Tessellator tessellator = Tessellator.instance;

		block.setBlockBoundsForItemRender();
		renderer.setRenderBoundsFromBlock(block);
		glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
		glTranslatef(-0.5F, -0.5F, -0.5F);
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, -1.0F, 0.0F);
		renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, -1.0F);
		renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 2, metadata));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 3, metadata));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(-1.0F, 0.0F, 0.0F);
		renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 4, metadata));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 5, metadata));
		tessellator.draw();
		glTranslatef(0.5F, 0.5F, 0.5F);
	}

	public static void renderFloatingText(String text, float x, float y, float z)
	{
		renderFloatingText(text, x, y, z, 0xFFFFFF);
	}

	/**
	 * @author OpenBlocks
	 */
	public static void rotateFacesOnRenderer(Block block, ForgeDirection rotation, RenderBlocks renderer, boolean fullRotation)
	{
		if (fullRotation)
		{
			switch (rotation)
			{
				case DOWN:
					renderer.uvRotateSouth = 3;
					renderer.uvRotateNorth = 3;
					renderer.uvRotateEast = 3;
					renderer.uvRotateWest = 3;
					break;
				case EAST:
					renderer.uvRotateTop = 1;
					renderer.uvRotateBottom = 2;
					renderer.uvRotateWest = 1;
					renderer.uvRotateEast = 2;
					break;
				case NORTH:
					renderer.uvRotateNorth = 2;
					renderer.uvRotateSouth = 1;
					break;
				case SOUTH:
					renderer.uvRotateTop = 3;
					renderer.uvRotateBottom = 3;
					renderer.uvRotateNorth = 1;
					renderer.uvRotateSouth = 2;
					break;
				case UNKNOWN:
					break;
				case UP:
					break;
				case WEST:
					renderer.uvRotateTop = 2;
					renderer.uvRotateBottom = 1;
					renderer.uvRotateWest = 2;
					renderer.uvRotateEast = 1;
					break;
				default:
					break;

			}
		}
		else
		{
			switch (rotation)
			{
				case EAST:
					renderer.uvRotateTop = 1;
					break;
				case WEST:
					renderer.uvRotateTop = 2;
					break;
				case SOUTH:
					renderer.uvRotateTop = 3;
					break;
				default:
					break;
			}
		}
	}

	public static void resetFacesOnRenderer(RenderBlocks renderer)
	{
		renderer.uvRotateTop = 0;
		renderer.uvRotateBottom = 0;
		renderer.uvRotateEast = 0;
		renderer.uvRotateNorth = 0;
		renderer.uvRotateSouth = 0;
		renderer.uvRotateTop = 0;
		renderer.uvRotateWest = 0;
		renderer.flipTexture = false;
	}

	public static void renderInventoryBlock(RenderBlocks renderer, Block block, ForgeDirection rotation)
	{
		renderInventoryBlock(renderer, block, rotation, -1);
	}

	public static void renderInventoryBlock(RenderBlocks renderer, Block block, ForgeDirection rotation, int colorMultiplier)
	{
		renderInventoryBlock(renderer, block, rotation, colorMultiplier, null);
	}

	public static void renderInventoryBlock(RenderBlocks renderer, Block block, ForgeDirection rotation, int colorMultiplier, Set<ForgeDirection> enabledSides)
	{
		Tessellator tessellator = Tessellator.instance;
		block.setBlockBoundsForItemRender();
		renderer.setRenderBoundsFromBlock(block);

		float r;
		float g;
		float b;
		if (colorMultiplier > -1)
		{
			r = (colorMultiplier >> 16 & 255) / 255.0F;
			g = (colorMultiplier >> 8 & 255) / 255.0F;
			b = (colorMultiplier & 255) / 255.0F;
			GL11.glColor4f(r, g, b, 1.0F);
		}
		// Learn to matrix, please push and pop :D -- NC
		GL11.glPushMatrix();
		GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		int metadata = rotation.ordinal();
		if (enabledSides == null || enabledSides.contains(ForgeDirection.DOWN))
		{
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, -1.0F, 0.0F);
			renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
			tessellator.draw();
		}
		if (enabledSides == null || enabledSides.contains(ForgeDirection.UP))
		{
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 1.0F, 0.0F);
			renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));
			tessellator.draw();
		}
		if (enabledSides == null || enabledSides.contains(ForgeDirection.SOUTH))
		{
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, -1.0F);
			renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 2, metadata));
			tessellator.draw();
		}
		if (enabledSides == null || enabledSides.contains(ForgeDirection.NORTH))
		{
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, 1.0F);
			renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 3, metadata));
			tessellator.draw();
		}
		if (enabledSides == null || enabledSides.contains(ForgeDirection.WEST))
		{
			tessellator.startDrawingQuads();
			tessellator.setNormal(-1.0F, 0.0F, 0.0F);
			renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 4, metadata));
			tessellator.draw();
		}
		if (enabledSides == null || enabledSides.contains(ForgeDirection.EAST))
		{
			tessellator.startDrawingQuads();
			tessellator.setNormal(1.0F, 0.0F, 0.0F);
			renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 5, metadata));
			tessellator.draw();
		}
		GL11.glPopMatrix();
	}

	public static void renderCube(double x1, double y1, double z1, double x2, double y2, double z2, Block block)
	{
		renderCube(x1, y1, z1, x2, y2, z2, block, null);
	}

	public static void renderCube(double x1, double y1, double z1, double x2, double y2, double z2, Block block, Icon overrideTexture)
	{
		renderCube(x1, y1, z1, x2, y2, z2, block, overrideTexture, 0);
	}

	/**
	 * Renders a cube with custom block boundaries.
	 */
	public static void renderCube(double x1, double y1, double z1, double x2, double y2, double z2, Block block, Icon overrideTexture, int meta)
	{
		GL11.glPushMatrix();
		Tessellator t = Tessellator.instance;

		GL11.glColor4f(1, 1, 1, 1);

		renderBlocks.setRenderBounds(x1, y1, z1, x2, y2, z2);

		t.startDrawingQuads();

		Icon useTexture = overrideTexture != null ? overrideTexture : block.getIcon(0, meta);
		t.setNormal(0.0F, -1.0F, 0.0F);
		renderBlocks.renderFaceYNeg(block, 0, 0, 0, useTexture);

		useTexture = overrideTexture != null ? overrideTexture : block.getIcon(1, meta);
		t.setNormal(0.0F, 1.0F, 0.0F);
		renderBlocks.renderFaceYPos(block, 0, 0, 0, useTexture);

		useTexture = overrideTexture != null ? overrideTexture : block.getIcon(2, meta);
		t.setNormal(0.0F, 0.0F, -1.0F);
		renderBlocks.renderFaceZNeg(block, 0, 0, 0, useTexture);

		useTexture = overrideTexture != null ? overrideTexture : block.getIcon(3, meta);
		t.setNormal(0.0F, 0.0F, 1.0F);
		renderBlocks.renderFaceZPos(block, 0, 0, 0, useTexture);

		useTexture = overrideTexture != null ? overrideTexture : block.getIcon(4, meta);
		t.setNormal(-1.0F, 0.0F, 0.0F);
		renderBlocks.renderFaceXNeg(block, 0, 0, 0, useTexture);

		useTexture = overrideTexture != null ? overrideTexture : block.getIcon(5, meta);
		t.setNormal(1.0F, 0.0F, 0.0F);
		renderBlocks.renderFaceXPos(block, 0, 0, 0, useTexture);
		t.draw();

		GL11.glPopMatrix();
	}

	/**
	 * Renders a floating text in a specific position.
	 * 
	 * @author Briman0094
	 * 
	 */
	public static void renderFloatingText(String text, float x, float y, float z, int color)
	{
		RenderManager renderManager = RenderManager.instance;
		FontRenderer fontRenderer = renderManager.getFontRenderer();
		float scale = 0.027f;
		GL11.glColor4f(1f, 1f, 1f, 0.5f);
		GL11.glPushMatrix();
		GL11.glTranslatef(x + 0.0F, y + 2.3F, z);
		GL11.glNormal3f(0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		GL11.glScalef(-scale, -scale, scale);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDepthMask(false);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Tessellator tessellator = Tessellator.instance;
		int yOffset = 0;

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		tessellator.startDrawingQuads();
		int stringMiddle = fontRenderer.getStringWidth(text) / 2;
		tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.5F);
		tessellator.addVertex(-stringMiddle - 1, -1 + yOffset, 0.0D);
		tessellator.addVertex(-stringMiddle - 1, 8 + yOffset, 0.0D);
		tessellator.addVertex(stringMiddle + 1, 8 + yOffset, 0.0D);
		tessellator.addVertex(stringMiddle + 1, -1 + yOffset, 0.0D);
		tessellator.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(1f, 1f, 1f, 0.5f);
		fontRenderer.drawString(text, -fontRenderer.getStringWidth(text) / 2, yOffset, color);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		fontRenderer.drawString(text, -fontRenderer.getStringWidth(text) / 2, yOffset, color);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
	}

}