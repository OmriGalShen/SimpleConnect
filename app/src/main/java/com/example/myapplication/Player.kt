package com.example.myapplication

import android.media.Image

class Player(name:String, color:Int,image:Int) {
    var name = name
    var color = color
    var image = image

    fun turnMessage():String{
        return "${this.name} Turn"
    }
    fun winMessage():String{
        return "${this.name} Won!"
    }
    fun equals(other:Player?):Boolean{
        if(other==null)return false
        return other.name==this.name&&other.color==this.color&&other.image==this.image
    }
}