package uk.co.barbuzz.zigzagrecyclerview;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
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

    private List<? extends ZigzagImage> zigzagImageList = new ArrayList<>();
    private Context context;
    private ZigzagListOnClickListener zigzagListOnClickListener;
    private int backgroundColourResId;
    private int placeholderDrawableResId;

    public interface ZigzagListOnClickListener {
        void onZigzagImageClicked(int position, ZigzagImage zigzagImage);
    }

    public ZigzagGridRecyclerViewAdapter(Context context, List<? extends ZigzagImage> zigzagImageList,
                                         ZigzagListOnClickListener zigzagListOnClickListener) {
        this.context = context;
        this.zigzagImageList = zigzagImageList;
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
        final ZigzagImage zigzagImage1 = zigzagImageList.get(pos1);
        String imageUrl1 = zigzagImage1.getZigzagImageUrl();
        if (imageUrl1 == null || imageUrl1.trim().equalsIgnoreCase("")) {
            int imageResource1 = zigzagImage1.getZigzagImageResourceId();
            loadImageResource(imageResource1, imageViewHolder.obliqueView1);
        } else {
            loadImageResourceUrl(imageUrl1, imageViewHolder.obliqueView1);
        }
        imageViewHolder.obliqueView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zigzagListOnClickListener.onZigzagImageClicked(position, zigzagImage1);
            }
        });

        //load second image if need as we might have odd number in list
        int secondImagePos = pos1 + 1;
        if (secondImagePos < zigzagImageList.size()) {
            final ZigzagImage zigzagImage2 = zigzagImageList.get(secondImagePos);
            if (zigzagImage2 != null) {
                String imageUrl2 = zigzagImage2.getZigzagImageUrl();
                if (imageUrl1 == null || imageUrl1.trim().equalsIgnoreCase("")) {
                    int imageResource2 = zigzagImage2.getZigzagImageResourceId();
                    loadImageResource(imageResource2, imageViewHolder.obliqueView2);
                } else {
                    loadImageResourceUrl(imageUrl2, imageViewHolder.obliqueView2);
                }
                imageViewHolder.obliqueView2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        zigzagListOnClickListener.onZigzagImageClicked(position, zigzagImage2);
                    }
                });
            }
        } else {
            imageViewHolder.obliqueView2.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        float size = zigzagImageList.size();
        int count = Math.round(size / 2);
        return count;
    }

    private void loadImageResource(int imageResource, ImageView obliqueView) {
        if (placeholderDrawableResId==0) {
            Picasso.with(context).load(imageResource)
                    .into(obliqueView);
        } else {
            Picasso.with(context).load(imageResource)
                    .placeholder(placeholderDrawableResId)
                    .into(obliqueView);
        }
    }

    private void loadImageResourceUrl(String imageUrlString, ImageView obliqueView) {
        if (placeholderDrawableResId==0) {
            Picasso.with(context).load(imageUrlString)
                    .into(obliqueView);
        } else {
            Picasso.with(context).load(imageUrlString)
                    .placeholder(placeholderDrawableResId)
                    .into(obliqueView);
        }
    }

    public void setData(List<? extends ZigzagImage> zigzagImageList) {
        this.zigzagImageList = zigzagImageList;
        notifyDataSetChanged();
    }

    public List<? extends ZigzagImage> getData() {
        return zigzagImageList;
    }

    public ZigzagListOnClickListener getZigzagListOnClickListener() {
        return zigzagListOnClickListener;
    }

    public void setZigzagListOnClickListener(ZigzagListOnClickListener zigzagListOnClickListener) {
        this.zigzagListOnClickListener = zigzagListOnClickListener;
    }

    public void setBackgroundColourResId(int backgroundColourResId) {
        this.backgroundColourResId = backgroundColourResId;
    }

    public void setPlaceholderDrawableResId(int placeholderDrawableResId) {
        this.placeholderDrawableResId = placeholderDrawableResId;
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

