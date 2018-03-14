package uk.co.barbuzz.zigzagrecyclerview

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.squareup.picasso.Picasso
import java.util.*

class ZigzagGridRecyclerViewAdapter(private val context: Context, zigzagImageList: List<ZigzagImage>,
                                    var zigzagListOnClickListener: ZigzagListOnClickListener?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var zigzagImageList: List<ZigzagImage> = ArrayList()
    private var backgroundColourResId: Int = 0
    private var placeholderDrawableResId: Int = 0

    var data: List<ZigzagImage>
        get() = zigzagImageList
        set(zigzagImageList) {
            this.zigzagImageList = zigzagImageList
            notifyDataSetChanged()
        }

    interface ZigzagListOnClickListener {
        fun onZigzagImageClicked(position: Int, zigzagImage: ZigzagImage?)
    }

    init {
        this.zigzagImageList = zigzagImageList
        backgroundColourResId = context.resources.getColor(R.color.oblique_background)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 0) IMAGE_VIEW_EVEN_TYPE else IMAGE_VIEW_ODD_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        if (viewType == IMAGE_VIEW_ODD_TYPE) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.element_oblique_views, parent, false)
        } else {
            view = LayoutInflater.from(parent.context).inflate(R.layout.element_oblique_views_2, parent, false)
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
        val imageUrl1 = zigzagImage1.zigzagImageUrl
        if (TextUtils.isEmpty(imageUrl1)) {
            val imageResource1 = zigzagImage1.zigzagImageResourceId
            loadImageResource(imageResource1, imageViewHolder.obliqueView1)
        } else {
            loadImageResourceUrl(imageUrl1, imageViewHolder.obliqueView1)
        }
        imageViewHolder.obliqueView1.setOnClickListener { zigzagListOnClickListener!!.onZigzagImageClicked(position, zigzagImage1) }

        //load second image if need as we might have odd number in list
        val secondImagePos = pos1 + 1
        if (secondImagePos < zigzagImageList.size) {
            val zigzagImage2 = zigzagImageList[secondImagePos]
            val imageUrl2 = zigzagImage2.zigzagImageUrl
            if (TextUtils.isEmpty(imageUrl2)) {
                val imageResource2 = zigzagImage2.zigzagImageResourceId
                loadImageResource(imageResource2, imageViewHolder.obliqueView2)
            } else {
                loadImageResourceUrl(imageUrl2, imageViewHolder.obliqueView2)
            }
            imageViewHolder.obliqueView2.setOnClickListener { zigzagListOnClickListener!!.onZigzagImageClicked(position, zigzagImage2) }
        } else {
            imageViewHolder.obliqueView2.visibility = View.INVISIBLE
        }
    }

    override fun getItemCount(): Int {
        val size = zigzagImageList.size.toFloat()
        val count = Math.round(size / 2)
        return count
    }

    private fun loadImageResource(imageResource: Int, obliqueView: ImageView) {
        if (placeholderDrawableResId == 0) {
            Picasso.with(context).load(imageResource)
                    .into(obliqueView)
        } else {
            Picasso.with(context).load(imageResource)
                    .placeholder(placeholderDrawableResId)
                    .into(obliqueView)
        }
    }

    private fun loadImageResourceUrl(imageUrlString: String, obliqueView: ImageView) {
        if (placeholderDrawableResId == 0) {
            Picasso.with(context).load(imageUrlString)
                    .into(obliqueView)
        } else {
            Picasso.with(context).load(imageUrlString)
                    .placeholder(placeholderDrawableResId)
                    .into(obliqueView)
        }
    }

    fun setBackgroundColourResId(backgroundColourResId: Int) {
        this.backgroundColourResId = backgroundColourResId
    }

    fun setPlaceholderDrawableResId(placeholderDrawableResId: Int) {
        this.placeholderDrawableResId = placeholderDrawableResId
    }

    inner class ImageViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        internal var obliqueViewParentLayout: LinearLayout
        internal var obliqueView1: ImageView
        internal var obliqueView2: ImageView

        init {

            obliqueViewParentLayout = v.findViewById(R.id.obliqueViewLayout)
            obliqueView1 = v.findViewById(R.id.obliqueView)
            obliqueView2 = v.findViewById(R.id.obliqueView2)
        }
    }

    companion object {

        private val IMAGE_VIEW_EVEN_TYPE = 0
        private val IMAGE_VIEW_ODD_TYPE = 1
    }

}

