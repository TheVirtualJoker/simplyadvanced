package com.joker.simplyadvanced.common.config;

import com.joker.simplyadvanced.common.lib.References;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Config {
	private static Configuration config = null;
	public static final String CLIENT = "client", GUI = "gui";
	public static boolean RENDER_ITEM,
			SHOW_PARTICLES;
	public static boolean TASK_PERCENTAGE,
			POWER_PERCENTAGE,
			POWER_STORED;

	public static void preInit() {
		File configFile = new File(Loader.instance().getConfigDir(), "SimplyAdvanced.cfg");
		config = new Configuration(configFile);
		syncFromFile();
	}

	public static void clientPreInit() {
		MinecraftForge.EVENT_BUS.register(new ConfigEventHandler());
	}

	public static Configuration getConfig() {
		return config;
	}

	public static void syncFromFile() {
		syncConfig(true);
	}

	public static void syncFromGUI() {
		syncConfig(false);
	}

	private static void syncConfig(boolean loadConfigFromFile) {
		if (loadConfigFromFile) config.load();

		{ // This section is for the GUI config
			Property powerPercent = config.get(GUI, "Power Percentage", true);
			Property progressComplete = config.get(GUI, "Task Percentage", true);
			Property powerStored = config.get(GUI, "Power Stored", true);

			// Set the comments for the Properties
			powerPercent.setComment("Show the percentage of RF stored in the block");
			progressComplete.setComment("Show the percentage for the task completed");
			powerStored.setComment("Show the minimum and maximum power stored in the block");

			// Handles the order of the properties
			List<String> propertyOrder = new ArrayList<>();
			propertyOrder.add(powerPercent.getName());
			propertyOrder.add(progressComplete.getName());
			propertyOrder.add(powerStored.getName());
			config.setCategoryPropertyOrder(GUI, propertyOrder);

			// Updates the Values
			POWER_PERCENTAGE = powerPercent.getBoolean();
			TASK_PERCENTAGE = progressComplete.getBoolean();
			POWER_STORED = powerStored.getBoolean();
			powerPercent.set(POWER_PERCENTAGE);
			progressComplete.set(TASK_PERCENTAGE);
			powerStored.set(POWER_STORED);
		}

		{ // This section is for the Client Config Section
			Property itemRendering = config.get(CLIENT, "Item Rendering", true);
			Property particles = config.get(CLIENT, "Particles", true);

			// Set the comments for the Properties
			itemRendering.setComment("Do you want to see the Items rendered with our blocks");
			particles.setComment("Do you want to see the Particles rendered with our blocks");

			// Handles the order of the properties
			List<String> propertyOrder = new ArrayList<>();
			propertyOrder.add(itemRendering.getName());
			propertyOrder.add(particles.getName());
			config.setCategoryPropertyOrder(CLIENT, propertyOrder);

			// Updates the Values
			RENDER_ITEM = itemRendering.getBoolean();
			SHOW_PARTICLES = particles.getBoolean();
			itemRendering.set(RENDER_ITEM);
			particles.set(SHOW_PARTICLES);
		}

		if (config.hasChanged()) config.save();
	}

	public static class ConfigEventHandler {
		@SubscribeEvent(priority = EventPriority.NORMAL)
		public void onEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
			if (References.MODID.equalsIgnoreCase(event.getModID())) {
				syncFromGUI();
			}
		}
	}
}