package io.github.guenzelsen.opensearch.dsl

import org.opensearch.client.opensearch._types.query_dsl.Query
import org.opensearch.client.opensearch._types.aggregations.Aggregation

/**
 * Marker annotation for the OpenSearch Kotlin DSL.
 *
 * This ensures type-safe builders and prevents accessing outer builder
 * scopes from within nested ones.
 */
@DslMarker
annotation class OpenSearchDslMarker

/**
 * Entry point for building an OpenSearch query using the Kotlin DSL.
 *
 * @param block The configuration block for building the query.
 * @return The constructed [Query] instance ready to be used in client requests.
 */
fun query(block: QueryBuilder.() -> Unit): Query {
    val builder = QueryBuilder()
    builder.block()
    return builder.build()
}

/**
 * Entry point for building a map of OpenSearch aggregations using the Kotlin DSL.
 *
 * @param block The configuration block for building the aggregations.
 * @return The constructed map of aggregations ready to be used in client requests.
 */
fun aggregations(block: AggregationMapBuilder.() -> Unit): Map<String, Aggregation> {
    val builder = AggregationMapBuilder()
    builder.block()
    return builder.build()
}
