package com.joker.simplyadvanced.common.config;

import com.joker.simplyadvanced.common.lib.References;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.DummyConfigElement.DummyCategoryElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.GuiConfigEntries.CategoryEntry;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ConfigGuiFactory implements IModGuiFactory {

    @Override
    public void initialize(Minecraft minecraftInstance) {
    }

    @Override
    public boolean hasConfigGui() {
        return true;
    }

    public static class CoreConfigGui extends GuiConfig {
        public CoreConfigGui(GuiScreen parentScreen) {
            super(parentScreen, getConfigElements(), References.MODID, false, false, "Simply Advanced Config");
        }

        private static List<IConfigElement> getConfigElements() {
            List<IConfigElement> list = new ArrayList<>();
            list.add(new DummyCategoryElement("Client Settings", "gui.config.category.client",
                    CategoryClient.class));
            list.add(new DummyCategoryElement("GUI Settings", "gui.config.category.gui",
                    CategoryGUI.class));
            return list;
        }

        public static class CategoryClient extends CategoryEntry {

            public CategoryClient(GuiConfig owningScreen, GuiConfigEntries owningEntryList,
                                  IConfigElement configElement) {
                super(owningScreen, owningEntryList, configElement);
            }

            @Override
            protected GuiScreen buildChildScreen() {
                Configuration config = Config.getConfig();
                ConfigElement category = new ConfigElement(config.getCategory(Config.CLIENT));
                List<IConfigElement> propertiesOnThisScreen = category.getChildElements();
                String windowTitle = "Client Settings";
                return new GuiConfig(this.owningScreen, propertiesOnThisScreen, this.owningScreen.modID,
                        Config.CLIENT,
                        this.configElement.requiresWorldRestart() || this.owningScreen.allRequireWorldRestart,
                        this.configElement.requiresMcRestart() || this.owningScreen.allRequireMcRestart, windowTitle);
            }
        }
        public static class CategoryGUI extends CategoryEntry {

            public CategoryGUI(GuiConfig owningScreen, GuiConfigEntries owningEntryList,
                                  IConfigElement configElement) {
                super(owningScreen, owningEntryList, configElement);
            }

            @Override
            protected GuiScreen buildChildScreen() {
                Configuration config = Config.getConfig();
                ConfigElement category = new ConfigElement(config.getCategory(Config.GUI));
                List<IConfigElement> propertiesOnThisScreen = category.getChildElements();
                String windowTitle = "GUI Settings";
                return new GuiConfig(this.owningScreen, propertiesOnThisScreen, this.owningScreen.modID,
                        Config.GUI,
                        this.configElement.requiresWorldRestart() || this.owningScreen.allRequireWorldRestart,
                        this.configElement.requiresMcRestart() || this.owningScreen.allRequireMcRestart, windowTitle);
            }
        }
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen parentScreen) {
        return new CoreConfigGui(parentScreen);
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }
}