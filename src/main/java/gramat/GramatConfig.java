package gramat;

import gramat.framework.ConfigurationEntry;
import gramat.framework.LogLevel;

public enum GramatConfig implements ConfigurationEntry {
    DEBUG("debug", false),
    LOG_LEVEL("logger.level", LogLevel.WARNING),
    ;

    private final String key;
    private final Object defaultValue;

    GramatConfig(String key, Object defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public Object getDefault() {
        return defaultValue;
    }
}
