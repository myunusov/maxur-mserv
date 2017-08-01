---
title: FaviconResource.favicon - maxur-mserv-core
---

[maxur-mserv-core](../../index.html) / [org.maxur.mserv.core.rest](../index.html) / [FaviconResource](index.html) / [favicon](.)

# favicon

`@GET @Path("/{fileName: .*ico}") @Produces(["image/x-icon"]) fun favicon(@PathParam("fileName") fileName: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): Response` [(source)](https://github.com/myunusov/maxur-mserv/tree/master/maxur-mserv-core/src/main/kotlin/org/maxur/mserv/core/rest/FaviconResource.kt#L31)

Gets favicon.

### Parameters

`fileName` - the file name

### Exceptions

`IOException` - the io exception

**Return**
the favicon

