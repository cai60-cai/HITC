<template>
  <div id="asideHotArticle" class="aside-box sidebar">
    <h3 class="aside-title">{{ title }}</h3>
    <div class="item-rank"></div>
    <div class="aside-content">
      <ul class="hotArticle-list">
        <li v-for="item in list" :key="item.id">
          <span class="substr" @click="$router.push({name:'relaQuestion', params:{id: item.id}})">
            <span>{{ item.question }}</span>
          </span>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  name: "FallbackSide",
  props: {
    title: {
      type: String,
      default: ''
    },
    list: {
      type: Array,
      default: () => []
    }
  },
  data(){
    return {

    }
  }
}
</script>

<style scoped lang="less">
ol, ul, li {
  list-style: none;
}
#asideHotArticle{
  h3.aside-title {
    background-color: white;
    color: #3d3d3d;
    padding: 0 16px;
    height: 38px;
    line-height: 38px;
    position: relative;
    font-size: 14px;
    font-weight: bold;
  }

  .item-rank {
    height: 1px;
    background-color: #f5f6f7;
    width: 268px;
    margin: auto;
  }

  div.aside-content {
    padding: 12px 16px 16px 16px;
    overflow: hidden;

    *{
      word-wrap: break-word;
    }

    .hotArticle-list {
      margin-top: -8px;

      li {
        margin-top: 8px;
        display: flex;
        width: 100%;
        max-width: 100%;
        align-items: center;
        margin-bottom: 12px;

        .num {
          display: block;
          flex: 0 0 16px;
          height: 16px;
          line-height: 16px;
          text-align: center;
          font-size: 14px;
          margin-right: 8px;
          border-radius: 2px;
        }
        .substr {
          display: block;
          flex: 1;
          color: #222226;
          cursor: pointer;
          vertical-align: sub;
          white-space: nowrap;
          overflow: hidden;
        }

        .icon-hot-fire {
          flex: 0 0 16px;
          height: 16px;
          background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAD50lEQVRYR7WWa2gcVRTHz7l3kmYFkdKWSkCpGFHzQdyZljQYnxAFg6UqM7spRVqqVqpUEMViKaFQfGD94AulaCxG2mTGKj5QUUlRUmmb3Rn7wfgsKIJWiw+wBtLuvX/ZsVt2kpnN7Gy735a55///3XPPPfcwtfCDY91IRu5L3jPxV1YZzhpYjVNOfjcR9wvQKvb8chatFgHMKQBXMvG0kLKfRye/aBYiMwCK+eVK0WTNkIl+F3JBD48e/LEZiMwAFcf8jIDrImZMHxhuMHDOASqFfJE07Y0zkiRvZq/0SVqIpjMAO9+riT8F4bwEkw8NL7j1nADA7unWdOpzEBYlGTDRjOhYsohHPv43DUTqDKBgrtSa3m9kXjOUQq7msdI7Zw0AtjWgCW6DtEdrkcUj0i3vPCsAlYK5njXtAsFII1hdw8yPS9ffWluPoRsMmjqxmr3Sm7M1Gh6BcvKPAvRkWuO6nvCS9IJNZwDsFVdrquwXubYufv3wH/V6iQDKMe8H8EKz5v9ngF6RbnBPLVY55giAtcz0hHSDx+YFgJO/UxO5AIlsAGKHdMvbwvfCNh8A4fkQrNotqauTPU/VZStqAdu6QDN+ALA4i3kYw7TBcIPhim2tYdYj9RuRLPvYLR1IBFC29QxIP5TZnIikNK7RWt0FYOOcopt1QyI1ALs3p3nmTwAdrQAw8TEQLozTYOLd0vPXx2agOmAo6PFWzFPERlp1JAMVx1xLwEgKkRaW8ITh+dfGZqDRKxe9OjydtivOIWV+z3D9VQlHYN6igI8abY9ZvAxgKRFuz5IGZnpOusGD8QC22aUI3zcSlu3tF+lTla2Avi8LAAkaNMaC0eRr6JjHk3oAM38tXb9bFaxN0PrFZgGY6IRYKJbyrvJ0gz5gvgbCunhxftvw/Duqc4Gik19lAIi8Eae746xOWLR6lNIH4+8wvSq94O7qN2Wb34BweVqIcPdkXMbe5LFoQccoVGzzrbgiY+Zh6fobQgDHehjQT6cGYNoi3eCpOZ0xTgDFfKfWfGROLTDvN1z/pmpM+GYQjqaZkIh4XHbf1s/bt+tUAKFBMb9cKx4H4fwzBcP8s3T9i2v/lWNtBvSzDa8t8U9iAffyG+VfE441ORwFy9TQ+wBaFhYMkxayvZP3HvothBwaEmrq3Wrrvj5B/LgQbX08dvi7JJd5h1Ks6VuoK9M7ibCu+qwy0zbpBjtqguFxKZoA0SWR4mI+KiAG2Ct92zhDKasIg+ZVWmELgVcIuvSK+qECxZXLtD65D4B5+mrtEbm2zbPHr6aPILZAB63FlOuY4eED/9R/x71WG/1NfQT+Zb5d18f9BxkAljBJLk7/AAAAAElFTkSuQmCC) no-repeat;
          background-size: 16px;
        }
      }

      li:first-child .num{
        color: #fe2d46;
      }

      li:nth-child(2) .num {
        color: #f60;
      }

      li:nth-child(3) .num {
        color: #faa90e;
      }
    }
  }
}

.aside-box{
  margin-bottom: 8px;
  background-color: #fff;
  border-radius: 2px;
}
</style>
