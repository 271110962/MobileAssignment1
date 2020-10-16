package com.example.controller

import com.example.dao.ProductDao
import com.example.model.Product
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import tornadofx.Controller
import tornadofx.SortedFilteredList
import tornadofx.asObservable
import java.util.*
import kotlin.collections.ArrayList

class ManagementController : Controller(){
    private val dao = ProductDao()
    val products = SortedFilteredList(items = listAllProducts().asObservable())
    var nameSearch = FXCollections.observableArrayList<Product>()


    fun addProduct(name: String, category: String, number: Int,price:Int){
        val product = Product(name,category,number,price)
        dao.addProduct(product)
        products.add(product)

    }


    private fun listAllProducts(): List<Product>{
        return dao.listProducts()
    }

    fun deleteProduct(item: Product){
        dao.deleteProduct(item)
        products.remove(item)
        if(nameSearch!=null){
            nameSearch.remove(item)
        }

    }

    fun updateProduct(oldItem: Product,number:Int,price:Int){
        val product = Product(oldItem.name,oldItem.category,number,price)
        dao.updateProduct(oldItem.name,oldItem.category,product)
        with(products){
            remove(oldItem)
            add(product)

        }
    }

    fun searchProduct(productName:String){
        nameSearch = dao.searchProduct(productName) as ObservableList<Product>?
    }

    fun purchaseProduct(product:Product,purchaseAmount: Int){
        val newProduct = Product(product.name,product.category,product.number-purchaseAmount,product.price)
        if(newProduct.number == 0){
            deleteProduct(product)
        }else {
            dao.purchaseProduct(product, purchaseAmount)
            with(products) {
                remove(product)
                add(newProduct)
            }
            with(nameSearch){
                remove(product)
                add(newProduct)
            }
        }

    }


}