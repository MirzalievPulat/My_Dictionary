package uz.frodo.mydictionary.adapter

import uz.frodo.mydictionary.db.Word

interface ItemClick {
    fun Click(word: Word)
}