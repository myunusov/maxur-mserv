package org.maxur.mserv.web;

import lombok.Setter;

import java.net.URI;
import java.util.Map;

/**
 * The type Web config.
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>12.07.2016</pre>
 */
public class WebProperties {

    @Setter
    private String url = "http://localhost:8080";

    @Setter
    private String rest = "api";

    @Setter
    private Map<String, String> content;


    /**
     * Uri uri.
     *
     * @return the uri
     */
    public URI uri() {
        return URI.create(url);
    }

    /**
     * Rest uri uri.
     *
     * @return the uri
     */
    public URI restUri() {
        return URI.create(url + "/" + rest);
    }

    /**
     * Content map.
     *
     * @return the map
     */
    public Map<String, String>  content() {
        return content;
    }

    @Override
    public String toString() {
        return url;
    }
}
