package com.example.view.shop

import com.example.controller.ManagementController
import com.example.model.Product
import com.example.utils.PopupDialog
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.TextField
import tornadofx.*

class CustomerFragment: Fragment("Customer Shopping Platform") {
    var productfield: TextField by singleAssign()
    var amountField: TextField by singleAssign()
    var searchField: TextField by singleAssign()
    private var purchaseAmountString = SimpleStringProperty()
    private var purchaseNameString = SimpleStringProperty()
    var itemPurchase :Product? = null
    private val managementController: ManagementController by inject()
    override val root = borderpane {

        left = vbox {
            form{
                setPrefSize(230.0, 600.0)
                fieldset {

                    field("SearchBox")
                    searchField = textfield(purchaseNameString){
                        promptText = "Please Input the product you want to purchase here"
                    }

                    button("Search") {
                        spacing = 10.0
                        setOnAction {

                        }
                    }


                    field("The Product you select:")
                    productfield = textfield{
                        isEditable = false
                    }

                    field("Amount Purchase")
                    amountField = textfield(purchaseAmountString){
                        promptText = "Please Input purchase amount here"
                        filterInput { it.controlNewText.isInt() }
                    }

                    button("Purchase") {
                        spacing = 10.0
                        setOnAction {

                            find<PopupDialog>(params = mapOf("message" to "Purchase Success!!!")).openModal()
                        }
                    }


                }
            }
        }

        center  = tableview<Product> {
            items = managementController.products
            columnResizePolicy = SmartResize.POLICY

            column("Name",String::class){
                value{
                    it.value.name
                }
                remainingWidth()
            }
            column("Category",String::class){
                value{
                    it.value.category
                }
                remainingWidth()
            }
            column("Amount",Int::class){
                value{
                    it.value.number
                }
                remainingWidth()
            }
            column("Price",Int::class){
                value{
                    it.value.price
                }
                remainingWidth()
            }


            onUserSelect(clickCount = 1) { product ->
                itemPurchase = product
                productfield.text = itemPurchase?.name
                println("The Product you select: ${itemPurchase?.name}")
            }
        }

    }
}