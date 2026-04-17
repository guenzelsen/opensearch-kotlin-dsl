package io.github.guenzelsen.opensearch.dsl

import org.opensearch.client.opensearch._types.aggregations.Aggregation
import org.opensearch.client.opensearch._types.aggregations.TermsAggregation
import org.opensearch.client.opensearch._types.aggregations.MinAggregation
import org.opensearch.client.opensearch._types.aggregations.MaxAggregation
import org.opensearch.client.opensearch._types.aggregations.AverageAggregation
import org.opensearch.client.opensearch._types.aggregations.SumAggregation

/**
 * Primary builder for constructing a map of OpenSearch aggregations.
 */
@OpenSearchDslMarker
class AggregationMapBuilder {
    private val aggs = mutableMapOf<String, Aggregation>()

    /**
     * Constructs a `terms` aggregation.
     *
     * @param name The name of the aggregation.
     * @param field The field to aggregate on.
     * @param block Optional configuration block for further customization.
     */
    fun terms(name: String, field: String, block: TermsAggregation.Builder.() -> Unit = {}) {
        val builder = TermsAggregation.Builder().field(field)
        builder.block()
        aggs[name] = Aggregation.of { a -> a.terms(builder.build()) }
    }

    /**
     * Constructs a `min` aggregation.
     *
     * @param name The name of the aggregation.
     * @param field The field to compute the minimum on.
     * @param block Optional configuration block for further customization.
     */
    fun min(name: String, field: String, block: MinAggregation.Builder.() -> Unit = {}) {
        val builder = MinAggregation.Builder().field(field)
        builder.block()
        aggs[name] = Aggregation.of { a -> a.min(builder.build()) }
    }

    /**
     * Constructs a `max` aggregation.
     *
     * @param name The name of the aggregation.
     * @param field The field to compute the maximum on.
     * @param block Optional configuration block for further customization.
     */
    fun max(name: String, field: String, block: MaxAggregation.Builder.() -> Unit = {}) {
        val builder = MaxAggregation.Builder().field(field)
        builder.block()
        aggs[name] = Aggregation.of { a -> a.max(builder.build()) }
    }

    /**
     * Constructs an `avg` aggregation.
     *
     * @param name The name of the aggregation.
     * @param field The field to compute the average on.
     * @param block Optional configuration block for further customization.
     */
    fun avg(name: String, field: String, block: AverageAggregation.Builder.() -> Unit = {}) {
        val builder = AverageAggregation.Builder().field(field)
        builder.block()
        aggs[name] = Aggregation.of { a -> a.avg(builder.build()) }
    }

    /**
     * Constructs a `sum` aggregation.
     *
     * @param name The name of the aggregation.
     * @param field The field to compute the sum on.
     * @param block Optional configuration block for further customization.
     */
    fun sum(name: String, field: String, block: SumAggregation.Builder.() -> Unit = {}) {
        val builder = SumAggregation.Builder().field(field)
        builder.block()
        aggs[name] = Aggregation.of { a -> a.sum(builder.build()) }
    }

    internal fun build(): Map<String, Aggregation> {
        return aggs.toMap()
    }
}
