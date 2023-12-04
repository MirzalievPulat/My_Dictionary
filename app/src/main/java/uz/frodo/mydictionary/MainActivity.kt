package uz.frodo.mydictionary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import eightbitlab.com.blurview.RenderScriptBlur
import nl.joery.animatedbottombar.AnimatedBottomBar
import uz.frodo.mydictionary.databinding.ActivityMainBinding
import uz.frodo.mydictionary.fragments.LikedFragment
import uz.frodo.mydictionary.fragments.MainFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMain)

        binding.bottomBar.setOnTabSelectListener(object : AnimatedBottomBar.OnTabSelectListener{
            override fun onTabSelected(
                lastIndex: Int,
                lastTab: AnimatedBottomBar.Tab?,
                newIndex: Int,
                newTab: AnimatedBottomBar.Tab
            ) {
                when(newTab.id){
                    R.id.menu_home ->{
                        replace(MainFragment())
                    }
                    R.id.menu_liked ->{
                        replace(LikedFragment())
                    }

                }
            }
        })
        replace(MainFragment())



        addMenuProvider(object :MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.edit_menu,menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId){
                    R.id.edit_menu ->{
                        startActivity(Intent(this@MainActivity,SettingsActivity::class.java))
                    }
                }
                return true
            }
        })
    }

    fun replace(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(binding.containerMain.id,fragment).commit()
    }
}