package yevoli.release.yev.foodbarbaz

import POJO.ActivityStarter
import POJO.User
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar

class ReportBug : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    internal lateinit var user:User
    internal lateinit var toolBar: Toolbar
    internal lateinit var drawerLayout: DrawerLayout
    internal lateinit var navigationView: NavigationView
    internal lateinit var progressBarLogin: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_bug)

        toolBar = findViewById(R.id.include)
        setSupportActionBar(toolBar)
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.open_drawer, R.string.closed_drawer)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        progressBarLogin = findViewById(R.id.progressBar)
        progressBarLogin.visibility = View.GONE

        val data = intent.extras
        if (data != null){
            if (data.containsKey("user"))
                user = data.get("user") as User
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        ActivityStarter.NavigationItemSelected(this, user, item)
        Log.i("NAVIGATION:::", "" + item.toString())

        return false
    }
}
