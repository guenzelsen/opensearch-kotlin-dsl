package io.github.guenzelsen.opensearch.dsl

import org.opensearch.client.opensearch._types.query_dsl.Query
import org.opensearch.client.opensearch._types.query_dsl.MatchQuery
import org.opensearch.client.opensearch._types.query_dsl.TermQuery
import org.opensearch.client.opensearch._types.query_dsl.TermsQuery
import org.opensearch.client.opensearch._types.query_dsl.NestedQuery
import org.opensearch.client.opensearch._types.query_dsl.SimpleQueryStringQuery
import org.opensearch.client.opensearch._types.query_dsl.WildcardQuery
import org.opensearch.client.opensearch._types.query_dsl.HasChildQuery
import org.opensearch.client.opensearch._types.query_dsl.HasParentQuery
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

    /**
     * Constructs a `simple_query_string` query.
     *
     * @param query The query string to be parsed.
     * @param fields Optional list of fields to search. If omitted, searches all fields.
     * @param block Optional configuration block.
     */
    fun simpleQueryString(query: String, fields: List<String>? = null, block: SimpleQueryStringQuery.Builder.() -> Unit = {}) {
        val builder = SimpleQueryStringQuery.Builder().query(query)
        if (fields != null) {
            builder.fields(fields)
        }
        builder.block()
        setQuery(Query.of { q -> q.simpleQueryString(builder.build()) })
    }

    /**
     * Constructs a `wildcard` query for a given String value.
     *
     * @param field The field to match against.
     * @param value The wildcard pattern to search for (e.g., "ki*y").
     * @param block Optional configuration block.
     */
    fun wildcard(field: String, value: String, block: WildcardQuery.Builder.() -> Unit = {}) {
        val builder = WildcardQuery.Builder().field(field).value(value)
        builder.block()
        setQuery(Query.of { q -> q.wildcard(builder.build()) })
    }

    /**
     * Constructs a `has_child` query to query child objects.
     *
     * @param type The child type to query against.
     * @param block Block to define the child query structure.
     */
    fun hasChild(type: String, block: HasChildQueryBuilder.() -> Unit) {
        val builder = HasChildQueryBuilder(type)
        builder.block()
        setQuery(Query.of { q -> q.hasChild(builder.build()) })
    }

    /**
     * Constructs a `has_parent` query to query parent objects.
     *
     * @param parentType The parent type to query against.
     * @param block Block to define the parent query structure.
     */
    fun hasParent(parentType: String, block: HasParentQueryBuilder.() -> Unit) {
        val builder = HasParentQueryBuilder(parentType)
        builder.block()
        setQuery(Query.of { q -> q.hasParent(builder.build()) })
    }

    internal fun build(): Query {
        return currentQuery ?: throw IllegalStateException("A query must be specified.")
    }
}
