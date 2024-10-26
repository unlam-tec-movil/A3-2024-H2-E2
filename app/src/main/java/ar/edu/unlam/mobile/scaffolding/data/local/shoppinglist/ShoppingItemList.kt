package ar.edu.unlam.mobile.scaffolding.data.local.shoppinglist

import androidx.room.TypeConverter
import ar.edu.unlam.mobile.scaffolding.domain.shoppinglist.ShoppingItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ShoppingItemConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromShopingItemList(list: List<ShoppingItem>): String = gson.toJson(list)

    @TypeConverter
    fun toShoppingItemList(data: String): List<ShoppingItem> {
        val listType = object : TypeToken<List<ShoppingItem>>() {}.type
        return gson.fromJson(data, listType)
    }
}
