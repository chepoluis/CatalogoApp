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
    public TextView saleDate;

    public TextView customerImageGone;
    public TextView productImageGone;
    public TextView customerAddressGone;
    public TextView customerCityGone;
    public TextView customerPhoneGone;
    public TextView customerEmailGone;
    public TextView sellerGone;
    public TextView productDescriptionGone;

    public HistoryCashViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        saleId = itemView.findViewById(R.id.saleId);
        customerName = itemView.findViewById(R.id.customer);
        productPurchased = itemView.findViewById(R.id.product);
        productCost = itemView.findViewById(R.id.product_c);
        customerImage = itemView.findViewById(R.id.customerPic);
        saleDate = itemView.findViewById(R.id.saleDate);

        customerImageGone = itemView.findViewById(R.id.imageCustomerGone);
        productImageGone = itemView.findViewById(R.id.productImageGone);
        customerAddressGone = itemView.findViewById(R.id.customerAddressGone);
        customerCityGone = itemView.findViewById(R.id.customerCityGone);
        customerPhoneGone = itemView.findViewById(R.id.customerPhoneGone);
        customerEmailGone = itemView.findViewById(R.id.customerEmailGone);
        sellerGone = itemView.findViewById(R.id.sellerGone);
        productDescriptionGone = itemView.findViewById(R.id.productDescriptionGone);
    }

    // Is triggered when a card of recycler view is click
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), HistoryCashSingleActivity.class);
        Bundle b = new Bundle();
        b.putString("saleId", saleId.getText().toString());
        intent.putExtra("saleId", saleId.getText().toString());
        intent.putExtra("customerName", customerName.getText().toString());
        intent.putExtra("productPurchase", productPurchased.getText().toString());
        intent.putExtra("productCost", productCost.getText().toString());
        intent.putExtra("saleDate", saleDate.getText().toString());

        intent.putExtra("customerImage", customerImageGone.getText().toString());
        intent.putExtra("productImage", productImageGone.getText().toString());
        intent.putExtra("customerCity", customerCityGone.getText().toString());
        intent.putExtra("customerAddress", customerAddressGone.getText().toString());
        intent.putExtra("customerPhone", customerPhoneGone.getText().toString());
        intent.putExtra("customerEmail", customerEmailGone.getText().toString());
        intent.putExtra("seller", sellerGone.getText().toString());
        intent.putExtra("productDescription", productDescriptionGone.getText().toString());
        intent.putExtras(b);
        v.getContext().startActivity(intent);
    }
}
