package ar.edu.unlam.mobile.scaffolding.di

import ar.edu.unlam.mobile.scaffolding.data.local.item.ItemRoomRepository
import ar.edu.unlam.mobile.scaffolding.data.local.shoppinglist.ShoppingListRoomRepository
import ar.edu.unlam.mobile.scaffolding.data.repository.item.ItemDefaultRepository
import ar.edu.unlam.mobile.scaffolding.data.repository.item.ItemLocalRepository
import ar.edu.unlam.mobile.scaffolding.data.repository.shoppinglist.ShoppingListDefaultRepository
import ar.edu.unlam.mobile.scaffolding.data.repository.shoppinglist.ShoppingListLocalRepository
import ar.edu.unlam.mobile.scaffolding.domain.item.ItemRepository
import ar.edu.unlam.mobile.scaffolding.domain.shoppinglist.ShoppingListRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModules {
    @Binds
    abstract fun bindShoppingListRepository(shoppingListRepositoryImpl: ShoppingListDefaultRepository): ShoppingListRepository

    @Binds
    abstract fun bindLocalShoppingListRepository(localShoppingListRepository: ShoppingListRoomRepository): ShoppingListLocalRepository

    @Binds
    abstract fun bindItemRepository(itemRepositoryImpl: ItemDefaultRepository): ItemRepository

    @Binds
    abstract fun bindLocalItemRepository(localItemRepository: ItemRoomRepository): ItemLocalRepository
}
