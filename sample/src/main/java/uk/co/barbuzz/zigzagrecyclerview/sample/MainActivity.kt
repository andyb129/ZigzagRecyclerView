package uk.co.barbuzz.zigzagrecyclerview.sample

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import uk.co.barbuzz.zigzagrecyclerview.ZigzagGridRecyclerViewAdapter
import uk.co.barbuzz.zigzagrecyclerview.ZigzagGridRecyclerViewAdapter.ZigzagListOnClickListener
import uk.co.barbuzz.zigzagrecyclerview.ZigzagImage
import java.util.*

class MainActivity : AppCompatActivity(), ZigzagListOnClickListener {

    private lateinit var imageList: ArrayList<ZigzagImage>
    private lateinit var infoDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageData
        initViews()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_github) {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(resources.getString(R.string.github_link))
            startActivity(i)
            return true
        } else if (id == R.id.action_info) {
            showInfoDialog()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showInfoDialog() {
        infoDialog = AlertDialog.Builder(this)
                .setTitle(R.string.app_name)
                .setMessage(R.string.info_details)
                .setCancelable(true)
                .setPositiveButton("OK") { dialog, which -> dialog.dismiss() }
                .setNegativeButton("More info") { dialog, which ->
                    dialog.dismiss()
                    startActivity(Intent(Intent.ACTION_VIEW,
                            Uri.parse(resources.getString(R.string.github_link))))
                }
                .create()
        if (!infoDialog.isShowing) infoDialog.show()
    }

    override fun onZigzagImageClicked(position: Int, zigzagImage: ZigzagImage?) {
        Snackbar.make(findViewById(android.R.id.content),
                "Tapped on image $position", Snackbar.LENGTH_LONG).show()
    }

    private val imageData: Unit
        get() {
            imageList = ArrayList()
            imageList.add(SnowImage(R.drawable.snowboard2))
            imageList.add(SnowImage(R.drawable.snowboard1))
            imageList.add(SnowImage(R.drawable.snowboard3))
            imageList.add(SnowImage(R.drawable.snowboard4))
            imageList.add(SnowImage(R.drawable.snowboard6))
            imageList.add(SnowImage(R.drawable.snowboard5))
            imageList.add(SnowImage(R.drawable.snowboard8))
            imageList.add(SnowImage(R.drawable.snowboard7))
            imageList.add(SnowImage(R.drawable.snowboard9))
            imageList.add(SnowImage(R.drawable.snowboard1))
            imageList.add(SnowImage(R.drawable.snowboard2))
            imageList.add(SnowImage(R.drawable.snowboard3))
            imageList.add(SnowImage(R.drawable.snowboard4))
            imageList.add(SnowImage(R.drawable.snowboard5))
            imageList.add(SnowImage(R.drawable.snowboard6))
            imageList.add(SnowImage(R.drawable.snowboard7))
        }

    private fun initViews() {
        val linearLayoutManager = LinearLayoutManager(this)
        val zigzagGridRecyclerViewAdapter = ZigzagGridRecyclerViewAdapter(this, imageList, this)
        zigzagGridRecyclerViewAdapter.setBackgroundColourResId(resources.getColor(R.color.separator))
        zigzagGridRecyclerViewAdapter.setPlaceholderDrawableResId(R.drawable.placeholder_image)
        val zigzagRecyclerView = findViewById<RecyclerView>(R.id.zigzag_recycler_view)
        zigzagRecyclerView.layoutManager = linearLayoutManager
        zigzagRecyclerView.adapter = zigzagGridRecyclerViewAdapter
    }
}