package ar.edu.unlam.mobile.scaffolding.ui.di

import ar.edu.unlam.mobile.scaffolding.domain.item.ItemService
import ar.edu.unlam.mobile.scaffolding.domain.item.ItemsUseCases
import ar.edu.unlam.mobile.scaffolding.domain.shoppinglist.ShoppingListService
import ar.edu.unlam.mobile.scaffolding.domain.shoppinglist.ShoppingListsUseCases
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class MainModule {
    @Binds
    abstract fun bindLists(listService: ShoppingListService): ShoppingListsUseCases

    @Binds
    abstract fun bindItems(itemService: ItemService): ItemsUseCases
}
