package lgbt.lily.settingskeybinds;

import lgbt.lily.settingskeybinds.config.SettingsKeybindsConfig;
import lgbt.lily.settingskeybinds.settingkeybinds.*;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.*;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.Difficulty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;

public class SettingsKeybinds implements ClientModInitializer {
	public static final String MODID = "settingskeybinds";
	public static final Logger LOGGER = LogManager.getLogger(MODID);

	public static final SettingKeybinds<?>[] SETTING_KEYBINDS = new SettingKeybinds[] {
		new BooleanSettingKeybinds("autoJump",
			o -> o.autoJump,
			(o, x) -> o.autoJump = x),
		new BooleanSettingKeybinds("viewBobbing",
			o -> o.bobView,
			(o, x) -> o.bobView = x),
		new BooleanSettingKeybinds("showSubtitles",
			o -> o.showSubtitles,
			(o, x) -> o.showSubtitles = x),
		new BooleanSettingKeybinds("advancedItemTooltips",
			o -> o.advancedItemTooltips,
			(o, x) -> o.advancedItemTooltips = x),
		new BooleanSettingKeybinds("heldItemTooltips",
			o -> o.heldItemTooltips,
			(o, x) -> o.heldItemTooltips = x),
		new BooleanSettingKeybinds("sneak",
			o -> o.sneakToggled,
			(o, x) -> o.sneakToggled = x),
		new BooleanSettingKeybinds("sprint",
			o -> o.sprintToggled,
			(o, x) -> o.sprintToggled = x),
		new IntegerSettingKeybinds("guiScale",
			o -> o.guiScale,
			(o, x) -> {
				o.guiScale = x;
				MinecraftClient.getInstance().onResolutionChanged();
			},
			0, 5, 1, true),
		new DoubleSettingKeybinds("renderDistance", Option.RENDER_DISTANCE,
			1.0, List.of(4.0, 8.0, 12.0, 16.0)),
		new DoubleSettingKeybinds("simulationDistance", Option.SIMULATION_DISTANCE,
			1.0, List.of(5.0, 8.0, 12.0, 16.0)),
		new DoubleSettingKeybinds("gamma", Option.GAMMA,
			0.05, List.of(0.0, 0.25, 0.5, 0.75, 1.0)),
		new DoubleSettingKeybinds("framerateLimit", Option.FRAMERATE_LIMIT,
			10.0, Arrays.asList(30.0, 60.0, 120.0, 240.0, 260.0)),
		new DoubleSettingKeybinds("fov", Option.FOV,
			1.0, List.of(70.0, 90.0, 110.0)),
		new DoubleSettingKeybinds("sensitivity", Option.SENSITIVITY,
			0.05, List.of(0.3, 0.4, 0.5, 0.6, 0.7)),
		new EnumSettingKeybinds<>("attackIndicator",
			o -> o.attackIndicator,
			(o, x) -> o.attackIndicator = x,
			x -> new TranslatableText(x.getTranslationKey()),
			AttackIndicator::values, AttackIndicator.class),
		new EnumSettingKeybinds<>("graphics",
			o -> o.graphicsMode,
			(o, x) -> o.graphicsMode = x,
			x -> new TranslatableText(x.getTranslationKey()),
			GraphicsMode::values, GraphicsMode.class),
		new EnumSettingKeybinds<>("particles",
			o -> o.particles,
			(o, x) -> o.particles = x,
			x -> new TranslatableText(x.getTranslationKey()),
			ParticlesMode::values, ParticlesMode.class),
		new EnumSettingKeybinds<>("difficulty",
			o -> o.difficulty,
			(o, x) -> {
				IntegratedServer s = MinecraftClient.getInstance().getServer();
				if (s != null) s.setDifficulty(x, true);
			},
			Difficulty::getTranslatableName,
			Difficulty::values, Difficulty.class),
	};

	public static SettingsKeybindsConfig CONFIG;

	public static void loadConfig() {
		CONFIG = AutoConfig.getConfigHolder(SettingsKeybindsConfig.class).getConfig();
		CONFIG.writeToSettingKeybinds(SETTING_KEYBINDS);
	}

	public static void saveConfig() {
		CONFIG.readFromSettingKeybinds(SETTING_KEYBINDS);
		AutoConfig.getConfigHolder(SettingsKeybindsConfig.class).save();
	}

	@Override
	public void onInitializeClient() {
		AutoConfig.register(SettingsKeybindsConfig.class, Toml4jConfigSerializer::new);
		loadConfig();
		saveConfig();

		for (SettingKeybinds<?> settingKeybinds : SETTING_KEYBINDS) {
			if (settingKeybinds.isEnabled()) settingKeybinds.init();
		}

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			for (SettingKeybinds<?> settingKeybinds : SETTING_KEYBINDS) {
				if (settingKeybinds.isEnabled() && settingKeybinds.handleTickEvent(client)) return;
			}
		});

		LOGGER.info("Initialized");
	}
}
