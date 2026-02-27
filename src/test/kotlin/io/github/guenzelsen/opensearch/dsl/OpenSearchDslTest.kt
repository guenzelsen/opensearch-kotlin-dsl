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

    @Test
    fun `test simple query string query`() {
        val q: Query = query {
            simpleQueryString("test query", listOf("title", "content"))
        }
        assertTrue(q.isSimpleQueryString)
        assertEquals("test query", q.simpleQueryString().query())
        assertEquals(listOf("title", "content"), q.simpleQueryString().fields())
    }

    @Test
    fun `test wildcard query`() {
        val q: Query = query {
            wildcard("status", "act*")
        }
        assertTrue(q.isWildcard)
        assertEquals("status", q.wildcard().field())
        assertEquals("act*", q.wildcard().value())
    }

    @Test
    fun `test has child query`() {
        val q: Query = query {
            hasChild("employees") {
                query {
                    match("role", "developer")
                }
            }
        }
        assertTrue(q.isHasChild)
        assertEquals("employees", q.hasChild().type())
        
        val childQuery = q.hasChild().query()
        assertTrue(childQuery.isMatch)
        assertEquals("developer", childQuery.match().query().stringValue())
    }

    @Test
    fun `test has child boolean query`() {
        val q: Query = query {
            bool {
                must {
                    hasChild("comments") {
                        query {
                            term("author", "admin")
                        }
                    }
                }
            }
        }
        assertTrue(q.isBool)
        val mustClauses = q.bool().must()
        assertEquals(1, mustClauses.size)
        assertTrue(mustClauses[0].isHasChild)
    }

    @Test
    fun `test has parent query`() {
        val q: Query = query {
            hasParent("company") {
                query {
                    term("department", "engineering")
                }
            }
        }
        assertTrue(q.isHasParent)
        assertEquals("company", q.hasParent().parentType())
        
        val parentQuery = q.hasParent().query()
        assertTrue(parentQuery.isTerm)
        assertEquals("engineering", parentQuery.term().value().stringValue())
    }
}
