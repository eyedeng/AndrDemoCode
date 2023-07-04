package com.example.andrdemocode.rahul

/**
 * @author dengyan
 * @date 2022/10/29
 * @desc
 */
data class Contact(val name: String, val age: Int) {
    val imageUrl = "https://picsum.photos/150?random=$age"
}
