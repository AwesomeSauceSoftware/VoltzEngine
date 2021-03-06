package com.builtbroken.mc.core.registry;

import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import com.builtbroken.mc.lib.render.block.RenderTileDummy;

public class ClientRegistryProxy extends CommonRegistryProxy
{
	@Override
	public void registerDummyRenderer(Class<? extends TileEntity> clazz)
	{
		if (!TileEntityRendererDispatcher.instance.mapSpecialRenderers.containsKey(clazz))
		{
			ClientRegistry.bindTileEntitySpecialRenderer(clazz, new RenderTileDummy());
		}
	}
}
