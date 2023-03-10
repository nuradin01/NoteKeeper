package com.example.notekeeper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notekeeper.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.jwhh.notekeeper.CourseRecyclerAdapter
import com.jwhh.notekeeper.NoteRecyclerAdapter

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding

    val noteLayoutManager by lazy {
        LinearLayoutManager(this)
    }

    val noteRecyclerAdapter by lazy {
        NoteRecyclerAdapter(this)
    }

    val courseLayoutManager by lazy {
        GridLayoutManager(this, 2)
    }

    val courseRecyclerAdapter by lazy {
        CourseRecyclerAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarInclude.toolbar)

        binding.appBarInclude.fab.setOnClickListener { view ->
            val activityIntent = Intent(this, NoteActivity::class.java)
            startActivity(activityIntent)
        }

        val toggle = ActionBarDrawerToggle(
            this,binding.drawerLayout, binding.appBarInclude.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navView.setNavigationItemSelectedListener(this)

        displayNotes()


    }
    override fun onResume() {
        super.onResume()
        noteRecyclerAdapter.notifyDataSetChanged()
    }

    fun displayNotes() {
        binding.appBarInclude.contentInclude.recyclerItems.layoutManager = noteLayoutManager
        binding.appBarInclude.contentInclude.recyclerItems.adapter = noteRecyclerAdapter
        binding.navView.menu.findItem(R.id.nav_notes).isChecked = true
    }

    fun displayCourses() {
        binding.appBarInclude.contentInclude.recyclerItems.layoutManager = courseLayoutManager
        binding.appBarInclude.contentInclude.recyclerItems.adapter = courseRecyclerAdapter
        binding.navView.menu.findItem(R.id.nav_courses).isChecked = true
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.items, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                showSnackbar("Display settings")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_notes -> {
                displayNotes()
            }
            R.id.nav_courses -> {
                displayCourses()
            }
            R.id.nav_share -> {
                showSnackbar("Don't you think you've shared enough")
            }
            R.id.nav_send -> {
                showSnackbar("Send")
            }
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.appBarInclude.contentInclude.recyclerItems, message, Snackbar.LENGTH_LONG).show()
    }
}