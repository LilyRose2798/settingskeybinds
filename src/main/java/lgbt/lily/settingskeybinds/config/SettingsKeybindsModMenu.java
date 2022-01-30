package lgbt.lily.settingskeybinds.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import lgbt.lily.settingskeybinds.SettingsKeybinds;
import lgbt.lily.settingskeybinds.Utils;
import lgbt.lily.settingskeybinds.settingkeybinds.*;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.text.TranslatableText;

public class SettingsKeybindsModMenu implements ModMenuApi {
    public static final TranslatableText GENERAL_CATEGORY_TEXT =
        Utils.getT9nText(Utils.T9nType.CONFIG_CATEGORY, "general");
    public static final TranslatableText USE_SEPARATE_CATEGORIES_ENTRY_TEXT =
        Utils.getT9nText(Utils.T9nType.CONFIG_ENTRY, "useSeparateCategories");
    public static final TranslatableText USE_SEPARATE_CATEGORIES_TOOLTIP_TEXT =
        Utils.getT9nText(Utils.T9nType.CONFIG_TOOLTIP, "useSeparateCategories");
    public static final TranslatableText ENABLED_ENTRY_TEXT =
        Utils.getT9nText(Utils.T9nType.CONFIG_ENTRY, "enabled");
    public static final TranslatableText ENABLED_TOOLTIP_TEXT =
        Utils.getT9nText(Utils.T9nType.CONFIG_TOOLTIP, "enabled");
    public static final TranslatableText SETTING_VALUES_ENTRY_TEXT =
        Utils.getT9nText(Utils.T9nType.CONFIG_ENTRY, "settingValues");
    public static final TranslatableText SETTING_VALUES_TOOLTIP_TEXT =
        Utils.getT9nText(Utils.T9nType.CONFIG_TOOLTIP, "settingValues");

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            final ConfigBuilder builder = ConfigBuilder.create().setParentScreen(parent)
                .setTitle(Utils.getT9nText(Utils.T9nType.TITLE, "config"))
                .setSavingRunnable(SettingsKeybinds::saveConfig);
            final ConfigEntryBuilder entryBuilder = builder.entryBuilder();
            final ConfigCategory general = builder.getOrCreateCategory(GENERAL_CATEGORY_TEXT);
            general.addEntry(entryBuilder.startBooleanToggle(USE_SEPARATE_CATEGORIES_ENTRY_TEXT,
                SettingsKeybinds.CONFIG.useSeparateCategories)
                .setTooltip(USE_SEPARATE_CATEGORIES_TOOLTIP_TEXT)
                .setSaveConsumer(b -> SettingsKeybinds.CONFIG.useSeparateCategories = b)
                .setDefaultValue(false).requireRestart().build());

            for (SettingKeybinds<?> settingKeybinds : SettingsKeybinds.SETTING_KEYBINDS) {
                final SubCategoryBuilder subBuilder = entryBuilder.startSubCategory(settingKeybinds.getSettingT9nText());
                subBuilder.add(entryBuilder
                    .startBooleanToggle(ENABLED_ENTRY_TEXT, settingKeybinds.isEnabled())
                    .setTooltip(ENABLED_TOOLTIP_TEXT)
                    .setSaveConsumer(settingKeybinds::setEnabled)
                    .setDefaultValue(true).requireRestart().build());
                subBuilder.add(settingKeybinds.getValuesConfigGuiEntry(entryBuilder));
                general.addEntry(subBuilder.build());
            }
            return builder.build();
        };
    }
}
