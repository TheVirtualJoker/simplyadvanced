package com.joker.simplyadvanced.common.proxy;

import com.joker.simplyadvanced.common.init.Blocks;

public class ClientProxy extends CommonProxy {
    @Override
    public void register() {
        Blocks.registerRender(Blocks.alloySmelter);
    }
}
