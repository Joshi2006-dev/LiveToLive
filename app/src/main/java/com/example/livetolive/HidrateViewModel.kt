package com.example.livetolive

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HidrateViewModel: ViewModel() {
    var objetivo = MutableLiveData<Float>()
}