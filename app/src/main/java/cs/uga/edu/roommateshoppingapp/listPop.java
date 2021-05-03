package cs.uga.edu.roommateshoppingapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class listPop extends Activity {
    private String item;
    private int price;
    private Item listItem;
    private Item[] itemsList;
    private ArrayList<Item> theItems = new ArrayList<Item>();
    private ListView itemList = (ListView) findViewById(R.id.list_item_view);
    private TextView textView = (TextView) findViewById(R.id.list_view_title);
    private Button saveItem = (Button) findViewById(R.id.save_list_button);
    private ImageButton addButton = (ImageButton) findViewById(R.id.add_image_button);
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popwindow);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

         int width = displayMetrics.widthPixels;
         int height = displayMetrics.heightPixels;

         getWindow().setLayout((int) (width * .8), (int) (height * .6));

         EditText itemName = (EditText) findViewById(R.id.item_name);
         item = itemName.getText().toString().trim();
         EditText itemPrice = (EditText) findViewById(R.id.item_price);
         price = Integer.parseInt(itemPrice.getText().toString());

        saveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemList.setVisibility(View.VISIBLE);
                textView.setVisibility(View.INVISIBLE);
                saveItem.setVisibility(View.INVISIBLE);
                addButton.setVisibility(View.VISIBLE);

                listItem = new Item(item, price);

                theItems.add(listItem);
                arrayAdapter = new ArrayAdapter(listPop.this, android.R.layout.simple_list_item_1);
                itemList.setAdapter(arrayAdapter);

                itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(listPop.this, "clicked item: " + position + theItems.get(position).toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}
