package com.yijian.them.ui.home;

import java.util.List;

public class GroupMoudle {

    /**
     * code : 200
     * data : [{"chatgroupId":null,"createAt":"2019-08-06 14:37:12","createBy":16,"distance":null,"latitude":22.946743,"longitude":113.410488,"members":[{"birthday":"1949-10-01","followed":null,"gender":"1","nickName":"假昵称","realImg":"http://image.themapp.cn/8dec215e-f712-4763-ba3c-02e124b184fa201907251721050.jpg","sign":"摇呀摇，完蛋！","userId":16}],"teamDesc":"都是弟弟","teamId":73,"teamImgHeight":300,"teamImgUrls":["http://image.themapp.cn/26187085-95db-4260-bd5d-d9200f3c46a7ddd.png","http://image.themapp.cn/54ab3c29-26e4-4474-a63c-f97925f1d10fQQ20190402111551.jpg","http://image.themapp.cn/f60660e2-c12d-479a-96f3-ea9b1c2d1d61wangsicong.jpg"],"teamImgWidth":50,"teamName":"测试小队997"}]
     */

    private int code;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * chatgroupId : null
         * createAt : 2019-08-06 14:37:12
         * createBy : 16
         * distance : null
         * latitude : 22.946743
         * longitude : 113.410488
         * members : [{"birthday":"1949-10-01","followed":null,"gender":"1","nickName":"假昵称","realImg":"http://image.themapp.cn/8dec215e-f712-4763-ba3c-02e124b184fa201907251721050.jpg","sign":"摇呀摇，完蛋！","userId":16}]
         * teamDesc : 都是弟弟
         * teamId : 73
         * teamImgHeight : 300
         * teamImgUrls : ["http://image.themapp.cn/26187085-95db-4260-bd5d-d9200f3c46a7ddd.png","http://image.themapp.cn/54ab3c29-26e4-4474-a63c-f97925f1d10fQQ20190402111551.jpg","http://image.themapp.cn/f60660e2-c12d-479a-96f3-ea9b1c2d1d61wangsicong.jpg"]
         * teamImgWidth : 50
         * teamName : 测试小队997
         */

        private String chatgroupId;
        private String createAt;
        private int createBy;
        private String distance;
        private double latitude;
        private double longitude;
        private String teamDesc;
        private int teamId;
        private int teamImgHeight;
        private int teamImgWidth;
        private String teamName;
        private List<MembersBean> members;
        private List<String> teamImgUrls;


        public String getChatgroupId() {
            return chatgroupId;
        }

        public void setChatgroupId(String chatgroupId) {
            this.chatgroupId = chatgroupId;
        }

        public String getCreateAt() {
            return createAt;
        }

        public void setCreateAt(String createAt) {
            this.createAt = createAt;
        }

        public int getCreateBy() {
            return createBy;
        }

        public void setCreateBy(int createBy) {
            this.createBy = createBy;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public String getTeamDesc() {
            return teamDesc;
        }

        public void setTeamDesc(String teamDesc) {
            this.teamDesc = teamDesc;
        }

        public int getTeamId() {
            return teamId;
        }

        public void setTeamId(int teamId) {
            this.teamId = teamId;
        }

        public int getTeamImgHeight() {
            return teamImgHeight;
        }

        public void setTeamImgHeight(int teamImgHeight) {
            this.teamImgHeight = teamImgHeight;
        }

        public int getTeamImgWidth() {
            return teamImgWidth;
        }

        public void setTeamImgWidth(int teamImgWidth) {
            this.teamImgWidth = teamImgWidth;
        }

        public String getTeamName() {
            return teamName;
        }

        public void setTeamName(String teamName) {
            this.teamName = teamName;
        }

        public List<MembersBean> getMembers() {
            return members;
        }

        public void setMembers(List<MembersBean> members) {
            this.members = members;
        }

        public List<String> getTeamImgUrls() {
            return teamImgUrls;
        }

        public void setTeamImgUrls(List<String> teamImgUrls) {
            this.teamImgUrls = teamImgUrls;
        }

        public static class MembersBean {
            /**
             * birthday : 1949-10-01
             * followed : null
             * gender : 1
             * nickName : 假昵称
             * realImg : http://image.themapp.cn/8dec215e-f712-4763-ba3c-02e124b184fa201907251721050.jpg
             * sign : 摇呀摇，完蛋！
             * userId : 16
             */

            private String birthday;
            private boolean followed;
            private String gender;
            private String nickName;
            private String realImg;
            private String sign;
            private int userId;

            public String getBirthday() {
                return birthday;
            }

            public void setBirthday(String birthday) {
                this.birthday = birthday;
            }

            public boolean getFollowed() {
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

            public String getSign() {
                return sign;
            }

            public void setSign(String sign) {
                this.sign = sign;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }
        }

        //获取话题列表
        private String topicId;
        private String topicName;

        public String getTopicId() {
            return topicId;
        }

        public void setTopicId(String topicId) {
            this.topicId = topicId;
        }

        public String getTopicName() {
            return topicName;
        }

        public void setTopicName(String topicName) {
            this.topicName = topicName;
        }

        //获取话题标签
        private String createDte;
        private String tagDesc;
        private String tagHeat;
        private String tagId;
        private String tagName;
        private String tagUrl;
        private boolean isFollow;

        public String getCreateDte() {
            return createDte;
        }

        public void setCreateDte(String createDte) {
            this.createDte = createDte;
        }

        public String getTagDesc() {
            return tagDesc;
        }

        public void setTagDesc(String tagDesc) {
            this.tagDesc = tagDesc;
        }

        public String getTagHeat() {
            return tagHeat;
        }

        public void setTagHeat(String tagHeat) {
            this.tagHeat = tagHeat;
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

        public String getTagUrl() {
            return tagUrl;
        }

        public void setTagUrl(String tagUrl) {
            this.tagUrl = tagUrl;
        }

        public boolean isFollow() {
            return isFollow;
        }

        public void setFollow(boolean follow) {
            isFollow = follow;
        }

        /**
         * 评论
         */
        private String content;
        private String fromAvatar;
        private String fromUname;
        private String fromUid;
        private String dynamicId;
        private String commentId;
        private int replyType;
        private String toUname;
        private List<ReplyListMoudle> replyList;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getFromAvatar() {
            return fromAvatar;
        }

        public void setFromAvatar(String fromAvatar) {
            this.fromAvatar = fromAvatar;
        }

        public String getFromUname() {
            return fromUname;
        }

        public void setFromUname(String fromUname) {
            this.fromUname = fromUname;
        }

        public String getFromUid() {
            return fromUid;
        }

        public void setFromUid(String fromUid) {
            this.fromUid = fromUid;
        }

        public String getDynamicId() {
            return dynamicId;
        }

        public void setDynamicId(String dynamicId) {
            this.dynamicId = dynamicId;
        }

        public String getCommentId() {
            return commentId;
        }

        public void setCommentId(String commentId) {
            this.commentId = commentId;
        }

        public int getReplyType() {
            return replyType;
        }

        public void setReplyType(int replyType) {
            this.replyType = replyType;
        }

        public String getToUname() {
            return toUname;
        }

        public void setToUname(String toUname) {
            this.toUname = toUname;
        }

        public List<ReplyListMoudle> getReplyList() {
            return replyList;
        }

        public void setReplyList(List<ReplyListMoudle> replyList) {
            this.replyList = replyList;
        }




    }
}
