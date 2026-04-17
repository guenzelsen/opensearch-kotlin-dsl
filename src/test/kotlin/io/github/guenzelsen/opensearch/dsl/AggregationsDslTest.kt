package io.github.guenzelsen.opensearch.dsl

import org.opensearch.client.opensearch._types.aggregations.Aggregation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertNotNull

class AggregationsDslTest {

    @Test
    fun `test terms aggregation`() {
        val aggs = aggregations {
            terms("my_terms", "category")
        }
        
        assertEquals(1, aggs.size)
        val agg = aggs["my_terms"]
        assertNotNull(agg)
        assertTrue(agg.isTerms)
        assertEquals("category", agg.terms().field())
    }

    @Test
    fun `test min aggregation`() {
        val aggs = aggregations {
            min("min_price", "price")
        }

        assertEquals(1, aggs.size)
        val agg = aggs["min_price"]
        assertNotNull(agg)
        assertTrue(agg.isMin)
        assertEquals("price", agg.min().field())
    }

    @Test
    fun `test max aggregation`() {
        val aggs = aggregations {
            max("max_price", "price")
        }

        assertEquals(1, aggs.size)
        val agg = aggs["max_price"]
        assertNotNull(agg)
        assertTrue(agg.isMax)
        assertEquals("price", agg.max().field())
    }

    @Test
    fun `test avg aggregation`() {
        val aggs = aggregations {
            avg("avg_price", "price")
        }

        assertEquals(1, aggs.size)
        val agg = aggs["avg_price"]
        assertNotNull(agg)
        assertTrue(agg.isAvg)
        assertEquals("price", agg.avg().field())
    }

    @Test
    fun `test sum aggregation`() {
        val aggs = aggregations {
            sum("total_sales", "sales")
        }

        assertEquals(1, aggs.size)
        val agg = aggs["total_sales"]
        assertNotNull(agg)
        assertTrue(agg.isSum)
        assertEquals("sales", agg.sum().field())
    }

    @Test
    fun `test multiple aggregations`() {
        val aggs = aggregations {
            terms("categories", "category")
            min("lowest_price", "price")
            avg("average_rating", "rating")
        }

        assertEquals(3, aggs.size)
        assertTrue(aggs.containsKey("categories"))
        assertTrue(aggs.containsKey("lowest_price"))
        assertTrue(aggs.containsKey("average_rating"))
        
        assertTrue(aggs["categories"]!!.isTerms)
        assertTrue(aggs["lowest_price"]!!.isMin)
        assertTrue(aggs["average_rating"]!!.isAvg)
    }
}
