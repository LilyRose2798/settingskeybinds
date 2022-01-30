package lgbt.lily.settingskeybinds.mixin;

import lgbt.lily.settingskeybinds.SettingsKeybinds;
import lgbt.lily.settingskeybinds.Utils;
import lgbt.lily.settingskeybinds.settingkeybinds.SettingKeybinds;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mixin(TranslationStorage.class)
public abstract class MixinTranslationStorage {
    private static final String KEY_BASE = Utils.getT9nKey(Utils.T9nType.KEY);
    private static final String KEY_CATEGORIES_BASE = Utils.getT9nKey(Utils.T9nType.KEY_CATEGORIES);
    private static final Map<String, SettingKeybinds<?>> SETTING_KEYBINDS_MAP = Arrays
        .stream(SettingsKeybinds.SETTING_KEYBINDS)
        .collect(Collectors.toMap(SettingKeybinds::getSettingKey, Function.identity()));

    @Inject(method = "get", at = @At("TAIL"), cancellable = true)
    private void settingskeybinds$get(String key, CallbackInfoReturnable<String> cir) {
        if (!key.equals(cir.getReturnValue())) return;
        if (key.startsWith(KEY_BASE)) {
            final String[] keyParts = key.substring(KEY_BASE.length() + 1).split("\\.", 2);
            final SettingKeybinds<?> sk = SETTING_KEYBINDS_MAP.get(keyParts[0]);
            if (sk == null) return;
            cir.setReturnValue(new TranslatableText(
                Utils.getT9nKey(Utils.T9nType.OUTPUT, "keyFormat"),
                sk.getSettingT9nText(), sk.getValueT9nText(keyParts[1])).getString());
        } else if (key.startsWith(KEY_CATEGORIES_BASE)) {
            final SettingKeybinds<?> sk = SETTING_KEYBINDS_MAP.get(key.substring(KEY_CATEGORIES_BASE.length() + 1));
            if (sk == null) return;
            cir.setReturnValue(new TranslatableText(
                Utils.getT9nKey(Utils.T9nType.OUTPUT, "keyCategoryFormat"),
                Utils.getT9nText(Utils.T9nType.KEY_CATEGORIES),
                sk.getSettingT9nText()).getString());
        }
    }

    @Inject(method = "hasTranslation", at = @At("TAIL"), cancellable = true)
    private void settingskeybinds$hasTranslation(String key, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) return;
        if (key.startsWith(KEY_BASE))
            cir.setReturnValue(SETTING_KEYBINDS_MAP.containsKey(key.substring(KEY_BASE.length() + 1).split("\\.", 2)[0]));
        else if (key.startsWith(KEY_CATEGORIES_BASE))
            cir.setReturnValue(SETTING_KEYBINDS_MAP.containsKey(key.substring(KEY_CATEGORIES_BASE.length() + 1)));
    }
}
