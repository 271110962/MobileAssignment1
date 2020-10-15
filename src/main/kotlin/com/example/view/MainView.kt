package com.example.view

import com.example.Styles
import tornadofx.*

class MainView : View("Shop") {
    override val root = borderpane {
        label(title) {
            addClass(Styles.heading)
        }

        top(Header::class)
    }
}
