package io.github.guenzelsen.opensearch.dsl

import org.opensearch.client.opensearch._types.query_dsl.Query
import org.opensearch.client.opensearch._types.query_dsl.NestedQuery

@OpenSearchDslMarker
class NestedQueryBuilder(private val path: String) {
    private var currentQuery: Query? = null

    fun query(block: QueryBuilder.() -> Unit) {
        val builder = QueryBuilder()
        builder.block()
        val builtQuery = builder.build()
        if (currentQuery != null) {
            throw IllegalStateException("Only one query can be specified inside nested.query.")
        }
        currentQuery = builtQuery
    }

    internal fun build(): NestedQuery {
        val query = currentQuery ?: throw IllegalStateException("A query must be specified for nested.")
        return NestedQuery.Builder().path(path).query(query).build()
    }
}
