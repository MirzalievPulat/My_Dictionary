package uz.frodo.mydictionary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import nl.joery.animatedbottombar.AnimatedBottomBar
import uz.frodo.mydictionary.databinding.ActivitySettingsBinding
import uz.frodo.mydictionary.databinding.EtidDialogBinding
import uz.frodo.mydictionary.db.AppDatabase
import uz.frodo.mydictionary.db.Category
import uz.frodo.mydictionary.db.Dao
import uz.frodo.mydictionary.fragments.CategoryFragment
import uz.frodo.mydictionary.fragments.LikedFragment
import uz.frodo.mydictionary.fragments.MainFragment
import uz.frodo.mydictionary.fragments.WordFragment

class SettingsActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettingsBinding
    lateinit var dao: Dao
    var next = "c"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarSettings)
        dao = AppDatabase.getInstance(this).dao()

        binding.bottomBarSettings.setOnTabSelectListener(object : AnimatedBottomBar.OnTabSelectListener{
            override fun onTabSelected(
                lastIndex: Int,
                lastTab: AnimatedBottomBar.Tab?,
                newIndex: Int,
                newTab: AnimatedBottomBar.Tab
            ) {
                when(newTab.id){
                    R.id.menu_category ->{
                        next = "c"
                        replace(CategoryFragment())
                    }
                    R.id.menu_words ->{
                        next = "w"
                        replace(WordFragment())
                    }

                }
            }
        })
        replace(CategoryFragment())

        binding.toolbarSettings.setNavigationOnClickListener {
            finish()
        }

        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.add_menu,menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId){
                    R.id.add_menu ->{
                        if (next == "c"){
                            var dialog = AlertDialog.Builder(this@SettingsActivity).create()
                            val edBinding = EtidDialogBinding.inflate(LayoutInflater.from(this@SettingsActivity),null,false)
                            dialog.setView(edBinding.root)
                            edBinding.editSave.setOnClickListener {
                                val text = edBinding.editCategory.text.toString().trim()
                                if (text.isNotBlank()){
                                    val category = Category(text)
                                    dao.addCategory(category)
                                    dialog.dismiss()
                                }
                            }
                            edBinding.editCancel.setOnClickListener { dialog.dismiss() }
                            dialog.show()
                        }else if (next == "w"){
                            startActivity(Intent(this@SettingsActivity,AddActivity::class.java))
                        }
                    }
                }
                return true
            }
        })
    }

    fun replace(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(binding.containerSettings.id,fragment).commit()
    }
}