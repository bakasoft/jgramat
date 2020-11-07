package gramat;

import gramat.framework.ConfigurationEntry;

public enum GramatConfig implements ConfigurationEntry {
    DEBUG("debug", false),
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
