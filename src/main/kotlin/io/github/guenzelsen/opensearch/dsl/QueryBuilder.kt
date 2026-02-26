package io.github.guenzelsen.opensearch.dsl

import org.opensearch.client.opensearch._types.query_dsl.Query
import org.opensearch.client.opensearch._types.query_dsl.MatchQuery
import org.opensearch.client.opensearch._types.query_dsl.TermQuery
import org.opensearch.client.opensearch._types.query_dsl.TermsQuery
import org.opensearch.client.opensearch._types.query_dsl.NestedQuery
import org.opensearch.client.opensearch._types.FieldValue

/**
 * Primary builder for constructing root-level OpenSearch queries.
 * 
 * This builder allows constructing exactly one root query. To combine multiple
 * queries, use a [bool] query block.
 */
@OpenSearchDslMarker
class QueryBuilder {
    private var currentQuery: Query? = null

    /**
     * Constructs a `match` query for a given String value.
     *
     * @param field The field to match against.
     * @param value The [String] value to search for.
     * @param block Optional configuration block for further customization of the query.
     */
    fun match(field: String, value: String, block: MatchQuery.Builder.() -> Unit = {}) {
        val builder = MatchQuery.Builder().field(field).query(FieldValue.of(value))
        builder.block()
        setQuery(Query.of { q -> q.match(builder.build()) })
    }
    
    /**
     * Constructs a `match` query for a given Int value.
     *
     * @param field The field to match against.
     * @param value The [Int] value to search for.
     * @param block Optional configuration block for further customization of the query.
     */
    fun match(field: String, value: Int, block: MatchQuery.Builder.() -> Unit = {}) {
        val builder = MatchQuery.Builder().field(field).query(FieldValue.of(value.toLong()))
        builder.block()
        setQuery(Query.of { q -> q.match(builder.build()) })
    }
    
    /**
     * Constructs a `match` query for a given Boolean value.
     *
     * @param field The field to match against.
     * @param value The [Boolean] value to search for.
     * @param block Optional configuration block for further customization of the query.
     */
    fun match(field: String, value: Boolean, block: MatchQuery.Builder.() -> Unit = {}) {
        val builder = MatchQuery.Builder().field(field).query(FieldValue.of(value))
        builder.block()
        setQuery(Query.of { q -> q.match(builder.build()) })
    }

    /**
     * Constructs a `term` query for a given String value.
     *
     * @param field The field to match exactly.
     * @param value The exact [String] value to match.
     * @param block Optional configuration block for further customization of the query.
     */
    fun term(field: String, value: String, block: TermQuery.Builder.() -> Unit = {}) {
        val builder = TermQuery.Builder().field(field).value(FieldValue.of(value))
        builder.block()
        setQuery(Query.of { q -> q.term(builder.build()) })
    }
    
    /**
     * Constructs a `term` query for a given Int value.
     *
     * @param field The field to match exactly.
     * @param value The exact [Int] value to match.
     * @param block Optional configuration block for further customization of the query.
     */
    fun term(field: String, value: Int, block: TermQuery.Builder.() -> Unit = {}) {
        val builder = TermQuery.Builder().field(field).value(FieldValue.of(value.toLong()))
        builder.block()
        setQuery(Query.of { q -> q.term(builder.build()) })
    }
    
    /**
     * Constructs a `term` query for a given Boolean value.
     *
     * @param field The field to match exactly.
     * @param value The exact [Boolean] value to match.
     * @param block Optional configuration block for further customization of the query.
     */
    fun term(field: String, value: Boolean, block: TermQuery.Builder.() -> Unit = {}) {
        val builder = TermQuery.Builder().field(field).value(FieldValue.of(value))
        builder.block()
        setQuery(Query.of { q -> q.term(builder.build()) })
    }

    /**
     * Constructs a `terms` query to match multiple String values.
     *
     * @param field The field to match against.
     * @param values The list of [String] values, where containing any value is a match.
     * @param block Optional configuration block for further customization of the query.
     */
    fun terms(field: String, values: List<String>, block: TermsQuery.Builder.() -> Unit = {}) {
        val fieldValues = values.map { FieldValue.of(it) }
        val termsQueryField = org.opensearch.client.opensearch._types.query_dsl.TermsQueryField.of { t -> t.value(fieldValues) }
        val builder = TermsQuery.Builder().field(field).terms(termsQueryField)
        builder.block()
        setQuery(Query.of { q -> q.terms(builder.build()) })
    }

    /**
     * Constructs a `nested` query to query nested objects.
     *
     * @param path The path of the nested object.
     * @param block Block to define the nested query structure.
     */
    fun nested(path: String, block: NestedQueryBuilder.() -> Unit) {
        val builder = NestedQueryBuilder(path)
        builder.block()
        setQuery(Query.of { q -> q.nested(builder.build()) })
    }

    /**
     * Constructs a `bool` query to combine multiple queries using boolean logic.
     *
     * @param block Block to define the boolean clauses (`must`, `should`, `filter`, `mustNot`).
     */
    fun bool(block: BoolQueryBuilder.() -> Unit) {
        val builder = BoolQueryBuilder()
        builder.block()
        setQuery(Query.of { q -> q.bool(builder.build()) })
    }

    private fun setQuery(query: Query) {
        if (currentQuery != null) {
            throw IllegalStateException("Only one query can be built at the root level. To combine queries, use a bool query.")
        }
        currentQuery = query
    }

    internal fun build(): Query {
        return currentQuery ?: throw IllegalStateException("A query must be specified.")
    }
}
