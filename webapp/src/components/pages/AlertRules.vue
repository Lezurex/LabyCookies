<template>
  <p>Erstelle benutzerdefinierte Regeln für Alarme nach deinen Präferenzen. Sofern Regeln konfiguriert wurden, werden
    die Einstellungen in LabyMod (Warn Rang, Warn Winrate, Warn Cookies/Spiel) ignoriert.</p>
  <ul>
    <rule style="margin-top: 1rem" v-for="rule in rules" :key="rule.id" :index="rule.id" :rule="rule"
          @delparam="delParam" @newparam="newParam" @delrule="delRule(rule.id)" @save="saveRule"></rule>
  </ul>
  <div style="display: flex; justify-content: flex-end; align-items: center">
    <button @click="newRule" class="btn btn-primary"><i class="ip ip-plus"></i> Regel</button>
  </div>
</template>

<script lang="ts">
import Rule from "@/components/Rule.vue";
import {defineComponent} from "vue";

export default defineComponent({
  name: "AlertRules",
  data() {
    return {
      rules: [] as any,
    }
  },
  emits: [
    "alert"
  ],
  mounted() {
    this.getRules()
  },
  methods: {
    getRules() {
      let req = new XMLHttpRequest();
      req.open("POST", window.location.origin + "/api/alertrules");
      req.addEventListener("load", () => {
        let rules = JSON.parse(req.responseText);
        for (let i = 0; i < rules.length; i++) {
          rules[i].id = i;
          for (let j = 0; j < rules[i].params.length; j++) {
            rules[i].params[j].id = j;
          }
        }
        this.rules = rules;
      })
      req.send(JSON.stringify({i: 1}))
    },
    delParam(id: number, delParam: { id: number; }) {
      for (let i = 0; i < this.rules.length; i++) {
        let rule = this.rules[i];
        if (rule.id === id) {
          for (let j = 0; j < rule.params.length; j++) {
            let param = rule.params[j];
            if (param.id === delParam.id) {
              rule.params.splice(j, 1);
              break;
            }
          }
          break;
        }
      }
    },
    newParam(id: number, param: any) {
      for (let i = 0; i < this.rules.length; i++) {
        let rule = this.rules[i];
        if (rule.id === id) {
          rule.params.push(param);
          break;
        }
      }
    },
    delRule(id: number) {
      let that = this;
      for (let i = 0; i < this.rules.length; i++) {
        let rule = this.rules[i];
        if (rule.id === id) {
          let req = new XMLHttpRequest();
          req.open("POST", window.location.origin + "/api/alertrules/delete/" + i);
          req.addEventListener("load", () => {
            if (req.responseText.length > 0) {
              this.rules.splice(i, 1);
              that.$emit("alert", "Löschen erfolgreich!", false, 2000)
            } else {
              that.$emit("alert", "Löschen fehlgeschlagen!");
            }
          });
          req.send(JSON.stringify({i: 1}))
          break;
        }
      }
    },
    saveRule(savedRule: { id: number; }) {
      let that = this;
      for (let i = 0; i < this.rules.length; i++) {
        let rule = this.rules[i];
        if (rule.id === savedRule.id) {
          let req = new XMLHttpRequest();
          req.open("POST", window.location.origin + "/api/alertrules/edit/" + i);
          req.addEventListener("load", () => {
            if (req.responseText.length > 1) {
              rule = savedRule;
              that.$emit("alert", "Speichern erfolgreich!", false, 2000)
            } else {
              that.$emit("alert", "Speichern fehlgeschlagen!")
            }
          })
          req.send(JSON.stringify(savedRule));
          break;
        }
      }
    },
    newRule() {
      let that = this;
      let nextIndex = this.rules[this.rules.length - 1] ? this.rules[this.rules.length - 1].id + 1 : 0;
      let req = new XMLHttpRequest();
      req.open("POST", window.location.origin + "/api/alertrules/new");
      let rule = {
        id: nextIndex,
        params: [
          {
            id: 0,
            value: 100,
            paramType: "WINS",
            compareType: "LESS_THAN"
          }
        ]
      };
      req.addEventListener("load", () => {
        if (req.responseText.length > 1) {
          that.rules.push(rule);
          that.$emit("alert", "Speichern erfolgreich!", false, 2000)
        } else {
          that.$emit("alert", "Speichern fehlgeschlagen!")
        }
      })
      req.send(JSON.stringify(rule))
    }
  },
  components: {
    rule: Rule
  }
})
</script>

<style scoped>
ul {
  list-style: none;
  padding: 0;
}
</style>