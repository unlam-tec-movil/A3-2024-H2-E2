package ar.edu.unlam.mobile.scaffolding.di

import android.content.Context
import ar.edu.unlam.mobile.scaffolding.data.local.AppDatabase
import ar.edu.unlam.mobile.scaffolding.data.local.item.ItemDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceProviders {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase = AppDatabase.getDatabase(context)

    @Provides
    fun provideShoppingListDao(db: AppDatabase) = db.shoppingListDao()

    @Provides
    fun provideItemDao(db: AppDatabase): ItemDao = db.itemDao()

    @Provides
    fun provideCategoryDao(db: AppDatabase) = db.categoryDao()
}
