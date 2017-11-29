package com.joker.simplyadvanced;

import com.joker.simplyadvanced.client.gui.AlloyFurnaceGuiHandler;
import com.joker.simplyadvanced.common.init.Blocks;
import com.joker.simplyadvanced.common.init.Items;
import com.joker.simplyadvanced.common.lib.References;
import com.joker.simplyadvanced.common.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod(modid = References.MODID, version = References.VERSION)
public class Main
{

    @Mod.Instance(References.MODID)
    public static Main instance;

    @SidedProxy(clientSide = References.CLIENT_PROXY, serverSide = References.COMMON_PROXY)
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        Items.init();
        Items.reg();
        Blocks.init();
        Blocks.register();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.register();
        NetworkRegistry.INSTANCE.registerGuiHandler(References.MODID, new AlloyFurnaceGuiHandler());
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event){

    }
}
