package uz.frodo.mydictionary

import android.R
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import eightbitlab.com.blurview.RenderScriptBlur
import uz.frodo.mydictionary.databinding.ActivityInfoBinding
import uz.frodo.mydictionary.db.AppDatabase
import uz.frodo.mydictionary.db.Word

class InfoActivity : AppCompatActivity() {
    lateinit var binding: ActivityInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarInfo)
        val dao = AppDatabase.getInstance(this).dao()

        val word = intent.getSerializableExtra("word") as Word

        supportActionBar?.title = word.word
        binding.imageInfo.setImageURI(Uri.parse(word.image))
        binding.wordInfo.text = word.word
        binding.transInfo.text = word.translation
        if (word.liked){
            binding.like.isChecked = true
        }


        val toggle = binding.like
        toggle.setOnClickListener {
            if (toggle.isChecked){
                word.liked  = true
                dao.updateWord(word)
            }else{
                word.liked  = false
                dao.updateWord(word)
            }
        }

        binding.toolbarInfo.setNavigationOnClickListener {
            finish()
        }

        val  decorView = window.decorView;
        val rootView = decorView.findViewById<View>(R.id.content) as ViewGroup
        val windowBackground = decorView.background
        binding.blur.setupWith(rootView, RenderScriptBlur(this))
            .setFrameClearDrawable(windowBackground)
            .setBlurRadius(25F)

    }
}