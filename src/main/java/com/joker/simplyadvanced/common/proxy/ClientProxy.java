package com.joker.simplyadvanced.common.proxy;

import com.joker.simplyadvanced.common.init.ModBlocks;

public class ClientProxy extends CommonProxy {
    @Override
    public void register() {
        ModBlocks.registerRender(ModBlocks.alloySmelter);
    }
}
