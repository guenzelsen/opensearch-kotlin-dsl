package io.github.guenzelsen.opensearch.dsl

import org.opensearch.client.opensearch._types.query_dsl.Query
import org.opensearch.client.opensearch._types.query_dsl.HasChildQuery

/**
 * Builder for constructing `has_child` queries.
 *
 * @property type The child type to query against.
 */
@OpenSearchDslMarker
class HasChildQueryBuilder(private val type: String) {
    private var currentQuery: Query? = null

    /**
     * Defines the query to execute on the child objects.
     * 
     * Only one root-level query is allowed inside the `hasChild.query` block.
     * Use a [QueryBuilder.bool] query if multiple conditions are required.
     *
     * @param block The configuration block for building the internal query.
     */
    fun query(block: QueryBuilder.() -> Unit) {
        val builder = QueryBuilder()
        builder.block()
        val builtQuery = builder.build()
        if (currentQuery != null) {
            throw IllegalStateException("Only one query can be specified inside hasChild.query.")
        }
        currentQuery = builtQuery
    }

    internal fun build(): HasChildQuery {
        val query = currentQuery ?: throw IllegalStateException("A query must be specified for hasChild.")
        return HasChildQuery.Builder().type(type).query(query).build()
    }
}
