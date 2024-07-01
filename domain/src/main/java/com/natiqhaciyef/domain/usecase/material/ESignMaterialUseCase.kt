package com.natiqhaciyef.domain.usecase.material

import android.graphics.Bitmap
import com.natiqhaciyef.common.constants.ONE
import com.natiqhaciyef.common.helpers.toResponseString
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.common.constants.SOMETHING_WENT_WRONG
import com.natiqhaciyef.common.constants.TWO_HUNDRED
import com.natiqhaciyef.common.constants.TWO_HUNDRED_NINETY_NINE
import com.natiqhaciyef.common.constants.UNKNOWN_ERROR
import com.natiqhaciyef.common.constants.ZERO
import com.natiqhaciyef.core.base.usecase.BaseUseCase
import com.natiqhaciyef.core.base.usecase.UseCase
import com.natiqhaciyef.domain.mapper.toMapped
import com.natiqhaciyef.domain.mapper.toMaterialResponse
import com.natiqhaciyef.domain.network.NetworkResult
import com.natiqhaciyef.domain.network.request.ESignRequest
import com.natiqhaciyef.domain.repository.MaterialRepository
import com.natiqhaciyef.domain.usecase.CURRENT_PAGE_NUMBER
import com.natiqhaciyef.domain.usecase.MATERIAL_E_SIGN
import com.natiqhaciyef.domain.usecase.MATERIAL_E_SIGN_BITMAP
import com.natiqhaciyef.domain.usecase.MATERIAL_MODEL
import com.natiqhaciyef.domain.usecase.POSITIONS_LIST
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@UseCase
class ESignMaterialUseCase @Inject constructor(
    materialRepository: MaterialRepository
) : BaseUseCase<MaterialRepository, Map<String, Any>, MappedMaterialModel>(materialRepository) {

    override fun operate(data: Map<String, Any>): Flow<Resource<MappedMaterialModel>> = flow {
        emit(Resource.loading(null))

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

        when (val result = repository.eSignMaterial(eSignRequest)) {
            is NetworkResult.Success -> {
                val mapped = result.data.toMapped()
                if (mapped != null && result.data.result?.resultCode in TWO_HUNDRED..TWO_HUNDRED_NINETY_NINE) {
                    emit(Resource.success(mapped))
                } else {
                    emit(
                        Resource.error(
                            data = mapped,
                            msg = "${mapped?.result?.resultCode}: ${mapped?.result?.message}",
                            exception = Exception(mapped?.result?.message)
                        )
                    )
                }
            }

            is NetworkResult.Error -> {
                emit(
                    Resource.error(
                        msg = result.message ?: UNKNOWN_ERROR,
                        data = null,
                        exception = Exception(result.message),
                        errorCode = result.code
                    )
                )
            }

            is NetworkResult.Exception -> {
                emit(
                    Resource.error(
                        msg = result.e.message ?: SOMETHING_WENT_WRONG,
                        data = null,
                        exception = Exception(result.e),
                        errorCode = -ONE
                    )
                )
            }

            else -> {}
        }
    }
}