package org.maxur.mserv.config.yaml;

import org.jvnet.hk2.annotations.Service;
import org.maxur.mserv.config.ConfigFile;
import org.maxur.mserv.config.ConfigFileFactory;

/**
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>8/19/2016</pre>
 */
@Service
public class YamlConfigFileFactory  implements ConfigFileFactory {

    @Override
    public ConfigFile make(final String fileName) {
        return new YamlConfigFile(fileName);
    }

}
