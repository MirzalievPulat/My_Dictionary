package uz.frodo.mydictionary.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable

@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCategory(category: Category)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addWord(word: Word)

    @Update
    fun updateCategory(category: Category)

    @Update
    fun updateWord(word: Word)

    @Delete
    fun deleteCategory(category: Category)

    @Delete
    fun deleteWord(word: Word)


    @Query("select * from Category")
    fun getAllCategory():Flowable<List<Category>>

    @Query("select * from Word")
    fun getAllWords():Flowable<List<Word>>

    @Query("select * from Word where categoryId=:categoryId")
    fun getWordsByCategory(categoryId:Int):Flowable<List<Word>>

    @Query("select * from Word where liked=1")
    fun getLikedWords():Flowable<List<Word>>
}