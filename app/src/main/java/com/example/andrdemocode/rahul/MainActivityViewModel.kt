package com.example.andrdemocode.rahul

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @author dengyan
 * @date 2022/10/30
 * @desc the ViewModel is designed to hold all of the UI data
 */
class MainActivityViewModel : ViewModel() {

    private val contactLiveData: MutableLiveData<MutableList<Contact>>

    init {
        contactLiveData = MutableLiveData()
        contactLiveData.value = getContacts()
    }

    private fun getContacts(): MutableList<Contact> {
        val contacts = mutableListOf<Contact>()
        for (i in 1..50) {
            contacts.add(Contact("Person #$i", i))
        }
        return contacts
    }

}