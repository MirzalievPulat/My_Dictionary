package uz.frodo.mydictionary.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import uz.frodo.mydictionary.R
import uz.frodo.mydictionary.adapter.CategoryAdapter
import uz.frodo.mydictionary.adapter.CategoryClick
import uz.frodo.mydictionary.databinding.DeleteDialogBinding
import uz.frodo.mydictionary.databinding.EtidDialogBinding
import uz.frodo.mydictionary.databinding.FirstDialogBinding
import uz.frodo.mydictionary.databinding.FragmentCategoryBinding
import uz.frodo.mydictionary.db.AppDatabase
import uz.frodo.mydictionary.db.Category
import uz.frodo.mydictionary.db.Dao


class CategoryFragment : Fragment(), CategoryClick {
    lateinit var binding: FragmentCategoryBinding
    var disposable = CompositeDisposable()
    lateinit var dao: Dao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryBinding.inflate(layoutInflater)
        dao = AppDatabase.getInstance(requireContext()).dao()

        disposable.add(dao.getAllCategory()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    val adapter = CategoryAdapter(this)
                    adapter.submitList(it)
                    binding.categoryRv.adapter = adapter
                }
            ))

        return binding.root
    }

    override fun click(category: Category) {
        val dialog = AlertDialog.Builder(requireContext()).create()
        val first = FirstDialogBinding.inflate(LayoutInflater.from(requireContext()), null, false)
        dialog.setView(first.root)
        first.firstChange.setOnClickListener {
            val d = AlertDialog.Builder(requireContext()).create()
            val change = EtidDialogBinding.inflate(LayoutInflater.from(requireContext()), null, false)
            d.setView(change.root)
            change.editCategory.setText(category.name)

            change.editSave.setOnClickListener {
                val name = change.editCategory.text.toString().trim()

                if (name.isNotBlank()) {
                    category.name = name
                    dao.updateCategory(category)
                    d.dismiss()
                }else{
                    change.textView.text = "Fill the blank"
                    change.textView.setTextColor(Color.RED)
                }


            }

            change.editCancel.setOnClickListener {
                d.dismiss()
            }
            d.show()

            dialog.dismiss()
        }
        first.firstDelete.setOnClickListener {
            val d = AlertDialog.Builder(requireContext()).create()
            val delete = DeleteDialogBinding.inflate(LayoutInflater.from(requireContext()), null, false)
            d.setView(delete.root)
            delete.textView6.text = "Bu kategoriyaga tegishli so'zlar ham o'chishiga rozimisiz"
            delete.yes.setOnClickListener {

                dao.deleteCategory(category)
                d.dismiss()
            }

            delete.no.setOnClickListener {
                d.dismiss()
            }
            d.show()

            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }
}