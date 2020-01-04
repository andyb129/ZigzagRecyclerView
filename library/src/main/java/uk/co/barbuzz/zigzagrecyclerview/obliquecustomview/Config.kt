package uk.co.barbuzz.zigzagrecyclerview.obliquecustomview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import androidx.annotation.FloatRange
import uk.co.barbuzz.zigzagrecyclerview.R

/**
 * Created by akshay on 21/3/17.
 */
class Config(context: Context, attrs: AttributeSet?) {
    var angle = GradientAngle.LEFT_TO_RIGHT
        private set
    var baseColor = Color.TRANSPARENT
        private set
    var startAngle = 0f
        private set
    var endAngle = 0f
        private set
    private var elevation = 0f
    var type = 0
        private set
    var radius = 0f
        private set
    var startColor = 0
        private set
    var endColor = 0
        private set
    val shadow: Float
        get() = if (elevation > 0) elevation + 10 else 0f

    fun setElevation(elevation: Float): Config {
        this.elevation = elevation
        return this
    }

    fun setStartColor(startColor: Int): Config {
        this.startColor = startColor
        return this
    }

    fun setEndColor(endColor: Int): Config {
        this.endColor = endColor
        return this
    }

    fun setAngle(gradientAngle: GradientAngle): Config {
        angle = gradientAngle
        return this
    }

    fun setRadius(@FloatRange(from = 0.0, to = 60.0) radius: Float): Config {
        this.radius = radius
        return this
    }

    fun setBaseColor(baseColor: Int): Config {
        this.baseColor = baseColor
        return this
    }

    fun setStartAngle(@FloatRange(from = 0.0, to = 180.0) startAngle: Float): Config {
        this.startAngle = startAngle
        return this
    }

    fun setEndAngle(@FloatRange(from = 0.0, to = 180.0) endAngle: Float): Config {
        this.endAngle = endAngle
        return this
    }

    private fun getTanWithOutConflict(h: Float, w: Float, angle: Float, hype: Double): Int {
        var angle = angle
        if (angle > 90) {
            angle = 180 - angle
        }
        val `val` = Math.ceil(h / Math.tan(Math.toRadians(angle.toDouble()))).toInt()
        if (`val` <= hype) {
            return `val`
        }
        val p = Math.ceil(w * Math.tan(Math.toRadians(angle.toDouble()))).toInt()
        return getTanWithOutConflict(p.toFloat(), w, angle, hype)
    }

    fun getPath(h: Float, w: Float): Path {
        val hyp = Math.hypot(w.toDouble(), h.toDouble())
        val left_base = getTanWithOutConflict(h, w, startAngle, hyp)
        val right_base = getTanWithOutConflict(h, w, endAngle, hyp)
        var a1 = 0f
        var a2 = 0f
        var b1 = w
        var b2 = 0f
        var c1 = w
        var c2 = h
        var d1 = 0f
        var d2 = h
        try {
            if (startAngle > 90 && startAngle <= 180) {
                if (startAngle > 145) {
                    d1 = w
                    d2 = Math.ceil(w * Math.tan(Math.toRadians(180 - startAngle.toDouble()))).toFloat()
                } else {
                    d1 = Math.abs(left_base).toFloat()
                }
            } else {
                a1 = Math.abs(left_base).toFloat()
                if (a1 == 0f) {
                    a1 = w
                }
                if (startAngle < 45) a2 = h - Math.ceil(w * Math.tan(Math.toRadians(startAngle.toDouble()))).toFloat()
            }
            if (endAngle > 90) {
                if (endAngle > 135) {
                    b1 = 0f
                    b2 = h - Math.ceil(w * Math.tan(Math.toRadians(180 - endAngle.toDouble()))).toFloat()
                } else {
                    b1 = Math.abs(w - right_base) //w - right_base;
                }
            } else if (endAngle <= 180) {
                if (endAngle < 45) {
                    c2 = Math.floor(w * Math.tan(Math.toRadians(endAngle.toDouble()))).toFloat()
                    c1 = 0f
                } else {
                    c1 = w - Math.abs(right_base) //w - right_base;
                }
            }
            /*    Log.e("startAngle " + startAngle + "  right_angle " + right_angle,
                    " | a1 " + a1 + " | a2 " + a2 + " | b1 " + b1 + " | b2 " + b2 + " | c1 " + c1 + " | c2 " + c2 + " | d1 " + d1 + " | d2 " + d2);
        */
        } catch (e: Exception) {
            Log.e("exception", "" + e.message)
        }
        val path = Path()
        path.moveTo(a1, a2)
        path.lineTo(b1, b2)
        path.lineTo(c1, c2)
        path.lineTo(d1, d2)
        path.lineTo(a1, a2)
        path.close()
        return path
    }

