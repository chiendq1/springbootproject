<template>
  <div>
    <div ref="paypal"></div>
  </div>
</template>

<script>
import { ref, onMounted } from "vue";
import { mixinMethods } from "@/utils/variables";
import { useI18n } from "vue-i18n";
import { PAYPAL_SUCCESS_STATUS } from "@/constants/application";

export default {
  name: "PayPal",
  props: {
    price: {
      type: Number,
      default: 0,
    },
    description: {
      type: String,
      default: "",
    },
  },
  setup(props, { emit }) {
    const paypal = ref(null);
    const loaded = ref(false);
    const { t } = useI18n();

    const setLoaded = () => {
      loaded.value = true;
      window.paypal
        .Buttons({
          style: {
            width: "50%",
            shape: "pill",
            layout: "horizontal",
            color: "blue",
            label: "paypal",
          },
          message: {
            amount: 100,
          },
          createOrder: (data, actions) => {
            return actions.order.create({
              purchase_units: [
                {
                  description: props.description,
                  amount: {
                    currency_code: "USD",
                    value: props.price,
                  },
                },
              ],
            });
          },
          onApprove: async (data, actions) => {
            const order = await actions.order.capture();
            if (order.status == PAYPAL_SUCCESS_STATUS) {
              emit("update", order);
            } else {
              mixinMethods.notifyError(
                t("response.message.update_bill_failed")
              );
            }
          },
          onError: (err) => {
            mixinMethods.notifyError(t("response.message.update_bill_failed"));
          },
        })
        .render(paypal.value);
    };

    onMounted(() => {
      const script = document.createElement("script");
      script.src =
        "https://www.paypal.com/sdk/js?client-id=AUCYX-PkQ63jmGseKSc8BA8-foTwvQcgxHBiwWnx9AEGuzXjTc8ERPcI1g_VaA70dtuMfBbBCNzo2yms&buyer-country=US&currency=USD&components=buttons&enable-funding=venmo";
      script.addEventListener("load", setLoaded);
      document.body.appendChild(script);
    });

    return {
      paypal,
      loaded,
    };
  },
};
</script>

<style scoped>
h3 {
  margin: 40px 0 0;
}
</style>
