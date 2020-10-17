package com.example.view.shop

import com.example.controller.ManagementController
import com.example.model.Product
import com.example.utils.PopupDialog
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.scene.control.ComboBox
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import tornadofx.*
import java.util.*
import kotlin.collections.ArrayList

/***
 * This is the Customer Page that could let customer shopping.
 * It contains the Search function, you can search the product by name or category
 *
 * Purchase function, you can purchase product after you select the item
 *
 * Filtering function, you need to select AllCategory option to show all products after search something. Or select the category you want to show.
 */

class CustomerFragment: Fragment("Customer Shopping Platform") {
    var productField: TextField by singleAssign()
    var amountField: TextField by singleAssign()
    var searchField: TextField by singleAssign()
    private lateinit var filterCb: ComboBox<String>
    lateinit var table: TableView<Product>
    private var purchaseAmountString = SimpleStringProperty()
    private var purchaseNameString = SimpleStringProperty()
    private var productCategory = SimpleStringProperty()
    var itemPurchase :Product? = null
    private val filterBox = FXCollections.observableArrayList("All Category","Food",
            "Electronic","Books","Clothes","Sports","Shoes","Fruits","Tableware")
    private val managementController: ManagementController by inject()
    override val root = borderpane {

        left = vbox {
            form {
                setPrefSize(230.0, 600.0)
                fieldset {
                    //Search textfield that can input the product name or product category
                    field("SearchBox")
                    searchField = textfield(purchaseNameString) {
                        promptText = "Search Product Name or Category"
                    }

                    //Search button
                    button("Search") {
                        spacing = 10.0
                        setOnAction {
                            if (searchField.text != null) {
                                managementController.searchProduct(searchField.text)
                                table.items = managementController.nameSearch
                            } else {
                                find<PopupDialog>(params = mapOf("message" to "You need to fill the box !!!")).openModal()
                            }
                        }
                    }

                    //Textfield that show the product you select and the field is uneditable
                    field("The Product you select:")
                    productField = textfield {
                        isEditable = false
                    }
                    //Textfield that input the amount of product want to purchase
                    field("Amount Purchase")
                    amountField = textfield(purchaseAmountString) {
                        promptText = "Please Input purchase amount here"
                        filterInput { it.controlNewText.isInt() }
                    }
                    //Purchase button
                    button("Purchase") {
                        spacing = 10.0
                        setOnAction {
                            println(itemPurchase)

                            if (productField.text != null && amountField.text != null) {
                                if (itemPurchase!!.number - amountField.text.toInt() >= 0) {
                                    managementController.purchaseProduct(itemPurchase!!, amountField.text.toInt())
                                    productField.text = null
                                    amountField.text = null
                                    find<PopupDialog>(params = mapOf("message" to "Purchase Success!!!")).openModal()
                                    itemPurchase = null
                                    println(itemPurchase)
                                } else {
                                    find<PopupDialog>(params = mapOf("message" to "We don't have enough goods !!!")).openModal()
                                }
                            } else {
                                find<PopupDialog>(params = mapOf("message" to "You need to fill the box !!!")).openModal()
                            }

                        }
                    }


                }
            }
        }
        //A Vertical box contains the tableview
        center = vbox {
            table = tableview<Product> {
                items = managementController.products
                columnResizePolicy = SmartResize.POLICY

                column("Name", String::class) {
                    value {
                        it.value.name
                    }
                    remainingWidth()
                }
                column("Category", String::class) {
                    value {
                        it.value.category
                    }
                    remainingWidth()
                }
                column("Amount", Int::class) {
                    value {
                        it.value.number
                    }
                    remainingWidth()
                }
                column("Price", Int::class) {
                    value {
                        it.value.price
                    }
                    remainingWidth()
                }

                //make the tableview was selectable and return selected product.
                onUserSelect(clickCount = 1) { product ->
                    itemPurchase = product
                    productField.text = itemPurchase?.name
                }
            }
            //Filtering Box, All Category return all products, others return product that category you choose
            label("Category Filter")
            spacing = 10.0
            filterCb = combobox(productCategory,filterBox){
                setOnAction {
                    if(this.value == "All Category"){
                        table.items = managementController.products
                    }else{
                        managementController.searchProduct(this.value)
                        table.items = managementController.nameSearch
                    }
                }
            }

            }

        }

}