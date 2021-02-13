package com.example.respolhpl.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

//@HiltViewModel
class GalleryViewModel @AssistedInject constructor(@Assisted arg : Int) : ViewModel() {

    @AssistedFactory
    interface GalleryViewModelFactory{
        fun create(arg : Int) : GalleryViewModel
    }
    companion object {
        fun provideFactory(
                assistedGalleryViewModelFactory: GalleryViewModelFactory,
                arg: Int
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedGalleryViewModelFactory.create(arg) as T
            }
        }
    }
    private val _text = MutableLiveData<String>().apply {
        value = "This is gallery Fragment"
    }
    val text: LiveData<String> = _text
}