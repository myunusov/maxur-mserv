package org.maxur.mserv.config;

/**
 * The interface Config resolver.
 *
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>8/18/2016</pre>
 */
public interface ConfigResolver {

    /**
     * Sets config.
     *
     * @param properties the config
     */
    void setProperties(PropertiesWrapper properties);

}
