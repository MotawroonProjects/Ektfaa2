package com.motaweron_apps.ektfaa.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_chat.ChatActivity;
import com.motaweron_apps.ektfaa.databinding.ChatImageLeftRowBinding;
import com.motaweron_apps.ektfaa.databinding.ChatImageRightRowBinding;
import com.motaweron_apps.ektfaa.databinding.ChatLocationLeftBinding;
import com.motaweron_apps.ektfaa.databinding.ChatLocationRightBinding;
import com.motaweron_apps.ektfaa.databinding.ChatMsgLeftBinding;
import com.motaweron_apps.ektfaa.databinding.ChatMsgRightBinding;
import com.motaweron_apps.ektfaa.model.OrderChatModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int msg_left = 1;
    private final int msg_right = 2;
    private final int img_left = 3;
    private final int img_right = 4;
    private final int location_left = 5;
    private final int location_right = 6;
    private LayoutInflater inflater;
    private List<OrderChatModel.OrderChatMessage> list;
    private Context context;
    private String current_user_id;
    private ChatActivity activity;



    public ChatAdapter(List<OrderChatModel.OrderChatMessage> list, Context context, String current_user_id) {
        this.list = list;
        this.context = context;
        this.current_user_id = current_user_id;
        inflater = LayoutInflater.from(context);
        activity = (ChatActivity) context;



    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == msg_left) {
            ChatMsgLeftBinding binding = DataBindingUtil.inflate(inflater, R.layout.chat_msg_left, parent, false);
            return new HolderMsgLeft(binding);
        } else if (viewType == msg_right) {
            ChatMsgRightBinding binding = DataBindingUtil.inflate(inflater, R.layout.chat_msg_right, parent, false);
            return new HolderMsgRight(binding);
        } else if (viewType == img_left) {
            ChatImageLeftRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.chat_image_left_row, parent, false);
            return new HolderImageLeft(binding);
        } else if (viewType == img_right) {
            ChatImageRightRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.chat_image_right_row, parent, false);
            return new HolderImageRight(binding);
        }  else if (viewType == location_left){
            ChatLocationLeftBinding binding = DataBindingUtil.inflate(inflater, R.layout.chat_location_left, parent, false);
            return new LocationLeftHolder(binding);
        }else {
            ChatLocationRightBinding binding = DataBindingUtil.inflate(inflater, R.layout.chat_location_right, parent, false);
            return new LocationRightHolder(binding);
        }




    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        OrderChatModel.OrderChatMessage model = list.get(position);





        ///////////////////



        if (holder instanceof HolderMsgLeft) {
            HolderMsgLeft holderMsgLeft = (HolderMsgLeft) holder;
            holderMsgLeft.binding.setModel(model);
            holderMsgLeft.binding.tvMessageDate.setText(getTime(Long.parseLong(model.getMessage_time())*1000));

            if (current_user_id.equals(model.getFrom_user().getId())){
               showUserImage(holderMsgLeft.binding.image,model.getTo_user().getLogo());
            }else {
                showUserImage(holderMsgLeft.binding.image,model.getFrom_user().getLogo());

            }

        } else if (holder instanceof HolderMsgRight) {
            HolderMsgRight holderMsgRight = (HolderMsgRight) holder;
            holderMsgRight.binding.setModel(model);
            holderMsgRight.binding.tvMessageDate.setText(getTime(Long.parseLong(model.getMessage_time())*1000));


        } else if (holder instanceof HolderImageLeft) {
            HolderImageLeft holderImageLeft = (HolderImageLeft) holder;
            holderImageLeft.binding.setModel(model);
            holderImageLeft.binding.tvTime.setText(getTime(Long.parseLong(model.getMessage_time())*1000));
            if (current_user_id.equals(model.getFrom_user().getId())){
                showUserImage(holderImageLeft.binding.userImage,model.getTo_user().getLogo());
            }else {
                showUserImage(holderImageLeft.binding.userImage,model.getFrom_user().getLogo());

            }
            holderImageLeft.itemView.setOnClickListener(v -> {
                OrderChatModel.OrderChatMessage messageModel = list.get(holderImageLeft.getAdapterPosition());
                String url = messageModel.getImage();
                //activity.openImage(url,holderImageLeft.binding.image);
            });


        } else if (holder instanceof HolderImageRight) {
            HolderImageRight holderImageRight = (HolderImageRight) holder;
            holderImageRight.binding.setModel(model);
            holderImageRight.binding.tvTime.setText(getTime(Long.parseLong(model.getMessage_time())*1000));

            holderImageRight.itemView.setOnClickListener(v -> {
                OrderChatModel.OrderChatMessage messageModel = list.get(holderImageRight.getAdapterPosition());
                String url = messageModel.getImage();
                //activity.openImage(url,holderImageRight.binding.image);
            });



        }else if (holder instanceof LocationLeftHolder) {
            LocationLeftHolder locationLeftHolder = (LocationLeftHolder) holder;
            locationLeftHolder.binding.setModel(model);
            locationLeftHolder.binding.tvMessageDate.setText(getTime(Long.parseLong(model.getMessage_time())*1000));
            if (current_user_id.equals(model.getFrom_user().getId())){
                showUserImage(locationLeftHolder.binding.userImage,model.getTo_user().getLogo());
            }else {
                showUserImage(locationLeftHolder.binding.userImage,model.getFrom_user().getLogo());

            }
            locationLeftHolder.itemView.setOnClickListener(v -> {
                OrderChatModel.OrderChatMessage model2 = list.get(locationLeftHolder.getAdapterPosition());
                activity.setLocationItem(model2);

            });



        }else if (holder instanceof LocationRightHolder) {
            LocationRightHolder locationRightHolder = (LocationRightHolder) holder;
            locationRightHolder.binding.setModel(model);
            locationRightHolder.binding.tvMessageDate.setText(getTime(Long.parseLong(model.getMessage_time())*1000));

            locationRightHolder.itemView.setOnClickListener(v -> {
                OrderChatModel.OrderChatMessage model2 = list.get(locationRightHolder.getAdapterPosition());
                activity.setLocationItem(model2);

            });




        }



    }

    private void showUserImage(ImageView imageView,String url){
        Glide.with(context)
                .load(Uri.parse(url))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private String getTime(long time) {
        return new SimpleDateFormat("hh:mm aa", Locale.ENGLISH).format(new Date(time));
    }


    public static class HolderMsgLeft extends RecyclerView.ViewHolder {
        private ChatMsgLeftBinding binding;

        public HolderMsgLeft(@NonNull ChatMsgLeftBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public static  class HolderMsgRight extends RecyclerView.ViewHolder {
        private ChatMsgRightBinding binding;

        public HolderMsgRight(@NonNull ChatMsgRightBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public static class HolderImageLeft extends RecyclerView.ViewHolder {
        private ChatImageLeftRowBinding binding;

        public HolderImageLeft(@NonNull ChatImageLeftRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public static class HolderImageRight extends RecyclerView.ViewHolder {
        private ChatImageRightRowBinding binding;

        public HolderImageRight(@NonNull ChatImageRightRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }




    public static class LocationRightHolder extends RecyclerView.ViewHolder {

        private ChatLocationRightBinding binding;


        public LocationRightHolder(ChatLocationRightBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }




    }

    public static class LocationLeftHolder extends RecyclerView.ViewHolder {
        private ChatLocationLeftBinding binding;


        public LocationLeftHolder(ChatLocationLeftBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }











    }


    @Override
    public int getItemViewType(int position) {

        OrderChatModel.OrderChatMessage messageModel = list.get(position);

        if (messageModel.getType().equals("message")){

            if (messageModel.getFrom_user().getId().equals(current_user_id)){

                return msg_right;
            }else {
                return msg_left;
            }
        }else if (messageModel.getType().equals("image")){

            if (messageModel.getFrom_user().getId().equals(current_user_id)){

                return img_right;
            }else {
                return img_left;
            }
        }else if (messageModel.getType().equals("location")){

            if (messageModel.getFrom_user().getId().equals(current_user_id)){

                return location_right;
            }else {
                return location_left;
            }
        }else {

            return 0;
        }


    }


    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        OrderChatModel.OrderChatMessage model = list.get(holder.getAdapterPosition());

        if (holder instanceof HolderImageLeft) {
            HolderImageLeft holderImageLeft = (HolderImageLeft) holder;
            holderImageLeft.binding.setModel(model);
            holderImageLeft.binding.tvTime.setText(getTime(Long.parseLong(model.getMessage_time())*1000));
            holderImageLeft.itemView.setOnClickListener(v -> {
                OrderChatModel.OrderChatMessage messageModel = list.get(holderImageLeft.getAdapterPosition());
                String url = messageModel.getImage();
                //activity.openImage(url,holderImageLeft.binding.image);
            });


        } else if (holder instanceof HolderImageRight) {
            HolderImageRight holderImageRight = (HolderImageRight) holder;
            holderImageRight.binding.setModel(model);
            holderImageRight.binding.tvTime.setText(getTime(Long.parseLong(model.getMessage_time())*1000));

            holderImageRight.itemView.setOnClickListener(v -> {
                OrderChatModel.OrderChatMessage messageModel = list.get(holderImageRight.getAdapterPosition());
                String url = messageModel.getImage();
                //activity.openImage(url,holderImageRight.binding.image);
            });
    }
}

}
