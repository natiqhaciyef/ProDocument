package com.natiqhaciyef.domain.base.mock

import retrofit2.adapter.rxjava2.Result

class BaseMockGenerator<In, Out>(
    var createMock: Out,
    var takenRequest: In,
    var setSuccessKey: String,
    var setErrorKey: String
){

//    fun generateMock(defaultData: String, request: In): Result<Out>? = when (defaultData) {
//        setSuccessKey -> {
//            if (takenRequest == request){
//
//            }else{
//                Result(null)
//            }
//
//        }
//        setErrorKey -> { }
//        else -> null
//    }
}