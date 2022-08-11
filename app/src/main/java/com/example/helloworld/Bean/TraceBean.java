package com.example.helloworld.Bean;

import java.util.Date;
import java.util.List;

public class TraceBean {
    private int code;
    private String message;
    private List<Data> data;

    public class Data {
        private int id;
        private int feedBackId;
        private String desc;
        private Date time;
        private String imageUrl;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getFeedBackId() {
            return feedBackId;
        }

        public void setFeedBackId(int feedBackId) {
            this.feedBackId = feedBackId;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public Date getTime() {
            return time;
        }

        public void setTime(Date time) {
            this.time = time;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

}
