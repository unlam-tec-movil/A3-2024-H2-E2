package ar.edu.unlam.mobile.scaffolding.data.repository.shoppingList

import ar.edu.unlam.mobile.scaffolding.data.local.item.ItemEntity
import ar.edu.unlam.mobile.scaffolding.data.local.shoppinglist.ItemWithQuantityAndChecked
import ar.edu.unlam.mobile.scaffolding.data.local.shoppinglist.ShoppingListEntity
import ar.edu.unlam.mobile.scaffolding.data.local.shoppinglist.ShoppingListWithItems
import ar.edu.unlam.mobile.scaffolding.data.repository.shoppinglist.ShoppingListDefaultRepository
import ar.edu.unlam.mobile.scaffolding.data.repository.shoppinglist.ShoppingListLocalRepository
import ar.edu.unlam.mobile.scaffolding.domain.item.ItemModel
import ar.edu.unlam.mobile.scaffolding.domain.shoppinglist.ShoppingListModel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class ShoppinngListDefaultRepositoryTest {
    @Mock
    private lateinit var local: ShoppingListLocalRepository

    private lateinit var repository: ShoppingListDefaultRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = ShoppingListDefaultRepository(local)
    }

    @Test
    fun testGetAllShoppingListsStream(): Unit =
        runTest {
            // Arrange o given, when y then
            val shoppingLists =
                listOf(
                    ShoppingListModel(
                        id = 1uL,
                        name = "Bebidas",
                        listItems = listOf(ItemModel(1uL, "Milk", 1)),
                        selectedColor = 0xFFFFFF,
                        selectedIcon = "ic_groceries",
                    ),
                    ShoppingListModel(
                        id = 2uL,
                        name = "Almacen",
                        listItems = listOf(ItemModel(2uL, "Nails", 50)),
                        selectedColor = 0x000000,
                        selectedIcon = "ic_hardware",
                    ),
                )
            whenever(local.getAllShoppingListsStream()).thenReturn(flowOf(shoppingLists))

            // Act
            val result = repository.getAllShoppingListsStream()

            // Assert
            result.collect {
                assert(it == shoppingLists)
            }
            verify(local).getAllShoppingListsStream()
        }

    @Test
    fun insertShoppingListTest() =
        runTest {
            // Arrange
            val shoppingList =
                ShoppingListModel(
                    id = 1uL,
                    name = "Bebidas",
                    listItems = listOf(ItemModel(1uL, "Leche", 1)),
                    selectedColor = 0xFFFFFF,
                    selectedIcon = "ic_bebidas",
                )

            // Act
            repository.insertShoppingList(shoppingList)

            // Assert
            verify(local).insertShoppingList(shoppingList)
        }

    @Test
    fun testGetShoppingListStream() =
        runTest {
            val shoppingList =
                ShoppingListModel(
                    id = 1uL,
                    name = "Bebidas",
                    listItems = listOf(ItemModel(1uL, "Leche", 1)),
                    selectedColor = 0xFFFFFF,
                    selectedIcon = "ic_Bebidas",
                )
            whenever(local.getShoppingListStream(1)).thenReturn(flowOf(shoppingList))

            val result = repository.getShoppingListStream(1)

            result.collect { list ->
                assertEquals(shoppingList, list)
            }
            verify(local).getShoppingListStream(1)
        }

    @Test
    fun testDeleteShoppingList() =
        runTest {
            val shoppingList =
                ShoppingListModel(
                    id = 1uL,
                    name = "Bebidas",
                    listItems = listOf(ItemModel(1uL, "Leche", 1)),
                    selectedColor = 0xFFFFFF,
                    selectedIcon = "ic_Bebidas",
                )

            repository.deleteShoppingList(shoppingList)

            verify(local).deleteShoppingList(shoppingList)
        }

    @Test
    fun testUpdateShoppingList() =
        runTest {
            val shoppingList =
                ShoppingListModel(
                    id = 1uL,
                    name = "Bebidas",
                    listItems = listOf(ItemModel(1uL, "Leche", 1)),
                    selectedColor = 0xFFFFFF,
                    selectedIcon = "ic_Bebidas",
                )

            repository.updateShoppingList(shoppingList)

            verify(local).updateShoppingList(shoppingList)
        }

    @Test
    fun testAddItemToList() =
        runTest {
            val listId = 1L
            val itemId = 2L
            val quantity = 3
            val isChecked = true

            repository.addItemToList(listId, itemId, quantity, isChecked)

            verify(local).addItemToList(listId, itemId, quantity, isChecked)
        }

    @Test
    fun testDeleteItemFromList() =
        runTest {
            val listId = 1L
            val itemId = 1L

            repository.deleteItemFromList(listId, itemId)

            verify(local).deleteItemFromList(listId, itemId)
        }

    @Test
    fun testGetShoppingListWithItemsStream() =
        runTest {
            val shoppingListWithItems =
                ShoppingListWithItems(
                    shoppingList =
                        ShoppingListEntity(
                            id = 1L,
                            name = "Bebidas",
                            selectedColor = 0xFFFFFF,
                            selectedIcon = "ic_Bebidas",
                        ),
                    itemsByShoppingList = listOf(ItemEntity(id = 1L, name = "Leche", 2)),
                )
            whenever(local.getShoppingListWithItemsStream(1)).thenReturn(flowOf(shoppingListWithItems))

            val result = repository.getShoppingListWithItemsStream(1)

            result.collect { list ->
                assertEquals(shoppingListWithItems, list)
            }
            verify(local).getShoppingListWithItemsStream(1)
        }

    @Test
    fun testGetItemsForShoppingList() =
        runTest {
            val items =
                listOf(
                    ItemWithQuantityAndChecked(id = 1L, name = "Leche", quantity = 2, isChecked = false),
                    ItemWithQuantityAndChecked(id = 2L, name = "Pan", quantity = 1, isChecked = true),
                )
            whenever(local.getItemsForShoppingList(1)).thenReturn(flowOf(items))

            val result = repository.getItemsForShoppingList(1)

            result.collect { itemList ->
                assertEquals(items, itemList)
            }
            verify(local).getItemsForShoppingList(1)
        }

    @Test
    fun testUpdateItemCheckedState() =
        runTest {
            val itemId = 1L
            val listId = 1L
            val checked = true

            repository.updateItemCheckedState(itemId, listId, checked)

            verify(local).updateItemCheckedState(itemId, listId, checked)
        }
}
