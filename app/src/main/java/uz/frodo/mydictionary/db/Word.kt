package uz.frodo.mydictionary.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(foreignKeys = [
    ForeignKey(entity = Category::class, parentColumns = ["id"], childColumns = ["categoryId"], onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE)
])
data class Word (
    var categoryId:Int,
    var word: String,
    var translation:String,
    var image:String,
    var liked:Boolean
        ):Serializable{
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}