package cs.uga.edu.roommateshoppingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;


public class FeatureAdapter extends ArrayAdapter<AddedFeatures> {

    public FeatureAdapter(Context context, ArrayList<AddedFeatures> buttonList) {
        super(context, 0, buttonList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent) {
        return init(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @Nullable ViewGroup parent) {
        return init(position, convertView, parent);
    }

    private View init(int position, @Nullable View convertView, @Nullable ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.add_spinner_row, parent, false); 
        }
        ImageView buttonViewFlag = convertView.findViewById(R.id.add_button);
        TextView textViewName = convertView.findViewById(R.id.spinner_text_view);

        AddedFeatures addedFeature = getItem(position);

        if(addedFeature != null) {
            buttonViewFlag.setImageResource(addedFeature.getButtonImage());
            textViewName.setText(addedFeature.getButtonName());
        }
        return convertView;
    }
}
