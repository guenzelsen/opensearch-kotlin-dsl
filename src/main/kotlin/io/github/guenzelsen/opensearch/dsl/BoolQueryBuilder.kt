package io.github.guenzelsen.opensearch.dsl

import org.opensearch.client.opensearch._types.query_dsl.Query
import org.opensearch.client.opensearch._types.query_dsl.BoolQuery

@OpenSearchDslMarker
class BoolQueryBuilder {
    private val mustQueries = mutableListOf<Query>()
    private val shouldQueries = mutableListOf<Query>()
    private val mustNotQueries = mutableListOf<Query>()
    private val filterQueries = mutableListOf<Query>()

    fun must(block: QueryListBuilder.() -> Unit) {
        val builder = QueryListBuilder()
        builder.block()
        mustQueries.addAll(builder.build())
    }

    fun should(block: QueryListBuilder.() -> Unit) {
        val builder = QueryListBuilder()
        builder.block()
        shouldQueries.addAll(builder.build())
    }

    fun mustNot(block: QueryListBuilder.() -> Unit) {
        val builder = QueryListBuilder()
        builder.block()
        mustNotQueries.addAll(builder.build())
    }

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
