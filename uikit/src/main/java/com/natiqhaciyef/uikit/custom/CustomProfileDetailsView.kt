package com.natiqhaciyef.uikit.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.natiqhaciyef.common.constants.FORMATTED_NUMBER_ONE
import com.natiqhaciyef.common.constants.HUNDRED
import com.natiqhaciyef.common.helpers.loadImage
import com.natiqhaciyef.common.model.StorageSize
import com.natiqhaciyef.common.model.mapped.MappedUserWithoutPasswordModel
import com.natiqhaciyef.common.model.ui.SubscriptionType
import com.natiqhaciyef.uikit.databinding.CustomAccountDetailsViewBinding

class CustomProfileDetailsView(
    private val context: Context,
    attribute: AttributeSet
) : ConstraintLayout(context, attribute) {
    private var binding: CustomAccountDetailsViewBinding? = null

    init {
        binding = CustomAccountDetailsViewBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun initAccountDetails(
        user: MappedUserWithoutPasswordModel,
        subscriptionType: SubscriptionType,
        filled: Double,
        total: Int,
        type: StorageSize
    ) {
        val customSize = FORMATTED_NUMBER_ONE.format(filled)
        val totalSize = total.toString()
        binding?.apply {
            accountFullName.text = user.name
            accountUserImage.loadImage(user.imageUrl)
            accountSubscriptionType.text = subscriptionType.title
            storageSizeRatio.text = context.getString(
                com.natiqhaciyef.common.R.string.captured_files_size_ratio,
                type.name,
                customSize,
                totalSize
            )
            loadLevelOfFilesPerUser.progress = (filled.toInt() / total) * HUNDRED
        }
    }
}