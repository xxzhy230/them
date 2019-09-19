package com.yijian.them.ui.home;

import com.tencent.imsdk.TIMGroupMemberInfo;

import java.util.List;

public class HomeMoudle {

    /**
     * code : 10001
     * data : [{"commentCount":14,"content":"图片处理测试","createDte":"2019-08-19 10:08:09","dynamicId":124,"groupId":"","groupName":"","imgUrls":["http://image.themapp.cn/3fd9686d-c191-4e97-a8c2-6d5c5cefade0201908191008000.jpg","http://image.themapp.cn/f9d5c9cc-3ff2-41fc-8564-72ec66c811c4201908191008011.jpg","http://image.themapp.cn/e23eccd5-cea1-4bf7-85b5-c59074f739ec201908191008012.jpg"],"isLike":0,"latitude":"","likeCount":0,"localName":"","longitude":"","tagId":"","tagName":"","topicId":"","userBriefVo":{"birthday":"1949-10-01","followed":"","gender":"1","nickName":"假昵称","realImg":"http://image.themapp.cn/8dec215e-f712-4763-ba3c-02e124b184fa201907251721050.jpg","sign":"","userId":16},"videoUrl":""},{"commentCount":8,"content":"神奇的后台测试","createDte":"2019-08-19 16:30:32","dynamicId":127,"groupId":"","groupName":"","imgUrls":"","isLike":0,"latitude":"","likeCount":0,"localName":"","longitude":"","tagId":"","tagName":"","topicId":"","userBriefVo":{"birthday":"2019-07-25","followed":"","gender":"0","nickName":"超管","realImg":"http://image.themapp.cn/80d420ce-4222-4060-acad-6b9a746e5212timg.jpg","sign":"","userId":10},"videoUrl":""},{"commentCount":2,"content":"爱我阿中，帝吧出征","createDte":"2019-08-19 10:08:32","dynamicId":125,"groupId":"","groupName":"","imgUrls":"","isLike":0,"latitude":"","likeCount":0,"localName":"","longitude":"","tagId":"34","tagName":"特别的话题","topicId":3,"userBriefVo":{"birthday":"1981-09-03","followed":"","gender":"1","nickName":"瑞克","realImg":"http://image.themapp.cn/0d73e26d-3d75-4a4f-9841-8b09f2805b9a201908071727530.jpg","sign":"","userId":24},"videoUrl":""},{"commentCount":0,"content":"一起玩LOL","createDte":"2019-08-19 10:07:44","dynamicId":123,"groupId":"@TGS#2TDTNS7FK","groupName":"lol群","imgUrls":["http://image.themapp.cn/4cf76eda-2e1e-4fa8-940e-df0f87a1b5d0201908191007420.jpg"],"isLike":0,"latitude":"","likeCount":1,"localName":"","longitude":"","tagId":"","tagName":"","topicId":"","userBriefVo":{"birthday":"1981-09-03","followed":"","gender":"1","nickName":"瑞克","realImg":"http://image.themapp.cn/0d73e26d-3d75-4a4f-9841-8b09f2805b9a201908071727530.jpg","sign":"","userId":24},"videoUrl":""},{"commentCount":0,"content":"测试测试测试","createDte":"2019-08-19 10:07:19","dynamicId":122,"groupId":"@TGS#236YQL7F2","groupName":"后台集成测试6","imgUrls":"","isLike":0,"latitude":"","likeCount":0,"localName":"","longitude":"","tagId":"38","tagName":"这是一个严肃的话题","topicId":"","userBriefVo":{"birthday":"1949-10-01","followed":"","gender":"1","nickName":"假昵称","realImg":"http://image.themapp.cn/8dec215e-f712-4763-ba3c-02e124b184fa201907251721050.jpg","sign":"","userId":16},"videoUrl":""}]
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
         * commentCount : 14
         * content : 图片处理测试
         * createDte : 2019-08-19 10:08:09
         * dynamicId : 124
         * groupId :
         * groupName :
         * imgUrls : ["http://image.themapp.cn/3fd9686d-c191-4e97-a8c2-6d5c5cefade0201908191008000.jpg","http://image.themapp.cn/f9d5c9cc-3ff2-41fc-8564-72ec66c811c4201908191008011.jpg","http://image.themapp.cn/e23eccd5-cea1-4bf7-85b5-c59074f739ec201908191008012.jpg"]
         * isLike : 0
         * latitude :
         * likeCount : 0
         * localName :
         * longitude :
         * tagId :
         * tagName :
         * topicId :
         * userBriefVo : {"birthday":"1949-10-01","followed":"","gender":"1","nickName":"假昵称","realImg":"http://image.themapp.cn/8dec215e-f712-4763-ba3c-02e124b184fa201907251721050.jpg","sign":"","userId":16}
         * videoUrl :
         */

        private int commentCount;
        private String content;
        private String createDte;
        private int dynamicId;
        private String groupId;
        private String groupName;
        private int isLike;
        private String latitude;
        private int likeCount;
        private String localName;
        private String longitude;
        private String tagId;
        private String tagName;
        private String topicId;
        private UserBriefVoBean userBriefVo;
        private String videoUrl;
        private List<String> imgUrls;



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

        public int getIsLike() {
            return isLike;
        }

        public void setIsLike(int isLike) {
            this.isLike = isLike;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public int getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(int likeCount) {
            this.likeCount = likeCount;
        }

        public String getLocalName() {
            return localName;
        }

        public void setLocalName(String localName) {
            this.localName = localName;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
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

        public String getTopicId() {
            return topicId;
        }

        public void setTopicId(String topicId) {
            this.topicId = topicId;
        }

        public UserBriefVoBean getUserBriefVo() {
            return userBriefVo;
        }

        public void setUserBriefVo(UserBriefVoBean userBriefVo) {
            this.userBriefVo = userBriefVo;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public List<String> getImgUrls() {
            return imgUrls;
        }

        public void setImgUrls(List<String> imgUrls) {
            this.imgUrls = imgUrls;
        }

        public static class UserBriefVoBean {
            /**
             * birthday : 1949-10-01
             * followed :
             * gender : 1
             * nickName : 假昵称
             * realImg : http://image.themapp.cn/8dec215e-f712-4763-ba3c-02e124b184fa201907251721050.jpg
             * sign :
             * userId : 16
             */

            private String birthday;
            private String followed;
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

            public String getFollowed() {
                return followed;
            }

            public void setFollowed(String followed) {
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

        //群组
        private String chatgroupId;
        private String createAt;
        private int createBy;
        private String distance;
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

        //话题
        private String topicName;

        public String getTopicName() {
            return topicName;
        }

        public void setTopicName(String topicName) {
            this.topicName = topicName;
        }

        //获取话题标签
        private String tagDesc;
        private String tagHeat;
        private String tagUrl;
        private boolean isFollow;

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
        private String fromAvatar;
        private String fromUname;
        private String fromUid;
        private String commentId;
        private int replyType;
        private String toUname;
        private List<ReplyListMoudle> replyList;

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

        //系统信息
        private String title;
        private String contentUrl;
        private String imgUrl;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContentUrl() {
            return contentUrl;
        }

        public void setContentUrl(String contentUrl) {
            this.contentUrl = contentUrl;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }
    }
}
