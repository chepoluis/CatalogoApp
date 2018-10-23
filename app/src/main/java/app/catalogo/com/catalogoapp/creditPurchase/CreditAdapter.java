package app.catalogo.com.catalogoapp.creditPurchase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import app.catalogo.com.catalogoapp.R;
import app.catalogo.com.catalogoapp.historyCashPurchase.HistoryCashObject;
import app.catalogo.com.catalogoapp.historyCashPurchase.HistoryCashViewHolders;

public class CreditAdapter extends RecyclerView.Adapter<CreditViewHolders> {

    private List<CreditObject> itemList;
    private Context context;

    public CreditAdapter(List<CreditObject> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public CreditViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_credit, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        CreditViewHolders rcv = new CreditViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull CreditViewHolders holder, int position) {
        holder.saleId.setText(itemList.get(position).getSaleId());
        holder.customerName.setText(itemList.get(position).getCustomerName());
        holder.productPurchased.setText("Product: " + itemList.get(position).getProductPurchased());
        holder.productCost.setText("Price: $ " + itemList.get(position).getProductCost() + " MXN");
        holder.productDescriptionGone.setText("Description: " + itemList.get(position).getProductDescription());
        holder.saleDate.setText(itemList.get(position).getSaleDate());
        holder.debt.setText("Debt: $ " + itemList.get(position).getDebt() + " MXN");
        holder.customerCityGone.setText("City: " + itemList.get(position).getCustomerCity());
        holder.customerAddressGone.setText("Address: " + itemList.get(position).getCustomerAddress());
        holder.customerPhoneGone.setText("Phone: " + itemList.get(position).getCustomerPhone());
        holder.customerEmailGone.setText(itemList.get(position).getCustomerEmail());
        holder.sellerGone.setText("Seller: " + itemList.get(position).getSeller());
        holder.customerImageGone.setText(itemList.get(position).getCustomerImage());
        holder.productImageGone.setText(itemList.get(position).getProductImage());

        holder.collectWhenGone.setText("Collect: " + itemList.get(position).getCollectWhen());
        holder.collectDayGone.setText("Day: " + itemList.get(position).getCollectDay());
        holder.firstHourGone.setText("Time: " + itemList.get(position).getFirstHour() + " - ");
        holder.secondHourGone.setText(itemList.get(position).getSecondHour());
        holder.amountMoneyGone.setText("Amount money: " + itemList.get(position).getAmountMoney());
        holder.initialPayGone.setText(itemList.get(position).getInitialPay());
        holder.numberPaymentsGone.setText("Payments: " + itemList.get(position).getNumberPayments());
        Glide.with(context).load(itemList.get(position).getCustomerImage()).into(holder.customerImage);
    }

    // This method provides the numbers of objects in the CardView
    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}
