package lgbt.lily.settingskeybinds.settingkeybinds;

import lgbt.lily.settingskeybinds.config.SettingKeybindsConfig;
import lgbt.lily.settingskeybinds.config.SettingsKeybindsModMenu;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.option.DoubleOption;
import net.minecraft.client.option.GameOptions;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class DoubleSettingKeybinds extends NumberSettingKeybinds<Double> {
    public DoubleSettingKeybinds(String settingKey, DoubleOption doubleOption,
                                 Double stepValue, List<Double> settingValues) {
        this(settingKey, doubleOption::get, doubleOption::set, doubleOption.getMin(), doubleOption.getMax(),
            stepValue, settingValues);

    }
    public DoubleSettingKeybinds(String settingKey, Function<GameOptions, Double> getSetting,
                                 BiConsumer<GameOptions, Double> setSetting, Double minValue, Double maxValue,
                                 Double stepValue, List<Double> settingValues) {
        super(settingKey, getSetting, setSetting, minValue, maxValue, stepValue, settingValues);
    }

    @Override
    public void readFromConfig(SettingKeybindsConfig config) {
        setSettingValues(config.doubleSettingValues);
    }

    @Override
    public void writeToConfig(SettingKeybindsConfig config) {
        config.doubleSettingValues = getSettingValues();
    }

    @Override
    public AbstractConfigListEntry<?> getValuesConfigGuiEntry(ConfigEntryBuilder entryBuilder) {
        return entryBuilder
            .startDoubleList(SettingsKeybindsModMenu.SETTING_VALUES_ENTRY_TEXT, getSettingValues())
            .setTooltip(SettingsKeybindsModMenu.SETTING_VALUES_TOOLTIP_TEXT)
            .setSaveConsumer(this::setSettingValues)
            .setExpanded(true).requireRestart().build();
    }

    @Override
    Double add(Double x, Double y) {
        return x + y;
    }
    @Override
    Double sub(Double x, Double y) {
        return x - y;
    }
    @Override
    boolean lt(Double x, Double y) {
        return x < y;
    }
    @Override
    boolean gt(Double x, Double y) {
        return x > y;
    }
}
