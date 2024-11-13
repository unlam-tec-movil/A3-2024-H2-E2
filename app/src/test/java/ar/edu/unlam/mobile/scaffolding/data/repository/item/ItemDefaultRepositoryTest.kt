package ar.edu.unlam.mobile.scaffolding.data.repository.item

import ar.edu.unlam.mobile.scaffolding.domain.item.ItemModel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class ItemDefaultRepositoryTest {
    private val local: ItemLocalRepository = mock()
    private lateinit var repository: ItemDefaultRepository

    @Before
    fun setUp() {
        repository = ItemDefaultRepository(local)
    }

    @Test
    fun testGetAllItemsStream() =
        runTest {
            val items =
                listOf(
                    ItemModel(id = 1uL, name = "Leche", 2),
                    ItemModel(id = 2uL, name = "Pan", 1),
                )
            whenever(local.getAllItemsStream()).thenReturn(flowOf(items))

            val result = repository.getAllItemsStream()

            result.collect { list ->
                assertEquals(items, list)
            }
            verify(local).getAllItemsStream()
        }

    @Test
    fun testGetItemStream() =
        runTest {
            val item = ItemModel(id = 1uL, name = "Leche", 2)
            whenever(local.getItemStream(1)).thenReturn(flowOf(item))

            val result = repository.getItemStream(1)

            result.collect { it ->
                assertEquals(item, it)
            }
            verify(local).getItemStream(1)
        }

    @Test
    fun testInsertItem() =
        runTest {
            val item = ItemModel(id = 1uL, name = "Leche", 2)
            whenever(local.insertItem(item)).thenReturn(1L)

            val result = repository.insertItem(item)

            assertEquals(1L, result)
            verify(local).insertItem(item)
        }

    @Test
    fun testDeleteItem() =
        runTest {
            val item = ItemModel(id = 1uL, name = "Leche", 2)

            repository.deleteItem(item)

            verify(local).deleteItem(item)
        }

    @Test
    fun testUpdateItem() =
        runTest {
            val item = ItemModel(id = 1uL, name = "Leche 2", 2)

            repository.updateItem(item)

            verify(local).deleteItem(item)
        }
}
