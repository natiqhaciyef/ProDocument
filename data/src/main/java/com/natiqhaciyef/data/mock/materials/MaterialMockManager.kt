package com.natiqhaciyef.data.mock.materials

import com.natiqhaciyef.common.constants.EMPTY_STRING
import com.natiqhaciyef.common.constants.FIVE_HUNDRED
import com.natiqhaciyef.common.constants.ONE
import com.natiqhaciyef.common.constants.THREE
import com.natiqhaciyef.common.constants.TWO
import com.natiqhaciyef.common.constants.TWO_HUNDRED_NINETY_NINE
import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.core.model.FileTypes.PDF
import com.natiqhaciyef.core.model.FileTypes.URL
import com.natiqhaciyef.domain.network.request.SplitRequest
import com.natiqhaciyef.domain.network.request.WatermarkRequest
import com.natiqhaciyef.domain.network.response.ListMaterialResponse
import com.natiqhaciyef.domain.network.response.MaterialResponse

object MaterialMockManager {
    private const val CRUD_MOCK = "crud mock"
    private const val MATERIAL_ID = "materialId"
    private const val LIST_MATERIAL_ID = "listMaterialId"
    private const val MODIFIED = "modified"
    private const val DEFAULT_DATE = "01.09.2024 12:45"
    private const val COMPRESS = "compress"
    private const val PROTECT = "protect"
    private const val MERGE = "merge"
    private const val E_SIGN = "e-sign"
    private const val SPLIT = "split"
    private const val WATERMARK = "watermark"

    private val material = MaterialResponse(
        id = MATERIAL_ID,
        publishDate = DEFAULT_DATE,
        image = EMPTY_STRING,
        title = EMPTY_STRING,
        description = EMPTY_STRING,
        type = PDF,
        url = URL,
        result = CRUDResponse(
            resultCode = TWO_HUNDRED_NINETY_NINE,
            message = CRUD_MOCK
        )
    )

    private val materialList = mutableListOf(
        material.copy(id = "$MATERIAL_ID $ONE", title = PROTECT),
        material.copy(id = "$MATERIAL_ID $TWO", title = COMPRESS),
        material.copy(id = "$MATERIAL_ID $THREE", title = E_SIGN),
    )


    private val crudResponse = CRUDResponse(
        resultCode = TWO_HUNDRED_NINETY_NINE,
        message = CRUD_MOCK
    )

    fun getEmptyMaterial() = material

    fun getMaterialById(id: String): MaterialResponse {
        return materialList.first { it.id == id }
    }

    fun getAllMaterials(): ListMaterialResponse {
        return ListMaterialResponse(
            materials = materialList,
            id = LIST_MATERIAL_ID,
            result = CRUDResponse(
                resultCode = TWO_HUNDRED_NINETY_NINE,
                message = CRUD_MOCK
            ),
            publishDate = DEFAULT_DATE
        )
    }

    fun createMaterial(material: MaterialResponse, title: String): CRUDResponse {
        materialList.add(material)
        return getCrudResult(title)
    }

    fun removeMaterial(materialId: String, title: String): CRUDResponse {
        val result = if (materialList.any { it.id == materialId })
            getCrudResult(title)
        else
            getCrudResult(title, FIVE_HUNDRED)

        materialList.removeIf { it.id == materialId }

        return result
    }

    fun updateMaterial(customMaterial: MaterialResponse, title: String): CRUDResponse{
        val result = if (materialList.any { it.id == customMaterial.id })
            getCrudResult(title)
        else
            getCrudResult(title, FIVE_HUNDRED)

        val foundMaterial = materialList.first { it.id == customMaterial.id }
        val index = materialList.indexOf(foundMaterial)
        materialList[index] = customMaterial

        return result
    }


