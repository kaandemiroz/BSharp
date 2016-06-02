package com.okd.bsharp;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OKD on 1.6.2016.
 */
public class TabAdapter extends Adapter<TabItemHolder> {

    private ArrayList<TabItem> list;

    public TabAdapter(ArrayList list){
        this.list = list;
    }

    public void addTabItem(TabItem tabItem){
        list.add(tabItem);
        notifyDataSetChanged();
    }

    public void clear(){
        list.clear();
        notifyDataSetChanged();
    }

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     * <p/>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p/>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(ViewHolder, int, List)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View     to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(ViewHolder, int)
     */
    @Override
    public TabItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tab, parent, false);
        return new TabItemHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p/>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link ViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p/>
     * Override {@link #onBindViewHolder(ViewHolder, int, List)} instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(TabItemHolder holder, int position) {
        TabItem tabItem = list.get(position);
        holder.bindTabItem(tabItem);
    }

    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return list.size();
    }

}

class TabItemHolder extends ViewHolder implements View.OnClickListener{

    private TabItem tabItem;
    private TextView string1;
    private TextView string2;
    private TextView string3;
    private TextView string4;
    private TextView string5;
    private TextView string6;

    public TabItemHolder(View itemView) {
        super(itemView);
        string1 = (TextView) itemView.findViewById(R.id.string1);
        string2 = (TextView) itemView.findViewById(R.id.string2);
        string3 = (TextView) itemView.findViewById(R.id.string3);
        string4 = (TextView) itemView.findViewById(R.id.string4);
        string5 = (TextView) itemView.findViewById(R.id.string5);
        string6 = (TextView) itemView.findViewById(R.id.string6);
        string1.setTag(1);
        string2.setTag(2);
        string3.setTag(3);
        string4.setTag(4);
        string5.setTag(5);
        string6.setTag(6);
        string1.setOnClickListener(this);
        string2.setOnClickListener(this);
        string3.setOnClickListener(this);
        string4.setOnClickListener(this);
        string5.setOnClickListener(this);
        string6.setOnClickListener(this);
    }

    public void bindTabItem(TabItem tabItem){
        this.tabItem = tabItem;
        string1.setText(tabItem.getString1());
        string2.setText(tabItem.getString2());
        string3.setText(tabItem.getString3());
        string4.setText(tabItem.getString4());
        string5.setText(tabItem.getString5());
        string6.setText(tabItem.getString6());
    }

    @Override
    public void onClick(final View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Enter note:");

        // Set up the input
        final EditText input = new EditText(view.getContext());
        // Specify the type of input expected;
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String inputString = input.getText().toString();
                switch ((Integer) view.getTag()){
                    case 1:
                        tabItem.setString1(inputString);
                        break;
                    case 2:
                        tabItem.setString2(inputString);
                        break;
                    case 3:
                        tabItem.setString3(inputString);
                        break;
                    case 4:
                        tabItem.setString4(inputString);
                        break;
                    case 5:
                        tabItem.setString5(inputString);
                        break;
                    case 6:
                        tabItem.setString6(inputString);
                        break;
                }
                bindTabItem(tabItem);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

}
