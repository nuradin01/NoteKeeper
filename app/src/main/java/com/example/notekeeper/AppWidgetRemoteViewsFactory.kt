package com.example.notekeeper

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import android.widget.RemoteViewsService.RemoteViewsFactory

class AppWidgetRemoteViewsFactory(val context: Context): RemoteViewsFactory {
    override fun onCreate() {

    }

    override fun onDataSetChanged() {

    }

    override fun onDestroy() {

    }

    override fun getCount()= DataManager.notes.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.item_note_widget)
        rv.setTextViewText(R.id.note_title, DataManager.notes[position].title)

        val extras = Bundle()
        extras.putInt(NOTE_POSITION, position)
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        rv.setOnClickFillInIntent(R.id.widget_item, fillInIntent)

        return rv
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getViewTypeCount() = 1

    override fun getItemId(position: Int) = position.toLong()

    override fun hasStableIds() = true

}