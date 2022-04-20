package com.motaweron_apps.ektfaa.model;

import java.io.Serializable;
import java.util.List;

public class DepartmentModel implements Serializable {
    private String id="";
    private String user_id;
    private String basic_department_id;
    private String parent_id;
    private String level;
    private String created_at;
    private String updated_at;
    private BasicDepartmentFk basic_department_fk;

    public String getId() {
        return id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getBasic_department_id() {
        return basic_department_id;
    }

    public String getParent_id() {
        return parent_id;
    }

    public String getLevel() {
        return level;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public BasicDepartmentFk getBasic_department_fk() {
        return basic_department_fk;
    }



    public static class BasicDepartmentFk implements Serializable {
        private String id;
        private String title;
        private String image;
        private String parent_id;
        private String level;
        private String created_at;
        private String updated_at;
        private List<BasicDepartmentFk> sub_department;
        private List<ProductModel>department_foods_data;

        private boolean selected = false;


        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getImage() {
            return image;
        }

        public String getParent_id() {
            return parent_id;
        }

        public String getLevel() {
            return level;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public List<BasicDepartmentFk> getSub_department() {
            return sub_department;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public List<ProductModel> getDepartment_foods_data() {
            return department_foods_data;
        }
    }
}
