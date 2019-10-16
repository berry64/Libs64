package net.berry64.libs64.utils.config;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ClassSerializable implements ConfigurationSerializable {
    private static Map<Class<? extends ClassSerializable>, Map<String, Field>> classes = new ConcurrentHashMap<>();

    private Map<String, Field> fields;
    private Class<? extends ClassSerializable> self;

    //determine the fields needed to be registered into config
    private ClassSerializable() {
        self = this.getClass();
        fields = classes.get(self);
        if (fields == null) {
            fields = new HashMap<>();
            for (Field f : self.getDeclaredFields()) {
                CfgField cfg = f.getAnnotation(CfgField.class);
                if (cfg == null)
                    continue;
                String name = f.getName();
                if (!cfg.name().isEmpty())
                    name = cfg.name();
                fields.put(name, f);
            }

            ConfigurationSerialization.registerClass(self);
            classes.put(self, fields);
        }
    }

    public Map<String, Object> serialize() {
        Map<String, Object> ret = new HashMap<>();
        fields.forEach((key, field) -> {
            try {
                if (!field.isAccessible())
                    field.setAccessible(true);
                ret.put(key, field.get(this));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return ret;
    }

    public ClassSerializable(Map<String, Object> deserialize) {
        this();
        if (deserialize == null || deserialize.isEmpty())
            return;
        fields.forEach((key, field) -> {
            Object val = deserialize.get(key);
            if (val != null) {
                try {
                    if (!field.isAccessible())
                        field.setAccessible(true);
                    field.set(this, val);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
