package com.yijian.them.ui.mine;

import java.util.List;

public class UserInfoMoudel {

    /**
     * code : 10001
     * data : [{"commentCount":1,"content":"测试测试测试","createDte":"2019-08-19 10:07:19","dynamicId":122,"groupId":"@TGS#236YQL7F2","groupName":"后台集成测试6","imgUrls":null,"isLike":1,"latitude":null,"likeCount":1,"localName":null,"longitude":null,"tagId":"38","tagName":"这是一个严肃的话题","topicId":null,"userBriefVo":{"birthday":"1949-10-01","followed":null,"gender":"1","nickName":"假昵称","realImg":"http://image.themapp.cn/8dec215e-f712-4763-ba3c-02e124b184fa201907251721050.jpg","sign":null,"userId":16},"videoUrl":null},{"commentCount":0,"content":"测试测试","createDte":"2019-08-15 16:28:59","dynamicId":118,"groupId":"@TGS#22MLDJ7FU","groupName":"后台测试集成","imgUrls":["http://image.themapp.cn/4e30e438-ffd2-4541-b92f-94c36734310e201908151628510.jpg","http://image.themapp.cn/c6b5a354-733a-4359-b243-fdac1fe2f822201908151628511.jpg"],"isLike":0,"latitude":22.946795,"likeCount":0,"localName":"万城万充充电站(花城创意产业园市莲路)","longitude":113.410673,"tagId":"59","tagName":"NewTag","topicId":null,"userBriefVo":{"birthday":"1949-10-01","followed":null,"gender":"1","nickName":"假昵称","realImg":"http://image.themapp.cn/8dec215e-f712-4763-ba3c-02e124b184fa201907251721050.jpg","sign":null,"userId":16},"videoUrl":null},{"commentCount":3,"content":"阿卡丽拒绝","createDte":"2019-07-26 20:08:26","dynamicId":38,"groupId":null,"groupName":null,"imgUrls":null,"isLike":0,"latitude":null,"likeCount":2,"localName":null,"longitude":null,"tagId":null,"tagName":null,"topicId":null,"userBriefVo":{"birthday":"1949-10-01","followed":null,"gender":"1","nickName":"假昵称","realImg":"http://image.themapp.cn/8dec215e-f712-4763-ba3c-02e124b184fa201907251721050.jpg","sign":null,"userId":16},"videoUrl":null}]
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
         * commentCount : 1
         * content : 测试测试测试
         * createDte : 2019-08-19 10:07:19
         * dynamicId : 122
         * groupId : @TGS#236YQL7F2
         * groupName : 后台集成测试6
         * imgUrls : null
         * isLike : 1
         * latitude : null
         * likeCount : 1
         * localName : null
         * longitude : null
         * tagId : 38
         * tagName : 这是一个严肃的话题
         * topicId : null
         * userBriefVo : {"birthday":"1949-10-01","followed":null,"gender":"1","nickName":"假昵称","realImg":"http://image.themapp.cn/8dec215e-f712-4763-ba3c-02e124b184fa201907251721050.jpg","sign":null,"userId":16}
         * videoUrl : null
         */

        private int commentCount;
        private String content;
        private String createDte;
        private int dynamicId;
        private String groupId;
        private String groupName;
        private Object imgUrls;
        private int isLike;
        private Object latitude;
        private int likeCount;
        private Object localName;
        private Object longitude;
        private String tagId;
        private String tagName;
        private Object topicId;
        private UserBriefVoBean userBriefVo;
        private Object videoUrl;

        public int getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(int commentCount) {
            this.commentCount = commentCount;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreateDte() {
            return createDte;
        }

        public void setCreateDte(String createDte) {
            this.createDte = createDte;
        }

        public int getDynamicId() {
            return dynamicId;
        }

        public void setDynamicId(int dynamicId) {
            this.dynamicId = dynamicId;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public Object getImgUrls() {
            return imgUrls;
        }

        public void setImgUrls(Object imgUrls) {
            this.imgUrls = imgUrls;
        }

        public int getIsLike() {
            return isLike;
        }

        public void setIsLike(int isLike) {
            this.isLike = isLike;
        }

        public Object getLatitude() {
            return latitude;
        }

        public void setLatitude(Object latitude) {
            this.latitude = latitude;
        }

        public int getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(int likeCount) {
            this.likeCount = likeCount;
        }

        public Object getLocalName() {
            return localName;
        }

        public void setLocalName(Object localName) {
            this.localName = localName;
        }

        public Object getLongitude() {
            return longitude;
        }

        public void setLongitude(Object longitude) {
            this.longitude = longitude;
        }

        public String getTagId() {
            return tagId;
        }

        public void setTagId(String tagId) {
            this.tagId = tagId;
        }

        public String getTagName() {
            return tagName;
        }

        public void setTagName(String tagName) {
            this.tagName = tagName;
        }

        public Object getTopicId() {
            return topicId;
        }

        public void setTopicId(Object topicId) {
            this.topicId = topicId;
        }

        public UserBriefVoBean getUserBriefVo() {
            return userBriefVo;
        }

        public void setUserBriefVo(UserBriefVoBean userBriefVo) {
            this.userBriefVo = userBriefVo;
        }

        public Object getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(Object videoUrl) {
            this.videoUrl = videoUrl;
        }

        public static class UserBriefVoBean {
            /**
             * birthday : 1949-10-01
             * followed : null
             * gender : 1
             * nickName : 假昵称
             * realImg : http://image.themapp.cn/8dec215e-f712-4763-ba3c-02e124b184fa201907251721050.jpg
             * sign : null
             * userId : 16
             */

            private String birthday;
            private Object followed;
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

            public Object getFollowed() {
                return followed;
            }

            public void setFollowed(Object followed) {
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
}
