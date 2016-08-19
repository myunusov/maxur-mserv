/*
 * Copyright 2015 Russian Post
 *
 * This source code is Russian Post Confidential Proprietary.
 * This software is protected by copyright. All rights and titles are reserved.
 * You shall not use, copy, distribute, modify, decompile, disassemble or reverse engineer the software.
 * Otherwise this violation would be treated by law and would be subject to legal prosecution.
 * Legal use of the software provides receipt of a license from the right holder only.
 */

package org.maxur.mserv.microservice;

/**
 * The interface Micro service.
 *
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>9/15/2015</pre>
 */
public interface MicroService {

    /**
     * start
     */
    void start();

    /**
     * stop
     */
    void stop();


    /**
     * With name micro service.
     *
     * @param name the name
     * @return the micro service
     */
    MicroService withName(String name);

    /**
     * Gets name.
     *
     * @return the name
     */
    String getName();

    /**
     * Gets version.
     *
     * @return the version
     */
    String getVersion();

    /**
     * Gets released.
     *
     * @return the released
     */
    java.util.Date getReleased();
}
