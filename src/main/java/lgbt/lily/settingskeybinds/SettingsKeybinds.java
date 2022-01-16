package lgbt.lily.settingskeybinds;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.TranslatableText;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SettingsKeybinds implements ModInitializer {

	public static final Logger LOGGER = LogManager.getLogger("settingskeybinds");
	public static final int MIN_GUI_SCALE = 0;
	public static final int MAX_GUI_SCALE = 5;

	@Override
	public void onInitialize() {
		final KeyBinding guiScaleDecreaseKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"key.settingskeybinds.guiscale.decrease", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_G,
			"category.settingskeybinds"));
		final KeyBinding guiScaleIncreaseKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"key.settingskeybinds.guiscale.increase", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_H,
			"category.settingskeybinds"));
		final Map<Integer, KeyBinding> guiScaleKeyBindings = IntStream.rangeClosed(MIN_GUI_SCALE, MAX_GUI_SCALE)
			.boxed().collect(Collectors.toMap(Function.identity(), n -> KeyBindingHelper.registerKeyBinding(
				new KeyBinding(String.format("key.settingskeybinds.guiscale.%d", n),
					InputUtil.Type.KEYSYM, -1, "category.settingskeybinds"))));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (guiScaleDecreaseKeyBinding.wasPressed()) {
				if (client.options.guiScale > MIN_GUI_SCALE)
					setGuiScale(client, client.options.guiScale - 1);
			} else if (guiScaleIncreaseKeyBinding.wasPressed()) {
				if (client.options.guiScale < MAX_GUI_SCALE)
					setGuiScale(client, client.options.guiScale + 1);
			} else {
				guiScaleKeyBindings.forEach((n, kb) -> {
					if (kb.wasPressed())
						setGuiScale(client, n);
				});
			}
		});

		LOGGER.info("Registered settings keybinds");
	}

	private void setGuiScale(MinecraftClient client, int guiScale) {
		client.options.guiScale = guiScale;
		client.onResolutionChanged();
		if (client.player != null)
			client.player.sendMessage(new TranslatableText("settingskeybinds.guiscale.set", guiScale), false);
	}
}
