package com.motaweron_apps.ektfaa.general_ui_method;

import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;


import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.model.ProductModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class GeneralMethod {

    @BindingAdapter("error")
    public static void errorValidation(View view, String error) {
        if (view instanceof EditText) {
            EditText ed = (EditText) view;
            ed.setError(error);
        } else if (view instanceof TextView) {
            TextView tv = (TextView) view;
            tv.setError(error);


        }
    }

    @BindingAdapter("image")
    public static void image(View view, String imageUrl) {
        if (view instanceof CircleImageView) {
            CircleImageView imageView = (CircleImageView) view;
            if (imageUrl != null) {

                Picasso.get().load(Uri.parse(imageUrl)).into(imageView);
            }
        } else if (view instanceof RoundedImageView) {
            RoundedImageView imageView = (RoundedImageView) view;

            if (imageUrl != null) {

                Picasso.get().load(Uri.parse(imageUrl)).into(imageView);
            }
        } else if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;

            if (imageUrl != null) {

                Picasso.get().load(Uri.parse(imageUrl)).into(imageView);
            }
        }

    }

    @BindingAdapter("user_image")
    public static void user_image(View view, String imageUrl) {
        if (view instanceof CircleImageView) {
            CircleImageView imageView = (CircleImageView) view;
            if (imageUrl != null) {
                Picasso.get().load(Uri.parse(imageUrl)).placeholder(R.drawable.circle_avatar).into(imageView);

            }

        } else if (view instanceof RoundedImageView) {
            RoundedImageView imageView = (RoundedImageView) view;

            if (imageUrl != null) {
                Picasso.get().load(Uri.parse(imageUrl)).placeholder(R.drawable.circle_avatar).into(imageView);

            }
        } else if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;

            if (imageUrl != null) {
                Picasso.get().load(Uri.parse(imageUrl)).placeholder(R.drawable.circle_avatar).into(imageView);

            }
        }

    }


    @BindingAdapter("offer")
    public static void offer(TextView textView, ProductModel model) {
        if (model != null) {

            if (model.getHave_offer().equals("yes")) {
                double offer_value = Double.parseDouble(model.getOffer_value());
                if (model.getOffer_type().equals("per")) {
                    double discount = (offer_value / 100.0) * Double.parseDouble(model.getPrice());
                    double price = Double.parseDouble(model.getPrice()) - discount;
                    textView.setText(String.valueOf(price));

                }
            } else {
                textView.setText(model.getPrice());
            }
        }
    }

    @BindingAdapter("status")
    public static void orderStatus(TextView textView, String s) {
        if (s.equals("new_order")) {
            textView.setText(R.string.new_order);
        } else if (s.equals("refuse_and_request_other_offer")) {
            textView.setText(R.string.less_offer);

        } else if (s.equals("driver_accept_order_and_make_offer")) {
            textView.setText(R.string.wait_client_response);

        } else if (s.equals("driver_deliveried_order_to_client")) {
            textView.setText(R.string.driver_at_client_loc);

        } else if (s.equals("client_rate_driver")) {
            textView.setText(R.string.driver_rated);

        }


    }


    @BindingAdapter("statusClient")
    public static void orderClientStatus(TextView textView, String s) {
        if (s.equals("new_order")) {
            textView.setText(R.string.wait_offer);
        } else if (s.equals("refuse_and_request_other_offer")) {
            textView.setText(R.string.wait_less_offer);

        } else if (s.equals("driver_accept_order_and_make_offer")) {
            textView.setText(R.string.new_offer);

        } else if (s.equals("driver_deliveried_order_to_client")) {
            textView.setText(R.string.driver_at_client_loc);

        } else if (s.equals("client_rate_driver")) {
            textView.setText(R.string.driver_rated);

        } else if (s.equals("driver_refuse_order")||s.equals("select_other_driver")) {
            textView.setText(R.string.refused);

        }


    }

    @BindingAdapter("refuseText")
    public static void refuseButtonText(Button btn, String s) {
        if (s.equals("select_other_driver")) {
            btn.setText(R.string.cancel);
        } else if (s.equals("driver_refuse_order")) {
            btn.setText(R.string.cancel);

        }else  {
            btn.setText(R.string.refuse);

        }


    }

    @BindingAdapter("createAt")
    public static void dateCreateAt(TextView textView, String s) {
        if (s != null) {
            try {
                String[] dates = s.split("T");
                textView.setText(dates[0]);
            } catch (Exception e) {

            }

        }

    }

    @BindingAdapter("chat_image")
    public static void chat_image(View view, String url) {
        if (view instanceof CircleImageView) {
            CircleImageView imageView = (CircleImageView) view;
            if (url != null) {
                Glide.with(view.getContext())
                        .load(Uri.parse(url))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(imageView);

            }


        } else if (view instanceof RoundedImageView) {
            RoundedImageView imageView = (RoundedImageView) view;
            if (url != null) {
                Glide.with(view.getContext())
                        .load(Uri.parse(url))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(imageView);
            }
        } else if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;
            if (url != null) {
                Glide.with(view.getContext())
                        .load(Uri.parse(url))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(imageView);
            }

        }

    }


}










