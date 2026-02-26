package io.github.guenzelsen.opensearch.dsl

import org.opensearch.client.opensearch._types.query_dsl.Query
import org.opensearch.client.opensearch._types.query_dsl.BoolQuery

/**
 * Builder for constructing boolean queries.
 * 
 * Supports adding `must`, `should`, `mustNot`, and `filter` clauses.
 * Each clause block receives a [QueryListBuilder] to allow defining multiple
 * query conditions within that specific boolean context.
 */
@OpenSearchDslMarker
class BoolQueryBuilder {
    private val mustQueries = mutableListOf<Query>()
    private val shouldQueries = mutableListOf<Query>()
    private val mustNotQueries = mutableListOf<Query>()
    private val filterQueries = mutableListOf<Query>()

    /**
     * Adds a `must` clause. Queries defined here must appear in matching documents 
     * and will contribute to the score.
     *
     * @param block Block defining the queries for this clause.
     */
    fun must(block: QueryListBuilder.() -> Unit) {
        val builder = QueryListBuilder()
        builder.block()
        mustQueries.addAll(builder.build())
    }

    /**
     * Adds a `should` clause. Queries defined here should appear in the matching
     * document and will contribute to the score.
     *
     * @param block Block defining the queries for this clause.
     */
    fun should(block: QueryListBuilder.() -> Unit) {
        val builder = QueryListBuilder()
        builder.block()
        shouldQueries.addAll(builder.build())
    }

    /**
     * Adds a `must_not` clause. Queries defined here must not appear in the 
     * matching documents. Clauses are executed in filter context.
     *
     * @param block Block defining the queries for this clause.
     */
    fun mustNot(block: QueryListBuilder.() -> Unit) {
        val builder = QueryListBuilder()
        builder.block()
        mustNotQueries.addAll(builder.build())
    }

    /**
     * Adds a `filter` clause. Queries defined here must appear in matching 
     * documents. Clauses are executed in filter context (no scoring).
     *
     * @param block Block defining the queries for this clause.
     */
    fun filter(block: QueryListBuilder.() -> Unit) {
        val builder = QueryListBuilder()
        builder.block()
        filterQueries.addAll(builder.build())
    }

    internal fun build(): BoolQuery {
        val builder = BoolQuery.Builder()
        if (mustQueries.isNotEmpty()) builder.must(mustQueries)
        if (shouldQueries.isNotEmpty()) builder.should(shouldQueries)
        if (mustNotQueries.isNotEmpty()) builder.mustNot(mustNotQueries)
        if (filterQueries.isNotEmpty()) builder.filter(filterQueries)
        return builder.build()
    }
}
