package com.natiqhaciyef.domain.usecase.qrCode

import com.natiqhaciyef.common.mapper.toMapped
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.model.mapped.MappedQrCodeResultModel
import com.natiqhaciyef.common.objects.ErrorMessages
import com.natiqhaciyef.common.objects.ResultExceptions
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

        result.onSuccess { value ->
            val mapped = value.toMapped()
            if (mapped.result!!.resultCode in 200..299) {
                emit(Resource.success(data = mapped))
            } else {
                emit(
                    Resource.error(
                        msg = "${value.result?.resultCode}: ${value.result?.message}",
                        data = value.toMapped(),
                        exception = Exception(value.result?.message)
                    )
                )
            }
        }.onFailure { exception ->
            emit(Resource.error(
                msg = exception.message ?: ErrorMessages.UNKNOWN_ERROR,
                data = null,
                exception = ResultExceptions.CustomIOException(
                    msg = exception.message ?: ErrorMessages.UNKNOWN_ERROR,
                    errorCode = 500
                )
            ))
        }
    }

}