<template>
  <div id="qr-container">
    <p id="qr-description">Der unten angezeigte QR-Code ermöglicht es dir, diese Webseite ebenfalls auf Mobilgeräten
      anzuzeigen. Scanne
      dafür einfach den Code mit einer kompatiblen App (z.B. Google Lens) und öffne die Webseite.</p>
    <div id="qrcode-content"></div>
    <br>
    <p>Oder <b>{{ ip }}</b> auf einem anderen Gerät aufrufen.</p>
  </div>
</template>

<script>
export default {
  data() {
    return {
      qrcode: undefined,
      ip: "Lädt..."
    }
  },
  mounted: function () {
    let request = new XMLHttpRequest();
    request.open("POST", window.location.origin + "/api/ip");
    let that = this;
    request.addEventListener("load", function () {
      let data = JSON.parse(request.responseText).data;
      let ip = data[0].ip;
      that.ip = "http://" + ip + ":" + window.location.port;
      that.renderCode(that.ip);
    });
    request.send("{i:1}");
  },
  methods: {
    /**
     * Updates the QR-Code to a specific web address
     * @param webAddress Valid URL to a webpage
     */
    renderCode(webAddress) {
      if (document.getElementById("qrcode-content").childElementCount === 0) {
        this.qrcode = new window.QRCode(document.getElementById("qrcode-content"), webAddress);
      }
    }
  }
}
</script>