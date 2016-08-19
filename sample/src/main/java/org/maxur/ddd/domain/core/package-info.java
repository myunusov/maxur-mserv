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

/**
 * domain package contains business logic layer's classes.
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>10.02.2016</pre>
 */
@BusinessDomain(
    name = "Ядро системы",
    description = "Содержит основные концепты системы"
)
@Link(related = "common", label = "acl")
package org.maxur.ddd.domain.core;

import org.maxur.ldoc.BusinessDomain;
import org.maxur.ldoc.Link;
