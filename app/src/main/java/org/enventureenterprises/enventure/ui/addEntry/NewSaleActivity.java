package org.enventureenterprises.enventure.ui.addEntry;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.reginald.editspinner.EditSpinner;

import org.enventureenterprises.enventure.R;
import org.enventureenterprises.enventure.data.model.Entry;
import org.enventureenterprises.enventure.data.model.Item;
import org.enventureenterprises.enventure.data.remote.EnventureApi;
import org.enventureenterprises.enventure.ui.base.BaseActivity;
import org.enventureenterprises.enventure.ui.general.HomeActivity;
import org.enventureenterprises.enventure.ui.general.ProgressDialogFragment;
import org.enventureenterprises.enventure.util.rx.Transformers;
import org.joda.time.DateTime;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

import static java.lang.Double.parseDouble;

/**
 * Created by mossplix on 7/8/17.
 */

public class NewSaleActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {
    Realm realm;
    private Long item_id;
    private String item_name;
    private String paymentType;
    private static Item item;
    private ProgressDialogFragment progressFragment;
    private PhoneNumberUtil phoneUtil ;
    @Inject
    EnventureApi client;
    

    @BindView(R.id.quantity)
    EditText quantityEditText;

    @BindView(R.id.amount_paid)
    EditText amountPaidEditText;

    @BindView(R.id.total_price)
    EditText totalPriceEditText;

    @BindView(R.id.totalcost)
    EditText totalcostEditText;

    @BindView(R.id.product)
    EditText nameEditText;

    @BindView(R.id.amount_paying)
    EditText amountPayingEditText;

    @BindView(R.id.amount_remaining)
    EditText amountRemainingEditText;

    @BindView(R.id.phone)
    EditText phoneEditText;


    @BindView(R.id.phone_layout)
    TextInputLayout phoneLayout;

    @BindView(R.id.amount_paid_layout)
    TextInputLayout amountPaidLayout;

    @BindView(R.id.total_price_layout)
    TextInputLayout totalPriceLayout;

    @BindView(R.id.amount_paying_layout)
    TextInputLayout amountPayingLayout;

    @BindView(R.id.amount_remaining_layout)
    TextInputLayout amountRemainingLayout;


    @BindView(R.id.quantity_layout)
    TextInputLayout quantityLayout;

    @BindView(R.id.totalcost_layout)
    TextInputLayout totalcostLayout;

    @BindView(R.id.installment_mobile)
    EditSpinner mobileNumbers;

