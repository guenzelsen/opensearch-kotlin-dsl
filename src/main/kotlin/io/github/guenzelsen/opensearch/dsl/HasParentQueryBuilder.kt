package io.github.guenzelsen.opensearch.dsl

import org.opensearch.client.opensearch._types.query_dsl.Query
import org.opensearch.client.opensearch._types.query_dsl.HasParentQuery

/**
 * Builder for constructing `has_parent` queries.
 *
 * @property parentType The parent type to query against.
 */
@OpenSearchDslMarker
class HasParentQueryBuilder(private val parentType: String) {
    private var currentQuery: Query? = null

    /**
     * Defines the query to execute on the parent objects.
     * 
     * Only one root-level query is allowed inside the `hasParent.query` block.
     * Use a [QueryBuilder.bool] query if multiple conditions are required.
     *
     * @param block The configuration block for building the internal query.
     */
    fun query(block: QueryBuilder.() -> Unit) {
        val builder = QueryBuilder()
        builder.block()
        val builtQuery = builder.build()
        if (currentQuery != null) {
            throw IllegalStateException("Only one query can be specified inside hasParent.query.")
        }
        currentQuery = builtQuery
    }

    internal fun build(): HasParentQuery {
        val query = currentQuery ?: throw IllegalStateException("A query must be specified for hasParent.")
        return HasParentQuery.Builder().parentType(parentType).query(query).build()
    }
}
