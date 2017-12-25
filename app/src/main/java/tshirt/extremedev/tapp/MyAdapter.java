package tshirt.extremedev.tapp;

/**
 * Created by sikinijjs on 10/1/17.
 */

        import android.content.Context;
        import android.content.Intent;
        import android.net.Uri;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.RelativeLayout;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.beardedhen.androidbootstrap.BootstrapLabel;
        import com.squareup.picasso.Picasso;

        import java.util.List;

        import static tshirt.extremedev.tapp.URLs.img_url;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>  {

    private List<ItemData> listItems;

    public MyAdapter(List<ItemData> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    private Context context;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.foodlistlayout, parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final ItemData itemData =listItems.get(position);


        holder.label.setText(itemData.getItem_name());
        holder.price.setText(itemData.getItem_price());
        //holder.ingresients.setText(itemData.getItem_description());


/*
public String item_price;
    public String image_url;
    public String item_name;
    public String item_description;

 */
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent= new Intent(context,Details.class);
                intent.putExtra("item_price",itemData.getItem_price());
                intent.putExtra("type","default");
                intent.putExtra("image_url",itemData.getImage_url());
                intent.putExtra("item_name",itemData.getItem_name());
                intent.putExtra("item_description",itemData.getItem_description());
                context.startActivity(intent);
            }

   });

        String final_url=img_url+itemData.getImage_url();
      //  Toast.makeText(context, final_url, Toast.LENGTH_LONG).show();



        Picasso.Builder builder = new Picasso.Builder(context);
        builder.listener(new Picasso.Listener()
        {

            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                Toast.makeText(context, String.valueOf(exception), Toast.LENGTH_SHORT).show();
            }
        });
        builder.build().load(final_url).into(holder.logo);




    }

    @Override
    public int getItemCount() {
        return  listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public BootstrapLabel label;
        public TextView price;
        public TextView ingresients;
        public RelativeLayout relativeLayout;
        public ImageView logo;

        public ViewHolder(View itemView)
        {
            super(itemView);
            label=(BootstrapLabel) itemView.findViewById(R.id.bootstrapLabel);
            price=(TextView)itemView.findViewById(R.id.textView5);
            ingresients=(TextView)itemView.findViewById(R.id.textView12);
            logo=(ImageView)itemView.findViewById(R.id.imageView4);
            relativeLayout=(RelativeLayout)itemView.findViewById(R.id.boundRelativeLayout);

        }
    }
    {

    }



}
