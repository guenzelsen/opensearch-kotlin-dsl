package io.github.guenzelsen.opensearch.dsl

import org.opensearch.client.opensearch._types.FieldValue
import org.opensearch.client.opensearch._types.query_dsl.Query
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertNotNull

class OpenSearchDslTest {

    @Test
    fun `test match query`() {
        val q: Query = query {
            match("title", "Kotlin")
        }
        assertTrue(q.isMatch)
        assertEquals("title", q.match().field())
        assertEquals("Kotlin", q.match().query().stringValue())
    }

    @Test
    fun `test term query`() {
        val q: Query = query {
            term("status", "active")
        }
        assertTrue(q.isTerm)
        assertEquals("status", q.term().field())
        assertEquals("active", q.term().value().stringValue())
    }

    @Test
    fun `test terms query`() {
        val q: Query = query {
            terms("tags", listOf("programming", "kotlin"))
        }
        assertTrue(q.isTerms)
        assertEquals("tags", q.terms().field())
        val values = q.terms().terms().value()
        assertEquals(2, values.size)
        assertEquals("programming", values[0].stringValue())
        assertEquals("kotlin", values[1].stringValue())
    }

    @Test
    fun `test nested query`() {
        val q: Query = query {
            nested("user") {
                query {
                    match("user.name", "john")
                }
            }
        }
        assertTrue(q.isNested)
        assertEquals("user", q.nested().path())
        val nestedQ = q.nested().query()
        assertTrue(nestedQ.isMatch)
        assertEquals("john", nestedQ.match().query().stringValue())
    }

    @Test
    fun `test complex bool query`() {
        val q: Query = query {
            bool {
                must {
                    match("title", "Kotlin DSL")
                    match("content", "search")
                }
                filter {
                    term("status", "published")
                    terms("tags", listOf("tutorial", "guide"))
                }
                mustNot {
                    term("author", "anonymous")
                }
                should {
                    nested("comments") {
                        query {
                            match("comments.text", "great")
                        }
                    }
                }
            }
        }
        
        assertTrue(q.isBool)
        val boolQuery = q.bool()
        
        // must
        assertEquals(2, boolQuery.must().size)
        assertTrue(boolQuery.must()[0].isMatch)
        assertEquals("Kotlin DSL", boolQuery.must()[0].match().query().stringValue())
        
        // filter
        assertEquals(2, boolQuery.filter().size)
        assertTrue(boolQuery.filter()[0].isTerm)
        assertEquals("published", boolQuery.filter()[0].term().value().stringValue())
        assertTrue(boolQuery.filter()[1].isTerms)
        
        // mustNot
        assertEquals(1, boolQuery.mustNot().size)
        assertTrue(boolQuery.mustNot()[0].isTerm)
        assertEquals("anonymous", boolQuery.mustNot()[0].term().value().stringValue())
        
        // should
        assertEquals(1, boolQuery.should().size)
        assertTrue(boolQuery.should()[0].isNested)
        assertEquals("comments", boolQuery.should()[0].nested().path())
    }
}
