package torcherino;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import torcherino.api.TorcherinoAPI;
import torcherino.blocks.Blocks;
import torcherino.config.Config;
import torcherino.network.Networker;

@Mod(Utilities.MOD_ID)
public class Torcherino
{
	public Torcherino()
	{
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		Config.initialise();
		Blocks.INSTANCE.initialise();
		Networker.INSTANCE.initialise();
		eventBus.register(Blocks.INSTANCE);
		eventBus.addListener(this::processIMC);
	}

	@SubscribeEvent public void processIMC(final InterModProcessEvent event)
	{
		// To use: in InterModEnqueueEvent call
		// InterModComms.sendTo( MOD_ID, Method , supplier);
		// See processMessage method below for method and what they take
		event.getIMCStream().forEach(this::processMessage);
	}

	public void processMessage(final InterModComms.IMCMessage message)
	{
		String method = message.getMethod();
		Object value = message.getMessageSupplier().get();
		if (method.equals("blacklist_block"))
		{
			if (value instanceof ResourceLocation) TorcherinoAPI.INSTANCE.blacklistBlock((ResourceLocation) value);
			else if (value instanceof Block) TorcherinoAPI.INSTANCE.blacklistBlock((Block) value);
		}
		else if (method.equals("blacklist_tile"))
		{
			if (value instanceof ResourceLocation) TorcherinoAPI.INSTANCE.blacklistTileEntity((ResourceLocation) value);
			else if (value instanceof TileEntityType) TorcherinoAPI.INSTANCE.blacklistTileEntity((TileEntityType) value);
		}
	}
}