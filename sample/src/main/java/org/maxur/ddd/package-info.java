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
 * ## This packageis root package of T-DDD application.
 *
 * ![Service Layers](packages.png)
 *
 * @startuml packages.png
 * note "Contains utility classes" as N1
 * package Util
 * package UI
 * package Config
 * package Service
 * package Domain
 * package DAO
 * Util .. N1
 * UI --> Service
 * UI ..> Util
 * Service --> Domain
 * Service ..> Util
 * Domain --> DAO
 * Domain ..> Util
 * DAO ..> Util
 * @enduml
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>10.02.2016</pre>
 */
package org.maxur.ddd;