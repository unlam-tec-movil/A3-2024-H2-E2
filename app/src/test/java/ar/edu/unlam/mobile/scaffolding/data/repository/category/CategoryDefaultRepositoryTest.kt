package ar.edu.unlam.mobile.scaffolding.data.repository.category

import ar.edu.unlam.mobile.scaffolding.data.local.category.CategoryEntity
import ar.edu.unlam.mobile.scaffolding.data.local.category.CategoryWithItems
import ar.edu.unlam.mobile.scaffolding.data.local.item.ItemEntity
import ar.edu.unlam.mobile.scaffolding.domain.category.CategoryModel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class CategoryDefaultRepositoryTest {
    private val local: CategoryLocalRepository = mock()
    private lateinit var repository: CategoryDefaultRepository

    @Before
    fun setUp() {
        repository = CategoryDefaultRepository(local)
    }

    @Test
    fun testGetAllCategoriesWithItemsStream() =
        runTest {
            val categories =
                listOf(
                    CategoryModel(id = 1uL, name = "Bebidas"),
                    CategoryModel(id = 2uL, name = "Almacen"),
                )
            whenever(local.getAllCategoriesWithItemsStream()).thenReturn(flowOf(categories))

            val result = repository.getAllCategoriesWithItemsStream()

            result.collect { list ->
                assertEquals(categories, list)
            }
            verify(local).getAllCategoriesWithItemsStream()
        }

    @Test
    fun testGetCategoryWithItemsStream() =
        runTest {
            val categoryWithItems =
                CategoryWithItems(
                    category = CategoryEntity(categoryId = 1L, name = "Bebidas"),
                    itemsListsByCategory =
                        listOf(
                            ItemEntity(id = 1L, name = "Leche", 2),
                            ItemEntity(id = 2L, name = "Pan", 1),
                        ),
                )
            whenever(local.getCategoryWithItemsStream(1)).thenReturn(flowOf(categoryWithItems))

            val result = repository.getCategoryWithItemsStream(1)

            result.collect { category ->
                assertEquals(categoryWithItems, category)
            }
            verify(local).getCategoryWithItemsStream(1)
        }
}
