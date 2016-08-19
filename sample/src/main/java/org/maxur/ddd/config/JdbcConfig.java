package org.maxur.ddd.config;

import lombok.Getter;
import lombok.Setter;

/**
 * The type Jdbc config.
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>12.07.2016</pre>
 */
public class JdbcConfig {

    @Getter
    @Setter
    private String url;

    @Getter
    @Setter
    private String user;

    @Getter
    @Setter
    private String password;

    @Override
    public String toString() {
        return String.format("%s:%s url: %s", user, password, url);
    }
}
