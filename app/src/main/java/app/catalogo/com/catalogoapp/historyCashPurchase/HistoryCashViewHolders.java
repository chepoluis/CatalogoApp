package app.catalogo.com.catalogoapp.historyCashPurchase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import app.catalogo.com.catalogoapp.HistoryCashSingleActivity;
import app.catalogo.com.catalogoapp.R;

public class HistoryCashViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView saleId;
    public TextView customerName;
    public TextView productPurchased;
    public TextView productCost;
    public ImageView customerImage;

    public HistoryCashViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        saleId = itemView.findViewById(R.id.saleId);
        customerName = itemView.findViewById(R.id.customer);
        productPurchased = itemView.findViewById(R.id.product);
        productCost = itemView.findViewById(R.id.product_c);
        customerImage = itemView.findViewById(R.id.customerPic);
    }

    // Is triggered when a card of recycler view is click
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), HistoryCashSingleActivity.class);
        Bundle b = new Bundle();
        b.putString("saleId", saleId.getText().toString());
        intent.putExtras(b);
        v.getContext().startActivity(intent);
    }
}
