<template>
  <li class="alert-card">
    <div class="header">
      <span>{{genEnumeration()}}</span>
      <button @click="toggleExpanded" class="btn btn-secondary">{{ expanded ? '&uarr;' : '&darr;' }}</button>
    </div>
    <div v-if="expanded">
      <ul>
        <li style="margin-top: 1rem" v-for="param in rule.params" :key="param.id" class="alert-card">
          <label>Stats-Typ
            <select class="form-select bg-dark border-dark" v-model="param.paramType">
              <option value="RANK">Rang</option>
              <option value="WINS">Wins</option>
              <option value="WINRATE">Winrate</option>
              <option value="COOKIES_PER_GAME">Cookies pro Spiel</option>
              <option value="GAMES">Gespielte Spiele</option>
            </select>
          </label>
          <label>Vergleichsoperator
            <select class="form-select bg-dark border-dark" v-model="param.compareType">
              <option value="GREATER_THAN">Grösser als (>=)</option>
              <option value="LESS_THAN">Kleiner als (&lt;)</option>
            </select>
          </label>
          <label>Wert
            <input type="number" class="form-control bg-dark border-dark" v-model="param.value">
          </label>
          <button @click="delParam(param)" class="btn btn-danger"><i class="ip ip-close"></i> Löschen</button>
        </li>
      </ul>
      <div class="footer">
        <button @click="delRule" class="btn btn-danger"><i class="ip ip-close"></i> Regel löschen</button>
        <button style="margin-left: 1rem" @click="newParam" class="btn btn-primary"><i class="ip ip-plus"></i> Parameter</button>
        <button style="margin-left: 1rem" @click="save" class="btn btn-success"><i class="ip ip-hard-drive-hdd"></i> Speichern</button>
      </div>
    </div>
  </li>
</template>

<script>
export default {
  name: "Rule",
  props: {
    index: {required: true},
    rule: {required: true},
  },
  data() {
    return {
      expanded: false
    }
  },
  methods: {
    delParam(param) {
      this.$emit("delparam", this.index, param);
    },
    newParam() {
      let nextIndex = this.rule.params[this.rule.params.length - 1] ? this.rule.params[this.rule.params.length - 1].id + 1 : 0;
      this.$emit("newparam", this.index, {
        value: 100,
        paramType: "RANK",
        compareType: "LESS_THAN",
        id: nextIndex
      })
    },
    delRule() {
      this.$emit("delrule");
    },
    save() {
      this.$emit("save", this.rule);
    },
    genEnumeration() {
      const readableNames = {
        RANK: "Rang",
        WINS: "Wins",
        WINRATE: "Winrate",
        COOKIES_PER_GAME: "Cookies/Spiel",
        GAMES: "Spiele"
      }
      const smallComparings = {
        GREATER_THAN: ">=",
        LESS_THAN: "<"
      }

      let str = "";
      for (let param of this.rule.params) {
        if (this.rule.params.indexOf(param) === this.rule.params.length - 1)
          str += readableNames[param.paramType] + " " + smallComparings[param.compareType] + " " + param.value
        else
          str += readableNames[param.paramType] + " " + smallComparings[param.compareType] + " " + param.value + ", "
      }
      return str;
    },
    toggleExpanded() {
      this.expanded = !this.expanded;
    }
  }
}
</script>

<style scoped>
.alert-card {
  display: block;
  border: .1rem solid var(--bg-primary);
  border-radius: .5rem;
  padding: 1rem;
  min-width: 15rem;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.footer {
  margin-top: 1rem;
  display: flex;
  justify-content: flex-end;
  align-items: center;
}

ul {
  list-style: none;
  padding: 0;
}

label {
  display: block;
  margin-bottom: .5rem;
}

label select, label input, label input:focus {
  margin-top: .5rem;
  color: var(--text-primary);
}
</style>