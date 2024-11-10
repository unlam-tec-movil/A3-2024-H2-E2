package ar.edu.unlam.mobile.scaffolding.domain.category

import ar.edu.unlam.mobile.scaffolding.data.local.category.CategoryEntity
import ar.edu.unlam.mobile.scaffolding.data.local.category.CategoryWithItems
import ar.edu.unlam.mobile.scaffolding.data.local.item.ItemEntity
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
class CategoryServiceTest {
    @Mock
    lateinit var categoryRepository: CategoryRepository
    lateinit var service: CategoryService

    @Before
    fun setUp() {
        service = CategoryService(categoryRepository)
    }

    @Test
    fun testGetAllCategoriesWithItemsStream() =
        runTest {
            val categories =
                listOf(
                    CategoryModel(id = 1uL, name = "Bebidas"),
                    CategoryModel(id = 2uL, name = "Almacen"),
                )
            // Mockeamos el comportamiento del repositorio
            whenever(categoryRepository.getAllCategoriesWithItemsStream()).thenReturn(flowOf(categories))

            val result = service.getAllCategoriesWithItemsStream()

            result.collect { categoryList ->
                assertEquals(categories, categoryList)
            }
            verify(categoryRepository).getAllCategoriesWithItemsStream()
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
            whenever(categoryRepository.getCategoryWithItemsStream(1L)).thenReturn(flowOf(categoryWithItems))

            val result = service.getCategoryWithItemsStream(1L)

            result.collect { resultCategory ->
                assertEquals(categoryWithItems, resultCategory)
            }
            verify(categoryRepository).getCategoryWithItemsStream(1L)
        }
}
