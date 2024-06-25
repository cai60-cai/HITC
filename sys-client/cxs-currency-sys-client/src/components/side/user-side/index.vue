<template>
  <div id="asideProfile" class="aside-box sidebar">
    <div class="profile-intro d-flex">
      <div class="avatar-box d-flex justify-content-center flex-column">
        <a href="javascript:void(0)">
          <img v-if="user.avatar" :src="baseUrl + user.avatar" class="avatar_pic">
        </a>
      </div>
      <div class="user-info d-flex flex-column profile-intro-name-box">
        <div class="profile-intro-name-boxTop">
          <a href="javascript:void(0)" class="" id="uid" :title="user.nickName">
            <span class="name " username="weixin_44211968"> {{ user.nickName }}</span>
          </a>
        </div>
        <div class="profile-intro-name-boxFooter">
          <span class="personal-home-page personal-home-years" :title="user.autograph"> {{ user.autograph }}</span>
        </div>
      </div>
    </div>
    <div class="item-rank"></div>
    <div class="data-info d-flex item-tiling">
      <dl class="text-center" :title="user.settings && user.settings.showPoint ? user.point : '用户已关闭积分显示功能'">
        <dt><span class="count"> {{user.settings && user.settings.showPoint ? user.point : 0}} </span></dt>
        <dd>积分</dd>
      </dl>
      <dl class="text-center" id="fanBox" :title="user.articleCount">
        <dt><span class="count" id="fan"> {{ user.articleCount }} </span></dt>
        <dd>文章</dd>
      </dl>
      <dl class="text-center" :title="user.selfArticleCount">
        <dt><span class="count"> {{user.selfArticleCount}} </span></dt>
        <dd>原创</dd>
      </dl>
      <dl class="text-center" :title="user.likedCount">
        <dt><span class="count"> {{ user.likedCount }} </span></dt>
        <dd>获赞</dd>
      </dl>
      <dl class="text-center" :title="user.collectionCount">
        <dt><span class="count">{{ user.collectionCount }}</span></dt>
        <dd>收藏</dd>
      </dl>
    </div>
    <div class="profile-intro-name-boxOpration">
      <div class="opt-letter-watch-box">
        <a class="bt-button personal-letter" href="http://127.0.0.1:3002" target="_blank" rel="noopener">私信</a>
      </div>
      <div class="opt-letter-watch-box">
        <a class="personal-watch bt-button cursor" id="btnAttent" @click="toggleFollow">{{ followText }}</a>
      </div>
    </div>
  </div>
</template>

<script>
import { URL_PREFIX } from "@/api/config";

export default {
  name: "UserSide",
  props: {
    user: {
      type: Object,
      default: () => ({
        articleCount: 6,
        autograph: "加油，未来可期!",
        avatar: '',
        collectionCount: 0,
        likedCount: 0,
        nickName: "HITer",
        point: 0,
        selfArticleCount: 0,
        userId: "67489e02c9b44da6aa33735ee9beb4d5"
      })
    },
    targetUser: {
      type: Object,
      required: true,
      default: () => ({
        userId: "defaultUserId"
      })
    }
  },
  data() {
    return {
      baseUrl: URL_PREFIX,
      isFollowing: false,
    };
  },
  computed: {
    followText() {
      return this.isFollowing ? "已关注" : "关注";
    }
  },
  methods: {
    toggleFollow() {
      this.isFollowing = !this.isFollowing;
      const message = this.isFollowing ? '关注成功' : '取消关注';
      this.$message.success(message);
    },
  }
};
</script>



<style scoped lang="less">
ol, ul, li {
  list-style: none;
}
.aside-box{
  margin-bottom: 8px;
  background-color: #fff;
  border-radius: 2px;
}
#asideProfile{
  padding-bottom: 10px;

  a{
    background-color: transparent;
  }

  .profile-intro{
    padding: 16px;

    .avatar-box{
      position: relative;
      width: 48px;
      height: 48px;
      flex-shrink: 0;
      margin-right: 5px;

      a{
        color: #555666;
        cursor: pointer;
      }

      img.avatar_pic {
        display: block;
        width: 48px;
        height: 48px;
        border-radius: 50%;
      }
    }

    .user-info {
      margin-left: 8px;
      width: 156px;
      position: relative;
      padding-top: 4px;

      span.name {
        width: 100%;
        font-size: 14px;
        font-weight: 500;
        height: 20px;
        line-height: 20px;
        display: block;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }

      #uid {
        white-space: inherit;
        margin-right: 6px;
        overflow: hidden;
      }

      .personal-home-years {
        min-width: 62px;
      }

      .personal-home-page {
        color: #999aaa;
        font-size: 13px;
        line-height: 20px;
        height: 20px;
        overflow: hidden;
      }
    }

    .profile-intro-name-box {
      -webkit-box-flex: 1;
      -ms-flex-positive: 1;
      flex-grow: 1;
    }

    .profile-intro-name-boxTop {
      display: -webkit-box;
      display: -ms-flexbox;
      display: flex;
    }

    .profile-intro-name-boxFooter {
      display: -webkit-box;
      display: -ms-flexbox;
      display: flex;
    }
  }

  .item-rank {
    height: 1px;
    background-color: #f5f6f7;
    width: 268px;
    margin: auto;
  }

  .item-tiling>* {
    width: 100%;
  }

  .data-info {
    padding: 9px 10px;

    .text-center {
      text-align: center;
    }
  }

  .data-info.item-tiling .count {
    color: #4a4d52;
    font-size: 14px;
    font-weight: 500;
    line-height: 22px;
  }

  .data-info.item-tiling dd {
    color: #999aaa;
    font-size: 14px;
    line-height: 22px;
    padding: 3px 0;
  }

  .d-flex{
    display: flex !important;
  }
  .justify-content-center {
    -ms-flex-pack: center !important;
    justify-content: center !important;
  }

  .flex-column{
    -webkit-box-orient: vertical !important;
    -webkit-box-direction: normal !important;
    -ms-flex-direction: column !important;
    flex-direction: column !important;
  }

  .profile-intro-name-boxOpration {
    padding: 8px 16px 10px 16px;
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-pack: justify;
    -ms-flex-pack: justify;
    justify-content: space-between;
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center;

    .opt-letter-watch-box:hover {
      color: #555666;
      border-color: #555666;
    }
    .opt-letter-watch-box {
      color: #f5a523;
      font-size: 12px;
      width: 126px;
      height: 28px;
      border-radius: 14px;
      line-height: 26px;
      text-align: center;
      border: 1px solid #cccccc;

      .personal-letter {
        border: 1px solid #ccccd8;
        background-color: #fff;
        color: #555666;
        line-height: 28px;
        border-radius: 14px;
      }

      .bt-button {
        display: block;
        -webkit-box-sizing: border-box;
        box-sizing: border-box;
        height: 100%;
        font-size: 14px;
        border: none;
      }
    }
  }
}
</style>