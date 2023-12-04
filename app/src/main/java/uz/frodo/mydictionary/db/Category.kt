package uz.frodo.mydictionary.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Category(
    var name:String
) :Serializable{
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}