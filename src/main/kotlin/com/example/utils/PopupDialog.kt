package com.example.utils

import com.example.Styles
import javafx.scene.Parent
import tornadofx.*

class PopupDialog : Fragment(){
    val message: String by param()
    override val root = vbox {

        label(message){
            addClass(Styles.heading)
        }
    }
}