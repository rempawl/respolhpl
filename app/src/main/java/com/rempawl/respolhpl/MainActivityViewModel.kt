package com.rempawl.respolhpl

import androidx.lifecycle.ViewModel
import com.rempawl.respolhpl.network.NetworkListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MainActivityViewModel @Inject constructor(networkListener: NetworkListener) : ViewModel(),
    NetworkListener by networkListener