package uz.frodo.mydictionary.adapter

import uz.frodo.mydictionary.db.Category
import uz.frodo.mydictionary.db.Word

interface CategoryClick {
    fun click(category: Category)
}