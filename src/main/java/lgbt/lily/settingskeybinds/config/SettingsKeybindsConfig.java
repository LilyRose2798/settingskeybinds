package lgbt.lily.settingskeybinds.config;

import lgbt.lily.settingskeybinds.SettingsKeybinds;
import lgbt.lily.settingskeybinds.settingkeybinds.SettingKeybinds;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

import java.util.HashMap;
import java.util.Map;

@Config(name = SettingsKeybinds.MODID)
public class SettingsKeybindsConfig implements ConfigData {
    public boolean useSeparateCategories = false;
    public Map<String, SettingKeybindsConfig> settingKeybindsConfigs = new HashMap<>();

    public void writeToSettingKeybinds(SettingKeybinds<?>[] settingKeybinds) {
        for (SettingKeybinds<?> sk : settingKeybinds) {
            final SettingKeybindsConfig skc = settingKeybindsConfigs.get(sk.getSettingKey());
            if (skc != null) skc.writeToSettingKeybinds(sk);
        }
    }

    public void readFromSettingKeybinds(SettingKeybinds<?>[] settingKeybinds) {
        for (SettingKeybinds<?> sk : settingKeybinds) {
            final SettingKeybindsConfig skc = settingKeybindsConfigs.getOrDefault(sk.getSettingKey(),
                new SettingKeybindsConfig());
            skc.readFromSettingKeybinds(sk);
            settingKeybindsConfigs.put(sk.getSettingKey(), skc);
        }
    }
}
