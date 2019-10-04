package com.yijian.them.ui.team.moudle;

import com.tencent.imsdk.TIMGroupMemberInfo;

import java.util.ArrayList;
import java.util.List;

public class TeamInfoMoudle {


    /**
     * code : 200
     * data : {"chatgroupId":null,"cityCode":"020","createAt":"2019-09-03 14:34:45","createBy":18,"latitude":22.946743,"localName":"花城创意产业园","longitude":113.410488,"members":[{"birthday":"2010-01-01","followed":null,"gender":"1","nickName":"小瑞瑞虎","realImg":"http://image.themapp.cn/30c7de5e-e259-4eda-9f9a-e30faef9e6f2201908010958230.jpg","sign":null,"userId":18}],"teamDesc":"dzd","teamId":132,"teamImgHeight":300,"teamImgUrl":"http://image.themapp.cn/24409385-fefa-4579-931d-0bbb431f9efdu=19110287473897928008&fm=26&gp=0.jpg","teamImgUrls":["http://image.themapp.cn/24409385-fefa-4579-931d-0bbb431f9efdu=19110287473897928008&fm=26&gp=0.jpg"],"teamImgWidth":50,"teamMember":"18,","teamName":"测试小队888","teamStatus":"0"}
     * msg : success
     */

    private int code;
    private DataBean data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        /**
         * chatgroupId : null
         * cityCode : 020
         * createAt : 2019-09-03 14:34:45
         * createBy : 18
         * latitude : 22.946743
         * localName : 花城创意产业园
         * longitude : 113.410488
         * members : [{"birthday":"2010-01-01","followed":null,"gender":"1","nickName":"小瑞瑞虎","realImg":"http://image.themapp.cn/30c7de5e-e259-4eda-9f9a-e30faef9e6f2201908010958230.jpg","sign":null,"userId":18}]
         * teamDesc : dzd
         * teamId : 132
         * teamImgHeight : 300
         * teamImgUrl : http://image.themapp.cn/24409385-fefa-4579-931d-0bbb431f9efdu=19110287473897928008&fm=26&gp=0.jpg
         * teamImgUrls : ["http://image.themapp.cn/24409385-fefa-4579-931d-0bbb431f9efdu=19110287473897928008&fm=26&gp=0.jpg"]
         * teamImgWidth : 50
         * teamMember : 18,
         * teamName : 测试小队888
         * teamStatus : 0
         */

        private Object chatgroupId;
        private String cityCode;
        private String createAt;
        private int createBy;
        private double latitude;
        private String localName;
        private double longitude;
        private String teamDesc;
        private int teamId;
        private int teamImgHeight;
        private String teamImgUrl;
        private int teamImgWidth;
        private String teamMember;
        private String teamName;
        private String teamStatus;
        private List<MembersBean> members;
        private List<String> teamImgUrls;

        public Object getChatgroupId() {
            return chatgroupId;
        }

        public void setChatgroupId(Object chatgroupId) {
            this.chatgroupId = chatgroupId;
        }

        public String getCityCode() {
            return cityCode;
        }

        public void setCityCode(String cityCode) {
            this.cityCode = cityCode;
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

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public String getLocalName() {
            return localName;
        }

        public void setLocalName(String localName) {
            this.localName = localName;
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

        public String getTeamImgUrl() {
            return teamImgUrl;
        }

        public void setTeamImgUrl(String teamImgUrl) {
            this.teamImgUrl = teamImgUrl;
        }

        public int getTeamImgWidth() {
            return teamImgWidth;
        }

        public void setTeamImgWidth(int teamImgWidth) {
            this.teamImgWidth = teamImgWidth;
        }

        public String getTeamMember() {
            return teamMember;
        }

        public void setTeamMember(String teamMember) {
            this.teamMember = teamMember;
        }

        public String getTeamName() {
            return teamName;
        }

        public void setTeamName(String teamName) {
            this.teamName = teamName;
        }

        public String getTeamStatus() {
            return teamStatus;
        }

        public void setTeamStatus(String teamStatus) {
            this.teamStatus = teamStatus;
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
             * birthday : 2010-01-01
             * followed : null
             * gender : 1
             * nickName : 小瑞瑞虎
             * realImg : http://image.themapp.cn/30c7de5e-e259-4eda-9f9a-e30faef9e6f2201908010958230.jpg
             * sign : null
             * userId : 18
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

//            public void changeData(List<TIMGroupMemberInfo> timGroupMemberInfos) {
//                List<MembersBean> mList = new ArrayList();
//                for (int i = 0; i < timGroupMemberInfos.size(); i++) {
//                    MembersBean membersBean = new MembersBean();
//                    TIMGroupMemberInfo timGroupMemberInfo = timGroupMemberInfos.get(i);
//                    membersBean.setRealImg(timGroupMemberInfo.);
//                }
//            }
        }
    }
}
