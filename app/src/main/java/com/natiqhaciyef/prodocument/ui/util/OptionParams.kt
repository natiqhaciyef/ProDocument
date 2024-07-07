package com.natiqhaciyef.prodocument.ui.util

import android.content.Context
import com.natiqhaciyef.common.R
import com.natiqhaciyef.common.constants.EMPTY_STRING
import com.natiqhaciyef.common.model.FieldType
import com.natiqhaciyef.common.model.ParamsUIModel

fun getOptions(ctx: Context) = mutableListOf(
    ParamsUIModel(
        ctx.getString(R.string.save_to_device),
        FieldType.NAVIGATION,
        true,
        R.drawable.dowload_bs_icon
    ),
    ParamsUIModel(
        ctx.getString(R.string.export_to),
        FieldType.NAVIGATION,
        true,
        R.drawable.export_bs_to
    ),
    ParamsUIModel(EMPTY_STRING, FieldType.LINE),
    ParamsUIModel(
        ctx.getString(R.string.add_watermark),
        FieldType.SPACE,
        true,
        R.drawable.watermark_bs_icon
    ),
    ParamsUIModel(
        ctx.getString(R.string.add_digital_signature),
        FieldType.SPACE,
        true,
        R.drawable.esign_bs_icon
    ),
    ParamsUIModel(
        ctx.getString(R.string.split_pdf),
        FieldType.SPACE,
        true,
        R.drawable.split_bs_icon
    ),
    ParamsUIModel(
        ctx.getString(R.string.merge_pdf),
        FieldType.SPACE,
        true,
        R.drawable.merge_bs_icon
    ),
    ParamsUIModel(
        ctx.getString(R.string.protect_pdf),
        FieldType.SPACE,
        true,
        R.drawable.password_bs_icon
    ),
    ParamsUIModel(
        ctx.getString(R.string.compress_pdf),
        FieldType.SPACE,
        true,
        R.drawable.compress_bs_icon
    ),
    ParamsUIModel(EMPTY_STRING, FieldType.LINE),
    ParamsUIModel(ctx.getString(R.string.rename), FieldType.SPACE, true, R.drawable.edit_bs_icon),
    ParamsUIModel(
        ctx.getString(R.string.move_to_folder),
        FieldType.SPACE,
        true,
        R.drawable.folder_bs_icon
    ),
    ParamsUIModel(ctx.getString(R.string.print), FieldType.SPACE, true, R.drawable.print_bs_icon),
    ParamsUIModel(ctx.getString(R.string.delete), FieldType.SPACE, true, R.drawable.delete_bs_icon),
)

