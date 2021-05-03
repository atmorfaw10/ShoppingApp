package cs.uga.edu.roommateshoppingapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListPop extends AppCompatActivity {
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText itemName, itemPrice;
    private Button addToList;
    private String item;
    private String price;
    private Item listItem;
    private Item[] itemsList;
    private ArrayList<Item> theItems = new ArrayList<Item>();
    private ListView itemList;
    private TextView textView;
    private Button saveItem;
    private ImageButton addButton;
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popwindow);


        saveItem = (Button) findViewById(R.id.save_list_button);
        EditText item = (EditText) findViewById(R.id.item_name);
        EditText price = (EditText) findViewById(R.id.item_price);

        String item_Name = item.toString().trim();
        int item_Price = Integer.parseInt(price.toString());

        listItem = new Item(item_Name, item_Price);

        TextView listView = (TextView) findViewById(R.id.list_text_view);
        listView.setText("Name: " + listItem.getName() + "Price: " + listItem.getPrice());
    }


    public void createNewDialoge() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View shoppingListView = getLayoutInflater().inflate(R.layout.popwindow, null);
        itemName = (EditText) shoppingListView.findViewById(R.id.item_name);
        itemPrice = (EditText) shoppingListView.findViewById(R.id.item_price);
        item = itemName.getText().toString().trim();
        price = itemPrice.getText().toString().trim();

      //  saveItem = (Button) findViewById(R.id.save_list_button);

        addToList = (Button) shoppingListView.findViewById(R.id.save_list_button);
      //  addButton = (ImageButton) findViewById(R.id.add_image_button);

        dialogBuilder.setView(shoppingListView);
        dialog = dialogBuilder.create();
        dialog.show();

      //  itemList = (ListView) findViewById(R.id.list_item_view);
     //   textView = (TextView) findViewById(R.id.list_view_title);

        addToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // itemList.setVisibility(View.VISIBLE);
                textView.setVisibility(View.INVISIBLE);
                saveItem.setVisibility(View.INVISIBLE);
                addButton.setVisibility(View.VISIBLE);

                listItem = new Item(item, Integer.parseInt(price));

                theItems.add(listItem);
                arrayAdapter = new ArrayAdapter(ListPop.this, android.R.layout.simple_list_item_1);
                itemList.setAdapter(arrayAdapter);

                /*
                itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(ListPop.this, "clicked item: " + position + theItems.get(position).toString(), Toast.LENGTH_SHORT).show();
                    }
                });
                 */
            }
        });
    }
}