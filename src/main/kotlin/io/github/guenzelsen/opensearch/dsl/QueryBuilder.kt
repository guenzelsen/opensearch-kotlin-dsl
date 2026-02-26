package io.github.guenzelsen.opensearch.dsl

import org.opensearch.client.opensearch._types.query_dsl.Query
import org.opensearch.client.opensearch._types.query_dsl.MatchQuery
import org.opensearch.client.opensearch._types.query_dsl.TermQuery
import org.opensearch.client.opensearch._types.query_dsl.TermsQuery
import org.opensearch.client.opensearch._types.query_dsl.NestedQuery
import org.opensearch.client.opensearch._types.FieldValue

@OpenSearchDslMarker
class QueryBuilder {
    private var currentQuery: Query? = null

    fun match(field: String, value: String, block: MatchQuery.Builder.() -> Unit = {}) {
        val builder = MatchQuery.Builder().field(field).query(FieldValue.of(value))
        builder.block()
        setQuery(Query.of { q -> q.match(builder.build()) })
    }
    
    fun match(field: String, value: Int, block: MatchQuery.Builder.() -> Unit = {}) {
        val builder = MatchQuery.Builder().field(field).query(FieldValue.of(value.toLong()))
        builder.block()
        setQuery(Query.of { q -> q.match(builder.build()) })
    }
    
    fun match(field: String, value: Boolean, block: MatchQuery.Builder.() -> Unit = {}) {
        val builder = MatchQuery.Builder().field(field).query(FieldValue.of(value))
        builder.block()
        setQuery(Query.of { q -> q.match(builder.build()) })
    }

    fun term(field: String, value: String, block: TermQuery.Builder.() -> Unit = {}) {
        val builder = TermQuery.Builder().field(field).value(FieldValue.of(value))
        builder.block()
        setQuery(Query.of { q -> q.term(builder.build()) })
    }
    
    fun term(field: String, value: Int, block: TermQuery.Builder.() -> Unit = {}) {
        val builder = TermQuery.Builder().field(field).value(FieldValue.of(value.toLong()))
        builder.block()
        setQuery(Query.of { q -> q.term(builder.build()) })
    }
    
    fun term(field: String, value: Boolean, block: TermQuery.Builder.() -> Unit = {}) {
        val builder = TermQuery.Builder().field(field).value(FieldValue.of(value))
        builder.block()
        setQuery(Query.of { q -> q.term(builder.build()) })
    }

    fun terms(field: String, values: List<String>, block: TermsQuery.Builder.() -> Unit = {}) {
        val fieldValues = values.map { FieldValue.of(it) }
        val termsQueryField = org.opensearch.client.opensearch._types.query_dsl.TermsQueryField.of { t -> t.value(fieldValues) }
        val builder = TermsQuery.Builder().field(field).terms(termsQueryField)
        builder.block()
        setQuery(Query.of { q -> q.terms(builder.build()) })
    }

    fun nested(path: String, block: NestedQueryBuilder.() -> Unit) {
        val builder = NestedQueryBuilder(path)
        builder.block()
        setQuery(Query.of { q -> q.nested(builder.build()) })
    }

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
