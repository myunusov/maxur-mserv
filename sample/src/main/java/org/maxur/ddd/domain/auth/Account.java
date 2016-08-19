package org.maxur.ddd.domain.auth;

import lombok.Getter;
import org.maxur.ddd.domain.core.User;
import org.maxur.ldoc.Concept;

/**
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>8/16/2016</pre>
 */
@Concept(name = "Учетная запись ", description = "Учетная запись пользователя системы")
public class Account {

    @Getter
    private final User user;

    public Account(final User user) {
        this.user = user;
    }
}
