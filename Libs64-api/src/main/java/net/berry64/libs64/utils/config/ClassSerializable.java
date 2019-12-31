package net.berry64.libs64.utils.config;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ClassSerializable implements ConfigurationSerializable {

    public Map<String, Object> serialize() {
        return null;
    }

    public ClassSerializable(Map<String, Object> deserialize) {
    }
}
