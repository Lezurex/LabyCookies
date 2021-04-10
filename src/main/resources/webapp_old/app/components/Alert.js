export default {
    props: {
        message: String,
        visible: Boolean
    },
    template: `
      <div :class="!visible ? 'hidden' : ''" class="notification">{{message}}</div>
    `
}