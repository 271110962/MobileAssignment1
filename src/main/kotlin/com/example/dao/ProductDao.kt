package com.example.dao

import com.example.model.Product
import javafx.collections.FXCollections
import java.sql.Connection
import java.sql.DriverManager

class ProductDao {
    fun addProduct(product: Product){
        val conn = Database().conn
        val ps = conn.prepareStatement("insert into product(name ,category ,number ,price) values(?,?,?,?)")
        ps.setString(1,product.name)
        ps.setString(2,product.category)
        ps.setInt(3,product.number)
        ps.setInt(4,product.price)
        ps.executeUpdate()
        ps.close()
        conn.close()

    }

    fun listProducts():List<Product>{
        val conn = Database().conn
        val resultSet = conn.createStatement().executeQuery("select * from product where number != 0 order by id")
        val products = ArrayList<Product>()
        while(resultSet.next()){
            val name = resultSet.getString("name")
            val category = resultSet.getString("category")
            val number = resultSet.getInt("number")
            val price = resultSet.getInt("price")
            val product = Product(name,category,number,price)
            products.add(product)
        }
        resultSet.close()
        conn.close()
        return products
    }

    fun deleteProduct(item: Product){
        val conn = Database().conn
        val ps = conn.prepareStatement("delete from product where name = ? and category = ? and number = ? and price = ?")
        ps.setString(1,item.name)
        ps.setString(2,item.category)
        ps.setInt(3,item.number)
        ps.setInt(4,item.price)
        ps.executeUpdate()
        ps.close()
        conn.close()
    }

    fun updateProduct(name:String, category:String, item: Product):Product{
        val conn = Database().conn
        val ps = conn.prepareStatement("update product set number = ? , price = ? where name = ? and category = ?")
        ps.setInt(1,item.number)
        ps.setInt(2,item.price)
        ps.setString(3,name)
        ps.setString(4,category)
        ps.executeUpdate()
        ps.close()
        conn.close()
        return item
    }

    fun searchProduct(name: String):List<Product>{
        println(name)
        val conn = Database().conn
        val ps = conn.prepareStatement("select * from product where name like ? or category like ?")
        ps.setString(1, "%$name%")
        ps.setString(2,name)
        val resultSet = ps.executeQuery()
        val searchProducts = FXCollections.observableArrayList<Product>()
        while(resultSet.next()){
            val name = resultSet.getString("name")
            val category = resultSet.getString("category")
            val number = resultSet.getInt("number")
            val price = resultSet.getInt("price")
            val product = Product(name,category,number,price)
            searchProducts.add(product)
        }
        resultSet.close()
        conn.close()
        return searchProducts
    }


    fun purchaseProduct(product:Product, amount:Int):Product{
        val conn = Database().conn
        val ps = conn.prepareStatement("update product set number = (number - ?) where name = ? and category = ?")
        ps.setInt(1,amount)
        ps.setString(2,product.name)
        ps.setString(3,product.category)
        ps.executeUpdate()
        ps.close()
        conn.close()
        return product
    }

}