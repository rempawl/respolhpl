package com.example.respolhpl.utils

import androidx.lifecycle.LiveData
import com.example.respolhpl.data.Result

abstract class ResultViewModel  : ObservableViewModel(){
    abstract val result : LiveData<Result<*>>
}