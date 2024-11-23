package ar.edu.unlam.mobile.scaffolding.domain.shoppingListRepository

import ar.edu.unlam.mobile.scaffolding.R
import ar.edu.unlam.mobile.scaffolding.data.local.item.ItemEntity
import ar.edu.unlam.mobile.scaffolding.data.local.shoppinglist.ItemWithQuantityAndChecked
import ar.edu.unlam.mobile.scaffolding.data.local.shoppinglist.ShoppingListEntity
import ar.edu.unlam.mobile.scaffolding.data.local.shoppinglist.ShoppingListWithItems
import ar.edu.unlam.mobile.scaffolding.domain.item.ItemModel
import ar.edu.unlam.mobile.scaffolding.domain.shoppinglist.ShoppingListModel
import ar.edu.unlam.mobile.scaffolding.domain.shoppinglist.ShoppingListRepository
import ar.edu.unlam.mobile.scaffolding.domain.shoppinglist.ShoppingListService
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class ShoppingListServiceTest {
    private val shoppingListRepository: ShoppingListRepository = mock()
    private lateinit var service: ShoppingListService

    @Before
    fun setUp() {
        service = ShoppingListService(shoppingListRepository)
    }

    @Test
    fun testGetAllShoppingLists() =
        runTest {
            val shoppingLists =
                listOf(
                    ShoppingListModel(
                        id = 1uL,
                        name = "Bebidas",
                        listItems = listOf(ItemModel(1uL, "Leche", 1)),
                        selectedImage = R.drawable.image0,
                    ),
                    ShoppingListModel(
                        id = 2uL,
                        name = "Almacen",
                        listItems = listOf(ItemModel(2uL, "Nails", 50)),
                        selectedImage = R.drawable.image0,
                    ),
                )
            whenever(shoppingListRepository.getAllShoppingListsStream()).thenReturn(flowOf(shoppingLists))

            val result = service.getAllShoppingLists()

            result.collect { list ->
                assertEquals(shoppingLists, list)
            }
            verify(shoppingListRepository).getAllShoppingListsStream()
        }

    @Test
    fun testGetShoppingList() =
        runTest {
            val shoppingList =
                ShoppingListModel(
                    id = 2uL,
                    name = "Almacen",
                    listItems = listOf(ItemModel(2uL, "Nails", 50)),
                    selectedImage = R.drawable.image0,
                )
            whenever(shoppingListRepository.getShoppingListStream(1)).thenReturn(flowOf(shoppingList))

            val result = service.getShoppingList(1)

            result.collect { list ->
                assertEquals(shoppingList, list)
            }
            verify(shoppingListRepository).getShoppingListStream(1)
        }

    @Test
    fun testInsertShoppingList() =
        runTest {
            val shoppingList =
                ShoppingListModel(
                    id = 2uL,
                    name = "Almacen",
                    listItems = listOf(ItemModel(2uL, "Nails", 50)),
                    selectedImage = R.drawable.image0,
                )

            service.insertShoppingList(shoppingList)

            verify(shoppingListRepository).insertShoppingList(shoppingList)
        }

    @Test
    fun testDeleteShoppingList() =
        runTest {
            val shoppingList =
                ShoppingListModel(
                    id = 2uL,
                    name = "Almacen",
                    listItems = listOf(ItemModel(2uL, "Nails", 50)),
                    selectedImage = R.drawable.image0,
                )

            service.deleteShoppingList(shoppingList)

            verify(shoppingListRepository).deleteShoppingList(shoppingList)
        }

    @Test
    fun testUpdateShoppingList() =
        runTest {
            val shoppingList =
                ShoppingListModel(
                    id = 2uL,
                    name = "Almacen",
                    listItems = listOf(ItemModel(2uL, "Nails", 50)),
                    selectedImage = R.drawable.image0,
                )

            service.updateShoppingList(shoppingList)

            verify(shoppingListRepository).updateShoppingList(shoppingList)
        }

    @Test
    fun testAddItemToList() =
        runTest {
            val listId = 1L
            val itemId = 1L
            val quantity = 2
            val isChecked = true

            service.addItemToList(listId, itemId, quantity, isChecked)

            verify(shoppingListRepository).addItemToList(listId, itemId, quantity, isChecked)
        }

    @Test
    fun testDeleteItemFromList() =
        runTest {
            val listId = 1L
            val itemId = 1L

            service.deleteItemFromList(listId, itemId)

            verify(shoppingListRepository).deleteItemFromList(listId, itemId)
        }

    @Test
    fun testGetShoppingListWithItems() =
        runTest {
            val shoppingListWithItems =
                ShoppingListWithItems(
                    shoppingList =
                        ShoppingListEntity(
                            id = 1L,
                            name = "Bebidas",
                            selectedImage = R.drawable.image0,
                        ),
                    itemsByShoppingList = listOf(ItemEntity(id = 1L, name = "Leche", 2)),
                )
            whenever(shoppingListRepository.getShoppingListWithItemsStream(1)).thenReturn(flowOf(shoppingListWithItems))

            val result = service.getShoppingListWithItems(1)

            result.collect { list ->
                assertEquals(shoppingListWithItems, list)
            }
            verify(shoppingListRepository).getShoppingListWithItemsStream(1)
        }

    @Test
    fun testGetItemsForShoppingList() =
        runTest {
            val items =
                listOf(
                    ItemWithQuantityAndChecked(id = 1L, name = "Leche", quantity = 2, isChecked = false),
                    ItemWithQuantityAndChecked(id = 2L, name = "Pan", quantity = 1, isChecked = true),
                )
            whenever(shoppingListRepository.getItemsForShoppingList(1)).thenReturn(flowOf(items))

            val result = service.getItemsForShoppingList(1)

            result.collect { itemList ->
                assertEquals(items, itemList)
            }
            verify(shoppingListRepository).getItemsForShoppingList(1)
        }

    @Test
    fun testUpdateItemCheckedState() =
        runTest {
            val itemId = 1L
            val listId = 1L
            val checked = true

            service.updateItemCheckedState(itemId, listId, checked)

            verify(shoppingListRepository).updateItemCheckedState(itemId, listId, checked)
        }
}
