package com.example.android.flavor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
    Adapted from code written by Colette Kirwan. DCU Open Education
    Last Modified by Colm O'Sullivan on December 15th 2018
    JAVADOC comments added to the class and numerous methods
    Functionality for order delivery / collection to be added
 */

/**
 * This class controls the content for the activity_order.xml layout *
 */
public class OrderActivity extends AppCompatActivity
{
    Uri mPhotoURI;
    Spinner mSpinner;
    EditText mCustomerName;
    EditText meditOptional;
    Boolean noPictureTaken = true;
    String collectionOrDelivery;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 2;
    private static final String TAG = "Assign3";

    /**
     * OnCreate method is called on creation of the layout in the app
     * <p>  Sets the content of the activity_order.xml views.
     *      The method sets settings for the picture activity.
     *      Sets the layout and values of the spinner
     *      Sets the view of the customer name. </p>
     *
     * @param savedInstanceState restores the layout to a previous state using the data stored in this bundle if applicable
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        meditOptional = (EditText) findViewById(R.id.editOptional);

        meditOptional.setImeOptions(EditorInfo.IME_ACTION_DONE);
        meditOptional.setRawInputType(InputType.TYPE_CLASS_TEXT);
        //initialise spinner using the integer array
        mSpinner = (Spinner) findViewById(R.id.spinner);
        mCustomerName = (EditText) findViewById(R.id.editCustomer);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.ui_time_entries, R.layout.spinner_days);
        mSpinner.setAdapter(adapter);

    }

    /**
     *
     * @param v
     */
    public void dispatchTakePictureIntent(View v)
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String imageFileName = "my_tshirt_image_" + timeStamp + ".jpg";

        Log.i(TAG, "imagefile");

        File file = new File(Environment.getExternalStorageDirectory(), imageFileName);

        mPhotoURI = Uri.fromFile(file);
        Log.i(TAG, mPhotoURI.toString());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoURI);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
        //incase of caching if it comes from the activity stack, just a precaution
        intent.removeExtra(MediaStore.EXTRA_OUTPUT);

    }

    /**
     * Confirms to the user that a photo was taken successfully
     * <p>  This is used in conjunction with the startActivityForResult method call in dispatchTakePictureIntent.
     *      requestCode is used to ensure the correct onActivityResult is called.
     *      resultCode is used to verify if the activity was successful
     *      The code displays both a notification popup and a toast to the user informing them that the image was taken successfully</p>
     *
     * @param requestCode is the request code you passed to startActivityForResult()
     * @param resultCode either RESULT_OK if the operation was successful or RESULT_CANCELED if not completed successfully
     * @param data is the intent that carries the data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        //also can give user a message that everything went ok
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK)
        {
            noPictureTaken = false;
            //let user know that image saved
            //I have strings in strings.xml but have hardcoded here to copy/paste to students if needed
            CharSequence text = "Image Taken successfully";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(this, text, duration);
            toast.show();

            //or perhaps do a dialog should only use one method i.e. toast or dialog, but have both code here for demo purposes
            //also I have strings in strings.xml but have hardcoded here to copy/paste to students if needed
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Notification!").setMessage("Image saved successfully.").setPositiveButton("OK", null).show();
        }


    }

    /**
     * Returns the Email Body Message.
     * <p>  Email body message is created used prescription related data inputed from user
     *      If the editOptional EditText contains a string containing either "Collect" or "collect" the collection option is chosen
     *      Otherwise the delivery option is chosen using the contents of the string as the address</p>
     *
     * @return Email Body Message
     */
    private String createOrderSummary()
    {

        String orderMessage = getString(R.string.customer_name) + " " + mCustomerName.getText().toString();
        orderMessage += "\n" + "\n" + getString(R.string.order_message_1);
        String optionalInstructions = meditOptional.getText().toString();
        if (collectionOrDelivery.contains("Collect") || collectionOrDelivery.contains("collect")) {
            orderMessage += "\n" + getString(R.string.order_message_collect);
        } else {
            orderMessage += "\n" + getString(R.string.order_message_delivery) + " " + collectionOrDelivery + " in";
        }
        orderMessage += ((CharSequence) mSpinner.getSelectedItem()).toString() + " days";
        orderMessage += "\n" + getString(R.string.order_message_end) + "\n" + mCustomerName.getText().toString();
        return orderMessage;

        //update screen
    }

    /**
     * Sends the content of the email to the email app
     * <p>  First checks to ensure a customer name was entered and displays a toast if there is no name.
     *      Next it check to ensure a picture was taken and stored, if no picture is stored a toast is displayed to inform the user
     *      Finally checks to ensure delkivery or collection information has been entered, a toast appears if not
     *      If a customer name and delivery or collection information has been set and a picture is stored a ACTION_SEND intent is initialized.
     *      The receivers email and subject are preset.
     *      A photo is imported from a photo that the user has taken.
     *      The email content message is built up via the method createOrderSummary. </p>
     *
     * @param v is the view
     */
    public void sendEmail(View v)
    {

        //check that Name is not empty, and ask do they want to continue

        String customerName = mCustomerName.getText().toString();
        collectionOrDelivery = meditOptional.getText().toString();
        Log.i(TAG, collectionOrDelivery);
        if (customerName.matches("")) {
            Toast.makeText(this, getString(R.string.customer_name_blank), Toast.LENGTH_SHORT).show();

            /* we can also use a dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Notification!").setMessage("Customer Name not set.").setPositiveButton("OK", null).show();
            */
        } else if (noPictureTaken == false){
            Toast.makeText(this, getString(R.string.no_picture_taken), Toast.LENGTH_SHORT).show();
        } else if (collectionOrDelivery.matches("")){
            Toast.makeText(this, getString(R.string.select_delivery_or_collection), Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.to_email)});
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
            intent.putExtra(Intent.EXTRA_STREAM, mPhotoURI);
            intent.putExtra(Intent.EXTRA_TEXT, createOrderSummary());
            if (intent.resolveActivity(getPackageManager()) != null)
            {
                startActivity(intent);
            }
        }
    }
}
