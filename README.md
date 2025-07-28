# JubaExpress SDK Integration Guide - Android

The **JubaExpress SDK** enables seamless integration of JubaExpress payment services into your Android application.

---

## 🚀 Introduction

This SDK simplifies the integration of JubaExpress payment features into Android apps, providing fast and secure transaction functionality.

---

## 📋 Minimum Requirements

- Android API level 27 (minSdk = 27) or higher
---

## 📦 Step 1: Integration

### 🔧 Enable ViewBinding

In your `app/build.gradle.kts`:

```kotlin
android {
    buildFeatures {
        viewBinding = true
    }
}
```

- Include External Libraries (**Required**):
Our SDK relies on the mentioned libraries. If you are using one of the above libraries in your app, be aware the highest version will be picked by Gradle. For example, if your app is using io.coil-kt:coil-base:1.3.0, our version 2.4.0 will be picked. When using the same dependencies, they should at least be in the same major in order to minimize risk of conflict.

```kotlin
dependencies {
implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.17")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("androidx.security:security-crypto:1.1.0-alpha06")
    implementation("androidx.compose.material3:material3:1.3.2")
}
```

## Method 1: Gradle Integration (Recommended)

- Update `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

- Update `app/build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.github.JubaExpress:JubaExpressSDK:{latest_version}")
}
```

## Method 2: Manual Integration

- Download `JubaExpressSDK-release.aar` from the repository.
- Place it inside your `app/` folder:
- In `app/build.gradle.kts`:

```kotlin
dependencies {
    implementation(files("JubaExpressSDK-release.aar"))
}
```

## ⚙️ Step 2: SDK Initialization

Initialize the JubaExpressSDK with the required `JESDKConfiguration`, The initialization process requires configuration parameters such as `DocumentInfo`, `CustomerName`, and `CustomerInfo` objects

#### DocumentInfo

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `documentType` | `Int` | **Required** |
| `documentNumber` | `string` | **Required** |
| `documentIssueDate` | `string` | **Required** |
| `documentExpiryDate` | `string` | **Required** |
| `documentIssuingCountry` | `string` | **Required** |


#### CustomerName


| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `firstName` | `string` | **Required** |
| `middleName` | `string` | **Required** |
| `lastName` | `string` | **Required** |

#### CustomerInfo


| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `cIF (Customer Identification Number)` | `string` | **Required** |
| `name` | `CustomerName` | **Required** |
| `mobile` | `string` | **Required** |
| `email` | `string` | **Required** |
| `nationality` | `string` | **Required** |
| `dateOfBirth` | `string` | **Required** |
| `placeOfBirth` | `string` | **Required** |
| `gender` | `Int` | **Required** |
| `document` | `DocumentInfo` | **Required** |


Create necessary objects and call the **SDK initialization** method:

```kotlin
initSDK(
    activity = this,
    sdkEnvironment = SDKEnvironment.Development, // or SDKEnvironment.Production
    sdkBaseUrl = "",
    jESDKConfiguration = JESDKConfiguration(
        subscriptionKey = "your_subscription_key",
        partnerKey = "your_partner_key",
        customerInfo = CustomerInfo(
            cIF = "",
            name = CustomerName(firstName = "", middleName = "", lastName = ""),
            mobile = "",
            email = "",
            nationality = "",
            dateOfBirth = "DD/MM/YYYY",
            placeOfBirth = "",
            gender = 0,
            document = DocumentInfo(
                documentType = 0,
                documentNumber = "",
                documentIssueDate = "DD/MM/YYYY",
                documentExpiryDate = "DD/MM/YYYY",
                documentIssuingCountry = ""
            )
        )
    ),
    paymentActivity = paymentActivity
)
```

## 🔁 Step 3: Register Response Callback
After Transacton Creation results will be trigered in this Callback, The returned object contains **referenceId** and **secretKey** for the transaction:

```kotlin
val paymentActivity =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.extras?.let {
                getParcelableClass(it, FinalTransactionResponse, JESDKFinalTransactionModel::class.java)?.let { response ->
                    Log.d("ReferenceId", response.referenceId)
                    Log.d("SecretKey", response.secretKey)
                }
            }
        }
    }
```

## 🧾 Step 4: Transaction Receipt
Open the Transaction receipt screen:

```kotlin
startActivity(Intent(this, JESDKFinalReceiptActivity::class.java).apply {
    putExtra("ReferenceId", it.referenceId)
    putExtra("ScreenTitle", "Remittance Details")
})
```

## 🎨 Step 6: Theme Customization(Optional)
- Add custom colors in your `res/values/colors.xml`:

```kotlin
<color name="colorPrimary">#custom_value</color>
<color name="colorPrimaryDark">#custom_value</color>
<color name="colorAccent">#custom_value</color>
<color name="colorFilledBackground">#custom_value</color>
<color name="colorHeadingTextColor">#custom_value</color>
<color name="colorButtonText">#custom_value</color>
<color name="colorButtonBackground">#custom_value</color>
```

## 📞 Support
For issues or support, please contact JubaExpress development team at support@jubaexpress.com
