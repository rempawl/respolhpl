package com.rempawl.respolhpl.data.model.remote

import com.rempawl.respolhpl.data.model.domain.details.ProductAttribute
import kotlinx.serialization.Serializable

@Serializable
data class RemoteAttribute(val name: String, val option: String)

fun RemoteAttribute.toDomain() = ProductAttribute(name = name, value = option)