    fun compressMaterial(
        quality: String,
        customMaterial: MaterialResponse,
        isModifiedMock: Boolean = false
    ): MaterialResponse {
        val result = customMaterial.result
        val prevId = customMaterial.id

        return if (isModifiedMock)
            customMaterial.copy(
                id = "$MODIFIED $prevId",
                quality = quality,
                result = result?.copy(message = "$COMPRESS $CRUD_MOCK")
            )
        else
            customMaterial.copy(
                quality = quality,
                result = result?.copy(message = "$COMPRESS $CRUD_MOCK")
            )
    }

    fun protectMaterial(
        protectionKey: String,
        customMaterial: MaterialResponse,
        isModifiedMock: Boolean = false
    ): MaterialResponse {
        val result = customMaterial.result
        val prevId = customMaterial.id

        return if (isModifiedMock)
            customMaterial.copy(
                id = "$MODIFIED $prevId",
                protectionKey = protectionKey,
                result = result?.copy(message = "$PROTECT $CRUD_MOCK")
            )
        else
            customMaterial.copy(
                protectionKey = protectionKey,
                result = result?.copy(message = "$PROTECT $CRUD_MOCK")
            )
    }

    fun mergeMaterial(
        list: List<MaterialResponse>,
        isModifiedMock: Boolean = false
    ): MaterialResponse {
        var idTops = EMPTY_STRING
        val result = material.result
        list.forEach { idTops = " ${it.id}" }

        return if (isModifiedMock)
            material.copy(
                id = "$MODIFIED $idTops",
                result = result?.copy(message = "$MERGE $CRUD_MOCK")
            )
        else
            material.copy(id = idTops, result = result?.copy(message = "$MERGE $CRUD_MOCK"))
    }

    fun eSignMaterial(
        eSign: String,
        customMaterial: MaterialResponse,
        isModifiedMock: Boolean = false
    ): MaterialResponse {
        val result = customMaterial.result
        val prevId = customMaterial.id

        return if (isModifiedMock)
            customMaterial.copy(
                id = "$MODIFIED $prevId",
                result = result?.copy(message = "$E_SIGN $CRUD_MOCK : $eSign")
            )
        else
            customMaterial.copy(result = result?.copy(message = "$E_SIGN $CRUD_MOCK : $eSign"))
    }

    fun splitMaterial(
        split: SplitRequest,
        isModifiedMock: Boolean = false
    ): List<MaterialResponse> {
        val result = split.material.result
        val prevId = split.material.id

        return if (isModifiedMock)
            listOf(
                material.copy(
                    id = "$MODIFIED $prevId $ONE",
                    title = split.firstLine,
                    result = result?.copy(message = "$SPLIT $CRUD_MOCK")
                ),
                material.copy(
                    id = "$MODIFIED $prevId $TWO",
                    title = split.lastLine,
                    result = result?.copy(message = "$SPLIT $CRUD_MOCK")
                )
            )
        else
            listOf(
                material.copy(
                    id = "$MATERIAL_ID $ONE",
                    title = split.firstLine,
                    result = result?.copy(message = "$SPLIT $CRUD_MOCK")
                ),
                material.copy(
                    id = "$MATERIAL_ID $TWO",
                    title = split.lastLine,
                    result = result?.copy(message = "$SPLIT $CRUD_MOCK")
                )
            )
    }

    fun watermarkMaterial(
        watermark: WatermarkRequest,
        isModifiedMock: Boolean = false
    ): MaterialResponse {
        val result = watermark.material.result
        val prevId = watermark.material.id

        return if (isModifiedMock)
            material.copy(
                id = "$MODIFIED $prevId",
                title = "${watermark.title} ${watermark.watermark}",
                result = result?.copy(message = "$WATERMARK $CRUD_MOCK")
            )
        else
            material.copy(
                id = prevId,
                title = "${watermark.title} ${watermark.watermark}",
                result = result?.copy(message = "$WATERMARK $CRUD_MOCK")
            )
    }


    fun getCrudResult(reason: String, code: Int = TWO_HUNDRED_NINETY_NINE): CRUDResponse {
        return crudResponse.copy(message = "$reason $CRUD_MOCK", resultCode = code)
    }
}