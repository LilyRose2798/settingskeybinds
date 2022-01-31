package lgbt.lily.settingskeybinds.settingkeybinds;

import lgbt.lily.settingskeybinds.Utils;
import lgbt.lily.settingskeybinds.config.SettingKeybindsConfig;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnumSettingKeybinds<T extends Enum<T>> extends SettingKeybinds<T> {
    private final Function<T, Text> getValueText;
    private final Class<T> clazz;
    private KeyBinding cycleKeyBinding;

    public EnumSettingKeybinds(String settingKey, Function<GameOptions, T> getSetting,
                               BiConsumer<GameOptions, T> setSetting, Function<T, Text> getValueText,
                               Supplier<T[]> getSettingValues, Class<T> clazz) {
        this(settingKey, getSetting, setSetting, getValueText,
            Arrays.stream(getSettingValues.get()).collect(Collectors.toList()), clazz);
    }
    public EnumSettingKeybinds(String settingKey, Function<GameOptions, T> getSetting,
                               BiConsumer<GameOptions, T> setSetting, Function<T, Text> getValueText,
                               List<T> settingValues, Class<T> clazz) {
        super(settingKey, getSetting, setSetting, settingValues);
        this.clazz = clazz;
        this.getValueText = getValueText;
    }

    @Override
    public void init() {
        super.init();
        cycleKeyBinding = Utils.getKeyBinding(getSettingKey(), "cycle");
        KeyBindingHelper.registerKeyBinding(cycleKeyBinding);
    }

    @Override
    public String getSettingValueKey(T settingValue) {
        return settingValue.name();
    }

    @Override
    public void readFromConfig(SettingKeybindsConfig config) {
        setSettingValuesByName(config.enumSettingValues);
    }

    @Override
    public void writeToConfig(SettingKeybindsConfig config) {
        config.enumSettingValues = getSettingValuesByName();
    }

    @Override
    public AbstractConfigListEntry<?> getValuesConfigGuiEntry(ConfigEntryBuilder entryBuilder) {
        return getValueTogglesConfigGuiEntry(entryBuilder, List.of(clazz.getEnumConstants()));
    }

    @Override
    public Text getValueT9nText(T value) {
        return getValueText.apply(value);
    }
    @Override
    public Text getValueT9nText(String name) {
        try {
            return getValueT9nText(T.valueOf(clazz, name));
        } catch (IllegalArgumentException e) {
            return super.getValueT9nText(name);
        }
    }

    public List<String> getSettingValuesByName() {
        return getSettingValues().stream().map(T::name).collect(Collectors.toList());
    }

    public void setSettingValuesByName(List<String> names) {
        setSettingValues(names.stream().flatMap(name -> {
            try {
                return Stream.of(T.valueOf(clazz, name));
            } catch (IllegalArgumentException e) {
                return Stream.empty();
            }
        }).collect(Collectors.toList()));
    }

    public T getNextValue(T value) {
        int index = value.ordinal();
        int nextIndex = index + 1;
        T[] values = clazz.getEnumConstants();
        nextIndex %= values.length;
        return values[nextIndex];
    }

    public boolean checkCycleKeyBinding(MinecraftClient client) {
        if (!cycleKeyBinding.wasPressed()) return false;
        setSetting(client, getNextValue(getSetting(client)));
        return true;
    }

    @Override
    public boolean handleTickEvent(MinecraftClient client) {
        return checkCycleKeyBinding(client) ||
            super.handleTickEvent(client);
    }
}
