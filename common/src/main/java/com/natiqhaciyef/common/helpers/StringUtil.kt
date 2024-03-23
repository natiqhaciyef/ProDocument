package com.natiqhaciyef.common.helpers

import com.google.gson.Gson
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel


fun String.toMappedMaterial(): MappedMaterialModel {
    return Gson().fromJson(this, MappedMaterialModel::class.java)
}