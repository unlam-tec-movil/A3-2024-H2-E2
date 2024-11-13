package ar.edu.unlam.mobile.scaffolding.domain.item

import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class ItemServiceTest {
    @Mock
    lateinit var itemRepository: ItemRepository

    lateinit var service: ItemService

    @Before
    fun setUp() {
        service = ItemService(itemRepository)
    }

    @Test
    fun testGetAllItemsStream() =
        runTest {
            val items =
                listOf(
                    ItemModel(id = 1uL, name = "Leche", 2),
                    ItemModel(id = 2uL, name = "Pan", 1),
                )
            whenever(itemRepository.getAllItemsStream()).thenReturn(flowOf(items))

            val result = service.getAllItemsStream()

            result.collect { itemList ->
                assertEquals(items, itemList)
            }
            verify(itemRepository).getAllItemsStream()
        }

    @Test
    fun testGetItemStream() =
        runTest {
            val item = ItemModel(id = 1uL, name = "Leche", 2)
            whenever(itemRepository.getItemStream(1L)).thenReturn(flowOf(item))

            val result = service.getItemStream(1L)

            result.collect { resultItem ->
                assertEquals(item, resultItem)
            }
            verify(itemRepository).getItemStream(1L)
        }

    @Test
    fun testInsertItem() =
        runTest {
            val item = ItemModel(id = 1uL, name = "Leche", 2)
            val insertedId = 1L
            whenever(itemRepository.insertItem(item)).thenReturn(insertedId)

            val result = service.insertItem(item)

            assertEquals(insertedId, result)
            verify(itemRepository).insertItem(item)
        }

    @Test
    fun testDeleteItem() =
        runTest {
            val item = ItemModel(id = 1uL, name = "Leche", 2)

            service.deleteItem(item)

            verify(itemRepository).deleteItem(item)
        }

    @Test
    fun testUpdateItem() =
        runTest {
            val item = ItemModel(id = 1uL, name = "Leche", 2)

            service.updateItem(item)

            verify(itemRepository).updateItem(item)
        }
}
