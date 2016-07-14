package org.maxur.mserv.sample.conf;

import lombok.Getter;
import lombok.Setter;

/**
 * @author myunusov
 * @version 1.0
 * @since <pre>12.07.2016</pre>
 */
public class RestConfig {

    @Getter
    @Setter
    private String url = "localhost";

    @Getter
    @Setter
    private int port = 8080;

    @Override
    public String toString() {
        return String.format("%s:%d", url, port);
    }
}
