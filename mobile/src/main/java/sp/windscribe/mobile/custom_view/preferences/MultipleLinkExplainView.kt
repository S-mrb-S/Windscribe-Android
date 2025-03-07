package sp.windscribe.mobile.custom_view.preferences

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.res.getResourceIdOrThrow
import sp.windscribe.mobile.R
import sp.windscribe.mobile.utils.UiUtil


@SuppressLint("ClickableViewAccessibility")
class MultipleLinkExplainView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val attributes: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.MultipleLinkExplainView)
    private val view: View = View.inflate(context, R.layout.multiple_link_explain_view, this)

    init {
        view.findViewById<TextView>(R.id.title).text =
                attributes.getString(R.styleable.MultipleLinkExplainView_MultiLinkTitle)
        view.findViewById<TextView>(R.id.description).text =
                attributes.getString(R.styleable.MultipleLinkExplainView_MultiLinkDescription)
        val leftIcon =
                attributes.getResourceIdOrThrow(R.styleable.MultipleLinkExplainView_MultiLinkLeftIcon)
        view.findViewById<ImageView>(R.id.left_icon).setImageResource(leftIcon)
        view.findViewById<TextView>(R.id.first_item_title).text =
                attributes.getString(R.styleable.MultipleLinkExplainView_FirstItemTitle)
        view.findViewById<TextView>(R.id.second_item_title).text =
                attributes.getString(R.styleable.MultipleLinkExplainView_SecondItemTitle)
        view.findViewById<ImageView>(R.id.first_item_right_icon).tag =
                R.drawable.ic_forward_arrow_settings
        view.findViewById<ImageView>(R.id.second_item_right_icon).tag =
                R.drawable.ic_forward_arrow_settings
        UiUtil.setupOnTouchListener(
                imageViewContainer = view.findViewById(R.id.first_item_tap_area),
                iconView = view.findViewById(R.id.first_item_right_icon),
                textView = view.findViewById(R.id.first_item_title)
        )
        UiUtil.setupOnTouchListener(
                imageViewContainer = view.findViewById(R.id.second_item_tap_area),
                iconView = view.findViewById(R.id.second_item_right_icon),
                textView = view.findViewById(R.id.second_item_title)
        )
    }

    fun onFirstItemClick(click: OnClickListener) {
        view.findViewById<AppCompatImageView>(R.id.first_item_tap_area).setOnClickListener(click)
    }

    fun onSecondItemClick(click: OnClickListener) {
        view.findViewById<AppCompatImageView>(R.id.second_item_tap_area).setOnClickListener(click)
        view.findViewById<AppCompatImageView>(R.id.clip_corner_background).setOnClickListener(click)
    }
}