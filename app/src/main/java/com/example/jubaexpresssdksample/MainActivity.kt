package com.example.jubaexpresssdksample

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.example.jubaexpresssdksample.ui.theme.JubaExpressSDKSampleTheme
import com.jubaexpress.sdk.base.JESDKConfig.initSDK
import com.jubaexpress.sdk.data.model.requests.CustomerInfo
import com.jubaexpress.sdk.data.model.requests.CustomerName
import com.jubaexpress.sdk.data.model.requests.DocumentInfo
import com.jubaexpress.sdk.data.model.requests.JESDKConfiguration
import com.jubaexpress.sdk.data.model.response.JESDKFinalTransactionModel
import com.jubaexpress.sdk.utils.FinalTransactionResponse
import com.jubaexpress.sdk.utils.SDKEnvironment
import com.jubaexpress.sdk.utils.getParcelableClass

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JubaExpressSDKSampleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    ElevatedButton(
                        modifier = Modifier.wrapContentSize(),
                        onClick = { initializeSDKForPayment() }) {
                        Text(text = "Click to Start Payment")
                    }
                }
            }
        }
    }

    private val paymentActivityResults =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.extras?.let {
                    getParcelableClass(
                        it, FinalTransactionResponse, JESDKFinalTransactionModel::class.java
                    )?.let {
                        Log.d("ReferenceId", it.referenceId)
                        Log.d("SecretKey", it.secretKey)
//                        if (it.secretKey.isNotBlank()) {
//                            viewBinding.startSDK.text = it.referenceId + it.secretKey
//                            startActivity(Intent(
//                                this, JESDKFinalReceiptActivity::class.java
//                            ).apply {
//                                putExtra(ReferenceId, it.referenceId)
//                                putExtra(ScreenTitle, "Remittance Details")
//                            })
//                        } else {
//                            showToast("Payment required")
//                        }
                    }
                }
            }
        }

    private fun initializeSDKForPayment() {
        initSDK(
            activity = this,
            sdkEnvironment = SDKEnvironment.Development,
            sdkBaseUrl = "https://online.jubaexpress.net/JubaExpressSDKAPIs/",
            jESDKConfiguration = JESDKConfiguration(
                subscriptionKey = getString(R.string.license_key),
                partnerKey = getString(R.string.authorization_key),
                customerInfo = CustomerInfo(
                    cIF = "SlVCQVNXRURFTjp0ZXN0aW5nMkBnbWFpbC5jb20=",
                    name = CustomerName(
                        firstName = "Yasir", middleName = "", lastName = "Tanveer"
                    ),
                    mobile = "923055142665",
                    email = "testing@gmail.com",
                    nationality = "Pakistan",
                    dateOfBirth = "20/06/1996",
                    placeOfBirth = "Pakistan",
                    gender = 0,
                    DocumentInfo(
                        documentType = 0,
                        documentNumber = "34502-9519963-9",
                        documentIssueDate = "18/01/2023",
                        documentExpiryDate = "18/01/2033",
                        documentIssuingCountry = ""
                    )
                )
            ),
            paymentActivity = paymentActivityResults
        )
    }

}



