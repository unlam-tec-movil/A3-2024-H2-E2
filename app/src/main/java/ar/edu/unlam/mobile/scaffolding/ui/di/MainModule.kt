package ar.edu.unlam.mobile.scaffolding.ui.di

import ar.edu.unlam.mobile.scaffolding.domain.item.services.ItemService
import ar.edu.unlam.mobile.scaffolding.domain.item.usecases.ItemsUseCases
import ar.edu.unlam.mobile.scaffolding.domain.shoppinglist.services.ShoppingListService
import ar.edu.unlam.mobile.scaffolding.domain.shoppinglist.usecases.ShoppingListsUseCases
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
