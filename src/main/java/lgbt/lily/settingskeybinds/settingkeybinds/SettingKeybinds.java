package lgbt.lily.settingskeybinds.settingkeybinds;

import lgbt.lily.settingskeybinds.Utils;
import lgbt.lily.settingskeybinds.config.SettingKeybindsConfig;
import lgbt.lily.settingskeybinds.config.SettingsKeybindsModMenu;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Language;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class SettingKeybinds<T> {
    private boolean enabled = true;
    private final String settingKey;
    private List<T> settingValues;
    private final Function<GameOptions, T> getSetting;
    private final BiConsumer<GameOptions, T> setSetting;
    private Map<T, KeyBinding> valueKeyBindings;
    private static final String LITERAL_VALUE_FORMAT_KEY =
        Utils.getT9nKey(Utils.T9nType.OUTPUT, "literalValueFormat");

    public SettingKeybinds(String settingKey, Function<GameOptions, T> getSetting,
                           BiConsumer<GameOptions, T> setSetting, List<T> settingValues) {
        this.settingKey = settingKey;
        this.getSetting = getSetting;
        this.setSetting = setSetting;
        this.settingValues = settingValues;
    }

    public String getSettingValueKey(T settingValue) {
        return settingValue.toString();
    }

    public void init() {
        valueKeyBindings = settingValues.stream().collect(Collectors.toMap(Function.identity(), n ->
            Utils.getKeyBinding(settingKey, getSettingValueKey(n))));
        valueKeyBindings.values().forEach(KeyBindingHelper::registerKeyBinding);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getSettingKey() {
        return settingKey;
    }

    public List<T> getSettingValues() {
        return settingValues;
    }

    public void setSettingValues(List<T> settingValues) {
        this.settingValues = settingValues;
    }

    public abstract void readFromConfig(SettingKeybindsConfig config);
    public abstract void writeToConfig(SettingKeybindsConfig config);

    public abstract AbstractConfigListEntry<?> getValuesConfigGuiEntry(ConfigEntryBuilder entryBuilder);

    public AbstractConfigListEntry<?> getValueTogglesConfigGuiEntry(ConfigEntryBuilder entryBuilder, List<T> values) {
        SubCategoryBuilder subBuilder = entryBuilder
            .startSubCategory(SettingsKeybindsModMenu.SETTING_VALUES_ENTRY_TEXT)
            .setTooltip(SettingsKeybindsModMenu.SETTING_VALUES_TOOLTIP_TEXT).setExpanded(true);
        Set<T> valueSet = new HashSet<>(getSettingValues());
        for (T value : values)
            subBuilder.add(entryBuilder
                .startBooleanToggle(getValueT9nText(value), valueSet.contains(value))
                .setSaveConsumer(enabled -> {
                    if (enabled) valueSet.add(value);
                    else valueSet.remove(value);
                    setSettingValues(valueSet.stream().toList());
                })
                .setDefaultValue(true).requireRestart().build());
        return subBuilder.build();
    }

    public Text getSettingT9nText() {
        Language lang = Language.getInstance();
        final String settingKeyName = Utils.getT9nKey(Utils.T9nType.NAME, settingKey);
        if (lang.hasTranslation(settingKeyName)) return new TranslatableText(settingKeyName);
        final String settingKeyKey = Utils.getT9nKey(Utils.T9nType.VANILLA_KEY, settingKey);
        if (lang.hasTranslation(settingKeyKey)) return new TranslatableText(settingKeyKey);
        return new TranslatableText(Utils.getT9nKey(Utils.T9nType.VANILLA_OPTIONS, settingKey));
    }

    public Text getValueT9nText(T value) {
        return getValueT9nText(value.toString());
    }
    public Text getValueT9nText(String valueStr) {
        Language lang = Language.getInstance();
        final String valueKey = Utils.getT9nKey(Utils.T9nType.VALUE, settingKey, valueStr);
        if (lang.hasTranslation(valueKey)) return new TranslatableText(valueKey);
        final String valueKeyBase = Utils.getT9nKey(Utils.T9nType.VALUE, valueStr);
        if (lang.hasTranslation(valueKeyBase)) return new TranslatableText(valueKeyBase);
        return new TranslatableText(LITERAL_VALUE_FORMAT_KEY, valueStr);
    }

    public T getSetting(MinecraftClient client) {
        return getSetting.apply(client.options);
    }

    public void setSetting(MinecraftClient client, T value) {
        setSetting.accept(client.options, value);
        Utils.sendPlayerOutputMessage(client, "setSetting", getSettingT9nText(), getValueT9nText(value));
    }

    public boolean checkValueKeyBindings(MinecraftClient client) {
        for (Map.Entry<T, KeyBinding> entry : valueKeyBindings.entrySet()) {
            if (entry.getValue().wasPressed()) {
                setSetting(client, entry.getKey());
                return true;
            }
        }
        return false;
    }

    public boolean handleTickEvent(MinecraftClient client) {
        return checkValueKeyBindings(client);
    }
}
