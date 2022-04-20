package com.motaweron_apps.ektfaa.model;

import java.io.Serializable;

public class SettingModel implements Serializable {
    private Setting data;
    private int status;

    public Setting getData() {
        return data;
    }

    public int getStatus() {
        return status;
    }

    public static class Setting implements Serializable {
        private int id;
        private String header_logo;
        private String footer_logo;
        private String login_banner;
        private String image_slider;
        private String title;
        private String desc;
        private String footer_desc;
        private String company_profile_pdf;
        private String address1;
        private String address2;
        private String phone1;
        private String phone2;
        private String fax;
        private String android_app;
        private String ios_app;
        private String email1;
        private String email2;
        private String link;
        private double latitude;
        private double longitude;
        private String address;
        private String sms_user_name;
        private String sms_user_pass;
        private String sms_sender;
        private String publisher;
        private String default_language;
        private String default_theme;
        private String advertisement_price;
        private String offer_muted;
        private double offer_notification;
        private String facebook;
        private String twitter;
        private String instagram;
        private String linkedin;
        private String telegram;
        private String youtube;
        private String google_plus;
        private String snapchat_ghost;
        private String whatsapp;
        private String about_app;
        private String ar_termis_condition;
        private String en_termis_condition;
        private double site_commission;
        private double debt_limit;
        private String created_at;
        private String updated_at;
        private String about_link;
        private String terms_link;

        public int getId() {
            return id;
        }

        public String getHeader_logo() {
            return header_logo;
        }

        public String getFooter_logo() {
            return footer_logo;
        }

        public String getLogin_banner() {
            return login_banner;
        }

        public String getImage_slider() {
            return image_slider;
        }

        public String getTitle() {
            return title;
        }

        public String getDesc() {
            return desc;
        }

        public String getFooter_desc() {
            return footer_desc;
        }

        public String getCompany_profile_pdf() {
            return company_profile_pdf;
        }

        public String getAddress1() {
            return address1;
        }

        public String getAddress2() {
            return address2;
        }

        public String getPhone1() {
            return phone1;
        }

        public String getPhone2() {
            return phone2;
        }

        public String getFax() {
            return fax;
        }

        public String getAndroid_app() {
            return android_app;
        }

        public String getIos_app() {
            return ios_app;
        }

        public String getEmail1() {
            return email1;
        }

        public String getEmail2() {
            return email2;
        }

        public String getLink() {
            return link;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public String getAddress() {
            return address;
        }

        public String getSms_user_name() {
            return sms_user_name;
        }

        public String getSms_user_pass() {
            return sms_user_pass;
        }

        public String getSms_sender() {
            return sms_sender;
        }

        public String getPublisher() {
            return publisher;
        }

        public String getDefault_language() {
            return default_language;
        }

        public String getDefault_theme() {
            return default_theme;
        }

        public String getOffer_muted() {
            return offer_muted;
        }

        public double getOffer_notification() {
            return offer_notification;
        }

        public String getFacebook() {
            return facebook;
        }

        public String getTwitter() {
            return twitter;
        }

        public String getInstagram() {
            return instagram;
        }

        public String getLinkedin() {
            return linkedin;
        }

        public String getTelegram() {
            return telegram;
        }

        public String getYoutube() {
            return youtube;
        }

        public String getGoogle_plus() {
            return google_plus;
        }

        public String getSnapchat_ghost() {
            return snapchat_ghost;
        }

        public String getWhatsapp() {
            return whatsapp;
        }

        public String getAbout_app() {
            return about_app;
        }

        public String getAr_termis_condition() {
            return ar_termis_condition;
        }

        public String getEn_termis_condition() {
            return en_termis_condition;
        }

        public double getSite_commission() {
            return site_commission;
        }

        public double getDebt_limit() {
            return debt_limit;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public String getAbout_link() {
            return about_link;
        }

        public String getTerms_link() {
            return terms_link;
        }

        public String getAdvertisement_price() {
            return advertisement_price;
        }
    }
}
