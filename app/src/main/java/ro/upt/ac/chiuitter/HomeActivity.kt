package ro.upt.ac.chiuitter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.view_home.*

class HomeActivity : AppCompatActivity() {

    private val dummyChiuitStore = DummyChiuitStore()

    private lateinit var listAdapter: ChiuitRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_home)

        fab_add.setOnClickListener { composeChiuit() }
        initList()
    }

    private fun initList() {
        val chiuitList = dummyChiuitStore.getAllData()
        listAdapter = ChiuitRecyclerViewAdapter(chiuitList.toMutableList()) {
            shareChiuit(it.description)
        }

        rv_chiuit_list.layoutManager = LinearLayoutManager(this)
        rv_chiuit_list.adapter = listAdapter
    }

    /*
    Defines text sharing/sending *implicit* intent, opens the application chooser menu
    and then starts a new activity which supports sharing/sending text.
     */
    private fun shareChiuit(text: String) {
        val sendIntent = Intent().apply {

            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)

        }

        val intentChooser = Intent.createChooser(sendIntent, "")

        startActivity(intentChooser)
    }

    /*
    Defines an *explicit* intent which will be used to start ComposeActivity.
     */
    private fun composeChiuit() {
        val intent = Intent(this, ComposeActivity::class.java)

        // Not only we are using the explicit approach, but we start a new activity
        // that we expect to return the text as result.
        startActivityForResult(intent, COMPOSE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            COMPOSE_REQUEST_CODE -> if (resultCode == Activity.RESULT_OK) extractText(data)
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun extractText(data: Intent?) {
        data?.let {
            val text = it.extras?.getString(ComposeActivity.EXTRA_TEXT)

            listAdapter.addItem(Chiuit(text!!))
        }
    }

    companion object {
        const val COMPOSE_REQUEST_CODE = 1213
    }

}
