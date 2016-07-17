package org.maxur.mserv.properties;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.representer.Representer;

/**
 * The type Properties file.
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>14.07.2016</pre>
 */
public abstract class PropertiesFile {

    /**
     * Yaml file properties file.
     *
     * @param path the path
     * @return the properties file
     */
    public static PropertiesFile yamlFile(final String path) {
        return new YamlPropertiesFile(path);
    }

    /**
     * As yaml string.
     *
     * @param data the data
     * @return the string
     */
    public static String asYaml(final Object data) {
        PropertyUtils propUtils = new PropertyUtils();
        propUtils.setAllowReadOnlyProperties(true);
        Representer repr = new Representer();
        repr.setPropertyUtils(propUtils);
        Yaml yaml = new Yaml(new Constructor(), repr);
        return yaml.dump(data);
    }

    /**
     * Bind properties class t.
     *
     * @param <T>             the type parameter
     * @param propertiesClass the properties class
     * @return the t
     */
    public <T> T bindPropertiesClass(Class<T> propertiesClass) {
        return load(propertiesClass);
    }

    /**
     * Load t.
     *
     * @param <T>             the type parameter
     * @param propertiesClass the properties class
     * @return the t
     */
    protected abstract <T> T load(Class<T> propertiesClass);
}
