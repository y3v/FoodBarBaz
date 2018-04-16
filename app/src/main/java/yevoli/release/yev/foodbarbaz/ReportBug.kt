package yevoli.release.yev.foodbarbaz

import POJO.ActivityStarter
import POJO.Const
import POJO.User
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast

class ReportBug : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    internal lateinit var user:User
    internal lateinit var toolBar: Toolbar
    internal lateinit var drawerLayout: DrawerLayout
    internal lateinit var navigationView: NavigationView
    internal lateinit var progressBarLogin: ProgressBar

    internal lateinit var bugTitle: EditText
    internal lateinit var bugDescription: EditText
    internal lateinit var submitBtn: Button


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

        bugTitle = findViewById(R.id.bug_title)
        bugDescription = findViewById(R.id.bug_description)
        submitBtn = findViewById(R.id.submit_bug_report)

        submitBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_EMAIL,Array(1, { Const.DEV_EMAIL})) // TO
            intent.setData(Uri.parse("mailto:"))
            intent.putExtra(Intent.EXTRA_SUBJECT, "FOODBARBAZ BUG REPORT - FROM : ${user.username}") // SUBJECT
            intent.putExtra(Intent.EXTRA_TEXT, "Activity : " + bugTitle.text.toString() + "\n" + bugDescription.text.toString())
            startActivityForResult(Intent.createChooser(intent, "Send Email"), 123)
        }

        val data = intent.extras
        if (data != null){
            if (data.containsKey("user"))
                user = data.get("user") as User
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 123 && resultCode == RESULT_OK){
            Toast.makeText(this, "Bug report sent! Thank you for your feedback", Toast.LENGTH_SHORT).show()
            finish()
        }
        else
            Toast.makeText(this, "Oops! Something went wrong", Toast.LENGTH_SHORT).show()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        ActivityStarter.NavigationItemSelected(this, user, item)
        Log.i("NAVIGATION:::", "" + item.toString())

        return false
    }
}
