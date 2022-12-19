package com.example.notekeeper

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.invalidateOptionsMenu
import androidx.core.content.ContextCompat
import com.example.notekeeper.databinding.ActivityNoteBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class NoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNoteBinding

    //    private lateinit var colorSelectorBinding: ColorSelectorBinding
    private var notePosition = POSITION_NOT_SET
    private var isNewNote = false
    private var isCancelling = false
    private var noteColor: Int = Color.TRANSPARENT
    private val reminderNotification by lazy { ReminderNotification(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNoteBinding.inflate(layoutInflater)
//        colorSelectorBinding = ColorSelectorBinding.bind(binding.root)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val adapterCourses = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            DataManager.courses.values.toList()
        )
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerCourses.adapter = adapterCourses

        notePosition =
            savedInstanceState?.getInt(NOTE_POSITION, POSITION_NOT_SET) ?: intent.getIntExtra(
                NOTE_POSITION,
                POSITION_NOT_SET
            )

//        val bundle = intent.extras
//        bundle?.let {
//            val currentPos = it.getInt(NOTE_POSITION)
//        Log.d("NoteActivity", DataManager.notes[currentPos].toString())
//        }
        if (notePosition != POSITION_NOT_SET)
            displayNote()
        else {
            isNewNote = true
            DataManager.notes.add(NoteInfo())
            notePosition = DataManager.notes.lastIndex
        }

        binding.colorSelector.addListener { color ->
            noteColor = color
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(NOTE_POSITION, notePosition)
    }

    private fun displayNote() {
        val note = DataManager.notes[notePosition]
        binding.textNoteTitle.setText(note.title)
        binding.textNoteText.setText(note.text)
        binding.colorSelector.selectedColorValue = note.color
        noteColor = note.color

        val coursePosition = DataManager.courses.values.indexOf(note.course)
        binding.spinnerCourses.setSelection(coursePosition)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_note, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_remind -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    when (PackageManager.PERMISSION_GRANTED) {
                        ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) -> {
                            reminderNotification.showNotification(notePosition)
                        }
                        else -> {
                            requestReminderNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                }
                reminderNotification.showNotification(notePosition)

                true
            }
            R.id.action_cancel -> {
                isCancelling = true
                finish()
                true
            }
            R.id.action_next -> {
                moveNext()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun moveNext() {
        saveNote()
        ++notePosition
        displayNote()
        invalidateOptionsMenu()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (notePosition >= DataManager.notes.lastIndex) {
            val menuItem = menu?.findItem(R.id.action_next)
            if (menuItem != null) {
                menuItem.icon = getDrawable(R.drawable.ic_block_white_24dp)
                menuItem.isEnabled = false
            }
        }

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onPause() {
        super.onPause()
        when {
            isCancelling -> {
                if (isNewNote)
                    DataManager.notes.removeAt(notePosition)
            }
            else -> saveNote()
        }
    }

    private fun saveNote() {
        val note = DataManager.notes[notePosition]
        note.title = binding.textNoteTitle.text.toString()
        note.text = binding.textNoteText.text.toString()
        note.course = binding.spinnerCourses.selectedItem as CourseInfo
        note.color = this.noteColor
        NoteKeeperAppWidget.sendRefreshBroadcast(this)
    }

    private val requestReminderNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                MaterialAlertDialogBuilder(this)
                    .setTitle("Notification Permission")
                    .setMessage("This app will not work without Notification permissions")
                    .setNegativeButton("cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setPositiveButton("Ok") { _, _ ->
                        openAppSetting.launch(
                            Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", packageName, null)
                            )
                        )
                    }
                    .show()
            } else {
                reminderNotification.showNotification(notePosition)
            }

        }
    private val openAppSetting =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {}
}











