package lgbt.lily.settingskeybinds.config;

import lgbt.lily.settingskeybinds.settingkeybinds.*;

import java.util.List;

public class SettingKeybindsConfig {
    public boolean enabled = true;
    public List<Boolean> booleanSettingValues;
    public List<Integer> integerSettingValues;
    public List<Double> doubleSettingValues;
    public List<String> enumSettingValues;

    public void writeToSettingKeybinds(SettingKeybinds<?> settingKeybinds) {
        settingKeybinds.setEnabled(enabled);
        settingKeybinds.readFromConfig(this);
    }

    public void readFromSettingKeybinds(SettingKeybinds<?> settingKeybinds) {
        enabled = settingKeybinds.isEnabled();
        settingKeybinds.writeToConfig(this);
    }
}
