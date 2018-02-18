package uk.co.barbuzz.zigzagrecyclerview;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ZigzagGridRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int IMAGE_VIEW_EVEN_TYPE = 0;
    private static final int IMAGE_VIEW_ODD_TYPE = 1;

    private List<ZigzagImage> photoList = new ArrayList<>();
    private Context context;
    private ZigzagListOnClickListener zigzagListOnClickListener;
    private int backgroundColourResId;
    private ColorDrawable blankDrawable;

    public interface ZigzagListOnClickListener {
        void onImageClicked(int position, ZigzagImage photo);
    }

    public ZigzagGridRecyclerViewAdapter(Context context, List<ZigzagImage> photoList,
                                         ZigzagListOnClickListener zigzagListOnClickListener) {
        this.context = context;
        this.photoList = photoList;
        this.zigzagListOnClickListener = zigzagListOnClickListener;
        backgroundColourResId = context.getResources().getColor(R.color.oblique_background);
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 == 0 ? IMAGE_VIEW_EVEN_TYPE : IMAGE_VIEW_ODD_TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == IMAGE_VIEW_ODD_TYPE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_oblique_views, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_oblique_views_2, parent, false);
        }
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        final ImageViewHolder imageViewHolder = (ImageViewHolder) viewHolder;

        if (backgroundColourResId != 0) {
            imageViewHolder.obliqueViewParentLayout.setBackgroundColor(backgroundColourResId);
        }

        //load first image
        int pos1 = position + position;
        final ZigzagImage zigzagImage1 = photoList.get(pos1);
        String imageUrl1 = zigzagImage1.getZigzagImageUrl();
        if (imageUrl1 == null || imageUrl1.trim().equalsIgnoreCase("")) {
            int imageResource1 = zigzagImage1.getZigzagImageResourceId();
            Picasso.with(context).load(imageResource1)
                    .into(imageViewHolder.obliqueView1);
        } else {
            Picasso.with(context).load(imageUrl1)
                    .into(imageViewHolder.obliqueView1);
        }
        imageViewHolder.obliqueView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zigzagListOnClickListener.onImageClicked(position, zigzagImage1);
            }
        });

        //load second image if need as we might have odd number in list
        int secondImagePos = pos1 + 1;
        if (secondImagePos < photoList.size()) {
            final ZigzagImage zigzagImage2 = photoList.get(secondImagePos);
            if (zigzagImage2 != null) {
                String imageUrl2 = zigzagImage2.getZigzagImageUrl();
                if (imageUrl1 == null || imageUrl1.trim().equalsIgnoreCase("")) {
                    int imageResource2 = zigzagImage2.getZigzagImageResourceId();
                    Picasso.with(context).load(imageResource2)
                            .into(imageViewHolder.obliqueView2);
                } else {
                    Picasso.with(context).load(imageUrl2)
                            .into(imageViewHolder.obliqueView2);
                }
                imageViewHolder.obliqueView2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        zigzagListOnClickListener.onImageClicked(position, zigzagImage2);
                    }
                });
            }
        } else {
            imageViewHolder.obliqueView2.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        float size = photoList.size();
        int count = Math.round(size / 2);
        return count;
    }

    public void addData(List<ZigzagImage> photoList) {
        this.photoList = photoList;
        notifyDataSetChanged();
    }

    public void setBackgroundColourResId(int backgroundColourResId) {
        this.backgroundColourResId = backgroundColourResId;
        blankDrawable = new ColorDrawable(backgroundColourResId);
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        LinearLayout obliqueViewParentLayout;
        ImageView obliqueView1;
        ImageView obliqueView2;

        public ImageViewHolder(View v) {
            super(v);

            obliqueViewParentLayout = v.findViewById(R.id.obliqueViewLayout);
            obliqueView1 = v.findViewById(R.id.obliqueView);
            obliqueView2 = v.findViewById(R.id.obliqueView2);
        }
    }

}

