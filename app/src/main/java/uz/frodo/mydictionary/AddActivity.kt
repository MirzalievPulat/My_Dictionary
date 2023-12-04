package uz.frodo.mydictionary

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import uz.frodo.mydictionary.databinding.ActivityAddBinding
import uz.frodo.mydictionary.db.AppDatabase
import uz.frodo.mydictionary.db.Category
import uz.frodo.mydictionary.db.Dao
import uz.frodo.mydictionary.db.Word
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.function.Consumer

class AddActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddBinding
    lateinit var dao: Dao
    var path = ""
    var editWord: Word? = null
    var disposable = CompositeDisposable()
    lateinit var spinnerCategory: List<Category>
    lateinit var spinnerList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarAdd)
        dao = AppDatabase.getInstance(this).dao()




        disposable.add(dao.getAllCategory()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy {
                spinnerCategory = it
                spinnerList = ArrayList(it.map { it.name })
                binding.addCategory.adapter =
                    ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, spinnerList)

                intent.getSerializableExtra("word")?.let {
                    editWord = it as Word
                    val a = spinnerCategory.filter { it.id == editWord!!.categoryId }
                    binding.addCategory.setSelection(spinnerCategory.indexOf(a[0]))
                    binding.addWord.setText(editWord!!.word)
                    binding.addTranslation.setText(editWord!!.translation)
                    binding.imagePlace.setImageURI(Uri.parse(editWord!!.image))
                    path = editWord!!.image

                    supportActionBar?.title = "So'z o'zgartirish"

                }
            })




        binding.addSave.setOnClickListener {
            if (spinnerList.isEmpty()) {
                binding.textView3.text = "Category required"
                binding.textView3.setTextColor(Color.RED)
                binding.addCategory.requestFocus()
                return@setOnClickListener
            }

            val category = spinnerCategory[binding.addCategory.selectedItemPosition]
            val word = binding.addWord.text.toString().trim()
            val trans = binding.addTranslation.text.toString().trim()
            if (word.isBlank()) {
                binding.addWord.error = "Word required"
                binding.addWord.requestFocus()
                return@setOnClickListener
            }

            if (trans.isBlank()) {
                binding.addTranslation.error = "Translation required"
                binding.addTranslation.requestFocus()
                return@setOnClickListener
            }

            if (path == "") {
                binding.textView2.text = "Image required"
                binding.textView2.setTextColor(Color.RED)
                return@setOnClickListener
            }

            val w = Word(category.id, word, trans, path, false)

            if (editWord != null){
                w.id = editWord!!.id
                dao.updateWord(w)
                finish()
            }else{
                dao.addWord(w)
                finish()
            }


        }



        binding.imagePlace.setOnClickListener {
            getImage.launch("image/*")
        }

        binding.addCancel.setOnClickListener {
            finish()
        }

        binding.toolbarAdd.setNavigationOnClickListener {
            finish()
        }
    }

    val getImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri ?: return@registerForActivityResult

        contentResolver.openInputStream(uri).use { ins ->
            val format = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault()).format(Date())
            val file = File(filesDir, "image_${format}.jpg")
            FileOutputStream(file).use { outs ->
                val bitmap = BitmapFactory.decodeStream(ins)
                val resizedBitmap = resizeBitmap(bitmap)
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, outs)
            }
            path = file.absolutePath
            binding.imagePlace.setImageURI(Uri.parse(path))
        }
    }

    private fun resizeBitmap(bitmap: Bitmap): Bitmap {
        val originalWidth = bitmap.width
        val originalHeight = bitmap.height

        val scaleWidth = (originalWidth/3).toFloat() / originalWidth
        val scaleHeight = (originalHeight/3).toFloat() / originalHeight

        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)

        return Bitmap.createBitmap(bitmap, 0, 0, originalWidth, originalHeight, matrix, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }
}