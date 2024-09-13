import { createApp } from 'vue';
import { createPinia } from 'pinia'
import App from './App.vue';
import router from './router';
import ElementPlus from '@/plugins/element-plus';
import globalVariable from '@/utils/variables';
import mixins from '@/helpers/mixins';
import { i18n } from '@/utils/i18n';
import elen from 'element-plus/es/locale/lang/en';
import piniaPluginPersistedState from "pinia-plugin-persistedstate"

const pinia = createPinia();
pinia.use(piniaPluginPersistedState)
const app = createApp(App);
app.config.globalProperties = { ...app.config.globalProperties, ...globalVariable }
app.mixin(mixins);
app
  .use(router)
  .use(pinia)
  .use(ElementPlus, {
    locale: elen,
  })
  .use(i18n).mount('#app')
