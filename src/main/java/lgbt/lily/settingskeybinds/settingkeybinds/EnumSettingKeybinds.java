package lgbt.lily.settingskeybinds.settingkeybinds;

import lgbt.lily.settingskeybinds.config.SettingKeybindsConfig;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.option.GameOptions;
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
            return new LiteralText(name);
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
}
