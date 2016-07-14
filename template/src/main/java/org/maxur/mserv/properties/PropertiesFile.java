package org.maxur.mserv.properties;

/**
 * The type Properties file.
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>14.07.2016</pre>
 */
public abstract class PropertiesFile {

    private Class<?> propertiesClass;

    /**
     * Yaml file properties file.
     *
     * @param path the path
     * @return the properties file
     */
    public static PropertiesFile yamlFile(final String path) {
        return new YamlPropertiesFile(path);
    }

    public void bindPropertiesClass(Class<?> propertiesClass) {
        this.propertiesClass = propertiesClass;
    }

    Class<?> propertiesClass() {
        return propertiesClass;
    }

    public abstract <T> T load();
}
