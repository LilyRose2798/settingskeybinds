package lgbt.lily.settingskeybinds.settingkeybinds;

import lgbt.lily.settingskeybinds.Utils;
import lgbt.lily.settingskeybinds.config.SettingKeybindsConfig;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class BooleanSettingKeybinds extends SettingKeybinds<Boolean> {
    private KeyBinding toggleKeyBinding;

    public BooleanSettingKeybinds(String settingKey, Function<GameOptions, Boolean> getSetting,
                                  BiConsumer<GameOptions, Boolean> setSetting) {
        this(settingKey, getSetting, setSetting, Arrays.asList(true, false));
    }
    public BooleanSettingKeybinds(String settingKey, Function<GameOptions, Boolean> getSetting,
                                  BiConsumer<GameOptions, Boolean> setSetting, List<Boolean> settingValues) {
        super(settingKey, getSetting, setSetting, settingValues);
    }

    @Override
    public void init() {
        super.init();
        toggleKeyBinding = Utils.getKeyBinding(getSettingKey(), "toggle");
        KeyBindingHelper.registerKeyBinding(toggleKeyBinding);
    }

    @Override
    public void readFromConfig(SettingKeybindsConfig config) {
        setSettingValues(config.booleanSettingValues);
    }

    @Override
    public void writeToConfig(SettingKeybindsConfig config) {
        config.booleanSettingValues = getSettingValues();
    }

    @Override
    public AbstractConfigListEntry<?> getValuesConfigGuiEntry(ConfigEntryBuilder entryBuilder) {
        return getValueTogglesConfigGuiEntry(entryBuilder, List.of(true, false));
    }

    public boolean checkToggleKeyBinding(MinecraftClient client) {
        if (!toggleKeyBinding.wasPressed()) return false;
        setSetting(client, !getSetting(client));
        return true;
    }

    @Override
    public boolean handleTickEvent(MinecraftClient client) {
        return checkToggleKeyBinding(client) ||
            super.handleTickEvent(client);
    }
}
