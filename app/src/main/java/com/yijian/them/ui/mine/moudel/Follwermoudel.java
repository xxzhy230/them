package com.yijian.them.ui.mine.moudel;

import java.util.List;

public class Follwermoudel {

    /**
     * code : 200
     * data : [{"birthday":"1979-09-03","followed":true,"gender":"1","nickName":"瑞克","realImg":"http://image.themapp.cn/0d73e26d-3d75-4a4f-9841-8b09f2805b9a201908071727530.jpg","sign":null,"userId":24},{"birthday":"2019-08-08","followed":true,"gender":"1","nickName":"木南","realImg":"http://image.themapp.cn/ef45eb38-eae8-4e20-b277-827d1be3697a201907251634090.jpg","sign":null,"userId":17}]
     * msg : success
     */

    private int code;
    private String msg;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * birthday : 1979-09-03
         * followed : true
         * gender : 1
         * nickName : 瑞克
         * realImg : http://image.themapp.cn/0d73e26d-3d75-4a4f-9841-8b09f2805b9a201908071727530.jpg
         * sign : null
         * userId : 24
         */

        private String birthday;
        private boolean followed;
        private String gender;
        private String nickName;
        private String realImg;
        private Object sign;
        private int userId;

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public boolean isFollowed() {
            return followed;
        }

        public void setFollowed(boolean followed) {
            this.followed = followed;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getRealImg() {
            return realImg;
        }

        public void setRealImg(String realImg) {
            this.realImg = realImg;
        }

        public Object getSign() {
            return sign;
        }

        public void setSign(Object sign) {
            this.sign = sign;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }
}
