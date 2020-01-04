package uk.co.barbuzz.zigzagrecyclerview.obliquecustomview

import android.annotation.TargetApi
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ViewCompat

/**
 * Created by akshay on 21/3/17.
 */
class ObliqueView : AppCompatImageView {
    //Variables
    private lateinit var shadowpath: Path
    private lateinit var path: Path
    private var rect: Rect? = null
    private var width = 0f
    private var height = 0f
    private var config: Config? = null
    private var bitmap: Bitmap? = null
    private var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var bitmapShader: BitmapShader? = null
    private var pdMode: PorterDuffXfermode? = null

    //Constructors
    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    //Initialisation method
    private fun init(context: Context, attrs: AttributeSet?) {
        config = Config(context, attrs)
        pdMode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)
    }

    //Getter and Setter methods
    var angle: GradientAngle?
        get() = config!!.angle
        set(angle) {
            config!!.setAngle(angle!!)
            invalidate()
        }

    fun setShadow(elevation: Float) {
        config!!.setElevation(elevation)
        invalidate()
    }

    var startColor: Int
        get() = config!!.startColor
        set(startColor) {
            config!!.setStartColor(startColor)
            invalidate()
        }

    var endColor: Int
        get() = config!!.endColor
        set(endColor) {
            config!!.setEndColor(endColor)
            invalidate()
        }

    var startAngle: Float
        get() = config!!.startAngle
        set(startAngle) {
            config!!.setStartAngle(startAngle)
            invalidate()
        }

    var endAngle: Float
        get() = config!!.endAngle
        set(endAngle) {
            config!!.setEndAngle(endAngle)
            invalidate()
        }

    var cornerRadius: Float
        get() = config!!.radius
        set(radius) {
            config!!.setRadius(if (radius <= 60) radius else 60f)
            invalidate()
        }

    var baseColor: Int
        get() = config!!.baseColor
        set(baseColor) {
            config!!.setBaseColor(baseColor)
            invalidate()
        }

    val type: Int
        get() = config!!.baseColor

    fun setType(type: Type?) {
        config!!.setType(type)
        invalidate()
    }

    //Private functionality methods
    private fun setupBitmap(imageView: ImageView, width: Float, height: Float) {
        val drawable = imageView.drawable ?: return
        try {
            bitmap = if (drawable is BitmapDrawable) drawable.bitmap else Bitmap.createBitmap(drawable.intrinsicWidth,
                    drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (bitmap == null) {
            imageView.invalidate()
            return
        }
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        bitmapShader = BitmapShader(bitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.shader = bitmapShader
        if (imageView.scaleType != ScaleType.CENTER_CROP && imageView.scaleType != ScaleType.FIT_XY) {
            imageView.scaleType = ScaleType.CENTER_CROP
        }
        bitmapShader!!.setLocalMatrix(setUpScaleType(bitmap, imageView, width, height))
        imageView.invalidate()
    }

    private fun setUpScaleType(bitmap: Bitmap?, iv: ImageView, width: Float, height: Float): Matrix? {
        var scaleX = 1f
        var scaleY = 1f
        var dx = 0f
        var dy = 0f
        val shaderMatrix = Matrix()
        if (bitmap == null) {
            return null
        }
        shaderMatrix.set(null)
        if (iv.scaleType == ScaleType.CENTER_CROP) {
            if (width != bitmap.width.toFloat()) {
                scaleX = width / bitmap.width
            }
            if (scaleX * bitmap.height < height) {
                scaleX = height / bitmap.height
            }
            dy = (height - bitmap.height * scaleX) * 0.5f
            dx = (width - bitmap.width * scaleX) * 0.5f
            shaderMatrix.setScale(scaleX, scaleX)
        } else {
            scaleX = width / bitmap.width
            scaleY = height / bitmap.height
            dy = (height - bitmap.height * scaleY) * 0.5f
            dx = (width - bitmap.width * scaleX) * 0.5f
            shaderMatrix.setScale(scaleX, scaleY)
        }
        shaderMatrix.postTranslate(dx + 0.5f, dy + 0.5f)
        return shaderMatrix
    }

    //Overriden Methods
    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        setupBitmap(this, width, height)
    }

    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        setupBitmap(this, width, height)
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        setupBitmap(this, width, height)
    }

    override fun setImageBitmap(bm: Bitmap) {
        super.setImageBitmap(bm)
        setupBitmap(this, width, height)
    }

    override fun setScaleType(scaleType: ScaleType) {
        if (scaleType == ScaleType.CENTER_CROP || scaleType == ScaleType.FIT_XY) super.setScaleType(scaleType) else throw IllegalArgumentException(String.format("ScaleType %s not supported.", scaleType))
    }

    override fun setAdjustViewBounds(adjustViewBounds: Boolean) {
        require(!adjustViewBounds) { "adjustViewBounds not supported." }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        width = measuredWidth.toFloat()
        height = measuredHeight.toFloat()
        path = config!!.getPath(height, width)
        invalidate()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun getOutlineProvider(): ViewOutlineProvider {
        shadowpath = Path()
        if (config!!.radius == 0f) {
            shadowpath = path
        } else {
            rect = Rect(0, 0, width.toInt(), height.toInt())
            val r = RectF(rect)
            shadowpath.addRoundRect(r, config!!.radius, config!!.radius, Path.Direction.CCW)
            shadowpath.op(path, shadowpath, Path.Op.INTERSECT)
        }
        return object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                if (path.isConvex) {
                    outline.setConvexPath(shadowpath)
                }
            }
        }
    }

    override fun onDraw(canvas: Canvas) { //  super.onDraw(canvas);
        paint.style = Paint.Style.FILL
        when (config!!.type) {
            0 -> paint.color = config!!.baseColor
            1 -> paint.shader = config!!.getLinearGradient(config!!.angle, width, height)
            2 -> paint.shader = config!!.getRadialGradient(width, height)
            3 -> setupBitmap(this, width, height)
        }
        paint.strokeJoin = Paint.Join.ROUND // set the join to round you want
        paint.strokeCap = Paint.Cap.ROUND // set the paint cap to round too
        paint.pathEffect = CornerPathEffect(config!!.radius)
        ViewCompat.setElevation(this, config!!.shadow)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && ViewCompat.getElevation(this) > 0f) {
            try {
                outlineProvider = outlineProvider
            } catch (e: Exception) {
                Log.e("Exception", e.message)
                e.printStackTrace()
            }
        }
        paint.xfermode = pdMode
        canvas.drawPath(path, paint)
    }
}