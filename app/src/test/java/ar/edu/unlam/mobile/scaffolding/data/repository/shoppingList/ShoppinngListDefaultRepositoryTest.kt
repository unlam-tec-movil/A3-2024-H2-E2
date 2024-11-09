package ar.edu.unlam.mobile.scaffolding.data.repository.shoppingList

import ar.edu.unlam.mobile.scaffolding.data.repository.shoppinglist.ShoppingListDefaultRepository
import ar.edu.unlam.mobile.scaffolding.data.repository.shoppinglist.ShoppingListLocalRepository
import ar.edu.unlam.mobile.scaffolding.domain.item.ItemModel
import ar.edu.unlam.mobile.scaffolding.domain.shoppinglist.ShoppingListModel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
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
    fun getAllShoppingListsStreamTest(): Unit =
        runTest {
            // Arrange o given, when y then
            val shoppingLists =
                listOf(
                    ShoppingListModel(
                        id = 1uL,
                        name = "Groceries",
                        listItems = listOf(ItemModel(1uL, "Milk", 1)),
                        selectedColor = 0xFFFFFF,
                        selectedIcon = "ic_groceries",
                    ),
                    ShoppingListModel(
                        id = 2uL,
                        name = "Hardware",
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
                    name = "Groceries",
                    listItems = listOf(ItemModel(1uL, "Milk", 1)),
                    selectedColor = 0xFFFFFF,
                    selectedIcon = "ic_groceries",
                )

            // Act
            repository.insertShoppingList(shoppingList)

            // Assert
            verify(local).insertShoppingList(shoppingList)
        }

    @Test
    fun addItemToListTest() =
        runTest {
            // Arrange
            val listId = 1L
            val itemId = 2L
            val quantity = 3
            val isChecked = true

            // Act
            repository.addItemToList(listId, itemId, quantity, isChecked)

            // Assert
            verify(local).addItemToList(listId, itemId, quantity, isChecked)
        }
}
