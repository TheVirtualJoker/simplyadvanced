package com.joker.simplyadvanced.common.proxy;

import com.joker.simplyadvanced.client.render.KilnItemRender;
import com.joker.simplyadvanced.common.init.ModBlocks;
import com.joker.simplyadvanced.common.tiles.machines.powered.TileEntityKiln;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {
    @Override
    public void register() {
        ModBlocks.registerRenders();

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityKiln.class, new KilnItemRender());
    }
}
