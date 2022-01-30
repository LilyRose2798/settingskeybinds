package lgbt.lily.settingskeybinds;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Language;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {
    public enum T9nType {
        KEY_CATEGORIES("key", "categories"),
        OUTPUT("output"),
        TITLE("title"),
        CONFIG_CATEGORY("config", "category"),
        CONFIG_ENTRY("config", "entry"),
        CONFIG_TOOLTIP("config", "tooltip"),
        NAME("name"),
        VALUE("value"),
        KEY("key"),
        VANILLA_KEY(true, "key"),
        VANILLA_OPTIONS(true, "options");

        private final String t9nTypeKey;

        T9nType(String... t9nKeyParts) {
            this(false, t9nKeyParts);
        }
        T9nType(boolean vanilla, String... t9nKeyParts) {
            this.t9nTypeKey = (vanilla ? Arrays.stream(t9nKeyParts) :
                Stream.concat(Arrays.stream(t9nKeyParts), Stream.of(SettingsKeybinds.MODID)))
                .collect(Collectors.joining("."));
        }

        @Override
        public String toString() {
            return t9nTypeKey;
        }
    }

    public static String getT9nKey(T9nType t9nKeyType, String... t9nKeyParts) {
        return Stream.concat(Stream.of(t9nKeyType.toString()), Arrays.stream(t9nKeyParts))
            .collect(Collectors.joining("."));
    }

    public static TranslatableText getT9nText(T9nType t9nKeyType, String... t9nKeyParts) {
        return new TranslatableText(getT9nKey(t9nKeyType, t9nKeyParts));
    }

    public static boolean hasT9n(T9nType t9nKeyType, String... t9nKeyParts) {
        return Language.getInstance().hasTranslation(getT9nKey(t9nKeyType, t9nKeyParts));
    }

    public static KeyBinding getKeyBinding(String settingKey, String settingValueKey) {
        return getKeyBinding(settingKey, settingValueKey, -1);
    }
    public static KeyBinding getKeyBinding(String settingKey, String settingValueKey, int code) {
        return new KeyBinding(getT9nKey(T9nType.KEY, settingKey, settingValueKey), code,
            SettingsKeybinds.CONFIG.useSeparateCategories ?
                getT9nKey(T9nType.KEY_CATEGORIES, settingKey) : getT9nKey(T9nType.KEY_CATEGORIES));
    }

    public static void sendPlayerOutputMessage(MinecraftClient client, String outputKey, Object... outputArgs) {
        if (client.player == null) return;
        client.player.sendMessage(new TranslatableText(getT9nKey(T9nType.OUTPUT, outputKey), outputArgs), false);
    }
}
