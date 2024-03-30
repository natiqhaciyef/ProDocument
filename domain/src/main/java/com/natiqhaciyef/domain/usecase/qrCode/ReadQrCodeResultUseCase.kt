package com.natiqhaciyef.domain.usecase.qrCode

import com.natiqhaciyef.common.mapper.toMapped
import com.natiqhaciyef.common.model.Resource
import com.natiqhaciyef.common.model.mapped.MappedQrCodeResultModel
import com.natiqhaciyef.common.objects.ErrorMessages.SOMETHING_WENT_WRONG
import com.natiqhaciyef.common.objects.ResultExceptions
import com.natiqhaciyef.domain.base.BaseUseCase
import com.natiqhaciyef.domain.base.UseCase
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

        val response = repository.readQrCodeResult(data)

        if (response?.result != null) {
            if (response.result!!.resultCode in 200..299) {
                emit(Resource.success(data = response.toMapped()))
            } else {
                emit(
                    Resource.error(
                        msg = "${response.result?.resultCode}: ${response.result?.message}",
                        data = response.toMapped(),
                        exception = Exception(response.result?.message)
                    )
                )
            }

        } else {
            emit(
                Resource.error(
                    msg = SOMETHING_WENT_WRONG,
                    data = null,
                    exception = ResultExceptions.UnknownError()
                )
            )
        }
    }

}