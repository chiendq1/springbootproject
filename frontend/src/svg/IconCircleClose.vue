<template>
  <svg
    :width="width"
    :height="height"
    viewBox="0 0 32 33"
    fill="none"
    xmlns="http://www.w3.org/2000/svg"
  >
    <g opacity="0.5">
      <rect
        width="32"
        height="32"
        transform="translate(0 0.5)"
        fill="white"
        fill-opacity="0.01"
      />
      <g clip-path="url(#clip0_1145_246307)">
        <rect
          width="16"
          height="16"
          transform="translate(8 8.5)"
          fill="white"
          fill-opacity="0.01"
        />
        <g clip-path="url(#clip1_1145_246307)">
          <path
            d="M8.29279 8.7921C8.48031 8.60463 8.73462 8.49932 8.99979 8.49932C9.26495 8.49932 9.51926 8.60463 9.70679 8.7921L15.9998 15.0851L22.2928 8.7921C22.385 8.69659 22.4954 8.62041 22.6174 8.568C22.7394 8.51559 22.8706 8.48801 23.0034 8.48685C23.1362 8.4857 23.2678 8.511 23.3907 8.56128C23.5136 8.61156 23.6253 8.68582 23.7192 8.77971C23.8131 8.8736 23.8873 8.98525 23.9376 9.10815C23.9879 9.23105 24.0132 9.36273 24.012 9.49551C24.0109 9.62829 23.9833 9.75951 23.9309 9.88151C23.8785 10.0035 23.8023 10.1139 23.7068 10.2061L17.4138 16.4991L23.7068 22.7921C23.8889 22.9807 23.9897 23.2333 23.9875 23.4955C23.9852 23.7577 23.88 24.0085 23.6946 24.1939C23.5092 24.3793 23.2584 24.4845 22.9962 24.4868C22.734 24.4891 22.4814 24.3883 22.2928 24.2061L15.9998 17.9131L9.70679 24.2061C9.51818 24.3883 9.26558 24.4891 9.00339 24.4868C8.74119 24.4845 8.49038 24.3793 8.30497 24.1939C8.11956 24.0085 8.01439 23.7577 8.01211 23.4955C8.00983 23.2333 8.11063 22.9807 8.29279 22.7921L14.5858 16.4991L8.29279 10.2061C8.10532 10.0186 8 9.76427 8 9.4991C8 9.23394 8.10532 8.97963 8.29279 8.7921Z"
            :fill="fill"
          />
        </g>
      </g>
    </g>
    <defs>
      <clipPath id="clip0_1145_246307">
        <rect
          width="16"
          height="16"
          fill="white"
          transform="translate(8 8.5)"
        />
      </clipPath>
      <clipPath id="clip1_1145_246307">
        <rect
          width="16"
          height="16"
          fill="white"
          transform="translate(8 8.5)"
        />
      </clipPath>
    </defs>
  </svg>
</template>

<script>
export default {
  props: {
    iconName: {
      type: String,
      default: "circle-close",
    },
    width: {
      type: [Number, String],
      default: 33,
    },
    height: {
      type: [Number, String],
      default: 32,
    },
    fill: {
      type: String,
      default: "black",
    },
  },
};
</script>
