package com.example.android.oldschoolRS;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DisplayAdapter extends RecyclerView.Adapter<DisplayAdapter.NumberViewHolder> {


    private final ListItemClickListener mOnClickListener;
    private int mNumberItems;
    private List<Item> mItemList = new ArrayList<Item>();
    //endregion

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    /**
     * Constructor for DisplayAdapter that accepts a number of items to display and the
     * specification for the ListItemClickListener.
     *
     * @param numberOfItems Number of items to display in list

     */
    public DisplayAdapter(int numberOfItems, List<Item> resultsList,
                          ListItemClickListener listener) {
        mNumberItems = numberOfItems;
        mOnClickListener = listener;
        mItemList = resultsList;
    }


    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup,
                shouldAttachToParentImmediately);
        NumberViewHolder viewHolder = new NumberViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NumberViewHolder holder, int position) {
        holder.bind(position, holder.itemView.getContext());

    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available
     */
    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    /**
     * Cache of the children views for a list item.
     */
    class NumberViewHolder extends RecyclerView.ViewHolder
        implements OnClickListener {

        // Will display the image of the item
        ImageView listItemImageView;
        // Will display the members icon if the item is members only
        ImageView memberImageView;
        // Will display the item name
        TextView listItemName;
        // Will display the item price
        TextView listItemPrice;
        // Will display the item price
        TextView listItemDesc;


        public NumberViewHolder(View itemView) {
            super(itemView);

            listItemImageView = (ImageView) itemView.findViewById(R.id.item_image);
            memberImageView = (ImageView) itemView.findViewById(R.id.item_member);
            listItemName = (TextView) itemView.findViewById(R.id.tv_itemName);
            listItemPrice = (TextView) itemView.findViewById(R.id.tv_itemPrice);
            listItemDesc = (TextView) itemView.findViewById(R.id.tv_itemDesc);

            itemView.setOnClickListener(this);
        }


        void bind(int listIndex, Context context) {

            try {
                Item item = mItemList.get(listIndex);

                // Load the image onto the ImageView
                Picasso.with(context).load(item.getIcon())
                        .error(R.mipmap.ic_launcher)
                        .into(listItemImageView);


                listItemName.setText(item.getName());
                listItemDesc.setText(item.getDescription());

                // If the item is a members item, display the members icon
                if(item.getMembers()){ memberImageView.setVisibility(View.VISIBLE);}
                else{
                    memberImageView.setVisibility(View.GONE);
                }

            }
            catch(Exception e) {
                e.printStackTrace();

            }
        }


        /**
         * Called whenever a user clicks on an item in the list.
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
