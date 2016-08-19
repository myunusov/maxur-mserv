package org.maxur.ddd.service;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>8/18/2016</pre>
 */
@Slf4j
public class Uow {

    public Uow() {
        log.info("create uof");
    }

    public void commit() {
        log.info("commit uof");
    }
}
