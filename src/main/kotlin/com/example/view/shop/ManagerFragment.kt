package com.example.view.shop

import com.example.controller.ManagementController
import com.example.model.Product
import com.example.utils.PopupDialog
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.scene.control.ComboBox
import javafx.scene.control.TextField
import javafx.stage.StageStyle
import tornadofx.*

class ManagerFragment : Fragment("Management System"){
    var nameField: TextField by singleAssign()
    var numberField: TextField by singleAssign()
    var priceField: TextField by singleAssign()

    private var productnameString = SimpleStringProperty()
    private var productcategoryString = SimpleStringProperty()
    private var productnumberString = SimpleStringProperty()
    private var productpriceString = SimpleStringProperty()
    private val categoryBox = FXCollections.observableArrayList("Food",
            "Electronic","Books","Clothes","Sports","Shoes","Fruits","Tableware")

    private lateinit var categoryCb: ComboBox<String>
    var item :Product? = null
    private val managementController: ManagementController by inject()

    override val root = borderpane {

        left = vbox {
            form {
                setPrefSize(230.0, 600.0)
                fieldset {
                    field("Product Name")
                    nameField = textfield(productnameString){
                        promptText = "Please Input product name here"
                    }
                    field("Product Category")
                    categoryCb = combobox(productcategoryString, categoryBox)
                    field("Product Amount")
                    numberField = textfield(productnumberString) {
                        promptText = "Please Input Integer here"
                        filterInput { it.controlNewText.isInt() }
                    }
                    field("Product Price")
                    priceField = textfield(productpriceString) {
                        promptText = "Please Input Integer here"
                        filterInput { it.controlNewText.isInt() }
                    }


                    button("On Shelf") {
                        spacing = 10.0
                         setOnAction {
                             managementController.addProduct(productnameString.value, productcategoryString.value, Integer.parseInt(productnumberString.value), Integer.parseInt(productpriceString.value))
                             find<PopupDialog>(params = mapOf("message" to "On Shelf Success!!!")).openModal()
                             nameField.clear()
                             numberField.clear()
                             priceField.clear()
                         }
                    }

                    button("Off Shelf") {
                        spacing = 10.0
                        action {
                            println("The product I select to delete is:   $item")
                            managementController.deleteProduct(item!!)
                            find<PopupDialog>(params = mapOf("message" to "Off Shelf Success!!!")).openModal()
                            nameField.clear()
                            numberField.clear()
                            priceField.clear()
                        }
                    }

                    button("Update Product"){
                        spacing = 10.0
                        action {
                            println("The product I select to update is:   $item")
                            if(item!!.name == nameField.text && item!!.category == categoryCb.selectionModel.selectedItem){
                                managementController.updateProduct(item!!,numberField.text.toInt(),priceField.text.toInt())
                                find<PopupDialog>(params = mapOf("message" to "Update Product Success!!!")).openModal()
                                nameField.clear()
                                numberField.clear()
                                priceField.clear()
                            }else{
                                find<PopupDialog>(params = mapOf("message" to "You can't change name and category!!!")).openModal()
                            }
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
                item = product
                nameField.text = item?.name
                categoryCb.selectionModel.select(item?.category)
                numberField.text = item?.number.toString()
                priceField.text = item?.price.toString()
            }

        }




    }
}