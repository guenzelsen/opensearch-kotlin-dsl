package io.github.guenzelsen.opensearch.dsl

import org.opensearch.client.opensearch._types.query_dsl.Query
import org.opensearch.client.opensearch._types.query_dsl.MatchQuery
import org.opensearch.client.opensearch._types.query_dsl.TermQuery
import org.opensearch.client.opensearch._types.query_dsl.TermsQuery
import org.opensearch.client.opensearch._types.query_dsl.SimpleQueryStringQuery
import org.opensearch.client.opensearch._types.query_dsl.WildcardQuery
import org.opensearch.client.opensearch._types.FieldValue

/**
 * Builder for accumulating multiple queries.
 *
 * This is primarily used inside boolean clauses (`must`, `should`, etc.),
 * where multiple query conditions can be specified together.
 */
@OpenSearchDslMarker
class QueryListBuilder {
    private val queries = mutableListOf<Query>()

    /**
     * Constructs and adds a `match` query for a given String value.
     *
     * @param field The field to match against.
     * @param value The [String] value to search for.
     * @param block Optional configuration block.
     */
    fun match(field: String, value: String, block: MatchQuery.Builder.() -> Unit = {}) {
        val builder = MatchQuery.Builder().field(field).query(FieldValue.of(value))
        builder.block()
        queries.add(Query.of { q -> q.match(builder.build()) })
    }
    
    /**
     * Constructs and adds a `match` query for a given Int value.
     *
     * @param field The field to match against.
     * @param value The [Int] value to search for.
     * @param block Optional configuration block.
     */
    fun match(field: String, value: Int, block: MatchQuery.Builder.() -> Unit = {}) {
        val builder = MatchQuery.Builder().field(field).query(FieldValue.of(value.toLong()))
        builder.block()
        queries.add(Query.of { q -> q.match(builder.build()) })
    }
    
    /**
     * Constructs and adds a `match` query for a given Boolean value.
     *
     * @param field The field to match against.
     * @param value The [Boolean] value to search for.
     * @param block Optional configuration block.
     */
    fun match(field: String, value: Boolean, block: MatchQuery.Builder.() -> Unit = {}) {
        val builder = MatchQuery.Builder().field(field).query(FieldValue.of(value))
        builder.block()
        queries.add(Query.of { q -> q.match(builder.build()) })
    }

    /**
     * Constructs and adds a `term` query for a given String value.
     *
     * @param field The field to match exactly.
     * @param value The exact [String] value to match.
     * @param block Optional configuration block.
     */
    fun term(field: String, value: String, block: TermQuery.Builder.() -> Unit = {}) {
        val builder = TermQuery.Builder().field(field).value(FieldValue.of(value))
        builder.block()
        queries.add(Query.of { q -> q.term(builder.build()) })
    }
    
    /**
     * Constructs and adds a `term` query for a given Int value.
     *
     * @param field The field to match exactly.
     * @param value The exact [Int] value to match.
     * @param block Optional configuration block.
     */
    fun term(field: String, value: Int, block: TermQuery.Builder.() -> Unit = {}) {
        val builder = TermQuery.Builder().field(field).value(FieldValue.of(value.toLong()))
        builder.block()
        queries.add(Query.of { q -> q.term(builder.build()) })
    }
    
    /**
     * Constructs and adds a `term` query for a given Boolean value.
     *
     * @param field The field to match exactly.
     * @param value The exact [Boolean] value to match.
     * @param block Optional configuration block.
     */
    fun term(field: String, value: Boolean, block: TermQuery.Builder.() -> Unit = {}) {
        val builder = TermQuery.Builder().field(field).value(FieldValue.of(value))
        builder.block()
        queries.add(Query.of { q -> q.term(builder.build()) })
    }

    /**
     * Constructs and adds a `terms` query to match multiple String values.
     *
     * @param field The field to match against.
     * @param values The list of [String] values to match.
     * @param block Optional configuration block.
     */
    fun terms(field: String, values: List<String>, block: TermsQuery.Builder.() -> Unit = {}) {
        val fieldValues = values.map { FieldValue.of(it) }
        val termsQueryField = org.opensearch.client.opensearch._types.query_dsl.TermsQueryField.of { t -> t.value(fieldValues) }
        val builder = TermsQuery.Builder().field(field).terms(termsQueryField)
        builder.block()
        queries.add(Query.of { q -> q.terms(builder.build()) })
    }

    /**
     * Constructs and adds a `nested` query to query nested objects.
     *
     * @param path The path of the nested object.
     * @param block Block to define the nested query structure.
     */
    fun nested(path: String, block: NestedQueryBuilder.() -> Unit) {
        val builder = NestedQueryBuilder(path)
        builder.block()
        queries.add(Query.of { q -> q.nested(builder.build()) })
    }

    /**
     * Constructs and adds a `bool` query to combine multiple queries.
     *
     * @param block Block to define the boolean clauses.
     */
    fun bool(block: BoolQueryBuilder.() -> Unit) {
        val builder = BoolQueryBuilder()
        builder.block()
        queries.add(Query.of { q -> q.bool(builder.build()) })
    }
    
    /**
     * Constructs and adds a `simple_query_string` query.
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
        queries.add(Query.of { q -> q.simpleQueryString(builder.build()) })
    }

    /**
     * Constructs and adds a `wildcard` query for a given String value.
     *
     * @param field The field to match against.
     * @param value The wildcard pattern to search for (e.g., "ki*y").
     * @param block Optional configuration block.
     */
    fun wildcard(field: String, value: String, block: WildcardQuery.Builder.() -> Unit = {}) {
        val builder = WildcardQuery.Builder().field(field).value(value)
        builder.block()
        queries.add(Query.of { q -> q.wildcard(builder.build()) })
    }

    internal fun build(): List<Query> = queries
}
