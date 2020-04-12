package com.example.seniorproject.Utils

import android.content.Context
import android.content.res.Resources
import android.graphics.*


class ProfileButton(private val context: Context, private val text: String, private val textSize: Int,
                    private val imageResId: Int, private val color: Int, private val listener: ButtonClickListener) {

    private val resources: Resources = context.resources
    private var clickRegion: RectF?=null
    private var pos: Int =0


    fun onClick(x: Float, y: Float): Boolean{

        if(clickRegion !=null && clickRegion!!.contains(x,y))
        {
            listener.onClick(pos)
            return true
        }
        return false
    }

    //draws the profile button, specifically how it'll look on the inside using text.
    fun onDraw(c: Canvas, rectF: RectF, pos: Int){
        val p= Paint()
        p.color = color
        c.drawRect(rectF, p)

        p.color = Color.WHITE
        p.textSize = textSize.toFloat()

        val r= Rect()
        val cHeight = rectF.height()
        val cWidth= rectF.width()
        p.textAlign= Paint.Align.LEFT
        p.getTextBounds(text, 0, text.length, r)


        if(imageResId ==0)
        {
            val x=cWidth /2f-r.width() /2f-r.left.toFloat()
            val y= cHeight / 2F+r.height() / 2f-r.bottom.toFloat()
            c.drawText(text, rectF.left+x, rectF.top+y, p)
        }

        clickRegion = rectF
        this.pos = pos
    }

}