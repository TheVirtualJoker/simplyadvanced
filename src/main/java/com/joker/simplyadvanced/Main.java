package com.joker.simplyadvanced;

import com.joker.simplyadvanced.common.init.Items;
import com.joker.simplyadvanced.common.lib.References;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = References.MODID, version = References.VERSION)
public class Main
{

    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        Items.init();
        Items.reg();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {

    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event){

    }
}
