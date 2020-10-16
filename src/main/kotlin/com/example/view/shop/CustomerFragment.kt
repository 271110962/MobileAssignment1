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

class CustomerFragment: Fragment("Customer Shopping Platform") {
    var productfield: TextField by singleAssign()
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

                    field("SearchBox")
                    searchField = textfield(purchaseNameString) {
                        promptText = "Search Product Name or Category"
                    }


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


                    field("The Product you select:")
                    productfield = textfield {
                        isEditable = false
                    }

                    field("Amount Purchase")
                    amountField = textfield(purchaseAmountString) {
                        promptText = "Please Input purchase amount here"
                        filterInput { it.controlNewText.isInt() }
                    }

                    button("Purchase") {
                        spacing = 10.0
                        setOnAction {
                            println(itemPurchase)

                            if (productfield.text != null && amountField.text != null) {
                                if (itemPurchase!!.number - amountField.text.toInt() >= 0) {
                                    managementController.purchaseProduct(itemPurchase!!, amountField.text.toInt())
                                    productfield.clear()
                                    amountField.clear()
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


                onUserSelect(clickCount = 1) { product ->
                    itemPurchase = product
                    productfield.text = itemPurchase?.name
                }
            }

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