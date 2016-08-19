/*
 * Copyright (c) 2016 Maxim Yunusov
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

package org.maxur.ddd;

import org.maxur.mserv.MaxurSystem;

/**
 * The Application Launcher.
 *
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>7/28/2016</pre>
 */
public final class Launcher {

    /**
     * Utils class.
     */
    private Launcher() {
    }

    /**
     * Command line entry point. This method kicks off the building of a application  object
     * and executes it.
     *
     * @param args - arguments of command.
     */
    public static void main(String[] args) {
        MaxurSystem.system("DDD Tutorial")
                .withAopInPackages("org.maxur.ddd")
                .start("rest.service");
    }

}
