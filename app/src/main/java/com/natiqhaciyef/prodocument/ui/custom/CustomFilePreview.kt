package com.natiqhaciyef.prodocument.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import coil.load
import com.natiqhaciyef.common.R
import com.natiqhaciyef.common.model.mapped.MappedMaterialModel
import com.natiqhaciyef.prodocument.databinding.CustomFilesPreviewBinding

class CustomFilePreview(
    private val context: Context,
    private val attrs: AttributeSet
) : ConstraintLayout(context, attrs) {
    private var binding: CustomFilesPreviewBinding? = null
    private var material: MappedMaterialModel? = null

    init {
        bind()
    }

    fun bind() {
        binding = CustomFilesPreviewBinding.inflate(LayoutInflater.from(context), this, true)
        customLayoutTags()
    }

    fun initFilePreview(file: MappedMaterialModel) {
        material = file
        binding?.apply {
            filePreviewImage.load(file.image)
            fileTitleText.text = file.title
            fileDateText.text = file.createdDate
        }
    }

    private fun customLayoutTags() {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.com_natiqhaciyef_prodocument_ui_custom_CustomFilePreview
        )
        val customOptionVisibility =
            typedArray.getString(R.styleable.com_natiqhaciyef_prodocument_ui_custom_CustomFilePreview_optionIconVisibility)
        val customShareVisibility =
            typedArray.getString(R.styleable.com_natiqhaciyef_prodocument_ui_custom_CustomFilePreview_shareIconVisibility)
        val customRemoveVisibility =
            typedArray.getString(R.styleable.com_natiqhaciyef_prodocument_ui_custom_CustomFilePreview_removeIconVisibility)

        val optionVisibility = if (customOptionVisibility == "true") View.VISIBLE else View.GONE
        val shareVisibility = if (customShareVisibility == "true") View.VISIBLE else View.GONE
        val removeVisibility = if (customRemoveVisibility == "true") View.VISIBLE else View.GONE

        changeOptionIconVisibility(optionVisibility)
        changeShareIconVisibility(shareVisibility)
        changeRemoveIconVisibility(removeVisibility)
    }

    fun setCustomTitle(title: String) {
        binding?.fileTitleText?.text = title
    }

    fun setCustomDate(date: String) {
        binding?.fileDateText?.text = date
    }

    fun setImageIcon(icon: String) {
        binding?.filePreviewImage?.load(icon)
    }

    fun changeShareIconVisibility(visibility: Int) {
        binding?.apply {
            fileShareIcon.visibility = visibility
            val params = fileTitleText.layoutParams as LayoutParams

            if (visibility == View.GONE) {
                if (fileOptionMenuIcon.visibility == View.VISIBLE) {
                    params.endToStart = fileOptionMenuIcon.id
                } else {
                    params.endToEnd = filePreviewConstraintLayout.id
                }
            } else {
                val paramsIcon = fileShareIcon.layoutParams as LayoutParams
                params.endToStart = fileShareIcon.id

                if (fileOptionMenuIcon.visibility == View.VISIBLE) {
                    paramsIcon.endToStart = fileOptionMenuIcon.id
                } else {
                    paramsIcon.endToEnd = filePreviewConstraintLayout.id
                }
            }
        }
    }

    fun changeOptionIconVisibility(visibility: Int) {
        binding?.apply {
            fileOptionMenuIcon.visibility = visibility

            if (visibility == View.GONE) {
                if (fileShareIcon.visibility == View.VISIBLE) {
                    val params = fileShareIcon.layoutParams as LayoutParams
                    params.endToEnd = filePreviewConstraintLayout.id
                    val marginParams = fileShareIcon.layoutParams as MarginLayoutParams
                    marginParams.setMargins(0, 0, 16, 0)
                } else {
                    val params = fileTitleText.layoutParams as LayoutParams
                    params.endToEnd = filePreviewConstraintLayout.id
                }
            } else {
                if (fileShareIcon.visibility == View.VISIBLE) {
                    val params = fileShareIcon.layoutParams as LayoutParams
                    params.endToStart = fileOptionMenuIcon.id

                    val marginParams = fileShareIcon.layoutParams as MarginLayoutParams
                    marginParams.setMargins(0, 0, 16, 0)
                } else {
                    val params = fileTitleText.layoutParams as LayoutParams
                    params.endToStart = fileOptionMenuIcon.id
                }
            }
        }
    }

    fun changeRemoveIconVisibility(visibility: Int) {
        binding?.apply {
            fileRemoveIcon.visibility = visibility

            if (visibility == View.GONE) {
                val paramsTitle = fileTitleText.layoutParams as LayoutParams
                paramsTitle.endToStart = fileRemoveIcon.id
            }
        }
    }


    fun changeBothIconsVisibility(visibility: Int) {
        binding?.apply {
            fileOptionMenuIcon.visibility = visibility
            fileShareIcon.visibility = visibility

            val params = fileTitleText.layoutParams as LayoutParams
            if (visibility == View.VISIBLE) {
                val shareIconParams = fileShareIcon.layoutParams as LayoutParams
                val optionsIconParams = fileOptionMenuIcon.layoutParams as LayoutParams

                params.endToStart = fileShareIcon.id
                shareIconParams.endToStart = fileOptionMenuIcon.id
                optionsIconParams.endToEnd = filePreviewConstraintLayout.id
            } else {
                params.endToEnd = filePreviewConstraintLayout.id
            }
        }
    }

    fun addActionToShare(onClick: (MappedMaterialModel?) -> Unit) {
        binding?.fileShareIcon?.setOnClickListener { onClick.invoke(material) }
    }

    fun addActionToOptions(onClick: (MappedMaterialModel?) -> Unit) {
        binding?.fileOptionMenuIcon?.setOnClickListener { onClick.invoke(material) }
    }

    fun addActionToRemove(onClick: (MappedMaterialModel?) -> Unit) {
        binding?.fileOptionMenuIcon?.setOnClickListener { onClick.invoke(material) }
    }

    fun itemOnClick(onClickEvent: (MappedMaterialModel?) -> Unit) {
        binding?.filePreviewConstraintLayout
            ?.setOnClickListener { onClickEvent.invoke(material) }
    }
}