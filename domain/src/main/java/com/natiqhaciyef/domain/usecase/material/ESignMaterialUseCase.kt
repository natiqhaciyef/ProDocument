package com.natiqhaciyef.domain.usecase.material

import android.graphics.Bitmap
import com.natiqhaciyef.common.constants.ONE
import com.natiqhaciyef.common.helpers.toResponseString
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.common.constants.ZERO
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.domain.mapper.toMapped
import com.natiqhaciyef.domain.mapper.toMaterialResponse
import com.natiqhaciyef.core.base.usecase.handleFlowResult
import com.natiqhaciyef.domain.network.request.ESignRequest
import com.natiqhaciyef.domain.repository.MaterialRepository
import com.natiqhaciyef.domain.usecase.CURRENT_PAGE_NUMBER
import com.natiqhaciyef.domain.usecase.MATERIAL_E_SIGN
import com.natiqhaciyef.domain.usecase.MATERIAL_E_SIGN_BITMAP
import com.natiqhaciyef.domain.usecase.MATERIAL_MODEL
import com.natiqhaciyef.domain.usecase.POSITIONS_LIST
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@UseCase
class ESignMaterialUseCase @Inject constructor(
    materialRepository: MaterialRepository
) : BaseUseCase<MaterialRepository, Map<String, Any>, MappedMaterialModel>(materialRepository) {

    override fun operate(data: Map<String, Any>): Flow<Resource<MappedMaterialModel>> {
        val sign = data[MATERIAL_E_SIGN].toString()
        val signBitmap = data[MATERIAL_E_SIGN_BITMAP] as Bitmap
        val material = (data[MATERIAL_MODEL] as MappedMaterialModel).toMaterialResponse()
        val page = data[CURRENT_PAGE_NUMBER].toString().toInt()
        val positions = data[POSITIONS_LIST] as MutableList<Float>
        val xPosition = positions[ZERO]
        val yPosition = positions[ONE]

        val eSignRequest = ESignRequest(
            sign = sign,
            material = material,
            signBitmap = signBitmap.toResponseString(),
            page = page,
            positionX = xPosition,
            positionY = yPosition
        )

        return handleFlowResult(
            networkResult = { repository.eSignMaterial(eSignRequest) },
            operation = { Resource.success(it.toMapped()) }
        )
    }
}