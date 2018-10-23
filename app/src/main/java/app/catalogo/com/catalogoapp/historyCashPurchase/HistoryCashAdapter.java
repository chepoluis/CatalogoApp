package app.catalogo.com.catalogoapp.historyCashPurchase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.List;

import app.catalogo.com.catalogoapp.R;

public class HistoryCashAdapter extends RecyclerView.Adapter<HistoryCashViewHolders>{
    private List<HistoryCashObject> itemList;
    private Context context;

    public HistoryCashAdapter(List<HistoryCashObject> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public HistoryCashViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cash_history, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        HistoryCashViewHolders rcv = new HistoryCashViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryCashViewHolders holder, int position) {
        holder.saleId.setText(itemList.get(position).getSaleId());
        holder.customerName.setText("Customer: " + itemList.get(position).getCustomerName());
        holder.productPurchased.setText("Product: " + itemList.get(position).getProductPurchased());
        holder.productCost.setText("Price: $ " + itemList.get(position).getProductCost() + " MXN");
        holder.productDescriptionGone.setText("Description: " + itemList.get(position).getProductDescription());
        holder.saleDate.setText(itemList.get(position).getSaleDate());
        holder.customerCityGone.setText("City: " + itemList.get(position).getCustomerCity());
        holder.customerAddressGone.setText("Address: " + itemList.get(position).getCustomerAddress());
        holder.customerPhoneGone.setText("Phone: " + itemList.get(position).getCustomerPhone());
        holder.customerEmailGone.setText("Email: " + itemList.get(position).getCustomerEmail());
        holder.sellerGone.setText("Seller: " + itemList.get(position).getSeller());
        holder.customerImageGone.setText(itemList.get(position).getCustomerImage());
        holder.productImageGone.setText(itemList.get(position).getProductImage());
        Glide.with(context).load(itemList.get(position).getCustomerImage()).into(holder.customerImage);
    }

    // This method provides the numbers of objects in the CardView
    @Override
    public int getItemCount() { return this.itemList.size(); }
}
