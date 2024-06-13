package com.natiqhaciyef.prodocument.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.natiqhaciyef.common.helpers.loadImage
import com.natiqhaciyef.common.model.StorageSize
import com.natiqhaciyef.common.model.ui.SubscriptionType
import com.natiqhaciyef.prodocument.databinding.CustomAccountDetailsViewBinding

class CustomProfileDetailsView(
    private val context: Context,
    attribute: AttributeSet
) : ConstraintLayout(context, attribute) {
    private var binding: CustomAccountDetailsViewBinding? = null

    init {
        binding = CustomAccountDetailsViewBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun initAccountDetails(
        fullName: String,
        image: String,
        subscriptionType: SubscriptionType,
        filled: Double,
        total: Int,
        type: StorageSize
    ) {
        val customSize = "%.1f".format(filled)
        val totalSize = total.toString()
        binding?.let {
            it.accountFullName.text = fullName
            it.accountUserImage.loadImage(image)
            it.accountSubscriptionType.text = subscriptionType.title
            it.storageSizeRatio.text = context.getString(
                com.natiqhaciyef.common.R.string.captured_files_size_ratio,
                type.name,
                customSize,
                totalSize
            )
            it.loadLevelOfFilesPerUser.progress = (filled.toInt() / total) * 100
        }
    }


}