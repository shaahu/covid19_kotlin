package com.indiacovid19.trackingapp.donation

/**
 * Created by Shahu Ronghe on 08, April, 2020
 * in Covid-19 India Stats App
 */
enum class PaymentApp(mPackageName: String) {
    NONE(""),
    AMAZON_PAY(Package.AMAZON_PAY),
    BHIM_UPI(Package.BHIM_UPI),
    GOOGLE_PAY(Package.GOOGLE_PAY),
    PAYTM(Package.PAYTM),
    PHONE_PE(Package.PHONE_PE);

    private val mPackageName: String = mPackageName
    fun getPackageName(): String {
        return mPackageName
    }

    private object Package {
        const val AMAZON_PAY = "in.amazon.mShop.android.shopping"
        const val BHIM_UPI = "in.org.npci.upiapp"
        const val GOOGLE_PAY = "com.google.android.apps.nbu.paisa.user"
        const val PHONE_PE = "com.phonepe.app"
        const val PAYTM = "net.one97.paytm"
    }

}