    @BindView(R.id.installment_mobile_layout)
    TextInputLayout mobileNumbersLayout;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);



        setContentView(R.layout.new_sale_activity);
        ButterKnife.bind(this);


        realm = getRealm();
        phoneUtil = PhoneNumberUtil.getInstance();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                item_name = extras.getString("name");

            }

        } else {
            item_name = savedInstanceState.getString("name");
        }

        nameEditText.setText(item_name);

        Spinner spinner = (Spinner) findViewById(R.id.cash_spinner);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.entry_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(NewSaleActivity.this);

        item = realm.where(Item.class).equalTo ("name",item_name).findFirst ();

        quantityEditText.addTextChangedListener(new FormTextWatcher(quantityEditText));
        totalcostEditText.addTextChangedListener(new FormTextWatcher(totalcostEditText));
        amountPayingEditText.addTextChangedListener(new FormTextWatcher(amountPayingEditText));
        amountRemainingEditText.addTextChangedListener(new FormTextWatcher(amountRemainingEditText));
        phoneEditText.addTextChangedListener(new FormTextWatcher(phoneEditText));
        amountPaidEditText.addTextChangedListener(new FormTextWatcher(amountPaidEditText));
        amountPaidEditText.addTextChangedListener(new FormTextWatcher(amountPaidEditText));
        totalPriceEditText.addTextChangedListener(new FormTextWatcher(totalPriceEditText));

    }


    private class FormTextWatcher implements TextWatcher {

        private View view;

        private FormTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.quantity:
                    validateQuantity ();
                    break;
                case R.id.total_cost:
                    validateTotalCost ();
                    break;
                case R.id.amount_paying:
                    validateAmountPaying ();
                    break;
                case R.id.phone:
                    validatePhone ();
                    break;
                case R.id.amount_paid:
                    validateAmountPaid();
                    break;
                case R.id.total_price:
                    validateTotalPrice();
                    break;

            }
        }
    }


    private void startProgress() {
        progressFragment= ProgressDialogFragment.newInstance ("creating Sale");
        progressFragment.show(getSupportFragmentManager(), "progress");
    }

    private void finishProgress() {
        progressFragment.dismiss();
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_save, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem nItem) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = nItem.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id){
            case R.id.save:

                if (!validateQuantity()) {
                    return false;
                }

                if (!validatePhone()) {
                    return false;
                }


                if (!validateAmountPaying()) {
                    return false;
                }

                if (! validateTotalCost()) {
                    return false;
                }

                if (!validateTotalPrice())
                {
                    return false;
                }


                if(!validateAmountPaid())
                {
                    return false;
                }

                Double amount = 0.0;
                String customer_mobile = "";
                Double amount_paid = 0.0;
                Double total_cost = 0.0;
                int quantity = 0;



                switch (paymentType.toString()) {

                    case "Installment Addon":
                        //amount = parseDouble(amountPayingEditText.getText().toString());
                        amount = _parseDouble(amountPayingEditText.getText().toString());
                        total_cost = amount;
                        customer_mobile = mobileNumbers.getText().toString();
                        amount_paid = _parseDouble(amountPayingEditText.getText().toString());



                        break;
                    case "Cash":

                        amount = _parseDouble(totalcostEditText.getText().toString());
                        total_cost = amount;
                        customer_mobile = "";
                        amount_paid = amount;
                        quantity = Integer.parseInt(quantityEditText.getText().toString());
                        break;
                    case "Installment":
                        //create account with phone number and add as entry
                        //amount = parseDouble(amountPaidEditText.getText().toString());
                        amount = _parseDouble(amountPaidEditText.getText().toString());
                        total_cost = _parseDouble(totalPriceEditText.getText().toString());
                        amount_paid =  _parseDouble(amountPaidEditText.getText().toString());
                        quantity = Integer.parseInt(quantityEditText.getText().toString());
                        try {

                            Phonenumber.PhoneNumber ugNumberProto = phoneUtil.parse(phoneEditText.getText().toString(), "UG");
                            customer_mobile = "0"+ugNumberProto.getNationalNumber();
                        }   catch (Exception e) {
                            return false;
                        }



                        break;
                    default:
                        //amount = parseDouble(totalcostEditText.getText().toString());

                        break;
                }

                realm = getRealm();
                DateTime d = new DateTime();


                String qty = quantityEditText.getText().toString().trim();

                //Entry.newSale(item, d, paymentType,Integer.parseInt(quantityEditText.getText().toString()),amount,phoneEditText.getText().toString(),Double.parseDouble(totalcostEditText.getText().toString()),getRealm());
                Entry entry = Entry.newSale(item, d, paymentType,quantity,amount,customer_mobile,total_cost,amount_paid,realm);
                client.createEntry(entry)
                    .compose(Transformers.neverError()).subscribe();


                final Intent intent = new Intent(NewSaleActivity.this, HomeActivity.class);
                intent.putExtra("navTab", HomeActivity.SALES);
                Toast.makeText(
                        NewSaleActivity.this,
                        "Sale Added Successfully",
                        Toast.LENGTH_LONG)
                        .show();
                startActivityWithTransition(intent, R.anim.slide_in_right, R.anim.fade_out_slide_out_left);

                break;
            case android.R.id.home:
                // this takes the user 'back', as if they pressed the left-facing triangle icon on the main android toolbar.
                // if this doesn't work as desired, another possibility is to call `finish()` here.
                this.onBackPressed();
                break;


        }

        return super.onOptionsItemSelected(nItem);
    }


    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        paymentType = parent.getItemAtPosition(pos).toString();

        switch (paymentType.toString()) {

            case "Installment Addon":
                isInstallmentAddon();
                break;
            case "Cash":
                isCash();
                break;
            case "Installment":
                isInstallment();

                break;
            default:
                isCash();
                break;
        }





    }


    public void isInstallmentAddon(){
        totalPriceLayout.setVisibility(View.GONE);
        totalPriceEditText.setVisibility(View.GONE);
        amountPaidLayout.setVisibility(View.GONE);
        amountPaidEditText.setVisibility(View.GONE);
        quantityEditText.setVisibility(View.GONE);
        totalcostEditText.setVisibility(View.GONE);
        amountPayingEditText.setVisibility(View.VISIBLE);
        amountRemainingEditText.setEnabled(false);
        phoneEditText.setVisibility(View.GONE);
        phoneLayout.setVisibility(View.GONE);
        amountPayingLayout.setVisibility(View.VISIBLE);
        amountRemainingLayout.setVisibility(View.VISIBLE);
        quantityLayout.setVisibility(View.GONE);
        totalcostLayout.setVisibility(View.GONE);
        mobileNumbers.setVisibility(View.VISIBLE);
        mobileNumbersLayout.setVisibility(View.VISIBLE);
        amountRemainingEditText.setVisibility(View.VISIBLE);

        ArrayList<String> mobiles = item.getCustomerNumbers();



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, mobiles);

        mobileNumbers.setAdapter(adapter);

    }

    public void  isCash(){
        totalPriceLayout.setVisibility(View.GONE);
        totalPriceEditText.setVisibility(View.GONE);
        amountPaidLayout.setVisibility(View.GONE);
        amountPaidEditText.setVisibility(View.GONE);
        quantityEditText.setVisibility(View.VISIBLE);
        totalcostEditText.setVisibility(View.VISIBLE);
        amountPayingEditText.setVisibility(View.GONE);
        amountRemainingEditText.setVisibility(View.GONE);
        phoneEditText.setVisibility(View.GONE);
        phoneLayout.setVisibility(View.GONE);
        amountPayingLayout.setVisibility(View.GONE);
        amountRemainingLayout.setVisibility(View.GONE);
        quantityLayout.setVisibility(View.VISIBLE);
        totalcostLayout.setVisibility(View.VISIBLE);
        mobileNumbers.setVisibility(View.GONE);

    }

    public void isInstallment(){
        quantityEditText.setVisibility(View.VISIBLE);
        quantityLayout.setVisibility(View.VISIBLE);
        totalPriceLayout.setVisibility(View.VISIBLE);
        totalPriceEditText.setVisibility(View.VISIBLE);
        amountPaidLayout.setVisibility(View.VISIBLE);
        amountPaidEditText.setVisibility(View.VISIBLE);
        amountPayingEditText.setVisibility(View.GONE);
        amountRemainingEditText.setVisibility(View.GONE);
        phoneEditText.setVisibility(View.VISIBLE);
        phoneLayout.setVisibility(View.VISIBLE);
        amountPayingLayout.setVisibility(View.GONE);
        amountRemainingLayout.setVisibility(View.GONE);
        totalcostEditText.setVisibility(View.GONE);
        totalcostLayout.setVisibility(View.GONE);
        mobileNumbers.setVisibility(View.GONE);


    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }





    private boolean validateQuantity() {
        if(paymentType.toString().equals("Cash") || paymentType.toString().equals("Installment")) {
            String quantity = quantityEditText.getText().toString();
            if (quantity.trim().isEmpty()) {
                quantityLayout.setErrorEnabled(true);
                quantityEditText.setError("Quantity is a required Field");
                requestFocus(quantityEditText);
                return false;
            } else if (_parseDouble(quantity) == Double.NaN) {
                quantityLayout.setErrorEnabled(true);
                quantityEditText.setError("Invalid entry. Enter numbers only");
                requestFocus(quantityEditText);
                return false;
            } else {


                Long items_stocked = item.getInventories().where().sum("quantity").longValue();
                Long items_sold  = item.getSales().where().sum("quantity").longValue();
                Long quantity_in_stock =  items_stocked - items_sold;
                Double val = _parseDouble(quantity);

                String error_message = "";

                if(val > quantity_in_stock){

                    error_message = "You do not have enough items in stock";
                }
                else if(val <0 ) {
                    error_message = "Enter a positive number";

                }

                else{
                    quantityLayout.setErrorEnabled(false);
                    return true;

                }

                quantityLayout.setErrorEnabled(true);
                quantityEditText.setError(error_message);
                requestFocus(quantityEditText);


            }
            quantityLayout.setErrorEnabled(false);
            return true;
        }else{
            return true;
        }
    }

    private boolean validateTotalCost() {
        Boolean error = false;
        String error_message= "";
        if(paymentType.toString().equals("Cash")) {
            String totalcost = totalcostEditText.getText().toString();
            if (totalcostEditText.getText().toString().trim().isEmpty()) {
                error = true;
                error_message = "Total Cost is a required Field";
            } else if (_parseDouble(totalcost) == Double.NaN) {
                error = true;
                error_message = "Invalid entry. Enter numbers only";


            } else {
                if (_parseDouble(totalcost) < 100){
                    error=true;
                    error_message = "Price has to be greater than 100";

                }

            }



        }
        else{
            error = false;
        }

        if (error == true){
            totalcostLayout.setErrorEnabled(true);
            totalcostEditText.setError(error_message);
            requestFocus(totalcostEditText);
            return false;

        }
        else{
            totalcostLayout.setErrorEnabled(false);

            return true;

        }
    }


    private boolean validateTotalPrice()
    {
        if(paymentType.toString().equals("Installment")) {
            String totalprice = totalPriceEditText.getText().toString();
            if (totalPriceEditText.getText().toString().trim().isEmpty()) {
                totalPriceLayout.setErrorEnabled(true);
                totalPriceEditText.setError("Total Price is a required Field");
                requestFocus(totalPriceEditText);
                return false;
            } else if (_parseDouble(totalprice) == Double.NaN) {
                totalPriceLayout.setErrorEnabled(true);
                totalPriceEditText.setError("Invalid entry. Enter numbers only");
                requestFocus(totalPriceEditText);
                return false;

            } else {
                totalPriceLayout.setErrorEnabled(false);
            }

            return true;
        }else{
            return true;
        }

    }

    private boolean validateAmountPaid(){
        String amuntPaid = amountPaidEditText.getText().toString().trim();
        if(paymentType.toString().equals("Installment")) {

            if (amuntPaid.isEmpty()) {
                amountPaidLayout.setErrorEnabled(true);
                amountPaidEditText.setError("Amount Paid is a required Field");
                requestFocus(amountPaidEditText);
                return false;
            } else if (_parseDouble(amuntPaid) == Double.NaN) {
                amountPaidLayout.setErrorEnabled(true);
                amountPaidEditText.setError("Invalid entry. Enter numbers only");
                requestFocus(amountPaidEditText);
                return false;

            } else {
                amountPaidLayout.setErrorEnabled(false);
            }

            return true;
        }else{
            return true;
        }

    }

    private boolean validateAmountPaying (){
        if(paymentType.toString().equals("Installment Addon")) {

            String amountPaying = amountPayingEditText.getText().toString();
            if (amountPayingEditText.getText().toString().trim().isEmpty()) {
                amountPayingLayout.setErrorEnabled(true);
                amountPayingEditText.setError("Total Cost is a required Field");
                requestFocus(amountPayingEditText);
                return false;
            } else if (_parseDouble(amountPaying) == Double.NaN) {
                amountPayingLayout.setErrorEnabled(true);
                amountPayingEditText.setError("Invalid entry. Enter numbers only");
                requestFocus(amountPayingEditText);
                return false;

            } else {
                amountPayingLayout.setErrorEnabled(false);
                Double rt = 0.0;
                String mobile = mobileNumbers.getText().toString();
                if(!mobile.isEmpty()) {
                    Double balance = item.getDebtors().where().equalTo("name", mobile).findFirst().getBalance();
                    Double am = _parseDouble(amountPayingEditText.getText().toString());
                    rt = balance - am;
                }

                amountRemainingEditText.setText(rt.toString());
            }

            return true;
        }else{
            return true;
        }

    }


    private boolean validatePhone (){
        if( paymentType.toString().equals("Installment")) {


            String phone = phoneEditText.getText().toString();
            if (phone.trim().isEmpty()) {
                phoneLayout.setErrorEnabled(true);
                phoneEditText.setError("The Customer phone  is required");
                requestFocus(phoneEditText);
                return false;

            } else {

                try {
                    Phonenumber.PhoneNumber ugNumberProto = phoneUtil.parse(phone, "UG");
                    if (Long.toString(ugNumberProto.getNationalNumber()).length()>=9 ) {

                        if (phoneUtil.isValidNumber(ugNumberProto)) {
                            phoneLayout.setErrorEnabled(false);
                            return true;
                        } else {
                            phoneLayout.setErrorEnabled(true);
                            phoneEditText.setError("Please enter a valid phone number");
                            requestFocus(phoneEditText);
                            return false;

                        }
                    }
                } catch (Exception e) {

                    phoneLayout.setErrorEnabled(true);
                    phoneEditText.setError("Please enter a valid phone number");
                    requestFocus(phoneEditText);
                    return false;
                }


            }



        }


        phoneLayout.setErrorEnabled(false);

        return true;

    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private Double _parseDouble(String value){
        Double toret = 0.0;
        if( value != null || !value.trim().isEmpty())
        {
            try {
                toret = parseDouble(value);
            }catch (Exception e)
            {

            }
        }

        return toret;
    }


}
