package com.example.seniorproject.Utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/*
* This abstract class was designed to complete the swiping functionality for the posts and comments using an itemTouchHelper callback.
* This was originally in the views, but a need to isolate them and create a helper function, so that multiple views can reference the
* same itemtouch helper and to avoid code duplication across many views.
* */
abstract class SwipeHelper(context: Context, private val recyclerView: RecyclerView, private var buttonWidth: Int):
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
    //itemtouchhelper left means that you can only drag the swipe left

    //abstract function that will be implemented across community posts, clicked posts, and both fragments in user profile
    //initalizes profile buttons
    abstract fun initButton(viewHolders: RecyclerView.ViewHolder, buffer: MutableList<ProfileButton>)

    private lateinit var gestureDetector: GestureDetector
    private lateinit var buttonQueue: LinkedList<Int>

    var buttonBuffer: MutableMap<Int, MutableList<ProfileButton>>
    private var buttonList: MutableList<ProfileButton>? = null
    var swipePosition = -1
    var swipeThreshold =0.5f

    //detects gestures of swiping. swiping cannot be tapped upwards to initialize. they are clicked with ontouchlistener
    private val gestureListener = object: GestureDetector.SimpleOnGestureListener(){
        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            for(button in buttonList!!)
            {
                if(button.onClick(e!!.x, e!!.y))
                    break
            }
            return true
        }
    }


    private val onTouchListener = View.OnTouchListener{ _, motionEvent ->
        if(swipePosition < 0) return@OnTouchListener false
        val point= Point (motionEvent.rawX.toInt(), motionEvent.rawY.toInt())
        val swipeViewHolder = recyclerView.findViewHolderForAdapterPosition(swipePosition)
        val swipedItem = swipeViewHolder!!.itemView
        //create rectangle here
        val rect= Rect()
        swipedItem.getGlobalVisibleRect(rect)


        if(motionEvent.action == MotionEvent.ACTION_DOWN || motionEvent.action == MotionEvent.ACTION_MOVE || motionEvent.action == MotionEvent.ACTION_UP){
            if(rect.top < point.y && rect.bottom > point.y)
                gestureDetector.onTouchEvent(motionEvent)
            else {
                buttonQueue.add(swipePosition)
                swipePosition = -1

                while(!buttonQueue.isEmpty())
                {
                    val pos = buttonQueue.poll().toInt()
                    if(pos > -1)
                        recyclerView.adapter!!.notifyItemChanged(pos)

                }

            }

        }

        false

    }

    //initializes the onTouchListeners and other variables needed to use Swipe Helper
    init {
        this.recyclerView.setOnTouchListener(onTouchListener)
        this.gestureDetector = GestureDetector(context, gestureListener)
        this.buttonBuffer = HashMap ()
        this.buttonList = ArrayList()
        this.buttonQueue = IntLinkedList()

        //itemtouchhelper is attached to the following recyclerview and interacts with all its view holders.
        val itemTouchHelper = ItemTouchHelper(this)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }


    class IntLinkedList : LinkedList<Int>(){
        override fun contains(element: Int): Boolean {
            return false
        }

        override fun lastIndexOf(element: Int): Int {
            return element
        }

        override fun remove(element: Int): Boolean {
            return false
        }

        override fun indexOf(element: Int): Int {
            return element
        }

        override fun add(element: Int): Boolean {
            return if(contains(element))
                false
            else super.add(element)
        }
    }

    //this is false because we are not moving the viewHolder or the swipe. we just want to swipe
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    //this funciton is called after the user swipes on a post or comment
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val pos = viewHolder.adapterPosition
        if(swipePosition !=pos)
            buttonQueue.add(swipePosition)
        swipePosition = pos
        if(buttonBuffer.containsKey(swipePosition))
            buttonList = buttonBuffer[swipePosition]
        else
            buttonList!!.clear()
        buttonBuffer.clear()
        swipeThreshold = 0.5f*buttonList!!.size.toFloat()*buttonWidth.toFloat()

        while(!buttonQueue.isEmpty())
        {
            val pos = buttonQueue.poll().toInt()
            if(pos > -1)
                recyclerView.adapter!!.notifyItemChanged(pos)

        }

    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return swipeThreshold
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return 0.1f*defaultValue
    }

    override fun getSwipeVelocityThreshold(defaultValue: Float): Float {
        return 5.0f*defaultValue
    }

    //this function is responsible for drawing the button
    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {

        val pos = viewHolder.adapterPosition
        var translationX = dX
        val itemView = viewHolder.itemView
        if(pos < 0){
            swipePosition = pos
            return
        }
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE)
        {
            //if statement dx<0 is when user swipes in left direction
            if(dX < 0)
            {
                var buffer: MutableList<ProfileButton> = ArrayList()
                if(!buttonBuffer.containsKey(pos))
                {
                    initButton(viewHolder, buffer)
                    buttonBuffer[pos]= buffer
                }
                else{
                    buffer = buttonBuffer[pos]!!
                }
                translationX = dX*buffer.size.toFloat() * buttonWidth.toFloat()/ itemView.width
                drawButton(c, itemView, buffer, pos, translationX)
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, translationX, dY, actionState, isCurrentlyActive)


    }

    private fun drawButton(
        c: Canvas,
        itemView: View,
        buffer: MutableList<ProfileButton>,
        pos: Int,
        translationX: Float
    ) {
        var right = itemView.right.toFloat()
        val dButtonWidth = -1*translationX/buffer.size
        for(button in buffer){
            //this draws the button dimensions in each of the buttons in buffer
            val left= right - dButtonWidth
            button.onDraw(c, RectF(left, itemView.top.toFloat(), right, itemView.bottom.toFloat()), pos)
            right=left
        }

    }

}