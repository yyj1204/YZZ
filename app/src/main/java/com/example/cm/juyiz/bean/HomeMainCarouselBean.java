package com.example.cm.juyiz.bean;
import java.io.Serializable;
/**
 * 商城首页 --- 顶部首页类型的轮播图
 */
public class HomeMainCarouselBean implements Serializable {
        private String img_url;
        private String link_type;
        private int link_id;

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getLink_type() {
            return link_type;
        }

        public void setLink_type(String link_type) {
            this.link_type = link_type;
        }

        public int getLink_id() {
            return link_id;
        }

        public void setLink_id(int link_id) {
            this.link_id = link_id;
        }
}
