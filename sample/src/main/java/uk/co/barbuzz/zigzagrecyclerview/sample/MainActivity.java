package uk.co.barbuzz.zigzagrecyclerview.sample;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import uk.co.barbuzz.zigzagrecyclerview.ZigzagGridRecyclerViewAdapter;
import uk.co.barbuzz.zigzagrecyclerview.ZigzagImage;

public class MainActivity extends AppCompatActivity
        implements ZigzagGridRecyclerViewAdapter.ZigzagListOnClickListener {

    private ArrayList<ZigzagImage> imageList;
    private AlertDialog infoDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        getImageData();

        initViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_github) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(getResources().getString(R.string.github_link)));
            startActivity(i);
            return true;
        } else if (id == R.id.action_info) {
            showInfoDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showInfoDialog() {
        if (infoDialog != null && infoDialog.isShowing()) {
            //do nothing if already showing
        } else {
            infoDialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.app_name)
                    .setMessage(R.string.info_details)
                    .setCancelable(true)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("More info", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(getResources().getString(R.string.github_link))));
                        }
                    })
                    .create();
            infoDialog.show();
        }
    }

    @Override
    public void onZigzagImageClicked(int position, ZigzagImage zigzagImage) {
        Snackbar.make(findViewById(android.R.id.content),
                "Tapped on image " + position, Snackbar.LENGTH_LONG).show();
    }

    private void getImageData() {
        imageList = new ArrayList<>();
        imageList.add(new SnowImage(R.drawable.snowboard2));
        imageList.add(new SnowImage(R.drawable.snowboard1));
        imageList.add(new SnowImage(R.drawable.snowboard3));
        imageList.add(new SnowImage(R.drawable.snowboard4));

        imageList.add(new SnowImage(R.drawable.snowboard6));
        imageList.add(new SnowImage(R.drawable.snowboard5));
        imageList.add(new SnowImage(R.drawable.snowboard8));
        imageList.add(new SnowImage(R.drawable.snowboard7));

        imageList.add(new SnowImage(R.drawable.snowboard9));
        imageList.add(new SnowImage(R.drawable.snowboard1));
        imageList.add(new SnowImage(R.drawable.snowboard2));
        imageList.add(new SnowImage(R.drawable.snowboard3));

        imageList.add(new SnowImage(R.drawable.snowboard4));
        imageList.add(new SnowImage(R.drawable.snowboard5));
        imageList.add(new SnowImage(R.drawable.snowboard6));
        imageList.add(new SnowImage(R.drawable.snowboard7));
    }

    private void initViews() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        ZigzagGridRecyclerViewAdapter zigzagGridRecyclerViewAdapter
                = new ZigzagGridRecyclerViewAdapter(this, imageList, this);

        zigzagGridRecyclerViewAdapter.setBackgroundColourResId(getResources().getColor(R.color.separator));
        zigzagGridRecyclerViewAdapter.setPlaceholderDrawableResId(R.drawable.placeholder_image);

        RecyclerView zigzagRecyclerView = findViewById(R.id.zigzag_recycler_view);
        zigzagRecyclerView.setLayoutManager(linearLayoutManager);
        zigzagRecyclerView.setAdapter(zigzagGridRecyclerViewAdapter);
    }
}
