package com.natiqhaciyef.prodocument.ui.view.main.files.event

sealed class FileEvent {

    class GetFileById(val id: String, val token: String): FileEvent()

    class GetAllMaterials(val token: String): FileEvent()

}