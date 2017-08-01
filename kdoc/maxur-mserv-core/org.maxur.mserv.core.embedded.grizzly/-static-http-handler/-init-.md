---
title: StaticHttpHandler.<init> - maxur-mserv-core
---

[maxur-mserv-core](../../index.html) / [org.maxur.mserv.core.embedded.grizzly](../index.html) / [StaticHttpHandler](index.html) / [&lt;init&gt;](.)

# &lt;init&gt;

`StaticHttpHandler(path: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, vararg roots: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`)``StaticHttpHandler(staticContent: `[`StaticContent`](../../org.maxur.mserv.core.embedded.properties/-static-content/index.html)`, classLoader: `[`ClassLoader`](http://docs.oracle.com/javase/8/docs/api/java/lang/ClassLoader.html)` = StaticHttpHandler::class.java.classLoader)`

[HttpHandler](#), which processes requests to a static resources resolved
by a given [ClassLoader](http://docs.oracle.com/javase/8/docs/api/java/lang/ClassLoader.html).

Create HttpHandler, which will handle requests
to the static resources resolved by the given class loader.

### Parameters

`classLoader` - [ClassLoader](http://docs.oracle.com/javase/8/docs/api/java/lang/ClassLoader.html) to be used to resolve the resources

`staticContent` - is the static content configuration

**Author**
Grizzly Team

**Author**
Maxim Yunusov

