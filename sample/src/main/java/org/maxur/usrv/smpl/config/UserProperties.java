package org.maxur.usrv.smpl.config;

import lombok.Data;
import org.maxur.mserv.core.annotation.Key;
import org.maxur.mserv.core.annotation.Properties;
import org.maxur.mserv.web.WebProperties;

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
@Properties(fileName = "./conf/config.yaml")
public class UserProperties {

    @NotNull
    private Date released;

    @NotNull
    private String version;

    @Key("web")
    private WebProperties web;

    @NotNull
    @Key("jdbc")
    private JdbcProperties jdbc;
}
