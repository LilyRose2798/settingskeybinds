package lgbt.lily.settingskeybinds.settingkeybinds;

import lgbt.lily.settingskeybinds.config.SettingKeybindsConfig;
import lgbt.lily.settingskeybinds.config.SettingsKeybindsModMenu;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.option.GameOptions;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.IntStream;

public class IntegerSettingKeybinds extends NumberSettingKeybinds<Integer> {
    public IntegerSettingKeybinds(String settingKey, Function<GameOptions, Integer> getSetting,
                                  BiConsumer<GameOptions, Integer> setSetting, Integer minValue, Integer maxValue,
                                  Integer stepValue, boolean rangeClosed) {
        this(settingKey, getSetting, setSetting, minValue, maxValue, stepValue, (rangeClosed ?
            IntStream.rangeClosed(minValue, maxValue) : IntStream.range(minValue, maxValue)).boxed().toList());
    }
    public IntegerSettingKeybinds(String settingKey, Function<GameOptions, Integer> getSetting,
                                  BiConsumer<GameOptions, Integer> setSetting, Integer minValue, Integer maxValue,
                                  Integer stepValue, List<Integer> settingValues) {
        super(settingKey, getSetting, setSetting, minValue, maxValue, stepValue, settingValues);
    }

    @Override
    public void readFromConfig(SettingKeybindsConfig config) {
        setSettingValues(config.integerSettingValues);
    }

    @Override
    public void writeToConfig(SettingKeybindsConfig config) {
        config.integerSettingValues = getSettingValues();
    }

    @Override
    public AbstractConfigListEntry<?> getValuesConfigGuiEntry(ConfigEntryBuilder entryBuilder) {
        return entryBuilder
            .startIntList(SettingsKeybindsModMenu.SETTING_VALUES_ENTRY_TEXT, getSettingValues())
            .setTooltip(SettingsKeybindsModMenu.SETTING_VALUES_TOOLTIP_TEXT)
            .setSaveConsumer(this::setSettingValues)
            .setExpanded(true).requireRestart().build();
    }

    @Override
    Integer add(Integer x, Integer y) {
        return x + y;
    }
    @Override
    Integer sub(Integer x, Integer y) {
        return x - y;
    }
    @Override
    boolean lt(Integer x, Integer y) {
        return x < y;
    }
    @Override
    boolean gt(Integer x, Integer y) {
        return x > y;
    }
}
