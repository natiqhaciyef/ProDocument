package com.natiqhaciyef.domain.usecase.qrCode

import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.model.mapped.MappedQrCodeResultModel
import com.natiqhaciyef.common.objects.ErrorMessages
import com.natiqhaciyef.data.mapper.toMapped
import com.natiqhaciyef.data.network.NetworkResult
import com.natiqhaciyef.domain.base.usecase.BaseUseCase
import com.natiqhaciyef.domain.base.usecase.UseCase
import com.natiqhaciyef.domain.repository.QrCodeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@UseCase
class ReadQrCodeResultUseCase @Inject constructor(
    qrCodeRepository: QrCodeRepository
) : BaseUseCase<QrCodeRepository, String, MappedQrCodeResultModel>(qrCodeRepository) {

    override fun operate(data: String): Flow<Resource<MappedQrCodeResultModel>> = flow {
        emit(Resource.loading(null))

        val result = repository.readQrCodeResult(data)
        when (result) {
            is NetworkResult.Success -> {
                val model = result.data.toMapped()

                if (model.result?.resultCode in 200..299)
                    emit(Resource.success(data = model))
                else
                    emit(
                        Resource.error(
                            data = model,
                            msg = "${model.result?.resultCode}: ${model.result?.message}",
                            exception = Exception(model.result?.message)
                        )
                    )
            }

            is NetworkResult.Error -> {
                emit(
                    Resource.error(
                        msg = result.message ?: ErrorMessages.UNKNOWN_ERROR,
                        data = null,
                        exception = Exception(result.message),
                        errorCode = result.code
                    )
                )
            }

            is NetworkResult.Exception -> {
                emit(Resource.error(
                    msg = result.e.message ?: ErrorMessages.SOMETHING_WENT_WRONG,
                    data = null,
                    exception = Exception(result.e),
                    errorCode = -1
                ))
            }
        }
    }

}