package io.github.guenzelsen.opensearch.dsl

import org.opensearch.client.opensearch._types.query_dsl.Query

@DslMarker
annotation class OpenSearchDslMarker

/**
 * Entry point for building an OpenSearch query.
 */
fun query(block: QueryBuilder.() -> Unit): Query {
    val builder = QueryBuilder()
    builder.block()
    return builder.build()
}
