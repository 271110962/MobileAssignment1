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
            "Electronic","Books","Clothes","Sports","Shoes","Fruits")

    private lateinit var categoryCb: ComboBox<String>
    var item :Product? = null
    private val managementController: ManagementController by inject()

    override val root = borderpane {

        left = vbox {
            form {
                setPrefSize(400.0, 600.0)
                fieldset {
                    field("Product Name")
                    nameField = textfield(productnameString)
                    field("Product Category")
                    categoryCb = combobox(productcategoryString, categoryBox)
                    field("Product Number")
                    numberField = textfield(productnumberString) {
                        filterInput { it.controlNewText.isInt() }
                    }
                    field("Product Price")
                    priceField = textfield(productpriceString) {
                        filterInput { it.controlNewText.isInt() }
                    }


                    button("On Shelf") {
                         setOnAction {
                             managementController.addProduct(productnameString.value, productcategoryString.value, Integer.parseInt(productnumberString.value), Integer.parseInt(productpriceString.value))
                             find<PopupDialog>(params = mapOf("message" to "On Shelf Success!!!")).openModal()
                             nameField.clear()
                             numberField.clear()
                             priceField.clear()
                         }
                    }

                    button("Off Shelf") {
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

            column("Name",String::class){
                value{
                    it.value.name
                }
            }
            column("Category",String::class){
                value{
                    it.value.category
                }
            }
            column("Amount",Int::class){
                value{
                    it.value.number
                }
            }
            column("Price",Int::class){
                value{
                    it.value.price
                }
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