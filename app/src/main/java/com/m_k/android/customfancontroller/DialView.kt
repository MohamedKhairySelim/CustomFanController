package com.m_k.android.customfancontroller

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.*

private enum class FanSpeed(val Lable : Int){
    OFF(R.string.fan_off),
    LOW(R.string.fan_low),
    MEDIUM(R.string.fan_medium),
    HIGH(R.string.fan_high);


    fun next() = when(this){
        OFF->LOW
        LOW->MEDIUM
        MEDIUM->HIGH
        HIGH->OFF
    }
}

private const val radius_OffSet_Label = 30
private const val radius_OffSet_Indicator = -35

class DialView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var radius = 0.0f
    private var fanSpeed = FanSpeed.OFF
    private val pointPosition : PointF = PointF(0.0f,0.0f)


    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create("" , Typeface.BOLD)
    }

    init {
        isClickable = true
    }

    override fun performClick(): Boolean {
        if (super.performClick()) return true

        fanSpeed = fanSpeed.next()
        contentDescription = resources.getString(fanSpeed.Lable)

        invalidate()
        return true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        radius = (min(w,h)/2.5*0.8).toFloat()
    }

    private fun PointF.computeXYForSpeed(pos:FanSpeed,radius:Float){
        val startAngle = Math.PI * (9/8.0)
        val  angle = startAngle + pos.ordinal * (Math.PI/4)
        x = (radius * cos(angle)).toFloat() + width/2
        y = (radius * sin(angle)).toFloat() + height/2
    }

    override fun onDraw(canvas: Canvas?) {
        paint.color = if (fanSpeed == FanSpeed.OFF) Color.GRAY else Color.GREEN
        if (canvas != null) {
            canvas.drawCircle((width/2).toFloat(),(height/2).toFloat(),radius,paint)
        }

        val markerRadius = radius + radius_OffSet_Indicator
        pointPosition.computeXYForSpeed(fanSpeed,markerRadius)
        paint.color = Color.BLACK
        if (canvas != null) {
            canvas.drawCircle(pointPosition.x,pointPosition.y,radius/12,paint)
        }

    val labelRadius = radius + radius_OffSet_Label
       for (i in FanSpeed.values()){
           pointPosition.computeXYForSpeed(i,labelRadius)
           val label = resources.getString(i.Lable)
           if (canvas != null) {
               canvas.drawText(label,pointPosition.x,pointPosition.y,paint)
           }
       }
    }

}
