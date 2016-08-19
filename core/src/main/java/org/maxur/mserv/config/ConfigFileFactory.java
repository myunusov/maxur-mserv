package org.maxur.mserv.config;

import org.jvnet.hk2.annotations.Contract;

/**
 * The interface Config file factory.
 *
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>8/19/2016</pre>
 */
@Contract
public interface ConfigFileFactory {

    /**
     * Make config file.
     *
     * @param fileName the File name.
     *
     * @return the config file
     */
    ConfigFile make(final String fileName);

}
