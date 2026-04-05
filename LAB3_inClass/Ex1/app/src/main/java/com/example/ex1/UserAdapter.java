package com.example.ex1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class UserAdapter extends BaseAdapter {
    private Context context;
    private List<User> userList;
    private int layout;

    public UserAdapter(Context context, int layout, List<User> userList) {
        this.context = context;
        this.layout = layout;
        this.userList = userList;
    }

    @Override
    public int getCount() { return userList.size(); }

    @Override
    public Object getItem(int position) { return null; }

    @Override
    public long getItemId(int position) { return 0; }

    // Lớp ViewHolder
    private class ViewHolder {
        ImageView imgAvatar;
        TextView txtName;
        TextView txtCity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);

            holder = new ViewHolder();
            holder.imgAvatar = convertView.findViewById(R.id.imageViewAvatar);
            holder.txtName = convertView.findViewById(R.id.textViewName);
            holder.txtCity = convertView.findViewById(R.id.textViewCity);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Gán dữ liệu
        User user = userList.get(position);
        holder.txtName.setText(user.getName());
        holder.txtCity.setText("comes from " + user.getCity());
        holder.imgAvatar.setImageResource(user.getImageResId());

        return convertView;
    }
}