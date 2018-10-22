package app.catalogo.com.catalogoapp.creditPurchase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import app.catalogo.com.catalogoapp.HistoryCashSingleActivity;
import app.catalogo.com.catalogoapp.R;

public class CreditViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView saleId;
    public TextView customerName;
    public TextView productPurchased;
    public TextView productCost;
    public ImageView customerImage;
    public TextView debt;

    public TextView customerImageGone;
    public TextView productImageGone;
    public TextView customerAddressGone;
    public TextView customerCityGone;
    public TextView customerPhoneGone;
    public TextView customerEmailGone;
    public TextView sellerGone;
    public TextView productDescriptionGone;

    public TextView collectWhenGone;
    public TextView collectDayGone;
    public TextView firstHourGone;
    public TextView secondHourGone;
    public TextView amountMoneyGone;
    public TextView initialPayGone;
    public TextView numberPaymentsGone;

    public CreditViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        saleId = itemView.findViewById(R.id.saleIdCredit);
        customerName = itemView.findViewById(R.id.customerNameCredit);
        productPurchased = itemView.findViewById(R.id.productCredit);
        productCost = itemView.findViewById(R.id.productCostCredit);
        customerImage = itemView.findViewById(R.id.customerPicCredit);
        debt = itemView.findViewById(R.id.debt);

        customerImageGone = itemView.findViewById(R.id.imageCustomerGoneCredit);
        productImageGone = itemView.findViewById(R.id.productImageGoneCredit);
        customerAddressGone = itemView.findViewById(R.id.customerAddressGoneCredit);
        customerCityGone = itemView.findViewById(R.id.customerCityGoneCredit);
        customerPhoneGone = itemView.findViewById(R.id.customerPhoneGoneCredit);
        customerEmailGone = itemView.findViewById(R.id.customerEmailGoneCredit);
        sellerGone = itemView.findViewById(R.id.sellerGoneCredit);
        productDescriptionGone = itemView.findViewById(R.id.productDescriptionGoneCredit);

        collectWhenGone = itemView.findViewById(R.id.collectWhenGoneCredit);
        collectDayGone = itemView.findViewById(R.id.collectDayGoneCredit);
        firstHourGone = itemView.findViewById(R.id.firstHourGoneCredit);
        secondHourGone = itemView.findViewById(R.id.secondHourGoneCredit);
        amountMoneyGone = itemView.findViewById(R.id.amountMoneyGoneCredit);
        initialPayGone = itemView.findViewById(R.id.initialPayGoneCredit);
        numberPaymentsGone = itemView.findViewById(R.id.numberPaymentsGoneCredit);
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
        intent.putExtra("debt", debt.getText().toString());

        intent.putExtra("customerImage", customerImageGone.getText().toString());
        intent.putExtra("productImage", productImageGone.getText().toString());
        intent.putExtra("customerCity", customerCityGone.getText().toString());
        intent.putExtra("customerAddress", customerAddressGone.getText().toString());
        intent.putExtra("customerPhone", customerPhoneGone.getText().toString());
        intent.putExtra("customerEmail", customerEmailGone.getText().toString());
        intent.putExtra("seller", sellerGone.getText().toString());
        intent.putExtra("productDescription", productDescriptionGone.getText().toString());

        intent.putExtra("collectWhen", collectWhenGone.getText().toString());
        intent.putExtra("collectDay", collectDayGone.getText().toString());
        intent.putExtra("firstHour", firstHourGone.getText().toString());
        intent.putExtra("secondHour", secondHourGone.getText().toString());
        intent.putExtra("amountMoney", amountMoneyGone.getText().toString());
        intent.putExtra("initialPay", initialPayGone.getText().toString());
        intent.putExtra("numberPayments", numberPaymentsGone.getText().toString());
        intent.putExtras(b);
        v.getContext().startActivity(intent);
    }
}
