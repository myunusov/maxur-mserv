package org.maxur.ddd.config;

import lombok.Data;
import org.maxur.mserv.config.Config;
import org.maxur.mserv.core.annotation.Configuration;
import org.maxur.mserv.core.annotation.Key;
import org.maxur.mserv.web.WebConfig;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * The type User config.
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>12.07.2016</pre>
 */
@Data
@Configuration(fileName = "./conf/tddd.yaml")
public class UserConfig extends Config {

    @NotNull
    private Date released;

    @NotNull
    private String version;

    @Key("web")
    private WebConfig web;

    @NotNull
    @Key("jdbc")
    private JdbcConfig jdbc;
}
