package com.natiqhaciyef.domain.mapper

import com.natiqhaciyef.common.constants.FORMATTED_NUMBER_TWO
import com.natiqhaciyef.common.model.GraphCategory
import com.natiqhaciyef.common.model.MappedGraphDetailModel
import com.natiqhaciyef.common.model.ui.Color
import com.natiqhaciyef.domain.network.response.GraphDetailModel
import com.natiqhaciyef.domain.network.response.GraphDetailsListResponse


fun GraphDetailsListResponse.toMappedList(): List<MappedGraphDetailModel>{
    return this.details.map { it.toMapped() }
}

fun GraphDetailModel.toMapped(): MappedGraphDetailModel{
    return MappedGraphDetailModel(
        title = this.title,
        icon = GraphCategory.stringToIcon(this.type),
        percentage = FORMATTED_NUMBER_TWO.format(this.percentage),
        backgroundColor = Color.stringToColorRes(this.backgroundColor)
    )
}

fun MappedGraphDetailModel.toMapped(): GraphDetailModel {
    return GraphDetailModel(
        title = this.title,
        type = GraphCategory.iconToString(this.icon),
        percentage = this.percentage.toDouble(),
        backgroundColor = Color.colorGradientToString(this.backgroundColor)
    )
}