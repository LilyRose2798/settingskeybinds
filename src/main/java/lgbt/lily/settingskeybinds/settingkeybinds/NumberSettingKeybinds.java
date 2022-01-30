package lgbt.lily.settingskeybinds.settingkeybinds;

import lgbt.lily.settingskeybinds.Utils;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class NumberSettingKeybinds<T extends Number> extends SettingKeybinds<T> {
    private final T minValue;
    private final T maxValue;
    private final T stepValue;
    private KeyBinding decreaseKeyBinding;
    private KeyBinding increaseKeyBinding;

    public NumberSettingKeybinds(String settingKey, Function<GameOptions, T> getSetting,
                                 BiConsumer<GameOptions, T> setSetting, T minValue, T maxValue, T stepValue,
                                 List<T> settingValues) {
        super(settingKey, getSetting, setSetting, settingValues);
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.stepValue = stepValue;
    }

    @Override
    public void init() {
        super.init();
        decreaseKeyBinding = Utils.getKeyBinding(getSettingKey(), "decrease");
        increaseKeyBinding = Utils.getKeyBinding(getSettingKey(), "increase");
        KeyBindingHelper.registerKeyBinding(decreaseKeyBinding);
        KeyBindingHelper.registerKeyBinding(increaseKeyBinding);
    }

    public T getMinValue() {
        return minValue;
    }

    public T getMaxValue() {
        return maxValue;
    }

    public T getStepValue() {
        return stepValue;
    }

    abstract T add(T x, T y);
    abstract T sub(T x, T y);
    abstract boolean lt(T x, T y);
    abstract boolean gt(T x, T y);

    public boolean checkDecreaseKeyBinding(MinecraftClient client) {
        if (!decreaseKeyBinding.wasPressed()) return false;
        T newValue = sub(getSetting(client), stepValue);
        if (lt(newValue, minValue)) {
            Utils.sendPlayerOutputMessage(client, "minSetting", getSettingT9nText());
            return false;
        }
        setSetting(client, newValue);
        return true;
    }

    public boolean checkIncreaseKeyBinding(MinecraftClient client) {
        if (!increaseKeyBinding.wasPressed()) return false;
        T newValue = add(getSetting(client), stepValue);
        if (gt(newValue, maxValue)) {
            Utils.sendPlayerOutputMessage(client, "maxSetting", getSettingT9nText());
            return false;
        }
        setSetting(client, newValue);
        return true;
    }

    @Override
    public boolean handleTickEvent(MinecraftClient client) {
        return checkDecreaseKeyBinding(client) ||
            checkIncreaseKeyBinding(client) ||
            super.handleTickEvent(client);
    }
}
