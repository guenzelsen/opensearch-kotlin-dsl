# OpenSearch Kotlin DSL

A lightweight, idiomatic Kotlin DSL wrapper for the `opensearch-java` client.
This library provides a type-safe builder for constructing OpenSearch queries cleanly without deep nesting or cumbersome Java builder syntax.

**Compatibility:**
- Kotlin `2.3.10`
- OpenSearch Java Client `3.6.0`

## Features Supported
- `match`
- `term`
- `terms`
- `nested`
- `bool` (`must`, `should`, `filter`, `mustNot`)
- `simple_query_string`
- `wildcard`

## Usage

```kotlin
import io.github.guenzelsen.opensearch.dsl.query

val searchContextQuery = query {
    bool {
        must {
            match("title", "Kotlin DSL")
        }
        filter {
            term("status", "published")
            terms("tags", listOf("search", "database"))
        }
        mustNot {
            term("author", "anonymous")
        }
        should {
            nested("comments") {
                query {
                    match("comments.text", "great")
                }
            }
        }
    }
}
```

## Setup & Publishing to GitHub
The project is configured so the group ID is `io.github.guenzelsen`.
You can publish it to GitHub Packages using the standard Gradle Maven Publish plugin.
