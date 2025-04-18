package me.RareHyperIon.BlockTrials.utility.serialize;

import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.concurrent.Callable;

public final class SerializeUtility {

    public static Object serialize(Object object) {
        if(object instanceof ItemStack) {
            object = ((ItemStack) object).serialize();
        }

        return object;
    }

    @SuppressWarnings("unchecked")
    public static Object deserialize(Object object) {
        if(object instanceof Map<?, ?> map) {
            if(map.isEmpty()) return object;

            if(map.keySet().iterator().next() instanceof String) {
                return attempt(() -> ItemStack.deserialize((Map<String, Object>) object));
            }
        }

        return object;
    }

    private static <T> T attempt(final Callable<T> runnable) {
        try {
            return runnable.call();
        } catch (final Throwable throwable) {
            return null;
        }
    }

}