    fun getPathShadow(h: Float, w: Float): Path {
        val MARGIN = 10
        val hyp = Math.hypot(w.toDouble(), h.toDouble())
        val left_base = getTanWithOutConflict(h, w, startAngle, hyp)
        val right_base = getTanWithOutConflict(h, w, endAngle, hyp)
        var a1 = 0f
        var a2 = 0f
        var b1 = w
        var b2 = 0f
        var c1 = w
        var c2 = h
        var d1 = 0f
        var d2 = h
        try {
            if (startAngle > 90 && startAngle <= 180) {
                if (startAngle > 145) {
                    d1 = w
                    d2 = Math.ceil(w * Math.tan(Math.toRadians(180 - startAngle.toDouble()))).toFloat() - MARGIN
                } else {
                    d1 = Math.abs(left_base) - MARGIN.toFloat()
                }
            } else {
                a1 = Math.abs(left_base).toFloat()
                if (a1 == 0f) {
                    a1 = w
                }
                if (startAngle < 45) a2 = h - Math.ceil(w * Math.tan(Math.toRadians(startAngle.toDouble()))).toFloat() - MARGIN
            }
            if (endAngle > 90) {
                if (endAngle > 135) {
                    b1 = 0f
                    b2 = h - Math.ceil(w * Math.tan(Math.toRadians(180 - endAngle.toDouble()))).toFloat() - MARGIN
                } else {
                    b1 = Math.abs(w - right_base) - MARGIN //w - right_base;
                }
            } else if (endAngle <= 180) {
                if (endAngle < 45) {
                    c2 = Math.floor(w * Math.tan(Math.toRadians(endAngle.toDouble()))).toFloat()
                    c1 = 0f
                } else {
                    c1 = w - Math.abs(right_base) - MARGIN //w - right_base;
                }
            }
            /*    Log.e("startAngle " + startAngle + "  right_angle " + right_angle,
                    " | a1 " + a1 + " | a2 " + a2 + " | b1 " + b1 + " | b2 " + b2 + " | c1 " + c1 + " | c2 " + c2 + " | d1 " + d1 + " | d2 " + d2);
        */
        } catch (e: Exception) {
            Log.e("exception", "" + e.message)
        }
        val path = Path()
        path.moveTo(a1, a2)
        path.lineTo(b1, b2)
        path.lineTo(c1, c2)
        path.lineTo(d1, d2)
        path.lineTo(a1, a2)
        path.close()
        return path
    }

    fun setType(type: Type?): Config {
        var i = 0
        i = when (type) {
            Type.SOLID_COLOR -> 0
            Type.LINEAR_GRADIENT -> 1
            Type.RADIAL_GRADIENT -> 2
            Type.IMAGE -> 3
            else -> 0
        }
        this.type = i
        return this
    }

    fun getRadialGradient(width: Float, height: Float): Shader {
        val radius = Math.max(width, height) / 2
        return RadialGradient(width / 2, height / 2, radius, startColor, endColor, Shader.TileMode.CLAMP)
    }

    fun getLinearGradient(gradientAngle: GradientAngle?, width: Float, height: Float): Shader {
        var x1 = 0f
        var x2 = 0f
        var y1 = 0f
        var y2 = 0f
        when (gradientAngle) {
            GradientAngle.LEFT_TO_RIGHT -> x2 = width
            GradientAngle.RIGHT_TO_LEFT -> x1 = width
            GradientAngle.TOP_TO_BOTTOM -> y2 = height
            GradientAngle.BOTTOM_TO_TOP -> y1 = height
            GradientAngle.LEFT_TOP_TO_RIGHT_BOTTOM -> {
                x2 = width
                y2 = height
            }
            GradientAngle.RIGHT_BOTTOM_TO_LEFT_TOP -> {
                x1 = width
                y1 = height
            }
            GradientAngle.LEFT_BOTTOM_TO_RIGHT_TOP -> {
                x2 = width
                y1 = height
            }
            GradientAngle.RIGHT_TOP_TO_LEFT_BOTTOM -> {
                x1 = width
                y2 = height
            }
        }
        return LinearGradient(x1, y1, x2, y2, startColor, endColor, Shader.TileMode.CLAMP)
    }

    fun setupAngle(angle: Int) {
        when (angle) {
            0 -> this.angle = GradientAngle.LEFT_TO_RIGHT
            1 -> this.angle = GradientAngle.RIGHT_TO_LEFT
            2 -> this.angle = GradientAngle.TOP_TO_BOTTOM
            3 -> this.angle = GradientAngle.BOTTOM_TO_TOP
            4 -> this.angle = GradientAngle.LEFT_TOP_TO_RIGHT_BOTTOM
            5 -> this.angle = GradientAngle.RIGHT_TOP_TO_LEFT_BOTTOM
            6 -> this.angle = GradientAngle.RIGHT_BOTTOM_TO_LEFT_TOP
            7 -> this.angle = GradientAngle.LEFT_BOTTOM_TO_RIGHT_TOP
            else -> this.angle = GradientAngle.LEFT_TO_RIGHT
        }
    }

    init {
        val attributes = context.theme.obtainStyledAttributes(attrs, R.styleable.ObliqueView, 0, 0)
        try {
            startAngle = attributes.getFloat(R.styleable.ObliqueView_starting_slant_angle, 90f)
            endAngle = attributes.getFloat(R.styleable.ObliqueView_ending_slant_angle, 90f)
            baseColor = attributes.getColor(R.styleable.ObliqueView_basecolor, Color.TRANSPARENT)
            radius = attributes.getFloat(R.styleable.ObliqueView_radius, 0f)
            type = attributes.getInt(R.styleable.ObliqueView_type, 0)
            startColor = attributes.getColor(R.styleable.ObliqueView_startcolor, Color.TRANSPARENT)
            endColor = attributes.getColor(R.styleable.ObliqueView_endcolor, Color.TRANSPARENT)
            elevation = attributes.getFloat(R.styleable.ObliqueView_shadow_height, 0f)
            val gradientangle = attributes.getInteger(R.styleable.ObliqueView_angle, 0)
            setupAngle(gradientangle)
        } finally {
            attributes.recycle()
        }
    }
}