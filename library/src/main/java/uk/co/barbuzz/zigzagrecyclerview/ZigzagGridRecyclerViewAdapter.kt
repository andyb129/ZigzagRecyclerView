package uk.co.barbuzz.zigzagrecyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import java.util.*

class ZigzagGridRecyclerViewAdapter(context: Context, zigzagImageList: List<ZigzagImage>,
                                    zigzagListOnClickListener: ZigzagListOnClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var zigzagImageList: List<ZigzagImage> = ArrayList()
    var zigzagListOnClickListener: ZigzagListOnClickListener
    private var backgroundColourResId: Int
    private var placeholderDrawableResId = 0

    interface ZigzagListOnClickListener {
        fun onZigzagImageClicked(position: Int, zigzagImage: ZigzagImage?)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 0) IMAGE_VIEW_EVEN_TYPE else IMAGE_VIEW_ODD_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        view = if (viewType == IMAGE_VIEW_ODD_TYPE) {
            LayoutInflater.from(parent.context).inflate(R.layout.element_oblique_views, parent, false)
        } else {
            LayoutInflater.from(parent.context).inflate(R.layout.element_oblique_views_2, parent, false)
        }
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val imageViewHolder = viewHolder as ImageViewHolder
        if (backgroundColourResId != 0) {
            imageViewHolder.obliqueViewParentLayout.setBackgroundColor(backgroundColourResId)
        }
        //load first image
        val pos1 = position + position
        val zigzagImage1 = zigzagImageList[pos1]
        val imageUrl1 = zigzagImage1?.zigzagImageUrl
        if (imageUrl1 == null || imageUrl1.trim { it <= ' ' }.equals("", ignoreCase = true)) {
            val imageResource1 = zigzagImage1.zigzagImageResourceId
            loadImageResource(imageResource1, imageViewHolder.obliqueView1)
        } else {
            loadImageResourceUrl(imageUrl1, imageViewHolder.obliqueView1)
        }
        imageViewHolder.obliqueView1.setOnClickListener { zigzagListOnClickListener.onZigzagImageClicked(position, zigzagImage1) }
        //load second image if need as we might have odd number in list
        val secondImagePos = pos1 + 1
        if (secondImagePos < zigzagImageList.size) {
            val zigzagImage2 = zigzagImageList[secondImagePos]
            if (zigzagImage2 != null) {
                val imageUrl2 = zigzagImage2.zigzagImageUrl
                if (imageUrl1 == null || imageUrl1.trim { it <= ' ' }.equals("", ignoreCase = true)) {
                    val imageResource2 = zigzagImage2.zigzagImageResourceId
                    loadImageResource(imageResource2, imageViewHolder.obliqueView2)
                } else {
                    loadImageResourceUrl(imageUrl2, imageViewHolder.obliqueView2)
                }
                imageViewHolder.obliqueView2.setOnClickListener { zigzagListOnClickListener.onZigzagImageClicked(position, zigzagImage2) }
            }
        } else {
            imageViewHolder.obliqueView2.visibility = View.INVISIBLE
        }
    }

    override fun getItemCount(): Int {
        val size = zigzagImageList.size.toFloat()
        return Math.round(size / 2)
    }

    private fun loadImageResource(imageResource: Int, obliqueView: ImageView) {
        if (placeholderDrawableResId == 0) {
            Picasso.get().load(imageResource)
                    .into(obliqueView)
        } else {
            Picasso.get().load(imageResource)
                    .placeholder(placeholderDrawableResId)
                    .into(obliqueView)
        }
    }

    private fun loadImageResourceUrl(imageUrlString: String?, obliqueView: ImageView) {
        if (placeholderDrawableResId == 0) {
            Picasso.get().load(imageUrlString)
                    .into(obliqueView)
        } else {
            Picasso.get().load(imageUrlString)
                    .placeholder(placeholderDrawableResId)
                    .into(obliqueView)
        }
    }

    var data: List<ZigzagImage>
        get() = zigzagImageList
        set(zigzagImageList) {
            this.zigzagImageList = zigzagImageList
            notifyDataSetChanged()
        }

    fun setBackgroundColourResId(backgroundColourResId: Int) {
        this.backgroundColourResId = backgroundColourResId
    }

    fun setPlaceholderDrawableResId(placeholderDrawableResId: Int) {
        this.placeholderDrawableResId = placeholderDrawableResId
    }

    inner class ImageViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var obliqueViewParentLayout: LinearLayout
        var obliqueView1: ImageView
        var obliqueView2: ImageView

        init {
            obliqueViewParentLayout = v.findViewById(R.id.obliqueViewLayout)
            obliqueView1 = v.findViewById(R.id.obliqueView)
            obliqueView2 = v.findViewById(R.id.obliqueView2)
        }
    }

    companion object {
        private const val IMAGE_VIEW_EVEN_TYPE = 0
        private const val IMAGE_VIEW_ODD_TYPE = 1
    }

    init {
        this.zigzagImageList = zigzagImageList
        this.zigzagListOnClickListener = zigzagListOnClickListener
        backgroundColourResId = context.resources.getColor(R.color.oblique_background)
    }
}