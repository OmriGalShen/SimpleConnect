package com.example.myapplication

class Player(name:String, color:String) {
    var name = name
    var color = color

    fun turnMessage():String{
        return "${this.name} Turn"
    }
}